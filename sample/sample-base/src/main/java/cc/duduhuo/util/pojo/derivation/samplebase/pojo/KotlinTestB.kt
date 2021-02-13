package cc.duduhuo.util.pojo.derivation.samplebase.pojo

import cc.duduhuo.util.pojo.derivation.samplebase.enums.TestEnum1
import cc.duduhuo.util.pojo.derivation.samplebase.ext.Extra1

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

    var description: String = "this is a description"

    var score: Double = 0.0

    var setA: Boolean = false
    var getB: Boolean = false

    var extra1: Extra1? = null

    var testEnum1: TestEnum1 = TestEnum1.TYPE_A
}