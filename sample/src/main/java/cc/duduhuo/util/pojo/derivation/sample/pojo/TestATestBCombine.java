package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.annotation.DerivationFieldDefinition;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1;
import cc.duduhuo.util.pojo.derivation.sample.api.TestInterface;
import cc.duduhuo.util.pojo.derivation.sample.ext.Extra2;
import cc.duduhuo.util.pojo.derivation.sample.ext.Extra3;
import cc.duduhuo.util.pojo.derivation.sample.ext.SuperClass1;

import java.io.Serializable;
import java.util.HashMap;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:42
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
        name = "TestATestB",
        superClass = SuperClass1.class,
        superInterfaces = {Serializable.class, TestInterface.class},
        sourceTypes = {TestA.class, TestB.class, TestCs.class, KotlinTestCs.class},
        serialVersionUID = 7961728318907695402L,
        excludeFields = {"level2", "level3"},
        excludeFieldAnnotations = {TestAnno1.class},
        fieldDefinitions = {@DerivationFieldDefinition(name = "finalVar2", initialValue = "aa"),
                @DerivationFieldDefinition(name = "score", initialValue = "0.98", type = double.class),
                @DerivationFieldDefinition(name = "number", type = long[].class),
                @DerivationFieldDefinition(name = "setB", type = String[].class),
                @DerivationFieldDefinition(name = "setC", type = {HashMap.class, String.class, Extra3[].class}),
                @DerivationFieldDefinition(name = "setD", type = {Extra2.class, String.class})
        },
        excludeConstructorParams = {"number", "setB", "setC", "setD"},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS})
final class TestATestBCombine {

    private Integer other1 = 0;
    private String other2;
}
