package cc.duduhuo.util.pojo.derivation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/27 18:18
 * Description: 注解在 field 上，给 field 加上初始值
 * Remarks:
 * =======================================================
 */
@Retention(SOURCE)
@Target({FIELD})
@Documented
public @interface DerivationField {
    /**
     * 变量初始值
     *
     * @return
     */
    String initialValue();
}