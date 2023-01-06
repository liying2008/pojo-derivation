package cc.duduhuo.util.pojo.derivation.compiler.entity

import com.squareup.javapoet.FieldSpec
import javax.lang.model.element.TypeElement

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 22:02
 * Description: 自定义 Field 类
 * Remarks:
 * =======================================================
 */
class Field(
    /** field 名称 */
    val name: String
) {
    /** 是否是 Combine 类 */
    var combineType = false

    /** 是否需要在构造方法中排除此 field 的赋值 */
    var excludedInConstructor = false

    /** 该 field 初始的常量值（仅针对 final 修饰的 field） */
    var constantValue: Any? = null

    /** 该 field 是否被 final 修饰 */
    var isFinal = false

    /** field 所在类的 Element */
    lateinit var enclosingType: TypeElement

    /** field 所在类是否需要在构造方法中排除 */
    var enclosingClassExcludedInConstructor = false

    /** field 所在类是否是 Kotlin 类 */
    var isKotlinEnclosingType = false

    /** FieldSpec */
    lateinit var spec: FieldSpec

    override fun toString(): String {
        return "Field(name='$name', combineType=$combineType, excludedInConstructor=$excludedInConstructor, constantValue=$constantValue, isFinal=$isFinal, enclosingType=$enclosingType, enclosingClassExcludedInConstructor=$enclosingClassExcludedInConstructor, isKotlinEnclosingType=$isKotlinEnclosingType, spec=$spec)"
    }
}