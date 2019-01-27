package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.Language
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
                Logger.warn(element, element.simpleName())
                processElement(element as TypeElement)
            }
        return true
    }

    private fun processElement(element: TypeElement) {
        val targetClass = TargetClass()
        targetClass.packageName = element.packageName()

        val annotationMirror = getAnnotationMirror(element, Derivation::class.java).get()
        annotationMirror.elementValues.forEach { executableElement, annotationValue ->
            Logger.note(executableElement, annotationValue.value.toString())
            when (executableElement.simpleName()) {
                "name" -> targetClass.simpleName = annotationValue.value.toString()
                "sourceTypes" -> {
                    targetClass.sourceTypes = annotationValue.value.toString().split(",").map {
                        AptContext.elements.getTypeElement(it.trimEnd(*".class".toCharArray()))
                    }
                }
                "includeProperties" -> {
                    targetClass.includeProperties = annotationValue.value.toString().split(",").map {
                        it.trimStart('"').trimEnd('"')
                    }
                }
                "excludeProperties" -> {
                    targetClass.excludeProperties = annotationValue.value.toString().split(",").map {
                        it.trimStart('"').trimEnd('"')
                    }
                }
                "excludePropertyAnnotations" -> {
                    targetClass.excludePropertyAnnotations = annotationValue.value.toString().split(",").map {
                        AptContext.elements.getTypeElement(it.trimEnd(*".class".toCharArray()))
                    }
                }
                "constructorTypes" -> {
                    targetClass.constructorTypes = annotationValue.value.toString().split(",").map {
                        ConstructorType.valueOf(it.trimStart(*"cc.duduhuo.util.pojo.derivation.annotation.ConstructorType.".toCharArray()))
                    }
                }

                "languages" -> {
                    targetClass.languages = annotationValue.value.toString().split(",").map {
                        Language.valueOf(it.trimStart(*"cc.duduhuo.util.pojo.derivation.annotation.Language.".toCharArray()))
                    }
                }
            }
        }
        val derivationLib = DerivationLib(targetClass)
        derivationLib.parseFields()
        derivationLib.genGetterAndSetter()
        derivationLib.genConstructors()
        TargetClassBuilder(targetClass, derivationLib).build()
    }
}