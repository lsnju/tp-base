package com.lsnju.tpbase.debug.env;

import javax.annotation.PostConstruct;

import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author ls
 * @since 2021/4/2 16:02
 * @version V1.0
 */
@Slf4j
public class EnvShowConfig extends AbstractEnvShow {

    private final ConfigurableEnvironment env;

    public EnvShowConfig(ConfigurableEnvironment env) {
        this.env = env;
    }

    @PostConstruct
    public void setup() {
        showEnv(log, env);
    }

}
