package io.github.maccoycookies.mcconfig.client.config;

import io.github.maccoycookies.mcconfig.client.repository.McRepository;
import io.github.maccoycookies.mcconfig.client.repository.McRepositoryChangeListener;
import org.springframework.context.ApplicationContext;

public interface McConfigService extends McRepositoryChangeListener {

    static McConfigService getDefault(ApplicationContext applicationContext, ConfigMeta configMeta) {
        McRepository repository = McRepository.getDefault(configMeta);
        McConfigService configService = new McConfigServiceImpl(applicationContext, repository.getConfig());
        repository.addListener(configService);
        return configService;
    }

    String[] getPropertyNames();

    String getProperty(String name);

}
