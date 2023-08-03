package com.lsnju.tpbase.daemon;

import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;

/**
 *
 * @author lisong
 * @since 2022/4/28 9:58
 * @version V1.0
 */
public abstract class AbstractTpTaskConfig {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String METHOD = "run";

    protected JobDetail getJobDetail(AbstractTask task) throws ClassNotFoundException, NoSuchMethodException {
        final MethodInvokingJobDetailFactoryBean job = new MethodInvokingJobDetailFactoryBean();
        job.setTargetObject(task);
        job.setTargetMethod(METHOD);
        job.afterPropertiesSet();
        return job.getObject();
    }

}
