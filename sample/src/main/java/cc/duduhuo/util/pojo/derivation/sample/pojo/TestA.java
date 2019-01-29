package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude;
import cc.duduhuo.util.pojo.derivation.annotation.DerivationField;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno1;
import cc.duduhuo.util.pojo.derivation.sample.anno.TestAnno2;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:35
 * Description:
 * Remarks:
 * =======================================================
 */
@TestAnno1("TestA")
public final class TestA {
    @TestAnno2(name = "finalVar1", value = 21)
    public final char finalVar1;

    @DerivationField(initialValue = "final")
    @TestAnno2(name = "finalVar2", value = 22)
    public final String finalVar2 = "finalVar2";

    @DerivationField(initialValue = "SV")
    @TestAnno2(name = "STATIC_VAR", value = 23)
    public static final String STATIC_VAR = "SV";

    /**
     * Doc: name
     */
    @TestAnno1("TestA")
    @TestAnno2(name = "name", value = 24)
    private String name;

    @TestAnno1("TestA")
    @TestAnno2(name = "age", value = 25)
    private int age = 20;

    /**
     * Doc: isMale
     */
    @DerivationField(initialValue = "true")
    @TestAnno1("TestA")
    @TestAnno2(name = "isMale", value = 26)
    private boolean isMale = true;

    @TestAnno1("TestA")
    @TestAnno2(name = "level", value = 27)
    private Integer level;

    @TestAnno1("TestA")
    @TestAnno2(name = "level2", value = 28)
    private int level2;

    @DerivationField(initialValue = "new String[]{}")
    @TestAnno1("TestA")
    @TestAnno2(name = "hobies", value = 29)
    private String[] hobies = new String[]{};

    // 以 is 开头，但不是 Boolean/boolean 类型
    @DerivationConstructorExclude(classnames = "AAndB")
    @DerivationField(initialValue = "'a'")
    private Character isA;

    // 以 set 开头的变量
    @DerivationConstructorExclude
    @DerivationField(initialValue = "9")
    private char setB;

    public TestA(char finalVar1, String name, int age, boolean isMale, int level, String[] hobies) {
        this.finalVar1 = finalVar1;
        this.name = name;
        this.age = age;
        this.isMale = isMale;
        this.level = level;
        this.hobies = hobies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String[] getHobies() {
        return hobies;
    }

    public void setHobies(String[] hobies) {
        this.hobies = hobies;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public Character getIsA() {
        return isA;
    }

    public void setIsA(Character isA) {
        this.isA = isA;
    }

    public char getSetB() {
        return setB;
    }

    public void setSetB(char setB) {
        this.setB = setB;
    }
}
