package cc.duduhuo.util.pojo.derivation.compiler.builder

import cc.duduhuo.util.pojo.derivation.compiler.DerivationLib
import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.bennyhuo.aptutils.types.canonicalName
import com.bennyhuo.aptutils.utils.writeToFile
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.lang.model.element.Modifier


/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 22:16
 * Description:
 * Remarks:
 * =======================================================
 */
class TargetClassBuilder(private val derivationLib: DerivationLib) {

    /**
     * 生成目标文件
     */
    fun build() {
        val targetClass = derivationLib.targetClass
        val derivationClassBuild = TypeSpec.classBuilder(targetClass.simpleName).addModifiers(Modifier.PUBLIC)

        val fieldSpecs = derivationLib.fieldList.values.map {
            it.spec
        }
        derivationClassBuild.addFields(fieldSpecs)
        derivationClassBuild.addMethods(derivationLib.methodList)
        derivationClassBuild.addJavadoc(
            "Generated according to {@link \$L}.\n",
            targetClass.combineElement.canonicalName()
        )
        if (targetClass.superClass != AptContext.elements.getTypeElement(Object::class.java.canonicalName)) {
            derivationClassBuild.superclass(targetClass.superClass.asType().asJavaTypeName())
        }
        targetClass.superInterfaces.forEach {
            derivationClassBuild.addSuperinterface(it.asType().asJavaTypeName())
        }

        val javaFile = JavaFile.builder(targetClass.packageName, derivationClassBuild.build())
            .addFileComment(
                "This file is automatically generated by pojo-derivation (https://github.com/liying2008/pojo-derivation).\n" +
                        "Do not modify this file -- YOUR CHANGES WILL BE ERASED!\n" +
                        "File generation time: " + Date()
            )
            .build()
        javaFile.writeToFile()
    }
}