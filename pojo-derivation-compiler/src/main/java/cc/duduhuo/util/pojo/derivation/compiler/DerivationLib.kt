package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.compiler.entity.Field
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.simpleName
import com.squareup.javapoet.*
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/23 21:39
 * Description:
 * Remarks:
 * =======================================================
 */
class DerivationLib(private val targetClass: TargetClass) {
    val fieldList = mutableMapOf<String, Field>()
    val methodList = mutableListOf<MethodSpec>()


    fun parseFields() {
        val elementUtils = AptContext.elements
        val sourceTypes = targetClass.sourceTypes
        val excludePropertyAnnotations = targetClass.excludePropertyAnnotations.map {
            it.toString()
        }
        for (sourceType in sourceTypes) {
            val enclosedElements = sourceType.enclosedElements
            enclosedElements.forEach { element ->
                // Logger.warn(element.simpleName.toString())
                if (element.kind == ElementKind.FIELD) {
                    val name = element.simpleName()
                    if (name in fieldList) {
                        return@forEach
                    }
                    element as VariableElement
                    val typeName = element.asType().asJavaTypeName()
                    val modifiers = element.modifiers.toTypedArray()
                    // field 上的 注解
                    val annotationSpecs = mutableListOf<AnnotationSpec>()
                    element.annotationMirrors.forEach {
                        if (it.annotationType.toString() !in excludePropertyAnnotations) {
                            annotationSpecs.add(AnnotationSpec.get(it))
                        }
                    }
                    val field = Field(name)
                    field.enclosingType = sourceType
                    val fieldSpecBuilder = FieldSpec.builder(typeName, name, *modifiers)
                    fieldSpecBuilder.addAnnotations(annotationSpecs)
                    val javadoc = elementUtils.getDocComment(element)
                    if (javadoc != null) {
                        fieldSpecBuilder.addJavadoc(javadoc)
                    }
                    field.spec = fieldSpecBuilder.build()
                    fieldList[name] = field
                }
            }
        }
        // 过滤 Fields
        filterFields()
    }

    /**
     * 过滤 Fields
     */
    private fun filterFields() {
        val includeProperties = targetClass.includeProperties
        val excludeProperties = targetClass.excludeProperties
        val filteredMap = mutableMapOf<String, Field>()
        if (includeProperties.isNotEmpty()) {
            includeProperties.forEach {
                filteredMap[it] = fieldList.getValue(it)
            }
        } else if (excludeProperties.isNotEmpty()) {
            fieldList.forEach { name, _ ->
                if (name !in excludeProperties) {
                    filteredMap[name] = fieldList.getValue(name)
                }
            }
        }
        fieldList.clear()
        fieldList.putAll(filteredMap)
    }

    /**
     * 生成 Getter 和 Setter 方法
     */
    fun genGetterAndSetter() {
        fieldList.forEach { name, field ->
            val spec = field.spec
            if (spec.hasModifier(Modifier.PUBLIC) || spec.hasModifier(Modifier.PROTECTED)) {
                return@forEach
            }
            val getMethod = MethodSpec.methodBuilder(getGetterName(spec.type, name))
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.type)
                .addStatement("return \$L", name)
                .build()
            this.methodList.add(getMethod)
            if (!spec.hasModifier(Modifier.FINAL)) {
                val setMethod = MethodSpec.methodBuilder(getSetterName(spec.type, name))
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(spec.type, name)
                    .addStatement("this.\$L = \$L", name, name)
                    .build()
                this.methodList.add(setMethod)
            }
        }
    }

    /**
     * 生成构造方法
     */
    fun genConstructors() {
        val constructorTypes = targetClass.constructorTypes
        // 无参构造方法
        if (ConstructorType.NO_ARGS in constructorTypes) {
            val emptyConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build()
            methodList.add(emptyConstructor)
        }
        // 全参构造方法
        if (ConstructorType.ALL_ARGS in constructorTypes) {
            val parameterSpecs = mutableListOf<ParameterSpec>()
            val codeBlocks = mutableListOf<CodeBlock>()
            fieldList.forEach { name, field ->
                val spec = field.spec
                parameterSpecs.add(ParameterSpec.builder(spec.type, name).build())
                codeBlocks.add(CodeBlock.of("this.\$L = \$L", name, name))
            }
            val fullConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameters(parameterSpecs)

            codeBlocks.forEach {
                fullConstructorBuilder.addStatement(it)
            }
            methodList.add(fullConstructorBuilder.build())
        }
        // 源对象构造方法
        if (ConstructorType.ALL_SOURCE_OBJS in constructorTypes) {
            val parameterSpecs = mutableListOf<ParameterSpec>()
            val codeBlocks = mutableListOf<CodeBlock>()
            val groupedField = fieldList.values.groupBy { it.enclosingType }
            groupedField.forEach { enclosingType, fields ->
                val sourceTypeVar = enclosingType.simpleName().decapitalize()
                parameterSpecs.add(
                    ParameterSpec.builder(
                        enclosingType.asType().asJavaTypeName(),
                        sourceTypeVar
                    ).build()
                )
                for (field in fields) {
                    codeBlocks.add(
                        CodeBlock.of(
                            "this.\$L(\$L.\$L())",
                            getSetterName(field.spec.type, field.name),
                            sourceTypeVar,
                            getGetterName(field.spec.type, field.name)
                        )
                    )
                }
            }
            val sourceObjConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addParameters(parameterSpecs)
            codeBlocks.forEach {
                sourceObjConstructor.addStatement(it)
            }
            methodList.add(sourceObjConstructor.build())
        }
    }

    private fun getSetterName(typeName: TypeName, name: String): String {
        val property = if ((typeName.isPrimitive || typeName.isBoxedPrimitive)
            && typeName.unbox() == TypeName.BOOLEAN
            && name.startsWith("is")
        ) {
            name.substring(2)
        } else {
            name
        }
        return "set${property.capitalize()}"
    }

    private fun getGetterName(typeName: TypeName, name: String): String {
        return if (name.startsWith("is")) {
            when {
                typeName == TypeName.BOOLEAN -> name
                typeName.isBoxedPrimitive && typeName.unbox() == TypeName.BOOLEAN -> "get" + name.substring(2).capitalize()
                else -> "get${name.capitalize()}"
            }
        } else {
            "get${name.capitalize()}"
        }
    }
}