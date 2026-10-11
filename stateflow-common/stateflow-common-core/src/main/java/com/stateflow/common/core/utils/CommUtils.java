package com.stateflow.common.core.utils;

import java.math.BigDecimal;
import java.util.Collection;

public class CommUtils {
    /**
     * 保证bigdecimal一定大于等于0
     */
    public static BigDecimal defaultZeroBigDecimal(BigDecimal bigDecimal) {
        if (bigDecimal == null || bigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return bigDecimal;
    }

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isCollectionNotEmpty(Collection<?> collection) {
        return !isCollectionEmpty(collection);
    }
}
