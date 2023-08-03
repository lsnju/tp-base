package com.lsnju.tpbase.debug.env;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 *
 * @author ls
 * @since 2021/4/7 16:02
 * @version V1.0
 */
public abstract class AbstractEnvShow {

    private static final String TAG = "via location ";

    public static void showEnv(Logger log, ConfigurableEnvironment environment) {
        Map<String, Map<String, Object>> allMap = new LinkedHashMap<>();
        log.info("{}", environment);
        log.info("{}", environment.getClass());
        log.info("active  {}", Arrays.toString(environment.getActiveProfiles()));
        log.info("default {}", Arrays.toString(environment.getDefaultProfiles()));

        final MutablePropertySources ps = environment.getPropertySources();
        StringBuilder envSb = new StringBuilder();
        for (PropertySource<?> p : ps) {
//            log.info("{}, {}, {}", p instanceof EnumerablePropertySource, p.getName(), p);
            envSb.append(System.lineSeparator()).append("+ ");
            envSb.append(String.format("%5s", p instanceof EnumerablePropertySource)).append(", ");
            envSb.append(String.format("%-32s", getPsName(p))).append(", ");
            envSb.append(p.toString());
            if (p instanceof EnumerablePropertySource) {
                EnumerablePropertySource<?> ep = (EnumerablePropertySource<?>) p;
                Map<String, Object> props = new LinkedHashMap<>();
                for (String name : ep.getPropertyNames()) {
                    props.put(name, ep.getProperty(name));
                }
                allMap.put(p.getName(), props);
            }
        }
        log.info("env ------------------------------------------------{}", envSb.toString());

        log.debug("=====================================================");
        allMap.forEach((n, m) -> {
            log.debug("******************************************");
            log.debug("--- {} ---", n);
            log.debug("******************************************");
            StringBuilder sb = new StringBuilder(System.lineSeparator());
            new TreeMap<>(m).forEach((k, v) -> {
                sb.append(">>> ").append(k).append("=").append(v).append(System.lineSeparator());
            });
            log.debug("{}", sb.toString());
        });
        log.debug("=====================================================");
    }

    private static String getPsName(PropertySource<?> p) {
        String name = p.getName();
        if (StringUtils.length(name) <= 28) {
            return name;
        }
        // Config resource 'class path resource [config/application-dev.properties]' via location 'optional:classpath:/config/'
        if (StringUtils.contains(name, TAG)) {
            return StringUtils.substringAfterLast(name, TAG);
        }
        return name;
    }

}
