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
 * Description: 注解在 field 或 class 上，表示构造方法中不包含此 field 或 class
 * Remarks:
 * =======================================================
 */
@Retention(SOURCE)
@Target({FIELD, TYPE})
@Documented
public @interface DerivationConstructorExclude {
    /**
     * 被注解的属性或类将不会出现在<b>指定的生成的类（即 classnames）</b>的构造方法中。
     * classnames 为空，表示被注解的属性或类将不会出现在<b>所有生成的类</b>的构造方法中
     */
    String[] classnames() default {};
}
