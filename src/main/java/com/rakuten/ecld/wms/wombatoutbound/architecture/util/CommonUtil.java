package com.rakuten.ecld.wms.wombatoutbound.architecture.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Stream;

public class CommonUtil {

    private CommonUtil() {}

    public static <T> Stream<T> returnEmptyStreamIfNullOrEmpty(final Collection<T> object) {
        if (CollectionUtils.isEmpty(object))
            return Stream.empty();
        return object.stream();
    }
}
