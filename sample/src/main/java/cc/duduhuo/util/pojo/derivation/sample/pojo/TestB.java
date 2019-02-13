package cc.duduhuo.util.pojo.derivation.sample.pojo;

import cc.duduhuo.util.pojo.derivation.sample.ext.Extra1;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:35
 * Description:
 * Remarks:
 * =======================================================
 */
public final class TestB {
    private String name;

    private int number;

    private String description = "this is a description";

    private Double score;

    private Boolean getA;
    private Boolean setB;

    private boolean getC;
    private boolean setD;

    private Extra1 extra1;

    public TestB() {
    }

    public TestB(String name, int number, String description, double score, Extra1 extra1) {
        this.name = name;
        this.number = number;
        this.description = description;
        this.score = score;
        this.extra1 = extra1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Extra1 getExtra1() {
        return extra1;
    }

    public void setExtra1(Extra1 extra1) {
        this.extra1 = extra1;
    }

    public Boolean getGetA() {
        return getA;
    }

    public void setGetA(Boolean getA) {
        this.getA = getA;
    }

    public Boolean getSetB() {
        return setB;
    }

    public void setSetB(Boolean setB) {
        this.setB = setB;
    }

    public boolean isGetC() {
        return getC;
    }

    public void setGetC(boolean getC) {
        this.getC = getC;
    }

    public boolean isSetD() {
        return setD;
    }

    public void setSetD(boolean setD) {
        this.setD = setD;
    }
}
