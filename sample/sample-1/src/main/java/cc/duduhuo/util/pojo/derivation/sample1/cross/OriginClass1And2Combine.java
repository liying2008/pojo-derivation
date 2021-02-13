package cc.duduhuo.util.pojo.derivation.sample1.cross;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;

@Derivation(
        name = "OriginClass1And2",
        sourceTypes = {OriginClass1.class, OriginClass2.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS}
)
public final class OriginClass1And2Combine {
    // 要在新生成的类中包含生成的类，需要写全限定名
    private cc.duduhuo.util.pojo.derivation.sample1.pojo.KotlinTestATestB kotlinTestATestB;

    public cc.duduhuo.util.pojo.derivation.sample1.pojo.KotlinTestATestB getKotlinTestATestB() {
        return kotlinTestATestB;
    }

    public void setKotlinTestATestB(cc.duduhuo.util.pojo.derivation.sample1.pojo.KotlinTestATestB kotlinTestATestB) {
        this.kotlinTestATestB = kotlinTestATestB;
    }
}
