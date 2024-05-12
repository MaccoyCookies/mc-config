package io.github.maccoycookies.mcconfig.demo;

import io.github.maccoycookies.mcconfig.client.annotation.EnableMcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties({McDemoConfig.class})
@EnableMcConfig
@RestController
public class McConfigDemoApplication {

	@Value("${mc.a}")
	private String a;

	@Value("${mc.b}")
	private String b;

	@Autowired
	private McDemoConfig mcDemoConfig;

	public static void main(String[] args) {
		SpringApplication.run(McConfigDemoApplication.class, args);
	}

	@GetMapping("/demo")
	public String demo() {
		return "mc.a = " + a + "\n"
				+ "mc.b = " + b + "\n"
				+ "demo.a = " + mcDemoConfig.getA() + "\n"
				+ "demo.b = " + mcDemoConfig.getB() + "\n";
	}

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			System.out.println(a);
			System.out.println(mcDemoConfig);
		};
	}

}
