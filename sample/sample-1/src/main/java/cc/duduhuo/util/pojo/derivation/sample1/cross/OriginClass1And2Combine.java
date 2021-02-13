package cc.duduhuo.util.pojo.derivation.sample1.cross;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;
import cc.duduhuo.util.pojo.derivation.samplebase.pojo.TestCs;

@Derivation(
        name = "OriginClass1And2",
        sourceTypes = {OriginClass1.class, OriginClass2.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS}
)
public final class OriginClass1And2Combine {
    private TestCs testCs;

    public TestCs getTestCs() {
        return testCs;
    }

    public void setTestCs(TestCs testCs) {
        this.testCs = testCs;
    }
}
