package com.xj.scud.core;

import com.xj.scud.client.ClientConfig;
import com.xj.scud.commons.ParamSignUtil;
import com.xj.scud.core.network.SerializableHandler;

import java.lang.reflect.Method;

/**
 * Author: xiajun
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
     * @param serviceName 服务名称
     * @param version     服务版本
     * @param method      方法
     * @param args        参数
     * @return RpcInvocation
     */
    public NetworkProtocol buildRequestProtocol(String serviceName, String version, Method method, Object[] args, int seq) {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setService(serviceName);
        invocation.setVersion(version);
        invocation.setMethod(method.getName());
        invocation.setArgs(args);
        invocation.setRequestTime(System.currentTimeMillis());
        invocation.setRequestTimeout(conf.getTimeout());
        invocation.setArgsSign(ParamSignUtil.sign(args));//TODO: sig
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
        return method.getName() + ":" + ParamSignUtil.sign(pram);
    }

    /**
     * 根据方法名称和参数类型生成方法签名
     *
     * @param method   方法名
     * @param argsSign 方法参数
     * @return String
     */
    public static String buildMethodName(String method, String argsSign) {
        StringBuilder builder = new StringBuilder(method);
        if (argsSign != null) {
            builder.append(":").append(argsSign);
        }
        return builder.toString();
    }
}
