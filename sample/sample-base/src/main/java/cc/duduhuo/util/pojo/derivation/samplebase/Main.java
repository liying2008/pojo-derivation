package cc.duduhuo.util.pojo.derivation.samplebase;

import cc.duduhuo.util.pojo.derivation.samplebase.pojo.TestATestB;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:35
 * Description:
 * Remarks:
 * =======================================================
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Main");
        TestATestB aAndB = new TestATestB();
        System.out.println(aAndB);
        System.out.println(aAndB.getSetC());
    }
}
