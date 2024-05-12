package io.github.maccoycookies.mcconfig.client.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class PropertySourcesProcessor implements BeanFactoryPostProcessor, PriorityOrdered, EnvironmentAware, ApplicationContextAware {

    private final static String MC_PROPERTY_SOURCES = "McPropertySources";
    private final static String MC_PROPERTY_SOURCE = "McPropertySource";

    Environment environment;

    ApplicationContext applicationContext;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ConfigurableEnvironment ENV = (ConfigurableEnvironment) environment;
        if (ENV.getPropertySources().contains(MC_PROPERTY_SOURCES)) return;
        // 通过http请求去mcconfig-server获取配置

        String app = ENV.getProperty("mcconfig.app", "app1");
        String env = ENV.getProperty("mcconfig.env", "dev");
        String ns = ENV.getProperty("mcconfig.ns", "public");
        String configServer = ENV.getProperty("mcconfig.configServer", "http://localhost:9129");

        ConfigMeta configMeta = new ConfigMeta(app, env, ns, configServer);

        McConfigService configService = McConfigService.getDefault(applicationContext, configMeta);
        McPropertySource propertySource = new McPropertySource(MC_PROPERTY_SOURCE, configService);
        CompositePropertySource compositePropertySource = new CompositePropertySource(MC_PROPERTY_SOURCES);
        compositePropertySource.addPropertySource(propertySource);
        ENV.getPropertySources().addFirst(compositePropertySource);

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
