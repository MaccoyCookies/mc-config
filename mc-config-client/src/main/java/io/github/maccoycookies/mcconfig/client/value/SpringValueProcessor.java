package io.github.maccoycookies.mcconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import io.github.maccoycookies.mcconfig.client.util.PlaceholderHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * @author Maccoy
 * @date 2024/5/12 21:20
 * Description process spring value
 * 1. 扫描所有Spring @Value，保存起来
 * 2. 在配置变更的时候，更新所有的Spring value
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 第一步
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("[MCCONFIG] -> find spring value field: {}", field);
            Value value = field.getAnnotation(Value.class);
            helper.extractPlaceholderKeys(value.value()).forEach(key -> {
                log.info("[MCCONFIG] -> find spring value key: {}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value.value(), field);
                VALUE_HOLDER.add(key, springValue);
            });

        });
        return bean;
    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        // 第二步
        log.info("[MCCONFIG] -> update spring value keys: {}", event.getKeys());
        event.getKeys().forEach(key -> {
            log.info("[MCCONFIG] -> update spring value key: {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (springValues == null || springValues.isEmpty()) return;
            springValues.forEach(springValue -> {
                log.info("[MCCONFIG] -> update spring value springValue: {}", springValue);
                try {
                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory, springValue.getBeanName(), springValue.getPlaceholder());
                    log.info("[MCCONFIG] -> update value: {} for holder: {}", value, springValue.getPlaceholder());
                    springValue.getField().setAccessible(true);
                    springValue.getField().set(springValue.getBean(), value);
                } catch (Exception exception) {
                    log.error("[MCCONFIG] -> update spring value error. ", exception);
                }
            });
        });

    }
}
