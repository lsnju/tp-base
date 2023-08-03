package com.lsnju.tpbase.web.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.PatternMatchUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-26 20:23:25
 * @version V1.0
 */
@Slf4j
public class PageMatcher {

    private static final int default_size = 8;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    @Getter
    private final List<List<PageAuthItem>> groups = new ArrayList<>(default_size);

    public PageMatcher(Resource[] rs) {
        for (int i = 0; i < default_size; i++) {
            groups.add(i, new ArrayList<>());
        }
        for (Resource r : rs) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(r.getInputStream(), UTF_8))) {
                setup(br);
            } catch (IOException e) {
                log.error(String.format("%s", e.getMessage()), e);
            }
        }
        log.debug("groups={}", groups);
    }

    public String[] getUrlPermConf(String url) {
        for (int i = groups.size() - 1; i >= 0; i--) {
            List<PageAuthItem> list = groups.get(i);
            for (PageAuthItem item : list) {
                if (PatternMatchUtils.simpleMatch(item.getUrl(), url)) {
                    return item.getPermissions();
                }
            }
        }
        return null;
    }

    private void setup(BufferedReader br) {
        for (; ; ) {
            try {
                String str = br.readLine();
                if (str == null) {
                    break;
                }

                str = StringUtils.trim(str);
                str = StringUtils.remove(str, " ");
                if (StringUtils.isBlank(str)) {
                    continue;
                }

                if (StringUtils.startsWith(str, "#")) {
                    continue;
                }

                String[] ss = StringUtils.split(str, '>');
                if (ss.length < 2) {
                    log.warn("{}", str);
                    continue;
                }

                String url = StringUtils.trim(ss[0]);
                url = RegExUtils.replaceAll(url, "\\*+", "*");
                String permissions = StringUtils.trim(ss[1]);

                int level = StringUtils.countMatches(url, "/");
                if (StringUtils.endsWith(url, "*")) {
                    level--;
                }

                if (level < 0) {
                    continue;
                }
                if (level >= groups.size()) {
                    log.warn("[error url]={}", url);
                    continue;
                }
                List<PageAuthItem> list = groups.get(level);
                PageAuthItem item = new PageAuthItem();
                item.setUrl(url);
                item.setPermissions(StringUtils.split(permissions, '|'));
                list.add(item);
            } catch (IOException e) {
                log.error(String.format("%s", e.getMessage()), e);
                return;
            }
        }
    }
}
