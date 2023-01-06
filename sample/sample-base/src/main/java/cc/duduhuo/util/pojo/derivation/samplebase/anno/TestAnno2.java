package cc.duduhuo.util.pojo.derivation.samplebase.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
