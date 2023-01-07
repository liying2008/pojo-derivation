package cc.duduhuo.util.pojo.derivation.compiler.util.common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringExtsTest {
    @Test
    fun uppercaseFirstCharTest() {
        Assertions.assertEquals("Pojo", "pojo".uppercaseFirstChar())
        Assertions.assertEquals("Pojo", "Pojo".uppercaseFirstChar())
        Assertions.assertEquals("", "".uppercaseFirstChar())
        Assertions.assertEquals("_pojo", "_pojo".uppercaseFirstChar())
        Assertions.assertEquals("POJO", "pOJO".uppercaseFirstChar())
    }

    @Test
    fun lowercaseFirstCharTest() {
        Assertions.assertEquals("pojo", "pojo".lowercaseFirstChar())
        Assertions.assertEquals("pojo", "Pojo".lowercaseFirstChar())
        Assertions.assertEquals("", "".lowercaseFirstChar())
        Assertions.assertEquals("_pojo", "_pojo".lowercaseFirstChar())
        Assertions.assertEquals("pOJO", "POJO".lowercaseFirstChar())
    }
}
