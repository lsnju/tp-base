package com.lsnju.base.jdbc;

import com.lsnju.base.model.BaseMo;

import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author lis614
 * @since 2024/1/9 14:36
 * @version V1.0
 */
@Getter
@Builder
public class ConnectionInfo extends BaseMo {
    private final String catalog;
    private final String schema;
    private final String driverName;
    private final String driverVersion;
    private final String productName;
    private final String productVersion;
    private final String majorVersion;
    private final String minorVersion;
    private final String jdbcMajorVersion;
    private final String jdbcMinorVersion;
}
