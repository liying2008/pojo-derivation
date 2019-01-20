package cc.duduhuo.util.pojo.derivation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:07
 * Description:
 * Remarks:
 * =======================================================
 */
@Retention(CLASS)
@Target({TYPE})
@Documented
public @interface Derivation {
    /**
     * 衍生出的类型名字
     *
     * @return
     */
    String name();

    /**
     * 源 POJO 类型，将会按照数组的顺序逐个解析
     * 排在前面的类的优先级高，即同名属性优先采用前面的类中的
     *
     * @return
     */
    Class<?>[] sourceTypes() default {};

    /**
     * 衍生类型需包含的属性列表
     * 默认为全部包含
     *
     * @return
     */
    String[] includeProperties() default {};

    /**
     * 衍生类型需排除的属性列表
     * 当 {@link Derivation#includeProperties()} 为空时才会生效
     *
     * @return
     */
    String[] excludeProperties() default {};

    /**
     * 需统一排除的属性注解
     *
     * @return
     */
    Class<?>[] excludePropertyAnnotations() default {};

}
