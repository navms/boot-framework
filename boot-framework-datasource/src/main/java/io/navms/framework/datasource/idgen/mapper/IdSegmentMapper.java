package io.navms.framework.datasource.idgen.mapper;

import io.navms.framework.datasource.extension.ExtensionBaseMapper;
import io.navms.framework.datasource.idgen.entity.IdSegment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ID 号段Mapper
 *
 * @author navms
 */
public interface IdSegmentMapper extends ExtensionBaseMapper<IdSegment> {

    /**
     * 查询所有号段 Key
     *
     * @return 所有号段 Key
     */
    @Select("SELECT DISTINCT `key` FROM id_segment")
    List<String> selectAllKeys();

    /**
     * 根据号段 Key 查询号段信息
     *
     * @param key 号段 Key
     * @return 号段信息
     */
    @Select("SELECT * FROM id_segment WHERE `key` = #{key}")
    IdSegment selectByKey(@Param("key") String key);

    /**
     * 更新 maxId
     *
     * @param key 号段 Key
     * @return 更新行数
     */
    @Update("UPDATE id_segment SET max_id = max_id + step, update_time = NOW() WHERE `key` = #{key}")
    int updateMaxId(@Param("key") String key);

    /**
     * 使用步长更新 maxId
     *
     * @param key 号段 Key
     * @return 更新行数
     */
    @Update("UPDATE id_segment SET max_id = max_id + #{step}, update_time = NOW() WHERE `key` = #{key}")
    int updateMaxIdByStep(@Param("key") String key, @Param("step") int step);

}

