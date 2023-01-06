package cc.duduhuo.util.pojo.derivation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/2/2 21:14
 * Description: field 重新定义
 * Remarks:
 * =======================================================
 */
@Retention(SOURCE)
@Documented
public @interface DerivationFieldDefinition {
    /**
     * field 名称
     */
    String name();

    /**
     * field 初始值（数组长度只能为0或1，空数组表示没有初始值）
     */
    String[] initialValue() default {};

    /**
     * field 类型。
     * 如 int 类型请指定为 int.class；List&lt;String&gt; 类型请指定为 {List.class, String.class}
     */
    Class<?>[] type() default {};
}
