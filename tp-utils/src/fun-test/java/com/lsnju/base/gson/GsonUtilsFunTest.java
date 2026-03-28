package com.lsnju.base.gson;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 *
 * @author lis614
 * @since 2026-03-28 09:39
 * @version V1.0
 */
public class GsonUtilsFunTest {

    /**
     * 使用 {@link JsonParser} / {@link JsonElement} 读取字段的语义“原始”字符串。
     * <p>{@code tag} 不含 {@code '.'} 时：在整棵树中按对象键的 JSON 出现顺序做深度优先搜索，取<strong>第一个</strong>同名键，
     * 与 {@link #getRawValue} 在常规紧凑 JSON（键与 {@code :} 之间无空格）下效果一致。
     * <p>{@code tag} 含 {@code '.'} 时：按路径分段从<strong>根对象</strong>逐级向下解析（如 {@code a.b.c} 表示 {@code root["a"]["b"]["c"]}），
     * 根必须为 JSON 对象；任一段缺失或非对象则返回空串。
     * <ul>
     *   <li>非法 JSON：返回空串；{@code json}/{@code tag} 空白：返回空串；路径分段为空：返回空串。</li>
     *   <li>未找到字段：返回空串；字段值为 JSON {@code null}：返回 {@code null}。</li>
     *   <li>字符串、数字、布尔：{@link com.google.gson.JsonPrimitive#getAsString()}。</li>
     *   <li>对象或数组：与本类 Gson 实例一致的紧凑 JSON（可能与 {@link #getRawValue} 的原文切片空白不同，语义相同）。</li>
     * </ul>
     */
    public static String getRawValue2(final String json, final String tag) {
        if (StringUtils.isBlank(json) || StringUtils.isBlank(tag)) {
            return StringUtils.EMPTY;
        }
        try {
            JsonElement root = JsonParser.parseString(json);
            if (root == null) {
                return StringUtils.EMPTY;
            }
            JsonElement el;
            if (StringUtils.contains(tag, '.')) {
                String[] segments = StringUtils.split(tag, '.');
                if (segments == null || segments.length == 0) {
                    return StringUtils.EMPTY;
                }
                if (!root.isJsonObject()) {
                    return StringUtils.EMPTY;
                }
                el = navigatePath(root.getAsJsonObject(), segments);
            } else {
                el = findFirstByKeyDfs(root, tag);
            }
            return elementToRawString(el);
        } catch (JsonSyntaxException e) {
            return StringUtils.EMPTY;
        }
    }

    private static JsonElement navigatePath(JsonObject root, String[] segments) {
        JsonElement cur = root;
        for (String seg : segments) {
            if (StringUtils.isEmpty(seg) || cur == null || !cur.isJsonObject()) {
                return null;
            }
            JsonObject obj = cur.getAsJsonObject();
            if (!obj.has(seg)) {
                return null;
            }
            cur = obj.get(seg);
        }
        return cur;
    }

    /**
     * 深度优先：先按当前对象键声明顺序匹配 {@code key}，再递归进入每个值（与文本中首个 {@code "key":} 在常见 JSON 中一致）。
     */
    private static JsonElement findFirstByKeyDfs(JsonElement node, String key) {
        if (node.isJsonObject()) {
            for (Map.Entry<String, JsonElement> e : node.getAsJsonObject().entrySet()) {
                if (key.equals(e.getKey())) {
                    return e.getValue();
                }
                JsonElement found = findFirstByKeyDfs(e.getValue(), key);
                if (found != null) {
                    return found;
                }
            }
        } else if (node.isJsonArray()) {
            for (JsonElement child : node.getAsJsonArray()) {
                JsonElement found = findFirstByKeyDfs(child, key);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static String elementToRawString(JsonElement el) {
        if (el == null) {
            return StringUtils.EMPTY;
        }
        if (el.isJsonNull()) {
            return null;
        }
        if (el.isJsonPrimitive()) {
            return el.getAsJsonPrimitive().getAsString();
        }
        return GsonUtils.toJson(el);
    }

}
