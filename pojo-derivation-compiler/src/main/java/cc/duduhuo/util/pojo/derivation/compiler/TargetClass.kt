package cc.duduhuo.util.pojo.derivation.compiler

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
    var includeProperties = arrayOf<String>()
    var excludeProperties = arrayOf<String>()
    var classHeader = ""
}