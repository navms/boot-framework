package io.navms.framework.datasource.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.navms.framework.common.base.log.LogUtils;
import io.navms.framework.datasource.extension.ExtensionSqlInjector;
import io.navms.framework.datasource.idgen.IdGenerator;
import io.navms.framework.datasource.idgen.impl.SegmentIdGenerator;
import io.navms.framework.datasource.idgen.mapper.IdSegmentMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Datasource 模块自动配置类
 *
 * @author navms
 */
@MapperScan(value = {"${boot.framework.datasource.base-mapper}", "io.navms.framework.datasource.idgen.mapper"})
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(DatasourceProperties.class)
public class DatasourceAutoConfiguration {

    public DatasourceAutoConfiguration() {
        LogUtils.info("==============>>>>>>> Boot Framework Datasource 模块已启用");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MetaObjectHandler auditMetaObjectHandler() {
        return new AuditMetaObjectHandler();
    }

    @Bean
    public IdentifierGenerator sequenceIdentifierGenerator() {
        return new SequenceIdentifierGenerator();
    }

    @Bean
    public AbstractSqlInjector extensionSqlInjector() {
        return new ExtensionSqlInjector();
    }

    @Bean
    public IdGenerator segmentIdGenerator(IdSegmentMapper idSegmentMapper) {
        return new SegmentIdGenerator(idSegmentMapper);
    }

    @Bean
    public InitiateIdSegment initiateIdSegment() {
        return new InitiateIdSegment();
    }

}
