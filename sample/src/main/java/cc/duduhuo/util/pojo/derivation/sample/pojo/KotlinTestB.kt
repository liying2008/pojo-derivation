package cc.duduhuo.util.pojo.derivation.sample.pojo

import cc.duduhuo.util.pojo.derivation.annotation.DerivationField
import cc.duduhuo.util.pojo.derivation.sample.ext.Extra1

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 19:50
 * Description:
 * Remarks:
 * =======================================================
 */
class KotlinTestB {
    var name: String? = null

    var number: Int = 0

    @DerivationField(initialValue = "this is a description")
    var description: String = "this is a description"

    var score: Double = 0.0

    var setA: Boolean = false
    var getB: Boolean = false

    var extra1: Extra1? = null
}