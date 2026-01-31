package io.navms.framework.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.navms.framework.common.base.constant.Constants;
import io.navms.framework.common.base.utils.ThreadLocalUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 元数据处理器
 *
 * @author navms
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);

        Long userId = ThreadLocalUtils.getLong(Constants.USER_ID);
        this.strictInsertFill(metaObject, "createBy", String.class, String.valueOf(userId));
        this.strictInsertFill(metaObject, "updateBy", String.class, String.valueOf(userId));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

        Long userId = ThreadLocalUtils.getLong(Constants.USER_ID);
        this.strictUpdateFill(metaObject, "updateBy", String.class, String.valueOf(userId));
    }

}

