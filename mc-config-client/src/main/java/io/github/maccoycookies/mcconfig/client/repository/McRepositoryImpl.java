package io.github.maccoycookies.mcconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.maccoycookies.mcconfig.client.config.ConfigMeta;

import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Maccoy
 * @date 2024/5/12 17:22
 * Description default impl for mc repository
 */
public class McRepositoryImpl implements McRepository {

    ConfigMeta configMeta;

    Map<String, Long> versionMap = new HashMap<>();

    Map<String, Map<String, String>> configMap = new HashMap<>();

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    List<McRepositoryChangeListener> listeners = new ArrayList<>();

    public McRepositoryImpl(ConfigMeta configMeta) {
        this.configMeta = configMeta;
        executorService.scheduleWithFixedDelay(this::heartbeat, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void addListener(McRepositoryChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @Override
    public Map<String, String> getConfig() {
        String key = configMeta.genKey();
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }
        return findAll();
    }

    private Map<String, String> findAll() {
        String listPath = configMeta.listPath();
        System.out.println("[MCCONFIG] list all configs from config server");
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        Map<String, String> res = new HashMap<>();
        configs.forEach(config -> res.put(config.getPkey(), config.getPval()));
        return res;
    }

    private void heartbeat() {
        String versionPath = configMeta.versionPath();
        Long version = HttpUtils.httpGet(versionPath, new TypeReference<Long>() {
        });
        String key = configMeta.genKey();
        Long oldVersion = versionMap.getOrDefault(key, -1L);
        if (version > oldVersion) {
            System.out.println("[MCCONFIG] current = " + version + ", old = " + oldVersion);
            System.out.println("[MCCONFIG] need update new configs");
            versionMap.put(key, version);
            Map<String, String> newConfigs = findAll();
            configMap.put(key, newConfigs);
            System.out.println("[MCCONFIG] fire an EnvironmentChangeEvent with keys: " + newConfigs.keySet());
            listeners.forEach(listener -> listener.onChange(new McRepositoryChangeListener.ChangeEvent(configMeta, newConfigs)));
        }
    }
}
