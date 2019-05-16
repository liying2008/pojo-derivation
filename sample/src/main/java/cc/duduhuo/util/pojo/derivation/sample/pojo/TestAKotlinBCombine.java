package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.annotation.DerivationFieldDefinition;
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
        name = "TestAKotlinB",
        order = 101,
        sourceTypes = {TestA.class, KotlinTestB.class, cc.duduhuo.util.pojo.derivation.sample.readme.ABC.class},
//        includeFields = {"number"},
        excludeFields = {"level", "level2", "level3"},
        excludeFieldAnnotations = {TestAnno1.class},
        fieldDefinitions = {@DerivationFieldDefinition(name = "number", type = float.class)},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS}
)
final class TestAKotlinBCombine {
    private List<cc.duduhuo.util.pojo.derivation.sample.readme.ABC> abcs;
}
