package cc.duduhuo.util.pojo.derivation.sample.pojo

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.annotation.Language
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 19:51
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
    name = "KotlinAAndKotlinB",
    sourceTypes = [KotlinTestA::class, KotlinTestB::class],
    excludeProperties = ["level", "level2", "level3"],
    excludePropertyAnnotations = [TestAnno1::class],
    constructorTypes = [ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJ],
    languages = [Language.JAVA, Language.KOTLIN]
)
class KotlinTestATestBCombine {
}