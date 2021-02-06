package cc.duduhuo.util.pojo.derivation.samplebase

import cc.duduhuo.util.pojo.derivation.samplebase.pojo.KotlinTestATestB
import cc.duduhuo.util.pojo.derivation.samplebase.pojo.TestAKotlinB
import cc.duduhuo.util.pojo.derivation.samplebase.pojo.TestATestB

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/31 23:08
 * Description:
 * Remarks:
 * =======================================================
 */
fun main() {
    val a = KotlinTestATestB()
    println(a.aChar)

    val b = TestAKotlinB()
    println(b.number)

    val c = TestATestB()
    println(c.float1)
}