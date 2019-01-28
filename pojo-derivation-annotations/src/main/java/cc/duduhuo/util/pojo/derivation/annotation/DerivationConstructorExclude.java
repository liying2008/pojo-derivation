package cc.duduhuo.util.pojo.derivation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/27 21:31
 * Description: 注解在 field 上，表示构造方法中不包含此 field
 * Remarks:
 * =======================================================
 */
@Retention(SOURCE)
@Target({FIELD, TYPE})
@Documented
public @interface DerivationConstructorExclude {
    /**
     * 表示如下类中的构造方法中不含该属性/类对象
     * 空：表示生成的所有类的构造方法中都不含该属性/类对象
     */
    String[] classNames() default {};
}
