package cc.duduhuo.util.pojo.derivation.compiler.util

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.types.asJavaTypeName
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.TypeName

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/2/13 19:48
 * Description:
 * Remarks:
 * =======================================================
 */
object TypeUtils {
    fun getTypeNameFromClassname(name: String): TypeName? {
        when (name) {
            "void" -> return TypeName.VOID
            "boolean" -> return TypeName.BOOLEAN
            "byte" -> return TypeName.BYTE
            "short" -> return TypeName.SHORT
            "int" -> return TypeName.INT
            "long" -> return TypeName.LONG
            "char" -> return TypeName.CHAR
            "float" -> return TypeName.FLOAT
            "double" -> return TypeName.DOUBLE

            "boolean[]" -> return TypeName.get(BooleanArray::class.java)
            "byte[]" -> return TypeName.get(ByteArray::class.java)
            "short[]" -> return TypeName.get(ShortArray::class.java)
            "int[]" -> return TypeName.get(IntArray::class.java)
            "long[]" -> return TypeName.get(LongArray::class.java)
            "char[]" -> return TypeName.get(CharArray::class.java)
            "float[]" -> return TypeName.get(FloatArray::class.java)
            "double[]" -> return TypeName.get(DoubleArray::class.java)
            else -> {
                if (name.endsWith("[]")) {
                    // array
                    val classname = name.substring(0, name.length - 2)
                    val te = AptContext.elements.getTypeElement(classname)
                    if (te != null) {
                        return ArrayTypeName.of(te.asType().asJavaTypeName())
                    }
                } else {
                    val te = AptContext.elements.getTypeElement(name)
                    if (te != null) {
                        return TypeName.get(te.asType())
                    }
                }
            }
        }
        return null
    }
}
