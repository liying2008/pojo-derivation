package cc.duduhuo.util.pojo.derivation.sample.anno;

import java.lang.annotation.*;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/24 23:19
 * Description:
 * Remarks:
 * =======================================================
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface TestAnno1 {
    String value() default "";
}
