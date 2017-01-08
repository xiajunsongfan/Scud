package com.xj.scud.clent;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/04 15:06
 */
public class ScudClientFactory {

    /**
     * 获得服务客户端
     *
     * @param conf 客户端配置
     * @param <T> T
     * @return T
     */
    public static <T> T getServiceConsumer(ClientConfig<T> conf) {
        ScudClient<T> client = new ScudClient<>(conf);
        client.init();
        return client.getClient();
    }
}
