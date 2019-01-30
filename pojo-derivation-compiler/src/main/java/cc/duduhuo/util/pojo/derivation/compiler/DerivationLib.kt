package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude
import cc.duduhuo.util.pojo.derivation.annotation.DerivationField
import cc.duduhuo.util.pojo.derivation.compiler.entity.Field
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.isSameTypeWith
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
class DerivationLib(val targetClass: TargetClass) {
    val fieldList = mutableMapOf<String, Field>()
    val methodList = mutableListOf<MethodSpec>()


    /**
     * 解析 Field
     */
    fun parseFields() {
        val elementUtils = AptContext.elements
        val sourceTypes = targetClass.sourceTypes
        val excludeFieldAnnotations = targetClass.excludeFieldAnnotations.map {
            it.toString()
        }
        for (sourceType in sourceTypes) {
            // 判断是否是 Combine 类
            val combineType = sourceType.getAnnotation(Derivation::class.java) != null
            // 判断该类是否要在构造方法中排除
            var enclosingClassExcludedInConstructor = false
            if (sourceType.simpleName() in targetClass.excludeConstructorParams) {
                enclosingClassExcludedInConstructor = true
            }
            if (!enclosingClassExcludedInConstructor) {
                val constructorExcludeAnnotationInClass =
                    sourceType.getAnnotation(DerivationConstructorExclude::class.java)
                if (constructorExcludeAnnotationInClass != null) {
                    val classnames = constructorExcludeAnnotationInClass.classnames
                    if (classnames.isEmpty() || targetClass.simpleName in classnames) {
                        enclosingClassExcludedInConstructor = true
                    }
                }
            }
            // 获取类中所有的元素
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
                        val annotationName = it.annotationType.toString()
                        if (annotationName !in excludeFieldAnnotations) {
                            annotationSpecs.add(AnnotationSpec.get(it))
                        }
                    }
                    val field = Field(name)
                    // 判断该 field 是否要在构造方法中排除
                    field.excludedInConstructor = false
                    if (name in targetClass.excludeConstructorParams) {
                        field.excludedInConstructor = true
                    }
                    if (!field.excludedInConstructor) {
                        val constructorExcludeAnnotation =
                            element.getAnnotation(DerivationConstructorExclude::class.java)
                        if (constructorExcludeAnnotation != null) {
                            val classnames = constructorExcludeAnnotation.classnames
                            if (classnames.isEmpty() || targetClass.simpleName in classnames) {
                                field.excludedInConstructor = true
                            }
                        }
                    }
                    // 判断该 field 是否被 final 修饰且有初始值
                    field.hasConstantValue = element.constantValue != null
                    field.isFinal = Modifier.FINAL in modifiers
                    field.combineType = combineType
                    field.enclosingType = sourceType
                    field.enclosingClassExcludedInConstructor = enclosingClassExcludedInConstructor
                    val fieldSpecBuilder = FieldSpec.builder(typeName, name, *modifiers)
                    fieldSpecBuilder.addAnnotations(annotationSpecs)
                    val javadoc = elementUtils.getDocComment(element)
                    if (javadoc != null) {
                        fieldSpecBuilder.addJavadoc(javadoc)
                    }
                    addInitValue(name, element, fieldSpecBuilder)
                    field.spec = fieldSpecBuilder.build()
                    fieldList[name] = field
                }
            }
        }
        // 检查 Derivation 中的属性名称
        checkDerivation()
        // 过滤 Fields
        filterFields()
    }

    /**
     * 检查 Derivation 中填写的所有属性名称是否包含错误，如果有错误，则给出提示
     */
    private fun checkDerivation() {
        val element = targetClass.combineElement
        // 检查 includeFields
        targetClass.includeFields.forEach {
            if (it !in fieldList) {
                Logger.warn(element, "includeFields: $it is NOT in the field list!")
            }
        }
        // 检查 excludeFields
        targetClass.excludeFields.forEach {
            if (it !in fieldList) {
                Logger.warn(element, "excludeFields: $it is NOT in the field list!")
            }
        }
        // 检查 initializers
        targetClass.initializers.forEach { name, _ ->
            if (name !in fieldList) {
                Logger.warn(element, "initializers: $name is NOT in the field list!")
            }
        }
    }

    /**
     * 添加初始值
     *
     * @param name field name
     * @param element VariableElement
     * @param fieldSpecBuilder FieldSpec.Builder
     */
    private fun addInitValue(name: String, element: VariableElement, fieldSpecBuilder: FieldSpec.Builder) {
        var initialValue: String? = null
        val initializers = targetClass.initializers
        if (name in initializers) {
            initialValue = initializers[name]
        }
        if (initialValue == null) {
            val annotation = element.getAnnotation(DerivationField::class.java) ?: return
            initialValue = annotation.initialValue
        }

        if (element.asType().isSameTypeWith(String::class.java)) {
            fieldSpecBuilder.initializer("\$S", initialValue)
        } else {
            fieldSpecBuilder.initializer("\$L", initialValue)
        }
    }

    /**
     * 过滤 Fields
     */
    private fun filterFields() {
        val includeFields = targetClass.includeFields
        val excludeFields = targetClass.excludeFields
        val filteredMap = mutableMapOf<String, Field>()
        if (includeFields.isNotEmpty()) {
            includeFields.forEach {
                filteredMap[it] = fieldList.getValue(it)
            }
            fieldList.clear()
            fieldList.putAll(filteredMap)
        } else if (excludeFields.isNotEmpty()) {
            fieldList.forEach { name, _ ->
                if (name !in excludeFields) {
                    filteredMap[name] = fieldList.getValue(name)
                }
            }
            fieldList.clear()
            fieldList.putAll(filteredMap)
        }
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
            val emptyConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
            fieldList.forEach { name, field ->
                if (!field.excludedInConstructor && field.isFinal && !field.hasConstantValue) {
                    emptyConstructorBuilder.addStatement(
                        "this.\$L = \$L",
                        name,
                        getInitValueByTypeName(field.spec.type)
                    )
                }
            }
            methodList.add(emptyConstructorBuilder.build())
        }
        // 全参构造方法
        if (ConstructorType.ALL_ARGS in constructorTypes) {
            val parameterSpecs = mutableListOf<ParameterSpec>()
            val codeBlocks = mutableListOf<CodeBlock>()
            fieldList.forEach { name, field ->
                val spec = field.spec
                if (field.excludedInConstructor) {
                    return@forEach
                }
                if (spec.hasModifier(Modifier.STATIC)) {
                    return@forEach
                }
                if (field.isFinal && field.hasConstantValue) {
                    return@forEach
                }
                parameterSpecs.add(ParameterSpec.builder(spec.type, name).build())
                codeBlocks.add(CodeBlock.of("this.\$L = \$L", name, name))
            }
            if (parameterSpecs.isNotEmpty()) {
                // 构造方法有参数
                val fullConstructorBuilder = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                    .addParameters(parameterSpecs)

                codeBlocks.forEach {
                    fullConstructorBuilder.addStatement(it)
                }
                methodList.add(fullConstructorBuilder.build())
            }
        }
        // 源对象构造方法
        if (ConstructorType.ALL_SOURCE_OBJS in constructorTypes) {
            val parameterSpecs = mutableListOf<ParameterSpec>()
            val codeBlocks = mutableListOf<CodeBlock>()
            val groupedField = fieldList.values.filterNot { it.combineType || it.enclosingClassExcludedInConstructor }
                .groupBy { it.enclosingType }
            groupedField.forEach { enclosingType, fields ->
                val sourceTypeVar = enclosingType.simpleName().decapitalize()
                parameterSpecs.add(
                    ParameterSpec.builder(
                        enclosingType.asType().asJavaTypeName(),
                        sourceTypeVar
                    ).build()
                )
                for (field in fields) {
                    val spec = field.spec
                    if (field.excludedInConstructor) {
                        continue
                    }
                    if (spec.hasModifier(Modifier.STATIC)) {
                        continue
                    }
                    if (field.isFinal && field.hasConstantValue) {
                        continue
                    }
                    val codeBock = if (spec.hasModifier(Modifier.PRIVATE)) {
                        CodeBlock.of(
                            "this.\$L(\$L.\$L())",
                            getSetterName(spec.type, field.name),
                            sourceTypeVar,
                            getGetterName(spec.type, field.name)
                        )
                    } else {
                        CodeBlock.of("this.\$L = \$L.\$L", field.name, sourceTypeVar, field.name)
                    }
                    codeBlocks.add(codeBock)
                }
            }
            if (parameterSpecs.isNotEmpty()) {
                val sourceObjConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                    .addParameters(parameterSpecs)
                codeBlocks.forEach {
                    sourceObjConstructor.addStatement(it)
                }
                methodList.add(sourceObjConstructor.build())
            }
        }
    }

    /**
     * 根据类型获取对应的初始值
     *
     * @param typeName 类型名称
     *
     * @return 初始值
     */
    private fun getInitValueByTypeName(typeName: TypeName): Any? {
        return when (typeName) {
            TypeName.BOOLEAN -> false
            TypeName.BYTE, TypeName.CHAR, TypeName.INT, TypeName.SHORT, TypeName.LONG -> 0
            TypeName.FLOAT -> 0.0F
            TypeName.DOUBLE -> 0.0
            else -> null
        }
    }

    /**
     * 获取 set 方法名称
     *
     * @param typeName 类型名称
     * @param name 属性名
     *
     * @return 该属性的 set 方法名
     */
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

    /**
     * 获取 get 方法名称
     *
     * @param typeName 类型名称
     * @param name 属性名
     *
     * @return 该属性的 get 方法名
     */
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