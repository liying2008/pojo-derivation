package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.*
import cc.duduhuo.util.pojo.derivation.compiler.builder.TargetClassBuilder
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
        supportedAnnotations.mapTo(HashSet<String>(), Class<*>::getCanonicalName)

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
        targetClass.excludeFieldAnnotations.add(elementUtils.getTypeElement(DerivationField::class.java.canonicalName))
        targetClass.excludeFieldAnnotations.add(elementUtils.getTypeElement(DerivationConstructorExclude::class.java.canonicalName))

        val annotationMirror = getAnnotationMirror(element, Derivation::class.java).get()
        annotationMirror.elementValues.forEach { executableElement, annotationValue ->
            Logger.note(executableElement, annotationValue.value.toString())
            when (executableElement.simpleName()) {
                "name" -> targetClass.simpleName = annotationValue.value.toString()
                "sourceTypes" -> targetClass.sourceTypes.addAll(getTypeElementListFromAnnotationValue(annotationValue))
                "superClass" -> {
                    val superClass = getTypeElementFromAnnotationValue(annotationValue)
                    if (superClass != null) {
                        targetClass.superClass = superClass
                    }
                }
                "superInterfaces" -> targetClass.superInterfaces =
                    getTypeElementListFromAnnotationValue(annotationValue)
                "includeFields" -> targetClass.includeFields = getStringListFromAnnotationValue(annotationValue)
                "excludeFields" -> targetClass.excludeFields = getStringListFromAnnotationValue(annotationValue)
                "excludeConstructorParams" -> targetClass.excludeConstructorParams =
                    getStringListFromAnnotationValue(annotationValue)
                "excludeFieldAnnotations" -> {
                    targetClass.excludeFieldAnnotations.addAll(getTypeElementListFromAnnotationValue(annotationValue))
                }
                "constructorTypes" -> {
                    targetClass.constructorTypes =
                        annotationValue.value.toString().split(",").filter { it.isNotEmpty() }.map {
                            ConstructorType.valueOf(
                                it.replaceFirst(
                                    "cc.duduhuo.util.pojo.derivation.annotation.ConstructorType.",
                                    ""
                                )
                            )
                        }
                }
                "initializers" -> {
                    val initializersStringList = getStringListFromAnnotationValue(annotationValue)
                    initializersStringList.forEach {
                        val index = it.indexOf(":")
                        if (index > 0) {
                            targetClass.initializers[it.substring(0, index)] = it.substring(index + 1)
                        } else {
                            Logger.warn(element, "$it: Invalid format!")
                        }
                    }
                }
                "languages" -> {
                    targetClass.languages = annotationValue.value.toString().split(",").filter { it.isNotEmpty() }.map {
                        Language.valueOf(it.replaceFirst("cc.duduhuo.util.pojo.derivation.annotation.Language.", ""))
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

    private fun getStringListFromAnnotationValue(annotationValue: AnnotationValue): List<String> {
        return annotationValue.value.toString().split(",").filter { it.isNotEmpty() }.map {
            // 去掉字符串前后的 "
            val str = it.substring(1).substring(0, it.length - 2)
            str
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

    private fun getTypeElementListFromAnnotationValue(annotationValue: AnnotationValue): List<TypeElement> {
        return annotationValue.value.toString().split(",").filter { it.isNotEmpty() }.map {
            val classname = if (it.endsWith(".class")) {
                // 去掉末尾的 .class
                it.substring(0, it.length - 6)
            } else {
                it
            }
            AptContext.elements.getTypeElement(classname)
        }
    }
}