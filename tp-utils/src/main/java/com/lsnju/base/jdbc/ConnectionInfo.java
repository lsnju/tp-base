package com.lsnju.base.jdbc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author lis614
 * @since 2024/1/9 14:36
 * @version V1.0
 */
@Getter
@Builder
@ToString
public class ConnectionInfo {
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
