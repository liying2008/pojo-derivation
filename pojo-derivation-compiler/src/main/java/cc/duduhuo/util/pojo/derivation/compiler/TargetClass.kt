package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.Language
import javax.lang.model.element.TypeElement

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 22:11
 * Description:
 * Remarks:
 * =======================================================
 */
class TargetClass {
    lateinit var simpleName: String
    var packageName = ""
    var sourceTypes = mutableListOf<TypeElement>()
    var includeProperties = listOf<String>()
    var excludeProperties = listOf<String>()
    var excludePropertyAnnotations = mutableListOf<TypeElement>()
    var language = Language.JAVA
}