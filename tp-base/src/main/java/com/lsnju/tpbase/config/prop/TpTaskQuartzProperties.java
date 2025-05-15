package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author lis614
 * @since 2025/5/15 08:43
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tp.quartz")
public class TpTaskQuartzProperties {
    private boolean enable;
}
