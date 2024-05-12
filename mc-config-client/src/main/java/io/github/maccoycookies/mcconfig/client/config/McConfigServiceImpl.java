package io.github.maccoycookies.mcconfig.client.config;

import io.github.maccoycookies.mcconfig.client.repository.McRepositoryChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
        Set<String> keys = calcChangedKeys(this.config, changeEvent.getConfig());
        if (keys.isEmpty()) {
            log.info("[MCCONFIG] -> calcChangedKeys return empty, ignore update. ");
            return;
        }
        this.config = changeEvent.getConfig();
        if (config.isEmpty()) return;
        log.info("[MCCONFIG] -> fire an EnvironmentChangeEvent with keys: " + keys);
        applicationContext.publishEvent(new EnvironmentChangeEvent(keys));
    }

    private Set<String> calcChangedKeys(Map<String, String> oldConfigs, Map<String, String> newConfigs) {
        if (oldConfigs.isEmpty()) return newConfigs.keySet();
        if (newConfigs.isEmpty()) return oldConfigs.keySet();
        Set<String> news = newConfigs.entrySet().stream()
                .filter(entry -> !entry.getValue().equals(oldConfigs.get(entry.getKey())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        oldConfigs.keySet().stream().filter(key -> !newConfigs.containsKey(key)).forEach(news::add);
        return news;
    }
}
