package com.lsnju.base.money;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import lombok.extern.slf4j.Slf4j;

/**
 * Money类型转换处理器
 *
 * @author ls
 * @since 2023-07-20 07:28:29
 * @version V1.0
 */
@Slf4j
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType)
        throws SQLException {
        log.debug("{}, {}, {}", i, parameter, jdbcType);
        if (parameter == null) {
            ps.setLong(i, 0L);
        } else {
            ps.setLong(i, parameter.getCent());
        }
    }

    @Override
    public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.debug("columnName={}", columnName);
        long amount = rs.getLong(columnName);
        return new Money().setCent(amount);
    }

    @Override
    public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.debug("columnIndex={}", columnIndex);
        long amount = rs.getLong(columnIndex);
        return new Money().setCent(amount);
    }

    @Override
    public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        log.debug("columnIndex={}", columnIndex);
        long amount = cs.getLong(columnIndex);
        return new Money().setCent(amount);
    }
}
