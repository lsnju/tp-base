package com.lsnju.tpbase.util;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2023-07-27 07:19:56
 * @version V1.0
 */
@Slf4j
public class FreemarkerStaticModels extends HashMap<String, TemplateModel> {
    /** */
    private static final long serialVersionUID = 5355259926819984215L;

    private static final BeansWrapper BEANS_WRAPPER;
    private static FreemarkerStaticModels FREEMARKER_STATIC_MODELS;

    static {
        // Create the builder:
        BeansWrapperBuilder builder = new BeansWrapperBuilder(freemarker.template.Configuration.VERSION_2_3_30);
        // Set desired BeansWrapper configuration properties:
        builder.setUseModelCache(true);
        builder.setExposeFields(true);

        // Get the singleton:
        BEANS_WRAPPER = builder.build();
        // You don't need the builder anymore.
    }

    private Properties staticModels;

    private FreemarkerStaticModels() {
    }

    public static FreemarkerStaticModels getInstance() {
        if (FREEMARKER_STATIC_MODELS == null) {
            FREEMARKER_STATIC_MODELS = new FreemarkerStaticModels();
        }
        return FREEMARKER_STATIC_MODELS;
    }

    public static TemplateModel getStaticPackage(String packageName) {
        log.debug("item = {}", packageName);
        try {
            TemplateHashModel staticModels = BEANS_WRAPPER.getStaticModels();
            return staticModels.get(packageName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Properties getStaticModels() {
        return staticModels;
    }

    public void setStaticModels(Properties staticModels) {
        log.debug("{}", staticModels);
        if (this.staticModels == null && staticModels != null) {
            this.staticModels = staticModels;
            Set<String> keys = this.staticModels.stringPropertyNames();
            for (String key : keys) {
                FREEMARKER_STATIC_MODELS.put(key, getStaticPackage(this.staticModels.getProperty(key)));
            }
        }
    }
}
