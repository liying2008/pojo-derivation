package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude
import cc.duduhuo.util.pojo.derivation.annotation.DerivationFieldDefinition
import cc.duduhuo.util.pojo.derivation.compiler.builder.TargetClassBuilder
import cc.duduhuo.util.pojo.derivation.compiler.entity.FieldDefinition
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.google.auto.common.MoreElements.getAnnotationMirror
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement


/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:27
 * Description:
 * Remarks:
 * =======================================================
 */
@AutoService(Processor::class)
class DerivationProcessor : AbstractProcessor() {

    private val supportedAnnotations = setOf(Derivation::class.java)

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }

    override fun getSupportedAnnotationTypes() =
        supportedAnnotations.mapTo(HashSet(), Class<*>::getCanonicalName)

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(Derivation::class.java).filter { it.kind.isClass }
            .forEach { element: Element ->
                // Logger.warn(element, element.simpleName())
                processElement(element as TypeElement)
            }
        return true
    }

    private fun processElement(element: TypeElement) {
        val elementUtils = AptContext.elements
        val targetClass = TargetClass()
        targetClass.combineElement = element
        targetClass.packageName = element.packageName()
        targetClass.sourceTypes.add(element)
        targetClass.excludeFieldAnnotations.add(elementUtils.getTypeElement(Derivation::class.java.canonicalName))
        targetClass.excludeFieldAnnotations.add(elementUtils.getTypeElement(DerivationFieldDefinition::class.java.canonicalName))
        targetClass.excludeFieldAnnotations.add(elementUtils.getTypeElement(DerivationConstructorExclude::class.java.canonicalName))

        val derivation = element.getAnnotation(Derivation::class.java)
        targetClass.simpleName = derivation.name
        targetClass.includeFields = derivation.includeFields
        targetClass.excludeFields = derivation.excludeFields
        targetClass.excludeConstructorParams = derivation.excludeConstructorParams
        targetClass.constructorTypes = derivation.constructorTypes
        targetClass.serialVersionUID = derivation.serialVersionUID

        // NOTE: 无法使用下面的方法获取 class object
        // derivation.sourceTypes.forEach {
        //     targetClass.sourceTypes.add(it.asElement() as TypeElement)
        // }
        // targetClass.superClass = derivation.superClass.asElement() as TypeElement
        // derivation.superInterfaces.forEach {
        //     targetClass.superInterfaces.add(it.asElement() as TypeElement)
        // }
        // derivation.excludeFieldAnnotations.forEach {
        //     targetClass.excludeFieldAnnotations.add(it.asElement() as TypeElement)
        // }

        val derivationMirror = getAnnotationMirror(element, Derivation::class.java).get()
        derivationMirror.elementValues.forEach { (executableElement, annotationValue) ->
            val executableElementName = executableElement.simpleName()
            // Logger.note(executableElement, executableElementName + ": " + annotationValue.value.toString() + "\r\n")
            when (executableElementName) {
                "sourceTypes" -> targetClass.sourceTypes.addAll(getTypeElementListFromAnnotationValue(annotationValue))
                "superClass" -> {
                    val superClass = getTypeElementFromAnnotationValue(annotationValue)
                    if (superClass != null) {
                        targetClass.superClass = superClass
                    }
                }

                "superInterfaces" -> targetClass.superInterfaces =
                    getTypeElementListFromAnnotationValue(annotationValue)

                "excludeFieldAnnotations" -> {
                    targetClass.excludeFieldAnnotations.addAll(getTypeElementListFromAnnotationValue(annotationValue))
                }

                "fieldDefinitions" -> {
                    val fdStr = annotationValue.value.toString()
                    val fdParts = fdStr.split("@cc.duduhuo.util.pojo.derivation.annotation.DerivationFieldDefinition")
                    for (fdPart in fdParts) {
                        // trimmedPart 应当类似于 (name="score", initialValue={"0.98"}, type={double.class})
                        val trimmedPart = fdPart.trim(' ', ',')
                        if (trimmedPart == "") {
                            continue
                        }
                        // Logger.note(executableElement, fdPart + "\r\n")
                        if (!trimmedPart.startsWith("(") || !trimmedPart.endsWith(")")) {
                            // trimmedPart 不以 ( 开头，或不以 ) 结尾
                            Logger.warn(executableElement, "fieldDefinitions: $fdParts parsing error!")
                            continue
                        }
                        val fieldDefinition = parseFieldDefinitionString(trimmedPart)
                        // Logger.note(executableElement, "fieldDefinition: $fieldDefinition\r\n")
                        if (fieldDefinition != null) {
                            targetClass.fieldDefinitions[fieldDefinition.name] = fieldDefinition
                        }
                    }
                }
            }
        }

        val derivationLib = DerivationLib(targetClass)
        try {
            derivationLib.parseFields()
            derivationLib.genGetterAndSetter()
            derivationLib.genConstructors()
            TargetClassBuilder(derivationLib).build()
        } catch (e: Exception) {
            Logger.logParsingError(element, Derivation::class.java, e)
        }
    }

    private fun getTypeElementFromAnnotationValue(annotationValue: AnnotationValue): TypeElement? {
        val str = annotationValue.value.toString()
        if (str.isEmpty()) {
            return null
        }
        val classname = if (str.endsWith(".class")) {
            // 去掉末尾的 .class
            str.substring(0, str.length - 6)
        } else {
            str
        }
        return AptContext.elements.getTypeElement(classname)
    }

    private fun getTypeElementListFromAnnotationValue(annotationValue: AnnotationValue): MutableList<TypeElement> {
        return annotationValue.value.toString().split(",").filter { it.isNotEmpty() }.map {
            val classname = if (it.endsWith(".class")) {
                // 去掉末尾的 .class
                it.substring(0, it.length - 6)
            } else {
                it
            }
            AptContext.elements.getTypeElement(classname)
        }.toMutableList()
    }

    private fun parseFieldDefinitionString(s: String): FieldDefinition? {
        // 去掉左右小括号
        var name = ""
        var initialValue: String? = null
        var classnames: List<String> = listOf()

        var s2 = s.substring(1, s.length - 1)
        while (s2.isNotEmpty()) {
            val equalSignIndex = s2.indexOf('=')
            if (equalSignIndex == -1) {
                // 结束循环
                s2 = ""
                continue
            }
            val key = s2.substring(0, equalSignIndex).trim(' ', ',')
            // value 不包含 "" 或 {}
            var value = ""
            var endSignIndex = -1
            if (s2[equalSignIndex + 1] == '"') {
                endSignIndex = s2.indexOf('"', equalSignIndex + 2)
                if (endSignIndex == -1) {
                    // 未找到结尾符号，报错
                    Logger.warn("fieldDefinitions: $s parsing error!")
                    return null
                }
                value = s2.substring(equalSignIndex + 2, endSignIndex)
            } else if (s2[equalSignIndex + 1] == '{') {
                endSignIndex = s2.indexOf('}', equalSignIndex + 2)
                if (endSignIndex == -1) {
                    // 未找到结尾符号，报错
                    Logger.warn("fieldDefinitions: $s parsing error!")
                    return null
                }
                value = s2.substring(equalSignIndex + 2, endSignIndex)
            } else {
                // Unreachable code
                return null
            }
            when (key) {
                "name" -> name = value
                "initialValue" -> {
                    val array = value.split(", ")
                    initialValue = if (array.isEmpty()) {
                        null
                    } else {
                        array[0].trim().trim('"')
                    }
                }

                "type" -> {
                    classnames = value.split(", ").map {
                        if (it.endsWith(".class")) {
                            // 去掉末尾的 .class
                            it.substring(0, it.length - 6)
                        } else {
                            it
                        }
                    }
                }
            }
            if (s2.length == endSignIndex + 1) {
                // 结束循环
                s2 = ""
                continue
            } else {
                s2 = s2.substring(endSignIndex + 1)
            }
        }
        if (name.isEmpty()) {
            Logger.warn("fieldDefinitions: $s parsing error!")
            return null
        }
        return FieldDefinition(name).apply {
            this.initialValue = initialValue
            if (classnames.isNotEmpty()) {
                this.classnames = classnames
            }
        }
    }
}