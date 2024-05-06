package io.github.maccoycookies.mcconfig.demo;

import io.github.maccoycookies.mcconfig.client.annotation.EnableMcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({McDemoConfig.class})
@EnableMcConfig
public class McConfigDemoApplication {

	@Value("${mc.a}")
	private String a;

	@Autowired
	private McDemoConfig mcDemoConfig;

	public static void main(String[] args) {
		SpringApplication.run(McConfigDemoApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			System.out.println(a);
			System.out.println(mcDemoConfig);
		};
	}

}
