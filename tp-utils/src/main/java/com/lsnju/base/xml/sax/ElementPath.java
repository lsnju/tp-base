package com.lsnju.base.xml.sax;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lisong
 * @since 2023/3/24 14:49
 * @version V1.0
 */
public class ElementPath {

    private final List<String> partList = new ArrayList<>();

    public ElementPath() {
    }

    public ElementPath(List<String> list) {
        partList.addAll(list);
    }

    /**
     * Build an elementPath from a string.
     * <p/>
     * Note that "/x" is considered equivalent to "x" and to "x/"
     */
    public ElementPath(String pathStr) {
        if (pathStr == null) {
            return;
        }

        String[] partArray = pathStr.split("/");
        for (String part : partArray) {
            if (!part.isEmpty()) {
                partList.add(part);
            }
        }
    }

    public ElementPath duplicate() {
        return new ElementPath(this.partList);
    }

    public String getTagPath() {
        StringBuilder sb = new StringBuilder();
        for (String item : partList) {
            sb.append("/").append(item);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ElementPath)) {
            return false;
        }

        ElementPath r = (ElementPath) o;
        if (r.size() != size()) {
            return false;
        }

        int len = size();
        for (int i = 0; i < len; i++) {
            if (!equalityCheck(get(i), r.get(i))) {
                return false;
            }
        }
        // if everything matches, then the two patterns are equal
        return true;
    }

    private boolean equalityCheck(String x, String y) {
        return x.equalsIgnoreCase(y);
    }

    public List<String> getCopyOfPartList() {
        return new ArrayList<>(partList);
    }

    public void push(String s) {
        partList.add(s);
    }

    public String get(int i) {
        return partList.get(i);
    }

    public void pop() {
        if (!partList.isEmpty()) {
            partList.remove(partList.size() - 1);
        }
    }

    public String peekLast() {
        if (!partList.isEmpty()) {
            int size = partList.size();
            return partList.get(size - 1);
        } else {
            return null;
        }
    }

    public int size() {
        return partList.size();
    }

    protected String toStableString() {
        StringBuilder result = new StringBuilder();
        for (String current : partList) {
            result.append("[").append(current).append("]");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return toStableString();
    }
}
