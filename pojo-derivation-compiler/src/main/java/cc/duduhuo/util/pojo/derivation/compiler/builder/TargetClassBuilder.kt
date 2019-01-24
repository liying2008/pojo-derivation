package cc.duduhuo.util.pojo.derivation.compiler.builder

import cc.duduhuo.util.pojo.derivation.compiler.SourceClass
import cc.duduhuo.util.pojo.derivation.compiler.TargetClass
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier


/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 22:16
 * Description:
 * Remarks:
 * =======================================================
 */
class TargetClassBuilder(
    private val targetClass: TargetClass,
    private val sourceClass: SourceClass
) {

    fun build(filer: Filer) {

        val targetClassBuild = TypeSpec.classBuilder(targetClass.simpleName).addModifiers(Modifier.PUBLIC)

        val fieldSpecs = sourceClass.fieldList.values.map {
            it.spec
        }
        targetClassBuild.addFields(fieldSpecs)
        targetClassBuild.addMethods(sourceClass.methodList)
        val javaFile = JavaFile.builder(targetClass.packageName, targetClassBuild.build()).build()

        javaFile.writeTo(filer)
    }
}