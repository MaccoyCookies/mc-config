package io.github.maccoycookies.mcconfig.client.config;

import io.github.maccoycookies.mcconfig.client.repository.McRepository;

public interface McConfigService {

    static McConfigService getDefault(ConfigMeta configMeta) {
        McRepository repository = McRepository.getDefault(configMeta);
        return new McConfigServiceImpl(repository.getConfig());
    }

    String[] getPropertyNames();

    String getProperty(String name);

}
