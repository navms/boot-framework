package io.navms.framework.datasource.extension;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.base.MPJBaseMapper;
import io.navms.framework.common.base.utils.CollectionUtils;
import io.navms.framework.datasource.domain.PageQuery;
import io.navms.framework.datasource.utils.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 扩展 BaseMapper
 *
 * @author navms
 */
public interface ExtensionBaseMapper<T> extends MPJBaseMapper<T> {

    default T selectOne(String field, Object value) {
        return selectOne(new QueryWrapper<T>().eq(field, value));
    }

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default T selectOne(String field1, Object value1, String field2, Object value2,
                        String field3, Object value3) {
        return selectOne(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
                        SFunction<T, ?> field3, Object value3) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
    }

    default Long selectCount() {
        return selectCount(new QueryWrapper<>());
    }

    default Long selectCount(String field, Object value) {
        return selectCount(new QueryWrapper<T>().eq(field, value));
    }

    default Long selectCount(SFunction<T, ?> field, Object value) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default Long selectCount(String field1, Object value1, String field2, Object value2) {
        return selectCount(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default Long selectCount(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default Long selectCount(String field1, Object value1, String field2, Object value2,
                             String field3, Object value3) {
        return selectCount(new QueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
    }

    default Long selectCount(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2,
                             SFunction<T, ?> field3, Object value3) {
        return selectCount(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2).eq(field3, value3));
    }

    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(String field, Object value) {
        return selectList(new QueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectList(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default List<T> selectList(String field, Collection<?> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return selectList(new QueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    default int delete(String field, String value) {
        return delete(new QueryWrapper<T>().eq(field, value));
    }

    default int delete(SFunction<T, ?> field, Object value) {
        return delete(new LambdaQueryWrapper<T>().eq(field, value));
    }

    /**
     * 分页查询
     *
     * @param pageQuery    分页参数
     * @param queryWrapper 查询条件
     * @return 分页数据
     */
    default IPage<T> selectPage(PageQuery pageQuery, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return selectPage(PageUtil.buildPage(pageQuery), queryWrapper);
    }

    /**
     * 批量插入
     *
     * @param entityList 实体列表
     * @return 插入数量
     */
    int insertBatchSomeColumn(Collection<T> entityList);

    /**
     * 强制更新
     *
     * @param entity 实体
     * @return 更新数量
     */
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

    /**
     * 查询一条数据
     *
     * @param queryWrapper 查询条件
     * @return 数据
     */
    default <R, Children extends AbstractWrapper<T, R, Children>> T selectLimitOne(
            @Param(Constants.WRAPPER) AbstractWrapper<T, R, Children> queryWrapper) {
        queryWrapper.last("limit 1");
        return selectOne(queryWrapper);
    }

}
