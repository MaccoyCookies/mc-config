package io.github.maccoycookies.mcconfig.client.config;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

/**
 * register mc config bean
 */
public class McConfigRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        System.out.println("register PropertySourcesProcessor");
        boolean flag = Arrays.stream(registry.getBeanDefinitionNames())
                .anyMatch(beanName -> beanName.equals(PropertySourcesProcessor.class.getName()));
        if (flag) {
            System.out.println("PropertySourcesProcessor already registered");
            return;
        }
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesProcessor.class).getBeanDefinition();
        registry.registerBeanDefinition(PropertySourcesProcessor.class.getName(), beanDefinition);
    }
}
