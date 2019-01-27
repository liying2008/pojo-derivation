package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 23:13
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
        name = "AAndKotlinB",
        sourceTypes = {TestA.class, KotlinTestB.class},
        includeProperties = "number",
        excludeProperties = {"level", "level2", "level3"},
        excludePropertyAnnotations = {TestAnno1.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS})
public class TestAKotlinBCombine {
}
