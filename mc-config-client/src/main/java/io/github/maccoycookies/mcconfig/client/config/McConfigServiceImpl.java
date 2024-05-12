package io.github.maccoycookies.mcconfig.client.config;

import io.github.maccoycookies.mcconfig.client.repository.McRepository;
import io.github.maccoycookies.mcconfig.client.repository.McRepositoryChangeListener;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.security.cert.CertificateNotYetValidException;
import java.util.Map;

public class McConfigServiceImpl implements McConfigService {

    Map<String, String> config;

    ApplicationContext applicationContext;

    public McConfigServiceImpl(ApplicationContext applicationContext, Map<String, String> config) {
        this.applicationContext = applicationContext;
        this.config = config;
    }

    public String[] getPropertyNames() {
        return this.config.keySet().toArray(new String[0]);
    }

    public String getProperty(String name) {
        return this.config.get(name);
    }

    @Override
    public void onChange(McRepositoryChangeListener.ChangeEvent changeEvent) {
        this.config = changeEvent.getConfig();
        applicationContext.publishEvent(new EnvironmentChangeEvent(config.keySet()));
    }
}
