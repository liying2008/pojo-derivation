package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.Derivation;

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
        classHeader = "@TestAnno1(\"AAndB\")",
        sourceTypes = {TestA.class, TestB.class},
        excludeProperties = {"level", "level2", "level3"})
public final class TestATestBcombine {
}
