package cc.duduhuo.util.pojo.derivation.samplebase.anno;

import java.lang.annotation.*;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 23:20
 * Description:
 * Remarks:
 * =======================================================
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD})
public @interface TestAnno2 {
    String name() default "";

    int value() default 0;
}
