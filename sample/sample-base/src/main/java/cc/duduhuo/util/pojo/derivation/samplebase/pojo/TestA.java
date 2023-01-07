package cc.duduhuo.util.pojo.derivation.samplebase.pojo;

import cc.duduhuo.util.pojo.derivation.annotation.DerivationConstructorExclude;
import cc.duduhuo.util.pojo.derivation.samplebase.anno.TestAnno1;
import cc.duduhuo.util.pojo.derivation.samplebase.anno.TestAnno2;
import cc.duduhuo.util.pojo.derivation.samplebase.ext.Extra2;
import cc.duduhuo.util.pojo.derivation.samplebase.ext.Extra3;

import java.util.HashMap;
import java.util.List;

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

    @TestAnno2(name = "finalVar2", value = 22)
    public final String finalVar2 = "finalVar2";

    public final float finalVar3;
    public final double finalVar4;
    public final boolean finalVar5;
    public final String finalVar6;

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
    @TestAnno1("TestA")
    @TestAnno2(name = "isMale", value = 26)
    private boolean isMale = true;

    private boolean aaa;

    private Boolean bbb;

    private Boolean isCcc;

    @TestAnno1("TestA")
    @TestAnno2(name = "level", value = 27)
    private Integer level;

    @TestAnno1("TestA")
    @TestAnno2(name = "level2", value = 28)
    private int level2;

    @TestAnno1("TestA")
    @TestAnno2(name = "hobies", value = 29)
    private String[] hobies = new String[]{};

    @TestAnno1("TestA")
    private List<String> alist;

    // 以 is 开头，但不是 Boolean/boolean 类型
    @DerivationConstructorExclude(classnames = "AAndB")
    private Character isA;

    // 以 set 开头的变量
    @DerivationConstructorExclude
    private char setB;

    private String setC;

    private Extra2<Integer> setD;

    public TestA() {
        finalVar1 = 0;
        finalVar3 = 0.0F;
        finalVar4 = 0.0;
        finalVar5 = false;
        finalVar6 = null;
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

    public List<String> getAlist() {
        return alist;
    }

    public void setAlist(List<String> alist) {
        this.alist = alist;
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

    public String[] getSetB() {
        return new String[]{setB + ""};
    }

    public void setSetB(String[] setB) {
        this.setB = setB[0].charAt(0);
    }

    public HashMap<String, Extra3[]> getSetC() {
        return new HashMap<>();
    }

    public void setSetC(HashMap<String, Extra3[]> setC) {
        this.setC = "";
    }

    public boolean isAaa() {
        return aaa;
    }

    public void setAaa(boolean aaa) {
        this.aaa = aaa;
    }

    public Boolean getBbb() {
        return bbb;
    }

    public void setBbb(Boolean bbb) {
        this.bbb = bbb;
    }

    public Boolean getCcc() {
        return isCcc;
    }

    public void setCcc(Boolean ccc) {
        isCcc = ccc;
    }

    public Extra2<String> getSetD() {
        return new Extra2<>();
    }

    public void setSetD(Extra2<String> setD) {
        this.setD = new Extra2<>();
    }
}
