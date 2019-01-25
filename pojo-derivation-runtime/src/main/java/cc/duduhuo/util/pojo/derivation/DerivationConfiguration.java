package cc.duduhuo.util.pojo.derivation;

import java.util.Map;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/25 20:00
 * Description:
 * Remarks:
 * =======================================================
 */
public interface DerivationConfiguration {
    Map<Class<?>, Map<String, Object>> classAnnotations();
}
