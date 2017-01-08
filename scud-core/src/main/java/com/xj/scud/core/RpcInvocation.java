package com.xj.scud.core;

import java.io.Serializable;
import java.util.Map;

/**
 * Author: xiajun
 * Date: 2017/01/03 11:55
 * RPC客户端调用封装类
 */
public class RpcInvocation implements Serializable {
    private static final long serialVersionUID = -5618624997366006383L;
    private String service;//服务名称
    private String method;//方法名称
    private Object[] args;//方法参数
    private String[] argTypes;//方法参数类型
    private long requestTime;//客户端发起请求时间
    private int requestTimeout;//客户端超时时间
    private Map<String, Object> attach;//附加属性

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(String[] argTypes) {
        this.argTypes = argTypes;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Map<String, Object> getAttach() {
        return attach;
    }

    public void setAttach(Map<String, Object> attach) {
        this.attach = attach;
    }
}
