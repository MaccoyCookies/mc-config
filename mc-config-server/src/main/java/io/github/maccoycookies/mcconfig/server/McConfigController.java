package io.github.maccoycookies.mcconfig.server;

import io.github.maccoycookies.mcconfig.server.mapper.ConfigsMapper;
import io.github.maccoycookies.mcconfig.server.model.Configs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maccoy
 * @date 2024/5/3 22:42
 * Description config server endpoint
 */
@RestController
public class McConfigController {

    @Autowired
    private ConfigsMapper configsMapper;

    Map<String, Long> VERSIONS = new HashMap<>();

    @GetMapping("/list")
    public List<Configs> list(String app, String env, String ns) {
        return configsMapper.list(app, env, ns);
    }

    @PostMapping("/update")
    public List<Configs> update(@RequestParam("app") String app,
                                @RequestParam("env") String env,
                                @RequestParam("ns") String ns,
                                @RequestBody Map<String, String> params) {
        params.forEach((key, value) -> {
            insertOrUpdate(new Configs(app, env, ns, key, value));
        });
        VERSIONS.put(app + "-" + env + "-" + ns, System.currentTimeMillis());
        return configsMapper.list(app, env, ns);
    }

    private void insertOrUpdate(Configs paramConfig) {
        Configs dbConfig = configsMapper.select(paramConfig.getApp(), paramConfig.getEnv(), paramConfig.getNs(), paramConfig.getPkey());
        if (dbConfig == null) {
            configsMapper.insert(paramConfig);
        } else {
            configsMapper.update(paramConfig);
        }
    }

    @GetMapping("/version")
    public long version(@RequestParam("app") String app,
                        @RequestParam("env") String env,
                        @RequestParam("ns") String ns) {
        return VERSIONS.getOrDefault(app + "-" + env + "-" + ns, -1L);
    }

}
