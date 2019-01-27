package cc.duduhuo.util.pojo.derivation.compiler.entity

import com.squareup.javapoet.FieldSpec
import javax.lang.model.element.TypeElement

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 22:02
 * Description:
 * Remarks:
 * =======================================================
 */
class Field(val name: String) {
    var combineType = false
    var constructorExclude = false
    var hasConstantValue = false
    var isFinal = false
    lateinit var enclosingType: TypeElement
    lateinit var spec: FieldSpec
}