package cc.duduhuo.util.pojo.derivation.sample.pojo

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType
import cc.duduhuo.util.pojo.derivation.annotation.Derivation
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1
import cc.duduhuo.util.pojo.derivation.sample.ext.Extra3
import cc.duduhuo.util.pojo.derivation.sample.ext.SuperClass1

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 19:51
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
    name = "KotlinTestATestB",
    superClass = SuperClass1::class,
    sourceTypes = [KotlinTestA::class, KotlinTestB::class, Extra3::class],
    excludeFields = ["level", "level2", "level3"],
    excludeFieldAnnotations = [TestAnno1::class],
    constructorTypes = [ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS]
)
class KotlinTestATestBCombine {
}