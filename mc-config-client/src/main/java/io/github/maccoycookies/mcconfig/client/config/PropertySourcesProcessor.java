package io.github.maccoycookies.mcconfig.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * mc property sources processor
 */
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware {

    private final static String MC_PROPERTY_SOURCES = "McPropertySources";
    private final static String MC_PROPERTY_SOURCE = "McPropertySource";

    Environment environment;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment env = (ConfigurableEnvironment) environment;
        if (env.getPropertySources().contains(MC_PROPERTY_SOURCES)) return;
        // 通过http请求去mcconfig-server获取配置
        Map<String, String> config = new HashMap<>();
        config.put("mc.a", "dev500");
        config.put("mc.b", "dev600");
        config.put("mc.c", "dev700");
        McConfigService configService = new McConfigServiceImpl(config);
        McPropertySource propertySource = new McPropertySource(MC_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(MC_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        env.getPropertySources().addFirst(compositePropertySource);

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
