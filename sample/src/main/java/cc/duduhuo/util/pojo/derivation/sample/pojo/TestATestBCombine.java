package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.annotation.DerivationField;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:42
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
        name = "AAndB",
        sourceTypes = {TestA.class, TestB.class, TestCs.class},
        excludeProperties = {"level2", "level3"},
        excludePropertyAnnotations = {TestAnno1.class},
        excludeConstructorParams = {"number"},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS})
public final class TestATestBCombine {

    @DerivationField(initialValue = "0")
    private Integer other1 = 0;
    private String other2;
}
