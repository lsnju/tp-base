package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2023/9/22 10:46
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tp.aop")
public class TpAopConfigProperties extends BaseMo {
    private boolean useSpring = true;
}
