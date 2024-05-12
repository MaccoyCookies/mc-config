package io.github.maccoycookies.mcconfig.client.repository;

import io.github.maccoycookies.mcconfig.client.config.ConfigMeta;

import java.util.Map;

/**
 * @author Maccoy
 * @date 2024/5/12 17:21
 * Description interface to get config from remote
 */
public interface McRepository {

    static McRepository getDefault(ConfigMeta configMeta) {
        return new McRepositoryImpl(configMeta);
    }

    Map<String, String> getConfig();

}
