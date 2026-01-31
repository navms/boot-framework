package io.navms.framework.datasource.type;

import io.navms.framework.common.base.utils.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Set<Long> 的类型转换器实现类，对应数据库的 varchar 类型
 *
 * @author navms
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class LongSetTypeHandler implements TypeHandler<Set<Long>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Set<Long> strings, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StringUtils.join(StringUtils.COMMA, strings));
    }

    @Override
    public Set<Long> getResult(ResultSet rs, String columnName) throws SQLException {
        return getResult(rs.getString(columnName));
    }

    @Override
    public Set<Long> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getResult(rs.getString(columnIndex));
    }

    @Override
    public Set<Long> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getResult(cs.getString(columnIndex));
    }

    private Set<Long> getResult(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(value.split(StringUtils.COMMA))).stream().map(Long::parseLong).collect(Collectors.toSet());
    }
}
