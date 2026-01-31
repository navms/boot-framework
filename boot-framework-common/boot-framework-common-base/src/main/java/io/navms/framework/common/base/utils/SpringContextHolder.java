package io.navms.framework.common.base.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * Spring 上下文工具类
 *
 * @author navms
 */
public class SpringContextHolder implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

}
