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

/**
 * List<Long> 的类型转换器实现类，对应数据库的 varchar 类型
 *
 * @author navms
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class LongListTypeHandler implements TypeHandler<List<Long>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Long> strings, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StringUtils.join(StringUtils.COMMA, strings));
    }

    @Override
    public List<Long> getResult(ResultSet rs, String columnName) throws SQLException {
        return getResult(rs.getString(columnName));
    }

    @Override
    public List<Long> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getResult(rs.getString(columnIndex));
    }

    @Override
    public List<Long> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getResult(cs.getString(columnIndex));
    }

    private List<Long> getResult(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(value.split(StringUtils.COMMA))).stream().map(Long::parseLong).toList();
    }
}
