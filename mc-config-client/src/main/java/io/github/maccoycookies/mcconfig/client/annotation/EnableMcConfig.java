package io.github.maccoycookies.mcconfig.client.annotation;

import io.github.maccoycookies.mcconfig.client.config.McConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * mc config client entryPoint
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import({McConfigRegistrar.class})
public @interface EnableMcConfig {
}
