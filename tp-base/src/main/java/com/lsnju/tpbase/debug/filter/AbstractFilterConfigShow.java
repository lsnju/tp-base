package com.lsnju.tpbase.debug.filter;

import java.util.List;

import com.lsnju.tpbase.util.OrderUtils;

/**
 *
 * @author ls
 * @since 2022/12/1 21:52
 * @version V1.0
 */
public abstract class AbstractFilterConfigShow {

    protected static final String TEMP = "%12s, %-64s";
    protected static final String SEP = "------------------------------";
    protected static final String TAG_1 = "[";
    protected static final String TAG_2 = "] - size=";

    public static String toPrettyString(String tag, List<?> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(SEP).append(System.lineSeparator());
        sb.append(TAG_1).append(tag).append(TAG_2).append(list.size()).append(System.lineSeparator());
        for (Object item : list) {
            sb.append(String.format(TEMP, OrderUtils.getOrder(item), item)).append(System.lineSeparator());
        }
        return sb.toString();
    }

}

