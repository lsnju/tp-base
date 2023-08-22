package com.lsnju.tpbase.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;

/**
 *
 * @author lisong
 * @since 2021/12/16 9:57
 * @version V1.0
 */
@Setter
@RestController
@ConditionalOnClass(name = {"org.springframework.web.servlet.HandlerExceptionResolver"})
@RequestMapping(path = "${tp.sys.mo.page-path:/tp}")
public class TpHomeController extends AbstractTpController {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${swagger.enabled}")
    private String swaggerEnable;
    @Value("${springdoc.api-docs.enabled:xx}")
    private String springDocApiEnable;
    @Value("${springdoc.swagger-ui.enabled:xx}")
    private String springDocUiEnable;

    @PostConstruct
    public void setup() {
        log.info("appName            = {}", getAppName());
        log.info("db-url             = {}", url);
        log.info("swaggerEnable      = {}", swaggerEnable);
        log.info("springDocApiEnable = {}", springDocApiEnable);
        log.info("springDocUiEnable  = {}", springDocUiEnable);
        if (tpSpringWebMvcHelper != null) {
            log.debug("{}", tpSpringWebMvcHelper.getResolvers());
        }
    }

    @GetMapping()
    public String tpHome(HttpServletRequest request) {
        final String path = tpMoConfigProperties.getPagePath();
        log.info("{}", path);
        logInfo(request, true);
        return getMsg(path);
    }

    @GetMapping(path = {"/debug"})
    public String tpDebug() {
        String path = "" + tpMoConfigProperties.getPagePath() + "/debug";
        log.info("{}", path);
        setup();
        return getMsg(path);
    }

}
