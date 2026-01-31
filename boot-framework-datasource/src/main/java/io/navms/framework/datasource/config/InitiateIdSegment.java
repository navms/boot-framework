package io.navms.framework.datasource.config;

import io.navms.framework.datasource.utils.IdGenerateUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * 启动时初始化号段
 *
 * @author navms
 */
public class InitiateIdSegment implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        IdGenerateUtil.getIdGenerator().init();
    }

}
