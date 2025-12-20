package com.lsnju.tpbase.log.boolex;

import org.apache.commons.lang3.Strings;

/**
 *
 * @author lisong
 * @since 2025/12/20 18:39
 * @version V1.0
 */
public class IsConsoleOutputCondition extends AbstractPropertyConditionBase {

    @Override
    public boolean evaluate() {
        String logEnv = this.p("log_env");
        String logOut = this.p("log_out");
        if (isDebugMode()) {
            System.err.printf("IsConsoleOutputCondition logEnv=%s, logOut=%s \n", logEnv, logOut);
        }
        return Strings.CI.contains(logEnv, "dev") || Strings.CI.contains(logOut, "console");
    }

}
