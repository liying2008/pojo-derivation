package cc.duduhuo.util.pojo.derivation.compiler.util.common

import java.util.Locale

/**
 * convert string first char to uppercase
 */
fun String.uppercaseFirstChar(): String {
    return if (isNotEmpty() && !this[0].isUpperCase()) substring(
        0, 1
    ).uppercase(Locale.getDefault()) + substring(1) else this
}

/**
 * convert string first char to lowercase
 */
fun String.lowercaseFirstChar(): String {
    return if (isNotEmpty() && !this[0].isLowerCase()) substring(
        0, 1
    ).lowercase(Locale.getDefault()) + substring(1) else this
}
