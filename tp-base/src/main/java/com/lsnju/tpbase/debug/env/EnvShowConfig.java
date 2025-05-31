package com.lsnju.tpbase.debug.env;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2021/4/2 16:02
 * @version V1.0
 */
@Slf4j
public class EnvShowConfig extends AbstractEnvShow implements InitializingBean {

    private final ConfigurableEnvironment env;

    public EnvShowConfig(ConfigurableEnvironment env) {
        this.env = env;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        showEnv(log, env);
    }

}
