package com.lsnju.base.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 *
 * @author lis614
 * @since 2024/1/9 14:27
 * @version V1.0
 */
public class TpJdbcUtils {

    public static ConnectionInfo connectionInfo(DataSource ds) throws SQLException {
        return connectionInfo(ds.getConnection());
    }

    public static ConnectionInfo connectionInfo(Connection connection) throws SQLException {
        final DatabaseMetaData metaData = connection.getMetaData();
        return ConnectionInfo.builder()
            .catalog(connection.getCatalog())
            .schema(connection.getSchema())
            .driverName(metaData.getDriverName())
            .driverVersion(metaData.getDriverVersion())
            .productName(metaData.getDatabaseProductName())
            .productVersion(metaData.getDatabaseProductVersion())
            .majorVersion(String.valueOf(metaData.getDatabaseMajorVersion()))
            .minorVersion(String.valueOf(metaData.getDatabaseMinorVersion()))
            .jdbcMajorVersion(String.valueOf(metaData.getJDBCMajorVersion()))
            .jdbcMinorVersion(String.valueOf(metaData.getJDBCMinorVersion()))
            .build();
    }

}
