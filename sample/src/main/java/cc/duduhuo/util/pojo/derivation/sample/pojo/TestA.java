package cc.duduhuo.util.pojo.derivation.sample.pojo;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:35
 * Description:
 * Remarks:
 * =======================================================
 */
public final class TestA {
    private String name;
    private int age;
    private boolean isMale;
    private int level;
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
}
