package top.fuyuaaa.shadowpuppets.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressCarrierEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 如果枚举类的code不是(从0开始, 并且递增的)，需要创建一个自定义枚举类Handle
 *
 * @author: fuyuaaa
 * @creat: 2019-04-26 12:45
 */
@SuppressWarnings("unused")
public class ExpressCarrierEnumHandle extends BaseTypeHandler<ExpressCarrierEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ExpressCarrierEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.code());
    }

    @Override
    public ExpressCarrierEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ExpressCarrierEnum.find(rs.getInt(columnName));
    }

    @Override
    public ExpressCarrierEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ExpressCarrierEnum.find(rs.getInt(columnIndex));
    }

    @Override
    public ExpressCarrierEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ExpressCarrierEnum.find(cs.getInt(columnIndex));
    }
}
