package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.DerivationConfiguration;
import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.annotation.Language;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1;

import java.util.HashMap;
import java.util.Map;

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
        sourceTypes = {TestA.class, TestB.class},
        excludeProperties = {"level2", "level3"},
        excludePropertyAnnotations = {TestAnno1.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJ},
        languages = {Language.JAVA, Language.KOTLIN})
public final class TestATestBCombine implements DerivationConfiguration {

    @Override
    public Map<Class<?>, Map<String, Object>> classAnnotations() {
        Map<String, Object> params = new HashMap<>();
        params.put("value", "AAndB");
        Map<Class<?>, Map<String, Object>> anno = new HashMap<>();
        anno.put(TestAnno1.class, params);
        return anno;
    }
}
