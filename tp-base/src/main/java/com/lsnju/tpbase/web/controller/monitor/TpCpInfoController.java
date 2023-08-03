package com.lsnju.tpbase.web.controller.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;
import org.joor.ReflectException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsnju.base.model.BaseMo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2022/1/21 13:44
 * @version V1.0
 */
@Slf4j
@Setter
@ConditionalOnClass(name = {"org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider"})
@RestController
public class TpCpInfoController {

    private final Collection<DataSource> dataSources;
    private final DataSourcePoolMetadataProvider poolMetadataProvider;

    private static final String[] FIELD_NAMES = {"jdbcUrl", "poolName"};

    public TpCpInfoController(Collection<DataSource> dataSources, Collection<DataSourcePoolMetadataProvider> metadataProviders) {
        this.dataSources = dataSources;
        this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(metadataProviders);
    }

    @GetMapping("${tp.sys.mo.base-path}/cp.json")
    public List<CpInfo> show() {
        log.debug("x");
        final List<CpInfo> ret = new ArrayList<>();
        if (CollectionUtils.isEmpty(dataSources)) {
            return ret;
        }
        for (DataSource ds : dataSources) {
            final DataSourcePoolMetadata metadata = poolMetadataProvider.getDataSourcePoolMetadata(ds);
            if (metadata == null) {
                continue;
            }
            final CpInfo info = new CpInfo();
            info.setName(StringUtils.substringAfterLast(getPoolName(ds), "//"));
            info.setActive(metadata.getActive());
            info.setMax(metadata.getMax());
            info.setMin(metadata.getMin());
            info.setUsage(metadata.getUsage());
            ret.add(info);
        }
        return ret;
    }

    private String getPoolName(DataSource ds) {
        for (String field : FIELD_NAMES) {
            try {
                return Reflect.on(ds).field(field).get();
            } catch (ReflectException ignore) {
            }
        }
        return ds.getClass().getSimpleName();
    }

    @Getter
    @Setter
    static class CpInfo extends BaseMo {
        private String name;
        private Integer max;
        private Integer min;
        private Integer active;
        private Float usage;
    }

}
