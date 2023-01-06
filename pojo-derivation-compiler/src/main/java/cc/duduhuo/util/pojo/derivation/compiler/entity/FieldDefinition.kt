package cc.duduhuo.util.pojo.derivation.compiler.entity

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/2/2 21:34
 * Description:
 * Remarks:
 * =======================================================
 */
class FieldDefinition(
    /** field 名称 */
    var name: String
) {
    /** field 初始值 */
    var initialValue: String? = null

    /** field 类型 */
    var classnames: List<String> = listOf()
}