package com.github.codetanzania.feature.company.faq;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GroupBy {

    private static final String TAG = "WaterTariffs";

    public interface ValueExtractor<A, B> {
        B extract(A in);
    }

    public static <U, T> Map<U, List<T>> apply(List<T> lItems, ValueExtractor<T, U> valueExtractor) {

        Map<U, List<T>> retVal = new ArrayMap<>();

        for (T item : lItems) {
            U key = valueExtractor.extract(item);
            if (retVal.containsKey(key)) {
                retVal.get(key).add(item);
            } else {
                List<T> newList = new ArrayList<>();
                newList.add(item);
                retVal.put(key, newList);
            }
        }

        return retVal;
    }

    public static <U, T> Map<U, List<T>> apply(T[] tItems, ValueExtractor<T, U> valueExtractor) {
        return apply(Arrays.asList(tItems), valueExtractor);
    }
}
