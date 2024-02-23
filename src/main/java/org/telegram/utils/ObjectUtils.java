package org.telegram.utils;

import java.util.Objects;

public class ObjectUtils {
    @SafeVarargs
    public static <T> T firstNonNull(T... rest) {
        for (T object : rest)
            if (Objects.nonNull(object))
                return object;
        return null;
    }
}
