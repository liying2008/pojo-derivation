package cc.duduhuo.util.pojo.derivation.sample.pojo

import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno2

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 19:49
 * Description:
 * Remarks:
 * =======================================================
 */
class KotlinTestA {
    /**
     * Kotlin doc: name
     */
    @TestAnno1("TestA")
    @TestAnno2(name = "name", value = 24)
    var name: String? = null

    @TestAnno1("TestA")
    @TestAnno2(name = "age", value = 25)
    var age: Int = 20

    @TestAnno1("TestA")
    @TestAnno2(name = "isMale", value = 26)
    var isMale: Boolean = true

    var ba: Boolean = false

    @TestAnno1("TestA")
    @TestAnno2(name = "level", value = 27)
    var level: Int = 0

    @TestAnno1("TestA")
    @TestAnno2(name = "level2", value = 28)
    var level2: Int = 0

    @TestAnno1("TestA")
    @TestAnno2(name = "hobies", value = 29)
    var hobies: Array<String> = arrayOf()

    var aChar: Char = 'a'
}