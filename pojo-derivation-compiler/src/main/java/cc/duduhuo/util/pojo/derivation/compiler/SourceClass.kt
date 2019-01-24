package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.compiler.entity.Field
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.simpleName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/23 21:39
 * Description:
 * Remarks:
 * =======================================================
 */
class SourceClass() {
    val fieldList = mutableMapOf<String, Field>()
    val methodList = mutableListOf<MethodSpec>()


    fun parseFields(typeElement: TypeElement) {
        val enclosedElements = typeElement.enclosedElements
        enclosedElements.forEach { element ->
            Logger.warn(element.simpleName.toString())
            if (element.kind == ElementKind.FIELD) {
                val name = element.simpleName()
                if (name in fieldList) {
                    return@forEach
                }
                val typeName = element.asType().asJavaTypeName()
                val modifiers = element.modifiers.toTypedArray()
                val field = Field(name)
                field.enclosingType = typeElement
                field.spec = FieldSpec.builder(typeName, name, *modifiers)
                    .build()
                fieldList[name] = field
            }
        }
    }

    fun genGetterAndSetter() {
        fieldList.forEach { name, field ->
            val spec = field.spec
            if (spec.hasModifier(Modifier.PUBLIC) || spec.hasModifier(Modifier.PROTECTED)) {
                return@forEach
            }
            val getMethod = MethodSpec.methodBuilder(getGetterName(name))
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.type)
                .addStatement("return \$L", name)
                .build()
            this.methodList.add(getMethod)
            if (!spec.hasModifier(Modifier.FINAL)) {
                val setMethod = MethodSpec.methodBuilder(getSetterName(name))
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(spec.type, name)
                    .addStatement("this.\$L = \$L", name, name)
                    .build()
                this.methodList.add(setMethod)
            }
        }
    }

    fun genConstructors() {
        // 无参构造方法
        val emptyConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build()
        // 全参构造方法
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
        // 源对象构造方法
        parameterSpecs.clear()
        codeBlocks.clear()
        val groupedField = fieldList.values.groupBy { it.enclosingType }
        groupedField.forEach { enclosingType, fields ->
            val sourceTypeVar = enclosingType.simpleName().decapitalize()
            parameterSpecs.add(ParameterSpec.builder(enclosingType.asType().asJavaTypeName(), sourceTypeVar).build())
            for (field in fields) {
                codeBlocks.add(
                    CodeBlock.of(
                        "this.\$L(\$L.\$L())",
                        getSetterName(field.name),
                        sourceTypeVar,
                        getGetterName(field.name)
                    )
                )
            }
        }
        val sourceObjConstructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
            .addParameters(parameterSpecs)
        codeBlocks.forEach {
            sourceObjConstructor.addStatement(it)
        }

        methodList.add(emptyConstructor)
        methodList.add(fullConstructorBuilder.build())
        methodList.add(sourceObjConstructor.build())
    }

    private fun getSetterName(name: String): String {
        val property = if (name.startsWith("is")) {
            name.substring(2)
        } else {
            name
        }
        return "set${property.capitalize()}"
    }

    private fun getGetterName(name: String): String {
        return if (name.startsWith("is")) {
            name
        } else {
            "get${name.capitalize()}"
        }
    }
}