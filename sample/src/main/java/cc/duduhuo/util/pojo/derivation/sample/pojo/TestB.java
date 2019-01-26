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
    private String description;
    private Double score;
    private Boolean isSuccess;
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

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
