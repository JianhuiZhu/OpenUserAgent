package com.jianhui_zhu.openuseragent.util;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Reference from https://gist.github.com/mmarcon
 */
public class MapUtil {
    public static Bundle toBundle(Map<String, ? extends Parcelable> input) {
        Bundle output = new Bundle();
        for (String key : input.keySet()) {
            output.putParcelable(key, input.get(key));
        }
        return output;
    }

    public static <T extends Parcelable> Map<String, T> fromBundle(Bundle input, Class<T> c) {
        Map<String, T> output = new HashMap<>();
        for (String key : input.keySet()) {
            output.put(key, c.cast(input.getParcelable(key)));
        }
        return output;
    }
}
