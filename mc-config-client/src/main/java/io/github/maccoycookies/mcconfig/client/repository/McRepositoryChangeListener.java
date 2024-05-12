package io.github.maccoycookies.mcconfig.client.repository;

import io.github.maccoycookies.mcconfig.client.config.ConfigMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.event.ChangeEvent;
import java.util.Map;

/**
 * @author Maccoy
 * @date 2024/5/12 18:32
 * Description
 */
public interface McRepositoryChangeListener {

    void onChange(ChangeEvent changeEvent);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ChangeEvent {
        ConfigMeta configMeta;
        Map<String, String> config;
    }

}
