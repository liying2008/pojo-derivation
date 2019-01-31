package cc.duduhuo.util.pojo.derivation.sample.readme;

import cc.duduhuo.util.pojo.derivation.annotation.ConstructorType;
import cc.duduhuo.util.pojo.derivation.annotation.Derivation;

/**
 * =======================================================
 * Author: liying - liruoer2008@yeah.net
 * Datetime: 2019/1/31 22:31
 * Description:
 * Remarks:
 * =======================================================
 */
@Derivation(
        name = "ABC",
        sourceTypes = {A.class, B.class, C.class},
        constructorTypes = {ConstructorType.NO_ARGS, ConstructorType.ALL_ARGS, ConstructorType.ALL_SOURCE_OBJS}
)
class ABCCombine {
}
