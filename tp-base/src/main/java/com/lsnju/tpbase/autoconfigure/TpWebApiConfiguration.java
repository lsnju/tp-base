package com.lsnju.tpbase.autoconfigure;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lsnju.base.jdbc.TpJdbcUtils;
import com.lsnju.base.model.JarInfo;
import com.lsnju.base.util.ClazzUtils;
import com.lsnju.base.util.TpAppInfo;
import com.lsnju.base.util.TpDateFormatUtils;
import com.lsnju.tpbase.config.prop.TpLogConfigProperties;
import com.lsnju.tpbase.config.prop.TpMoConfigProperties;
import com.lsnju.tpbase.util.VersionConfig;
import com.lsnju.tpbase.web.controller.AbstractTpController;
import com.lsnju.tpbase.web.controller.monitor.vo.CpInfo;
import com.lsnju.tpbase.web.controller.monitor.vo.SysInfo;
import com.lsnju.tpbase.web.controller.monitor.vo.ThreadPoolStatusVo;
import com.lsnju.tpbase.web.controller.monitor.vo.TpInfoVo;
import com.lsnju.tpbase.web.mvc.TpSpringWebMvcHelper;

import lombok.Setter;

/**
 *
 * @author lis614
 * @since 2025/6/21 21:12
 * @version V1.0
 */
@Configuration
@EnableConfigurationProperties({TpMoConfigProperties.class})
public class TpWebApiConfiguration {

    @Configuration
    @EnableConfigurationProperties({TpLogConfigProperties.class})
    public static class TpSysInfoApiConfig {

        @Setter
        @RestController
        public static class TpSysInfoController {

            private static final Logger log = LoggerFactory.getLogger("com.lsnju.tpbase.web.controller.monitor.TpSysInfoController");

            @Value("${spring.profiles.active:none}")
            private String profile;
            @Value("${server.port:1010}")
            private int serverPort;

            private final TpLogConfigProperties tpLogConfigProperties;
            private final TpSpringWebMvcHelper tpSpringWebMvcHelper;

            public TpSysInfoController(TpLogConfigProperties tpLogConfigProperties,
                                       ObjectProvider<@NonNull TpSpringWebMvcHelper> tpSpringWebMvcHelper) {
                this.tpLogConfigProperties = tpLogConfigProperties;
                this.tpSpringWebMvcHelper = tpSpringWebMvcHelper.getIfAvailable();
            }

            private static final ApplicationHome HOME = new ApplicationHome(TpSysInfoController.class);

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/sysinfo.json")
            public SysInfo show() {
                log.debug("x");
                final SysInfo ret = new SysInfo();
                ret.setHostname(VersionConfig.getHostname());
                ret.setPid(VersionConfig.getPid());
                ret.setPort(serverPort);
                ret.setJavaVersion(TpAppInfo.JAVA_VERSION);
                ret.setJavaVersionDate(TpAppInfo.JAVA_VERSION_DATE);
                ret.setJavaVendor(TpAppInfo.JAVA_VENDOR);
                ret.setVersion(TpAppInfo.BUILD_VERSION);
                ret.setBuildTime(TpAppInfo.BUILD_TIME);
                ret.setBuildDate(TpAppInfo.BUILD_DATE);
                ret.setStartTime(VersionConfig.getStartDate());
                ret.setNow(TpDateFormatUtils.getNewFormatDateString(new Date()));
                ret.setProfile(profile);
                ret.setLogger(tpLogConfigProperties);
                ret.setTpVersion(TpAppInfo.TP_BASE_VERSION);
                ret.setSbVersion(SpringBootVersion.getVersion());
                ret.setHomeDir(String.valueOf(HOME.getDir()));
                ret.setHomeSrc(String.valueOf(HOME.getSource()));
                ret.setCoreSize(Runtime.getRuntime().availableProcessors());
                ret.setTotalMemory(Runtime.getRuntime().totalMemory());
                ret.setMaxMemory(Runtime.getRuntime().maxMemory());
                ret.setFreeMemory(Runtime.getRuntime().freeMemory());
                if (tpSpringWebMvcHelper != null) {
                    ret.setServerUrl(tpSpringWebMvcHelper.getServerUrl());
                }
                return ret;
            }

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/dep-info.json")
            public List<JarInfo> depInfo() throws IOException {
                log.debug("depInfo");
                return ClazzUtils.allJarInfo();
            }

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/dep-simple-mf.json")
            public Map<String, String> depSimpleMf(@RequestParam(name = "sorted", defaultValue = "false", required = false) boolean sorted) throws IOException {
                log.debug("depSimpleMf");
                Map<String, String> map = new LinkedHashMap<>();
                List<JarInfo> list = ClazzUtils.allJarInfo();
                for (JarInfo item : list) {
                    map.put(item.getJarFullName(), StringUtils.defaultIfBlank(item.getMfVersion(), item.getPath()));
                }
                if (sorted) {
                    return new TreeMap<>(map);
                }
                return map;
            }

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/dep-simple-jar.json")
            public Map<String, String> depSimpleJar(@RequestParam(name = "sorted", defaultValue = "false", required = false) boolean sorted) throws IOException {
                log.debug("depSimpleJar");
                Map<String, String> map = new LinkedHashMap<>();
                List<JarInfo> list = ClazzUtils.allJarInfo();
                for (JarInfo item : list) {
                    map.put(item.getJarFullName(), StringUtils.defaultIfBlank(item.getJarVersion(), item.getPath()));
                }
                if (sorted) {
                    return new TreeMap<>(map);
                }
                return map;
            }

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/classpath-jar.json")
            public Collection<String> uselessJar() {
                log.debug("uselessJar");
                final ClassLoader classLoader = ClazzUtils.class.getClassLoader();
                Set<String> jarUrlList = ClazzUtils.getJarURLs(classLoader).stream().map(URL::getPath).collect(Collectors.toSet());
                log.info("total.jar = {}", jarUrlList.size());
                return new TreeSet<>(jarUrlList);
            }

        }
    }

    @Configuration
    public static class TpThreadPoolApiConfig {

        @Setter
        @RestController
        public static class TpThreadPoolController {

            private static final Logger log = LoggerFactory.getLogger("com.lsnju.tpbase.web.controller.monitor.TpThreadPoolController");

            private final List<ThreadPoolTaskExecutor> threadPools;
            private final List<TaskScheduler> schedulerList;

            public TpThreadPoolController(ObjectProvider<@NonNull List<ThreadPoolTaskExecutor>> threadPools,
                                          ObjectProvider<@NonNull List<TaskScheduler>> schedulerList) {
                this.threadPools = threadPools.getIfAvailable();
                this.schedulerList = schedulerList.getIfAvailable();
            }

            @GetMapping(path = "${tp.sys.mo.base-path:/tp/mo}/tp.json")
            public ThreadPoolStatusVo show() {
                log.debug("{}", threadPools);
                log.debug("{}", schedulerList);
                final ThreadPoolStatusVo ret = new ThreadPoolStatusVo();
                ret.setHostname(VersionConfig.getHostname());
                ret.setSuccess(true);
                if (threadPools != null) {
                    ret.setTpInfos(threadPools.stream().map(this::convert).collect(Collectors.toList()));
                }
                if (schedulerList != null) {
                    ret.setScheduler(schedulerList.stream().map(this::convert).collect(Collectors.toList()));
                }
                return ret;
            }

            private TpInfoVo convert(ThreadPoolTaskExecutor item) {
                if (item == null) {
                    return null;
                }
                final TpInfoVo vo = new TpInfoVo();
                vo.setName(item.getThreadNamePrefix());
                vo.setCorePoolSize(item.getCorePoolSize());
                vo.setMaxPoolSize(item.getMaxPoolSize());
                vo.setPoolSize(item.getPoolSize());
                vo.setActiveCount(item.getActiveCount());
                vo.setKeepAliveSeconds(item.getKeepAliveSeconds());

                final ThreadPoolExecutor executor = item.getThreadPoolExecutor();
                log.debug("{}", executor.getRejectedExecutionHandler());
                vo.setCompletedTaskCount(executor.getCompletedTaskCount());
                vo.setTaskCount(executor.getTaskCount());
                vo.setLargestPoolSize(executor.getLargestPoolSize());
                vo.setQueueSize(executor.getQueue().size());
                vo.setRemainingCapacity(executor.getQueue().remainingCapacity());
                return vo;
            }

            private TpInfoVo convert(TaskScheduler taskScheduler) {
                if (taskScheduler == null) {
                    return null;
                }
                if (taskScheduler instanceof ThreadPoolTaskScheduler item) {
                    final TpInfoVo vo = new TpInfoVo();
                    vo.setName(item.getThreadNamePrefix());
                    vo.setPoolSize(item.getPoolSize());
                    vo.setActiveCount(item.getActiveCount());

                    final ScheduledThreadPoolExecutor executor = item.getScheduledThreadPoolExecutor();
                    log.debug("{}", executor.getRejectedExecutionHandler());
                    vo.setCompletedTaskCount(executor.getCompletedTaskCount());
                    vo.setTaskCount(executor.getTaskCount());
                    vo.setLargestPoolSize(executor.getLargestPoolSize());
                    vo.setQueueSize(executor.getQueue().size());
                    vo.setRemainingCapacity(executor.getQueue().remainingCapacity());

                    vo.setKeepAliveSeconds(executor.getKeepAliveTime(TimeUnit.SECONDS));
                    vo.setCorePoolSize(executor.getCorePoolSize());
                    vo.setMaxPoolSize(executor.getMaximumPoolSize());
                    return vo;
                }
                final TpInfoVo vo = new TpInfoVo();
                vo.setName(taskScheduler.getClass().getName());
                return vo;
            }

        }
    }

    @Configuration
    @ConditionalOnClass(name = {"org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider"})
    public static class TpCpApiConfig {

        @Setter
        @RestController
        public static class TpCpInfoController {

            private static final Logger log = LoggerFactory.getLogger("com.lsnju.tpbase.web.controller.monitor.TpCpInfoController");

            private final Collection<DataSource> dataSources;
            private final DataSourcePoolMetadataProvider poolMetadataProvider;

            private static final String[] FIELD_NAMES = {"jdbcUrl", "poolName"};

            public TpCpInfoController(Collection<DataSource> dataSources, Collection<DataSourcePoolMetadataProvider> metadataProviders) {
                this.dataSources = dataSources;
                this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(metadataProviders);
            }

            @GetMapping("${tp.sys.mo.base-path:/tp/mo}/cp.json")
            public List<CpInfo> show() {
                log.debug("x");
                final List<CpInfo> ret = new ArrayList<>();
                if (CollectionUtils.isEmpty(dataSources)) {
                    return ret;
                }
                for (DataSource ds : dataSources) {
                    final CpInfo info = new CpInfo();
                    try {
                        info.setName(StringUtils.substringAfterLast(getPoolName(ds), "//"));
                        info.setDetail(TpJdbcUtils.connectionInfo(ds));
                    } catch (Exception ignore) {
                    }
                    final DataSourcePoolMetadata metadata = poolMetadataProvider.getDataSourcePoolMetadata(ds);
                    if (metadata != null) {
                        info.setActive(metadata.getActive());
                        info.setMax(metadata.getMax());
                        info.setMin(metadata.getMin());
                        info.setUsage(metadata.getUsage());
                    }
                    ret.add(info);
                }
                return ret;
            }

            private String getPoolName(DataSource ds) {
                for (String field : FIELD_NAMES) {
                    try {
                        Object value = Reflect.on(ds).field(field).get();
                        if (value != null) {
                            return value.toString();
                        }
                    } catch (ReflectException ignore) {
                    }
                }
                return ds.getClass().getSimpleName();
            }

        }

    }

    @Configuration
    @ConditionalOnClass(name = {"org.springframework.web.servlet.HandlerExceptionResolver", "jakarta.servlet.http.HttpServletRequest"})
    public static class TpHomePageConfig {

        @Setter
        @RestController
        @RequestMapping(path = "${tp.sys.mo.page-path:/tp}")
        public static class TpHomeController extends AbstractTpController implements InitializingBean {

            private static final Logger log = LoggerFactory.getLogger("com.lsnju.tpbase.web.controller.TpHomeController");

            @Value("${spring.datasource.url:none}")
            private String url;
            @Value("${swagger.enabled:xx}")
            private String swaggerEnable;
            @Value("${springdoc.api-docs.enabled:xx}")
            private String springDocApiEnable;
            @Value("${springdoc.swagger-ui.enabled:xx}")
            private String springDocUiEnable;

            @Override
            protected Logger log() {
                return log;
            }

            @Override
            public void afterPropertiesSet() {
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
                String path = tpMoConfigProperties.getPagePath() + "/debug";
                log.info("{}", path);
                afterPropertiesSet();
                return getMsg(path);
            }

        }
    }

}
