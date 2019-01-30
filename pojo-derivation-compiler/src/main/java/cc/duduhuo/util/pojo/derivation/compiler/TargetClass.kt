package cc.duduhuo.util.pojo.derivation.compiler

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Language
import com.bennyhuo.aptutils.types.asElement
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
    /** Combine 类的 TypeElement */
    lateinit var combineElement: TypeElement

    /** 生成的类名称 */
    lateinit var simpleName: String

    /** 生成类所在包 */
    var packageName = ""

    /** 生成类需要继承的超类 */
    var superClass: TypeElement = Object::class.java.asElement() as TypeElement

    /** 生成类需要实现的接口 */
    var superInterfaces = listOf<TypeElement>()

    /** 源类型 */
    var sourceTypes = mutableListOf<TypeElement>()

    /** 需要包含的属性列表 */
    var includeFields = listOf<String>()

    /** 需要排除的属性列表 */
    var excludeFields = listOf<String>()

    /** 需要排除的注解列表 */
    var excludeFieldAnnotations = mutableListOf<TypeElement>()

    /** 需要排除的构造方法中的参数 */
    var excludeConstructorParams = listOf<String>()

    /** 需要生成的构造方法种类 */
    var constructorTypes = listOf(ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS)

    /** Field 初始化 */
    var initializers = mutableMapOf<String, String>()

    /** 生成类的语言（JAVA or KOTLIN） */
    var languages = listOf(Language.JAVA)
}