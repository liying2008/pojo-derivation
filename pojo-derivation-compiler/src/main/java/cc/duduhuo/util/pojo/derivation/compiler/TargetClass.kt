package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
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
    /** Combine 类完整名称 */
    lateinit var combineClassName: String

    /** 生成的类名称 */
    lateinit var simpleName: String

    /** 生成类所在包 */
    var packageName = ""

    /** 源类型 */
    var sourceTypes = mutableListOf<TypeElement>()

    /** 需要包含的属性列表 */
    var includeProperties = listOf<String>()

    /** 需要排除的属性列表 */
    var excludeProperties = listOf<String>()

    /** 需要排除的注解列表 */
    var excludePropertyAnnotations = mutableListOf<TypeElement>()

    /** 需要排除的构造方法中的参数 */
    var excludeConstructorParams = listOf<String>()

    /** 需要生成的构造方法种类 */
    var constructorTypes = listOf(ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS)

    /** 生成类的语言（JAVA or KOTLIN） */
    var languages = listOf(Language.JAVA)
}