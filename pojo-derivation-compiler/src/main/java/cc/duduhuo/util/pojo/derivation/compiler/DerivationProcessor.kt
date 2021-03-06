package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude
import cc.duduhuo.util.pojo.derivation.annotation.DerivationFieldDefinition
import cc.duduhuo.util.pojo.derivation.compiler.builder.TargetClassBuilder
import cc.duduhuo.util.pojo.derivation.compiler.entity.FieldDefinition
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.asElement
import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.google.auto.common.MoreElements.getAnnotationMirror
import com.google.auto.service.AutoService
import java.util.*
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
        return SourceVersion.RELEASE_8
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

        val typeSymbol = "(type="
        derivation.fieldDefinitions.forEach {
            val defStr = it.toString()
            // Logger.warn("\r\ndefStr=$defStr\r\n")
            val name = it.name
            val initialValue = it.initialValue
            val typeStartIndex = defStr.indexOf(typeSymbol)
            val typeEndIndex1 = defStr.indexOf(", ", typeStartIndex)
            val typeEndIndex2 = defStr.indexOf(")", typeStartIndex)
            val typeEndIndex = if (typeEndIndex1 != -1) typeEndIndex1 else typeEndIndex2
            val classnames = defStr.substring(typeStartIndex + typeSymbol.length, typeEndIndex)
            // Logger.warn("name=$name\r\n")
            // Logger.warn("initialValue=${initialValue.contentToString()}\r\n")
            // Logger.warn("classnames=$classnames\r\n")
            val fieldDefinition = FieldDefinition(name).apply {
                this.initialValue = if (initialValue.isEmpty()) null else initialValue[0]
                if (classnames.isNotEmpty()) {
                    this.classnames = classnames.split(",")
                }
            }
            targetClass.fieldDefinitions[name] = fieldDefinition
        }

        val annotationMirror = getAnnotationMirror(element, Derivation::class.java).get()
        annotationMirror.elementValues.forEach { (executableElement, annotationValue) ->
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
}