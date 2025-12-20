package com.lsnju.tpbase.log.boolex;

import org.apache.commons.lang3.Strings;

import ch.qos.logback.core.boolex.PropertyConditionBase;

/**
 *
 * @author ls
 * @since 2025/12/20 19:08
 * @version V1.0
 */
public abstract class AbstractPropertyConditionBase extends PropertyConditionBase {

    protected boolean isDebugMode() {
        String debugMode = this.p("logger.debug");
        return Strings.CI.equals("true", debugMode);
    }

}
