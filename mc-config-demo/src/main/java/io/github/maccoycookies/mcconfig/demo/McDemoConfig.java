package io.github.maccoycookies.mcconfig.demo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mc")
public class McDemoConfig {

    String a;

    String b;

    String c;

}
