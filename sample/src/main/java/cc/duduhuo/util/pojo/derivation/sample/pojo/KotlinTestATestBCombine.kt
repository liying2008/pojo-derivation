package cc.duduhuo.util.pojo.derivation.sample.pojo

import cc.duduhuo.util.pojo.derivation.annotation.Derivation
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
    excludePropertyAnnotations = [TestAnno1::class]
)
class KotlinTestATestBCombine {
}