package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude
import cc.duduhuo.util.pojo.derivation.compiler.entity.Field
import cc.duduhuo.util.pojo.derivation.compiler.util.TypeUtils
import cc.duduhuo.util.pojo.derivation.compiler.util.common.commonIgnoreFields
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.isSameTypeWith
import com.bennyhuo.aptutils.types.simpleName
import com.squareup.javapoet.*
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

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

    companion object {
        @SuppressWarnings("unchecked")
        val kotlinMetadata = Class.forName("kotlin.Metadata") as Class<Annotation>
    }

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
            // 判断是否是 Kotlin 类
            val isKotlin = sourceType.getAnnotation(kotlinMetadata) != null
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
                    if (name in commonIgnoreFields) {
                        return@forEach
                    }
                    if (name in fieldList) {
                        return@forEach
                    }
                    element as VariableElement
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
                    field.isKotlinEnclosingType = isKotlin
                    field.constantValue = element.constantValue
                    field.isFinal = Modifier.FINAL in modifiers
                    field.combineType = combineType
                    field.enclosingType = sourceType
                    field.enclosingClassExcludedInConstructor = enclosingClassExcludedInConstructor
                    // field type name
                    val typeName = getFieldTypeName(element)
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
     * 获取 field 的类型
     *
     * @param element Field element
     *
     * @return TypeName
     */
    private fun getFieldTypeName(element: VariableElement): TypeName {
        val name = element.simpleName()
        val fieldDefinitions = targetClass.fieldDefinitions
        var typeName = element.asType().asJavaTypeName()
        if (name in fieldDefinitions) {
            val classnames = fieldDefinitions[name]!!.classnames
            val typeName1 = TypeUtils.getTypeNameFromClassnames(classnames)
            if (typeName1 != null) {
                typeName = typeName1
            }
        }
        return typeName
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
        // 检查 fieldDefinitions
        targetClass.fieldDefinitions.forEach { (name, _) ->
            if (name !in fieldList) {
                Logger.warn(element, "fieldDefinitions: $name is NOT in the field list!")
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
        val fieldDefinitions = targetClass.fieldDefinitions
        val initialValue = if (name in fieldDefinitions) {
            fieldDefinitions[name]!!.initialValue
        } else {
            // Logger.warn("name=$name, constantValue=${element.constantValue}")
            element.constantValue
        }

        if (initialValue == null) {
            return
        }
        if (element.asType().isSameTypeWith(String::class.java)) {
            fieldSpecBuilder.initializer("\$S", initialValue)
        } else if (element.asType().kind == TypeKind.LONG) {
            fieldSpecBuilder.initializer("\$LL", initialValue)
        } else if (element.asType().kind == TypeKind.FLOAT) {
            fieldSpecBuilder.initializer("\$LF", initialValue)
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
            fieldList.forEach { (name, _) ->
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
        fieldList.forEach { (name, field) ->
            val spec = field.spec
            if (spec.hasModifier(Modifier.PUBLIC) || spec.hasModifier(Modifier.PROTECTED)) {
                return@forEach
            }
            val getMethod = MethodSpec.methodBuilder(getGetterName(spec.type, name, false))
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
            fieldList.forEach { (name, field) ->
                // Logger.warn(name + ":" + field)
                if (!field.excludedInConstructor && field.isFinal && (field.constantValue == null)) {
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
            fieldList.forEach { (name, field) ->
                val spec = field.spec
                if (field.excludedInConstructor) {
                    return@forEach
                }
                if (spec.hasModifier(Modifier.STATIC)) {
                    return@forEach
                }
                if (field.isFinal && field.constantValue != null) {
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
            groupedField.forEach { (enclosingType, fields) ->
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
                    if (field.isFinal && (field.constantValue != null)) {
                        continue
                    }
                    val codeBock = if (spec.hasModifier(Modifier.PRIVATE) && !spec.hasModifier(Modifier.FINAL)) {
                        CodeBlock.of(
                            "this.\$L(\$L.\$L())",
                            getSetterName(spec.type, field.name),
                            sourceTypeVar,
                            getGetterName(spec.type, field.name, field.isKotlinEnclosingType)
                        )
                    } else if (spec.hasModifier(Modifier.PRIVATE) && spec.hasModifier(Modifier.FINAL)) {
                        CodeBlock.of(
                            "this.\$L = \$L.\$L()",
                            field.name,
                            sourceTypeVar,
                            getGetterName(spec.type, field.name, field.isKotlinEnclosingType)
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
     * @param isKotlinClass 是否是 Kotlin 类
     *
     * @return 该属性的 get 方法名
     */
    private fun getGetterName(typeName: TypeName, name: String, isKotlinClass: Boolean): String {
        return if (name.startsWith("is")) {
            when {
                typeName == TypeName.BOOLEAN -> name
                typeName.isBoxedPrimitive && typeName.unbox() == TypeName.BOOLEAN -> "get" + name.substring(2).capitalize()
                else -> "get${name.capitalize()}"
            }
        } else {
            when {
                isKotlinClass -> "get${name.capitalize()}"
                typeName == TypeName.BOOLEAN -> "is${name.capitalize()}"
                else -> "get${name.capitalize()}"
            }
        }
    }
}