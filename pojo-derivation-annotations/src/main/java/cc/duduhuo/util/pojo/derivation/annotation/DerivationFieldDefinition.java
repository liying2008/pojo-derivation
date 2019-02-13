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
     * 变量名称
     */
    String name();

    /**
     * 变量初始值（数组长度只能为0或1，空数组表示没有初始值）
     */
    String[] initialValue() default {};

    /**
     * 变量类型
     */
    Class<?> type() default DefaultType.class;
}
