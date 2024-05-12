package io.github.maccoycookies.mcconfig.client.repository;

import cn.kimmking.utils.HttpUtils;
import com.alibaba.fastjson.TypeReference;
import io.github.maccoycookies.mcconfig.client.config.ConfigMeta;
import sun.security.krb5.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maccoy
 * @date 2024/5/12 17:22
 * Description default impl for mc repository
 */
public class McRepositoryImpl implements McRepository {

    ConfigMeta configMeta;

    public McRepositoryImpl(ConfigMeta configMeta) {
        this.configMeta = configMeta;
    }

    @Override
    public Map<String, String> getConfig() {
        String listPath = configMeta.getConfigServer() + "/list?app=" + configMeta.getApp()
                + "&env=" + configMeta.getEnv() + "&ns=" + configMeta.getNs();
        List<Configs> configs = HttpUtils.httpGet(listPath, new TypeReference<List<Configs>>() {
        });
        Map<String, String> res = new HashMap<>();
        configs.forEach(config -> res.put(config.getPkey(), config.getPval()));
        return res;
    }
}
