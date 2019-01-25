package cc.duduhuo.util.pojo.derivation.sample.pojo;

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
    @TestAnno1("TestA")
    @TestAnno2(name = "name", value = 24)
    private String name;

    @TestAnno1("TestA")
    @TestAnno2(name = "age", value = 25)
    private int age;

    @TestAnno1("TestA")
    @TestAnno2(name = "isMale", value = 26)
    private boolean isMale;

    @TestAnno1("TestA")
    @TestAnno2(name = "level", value = 27)
    private int level;

    @TestAnno1("TestA")
    @TestAnno2(name = "level2", value = 28)
    private int level2;

    @TestAnno1("TestA")
    @TestAnno2(name = "hobies", value = 29)
    private String[] hobies;

    public TestA() {
    }

    public TestA(String name, int age, boolean isMale, int level, String[] hobies) {
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
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
}
