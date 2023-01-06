package cc.duduhuo.util.pojo.derivation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/20 18:07
 * Description: POJO Derivation 描述
 * Remarks:
 * =======================================================
 */
@Retention(SOURCE)
@Target({TYPE})
@Documented
public @interface Derivation {
    /**
     * 衍生出的类型名字
     */
    String name();

    /**
     * 需要继承的超类
     */
    Class<?> superClass() default Object.class;

    /**
     * 需要实现的接口
     */
    Class<?>[] superInterfaces() default {};

    /**
     * 源 POJO 类型，将会按照数组的顺序逐个解析
     * 排在前面的类的优先级高，即同名属性优先采用前面的类中的
     */
    Class<?>[] sourceTypes() default {};

    /**
     * 衍生类型需包含的属性列表
     * 默认为全部包含
     */
    String[] includeFields() default {};

    /**
     * 衍生类型需排除的属性列表
     * 当 {@link Derivation#includeFields()} 为空时才会生效
     */
    String[] excludeFields() default {};

    /**
     * 需要排除的构造方法中的参数
     * 功能类似于：{@link DerivationConstructorExclude}
     */
    String[] excludeConstructorParams() default {};

    /**
     * 需统一排除的属性注解
     */
    Class<?>[] excludeFieldAnnotations() default {};

    /**
     * 构造方法类型
     */
    ConstructorType[] constructorTypes() default {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS};

    /**
     * 自定义的 serialVersionUID
     */
    long serialVersionUID() default 0L;

    /**
     * field 重新定义。
     */
    DerivationFieldDefinition[] fieldDefinitions() default {};
}
