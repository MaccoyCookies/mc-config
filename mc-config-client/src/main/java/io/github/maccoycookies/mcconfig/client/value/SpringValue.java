package io.github.maccoycookies.mcconfig.client.value;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author Maccoy
 * @date 2024/5/12 22:21
 * Description spring value
 */
@Data
@AllArgsConstructor
public class SpringValue {

    private Object bean;

    private String beanName;

    private String key;

    private String placeholder;

    private Field field;


}
