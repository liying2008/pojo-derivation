package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.compiler.util.MirrorUtils.getValueFieldOfClasses
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.simpleName
import com.google.auto.common.MoreElements.getAnnotationMirror
import com.google.auto.common.MoreTypes
import com.google.auto.service.AutoService
import com.google.common.annotations.VisibleForTesting
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
    companion object {
        @VisibleForTesting
        private const val MISSING_SERVICES_ERROR = "No service interfaces provided for element!"
    }

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
        val annotationMirror = getAnnotationMirror(element, Derivation::class.java).get()
        val providerInterfaces = getValueFieldOfClasses(annotationMirror, "sourceTypes")
        if (providerInterfaces.isEmpty()) {
            Logger.error(element, MISSING_SERVICES_ERROR, annotationMirror)
            return
        }

        val sourceTypes = mutableListOf<TypeElement>()
        for (providerInterface in providerInterfaces) {
            val providerType = MoreTypes.asTypeElement(providerInterface)
            Logger.warn("provider interface: " + providerType.qualifiedName)
            Logger.warn("provider implementer: " + element.qualifiedName)
            sourceTypes.add(providerType)
        }

        sourceTypes.forEach { sourceType->
            reflectClass(sourceType)
        }
    }

    private fun  reflectClass(e: TypeElement) {
        Logger.warn(e, "???")
    }
}