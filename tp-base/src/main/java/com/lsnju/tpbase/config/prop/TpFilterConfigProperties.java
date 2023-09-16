package com.lsnju.tpbase.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author ls
 * @since 2023/9/16 10:43
 * @version V1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "tp.web.filter")
public class TpFilterConfigProperties extends BaseMo {

    private boolean traceHeader = true;
    private boolean addResponse = true;
    private boolean overrideRemoteHost = true;

}
