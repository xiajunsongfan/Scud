package com.xj.scud.core;

import com.xj.scud.clent.ClientConfig;
import com.xj.scud.core.NetworkProtocol;
import com.xj.scud.core.RpcInvocation;
import com.xj.scud.core.RpcResult;
import com.xj.scud.network.SerializableHandler;

import java.lang.reflect.Method;

/**
 * Author: baichuan - xiajun
 * Date: 2017/01/03 18:20
 * 客户端协议封装
 */
public class ProtocolProcesser {
    private ClientConfig conf;

    public ProtocolProcesser(ClientConfig conf) {
        this.conf = conf;
    }

    /**
     * 封装客户端RPC调用协议
     *
     * @param method 方法
     * @param args   参数
     * @return RpcInvocation
     */
    public NetworkProtocol buildRequestProtocol(Method method, Object[] args, int seq) {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethod(method.getName());
        invocation.setArgs(args);
        invocation.setRequestTime(System.currentTimeMillis());
        invocation.setRequestTimeout(conf.getTimeout());
        if (args != null) {
            String[] argTypes = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass().getSimpleName();
            }
            invocation.setArgTypes(argTypes);
        }

        NetworkProtocol protocol = new NetworkProtocol();
        protocol.setType(conf.getType().getValue());
        protocol.setSequence(seq);
        byte[] content = SerializableHandler.requestEncode(protocol.getType(), invocation);
        protocol.setContent(content);
        return protocol;
    }

    /**
     * 封装服务端RPC响应协议
     *
     * @param requestProtocol 客户端请求时的协议信息
     * @param result          响应数据对象
     * @return NettyProtocol
     */
    public NetworkProtocol buildResponseProtocol(NetworkProtocol requestProtocol, RpcResult result) {
        byte[] content = SerializableHandler.responseEncode(requestProtocol.getType(), result);
        NetworkProtocol protocol = new NetworkProtocol();
        protocol.setContent(content);
        protocol.setSequence(requestProtocol.getSequence());
        protocol.setType(requestProtocol.getType());
        protocol.setVersion(requestProtocol.getVersion());
        return protocol;
    }

    /**
     * 根据方法对象生成方法签名
     *
     * @param method 方法对象
     * @return String
     */
    public static String buildMethodName(Method method) {
        Class[] pram = method.getParameterTypes();
        StringBuilder builder = new StringBuilder(method.getName());
        if (pram != null) {
            for (Class aClass : pram) {
                builder.append(":").append(aClass.getSimpleName());
            }
        }
        return builder.toString();
    }

    /**
     * 根据方法名称和参数类型生成方法签名
     *
     * @param method 方法名
     * @param args   方法参数
     * @return String
     */
    public static String buildMethodName(String method, String[] args) {
        StringBuilder builder = new StringBuilder(method);
        if (args != null) {
            for (String type : args) {
                builder.append(":").append(type);
            }
        }
        return builder.toString();
    }
}
