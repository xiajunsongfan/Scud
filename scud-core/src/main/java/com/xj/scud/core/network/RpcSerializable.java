package com.xj.scud.core.network;

/**
 * Author: xiajun
 * Date: 2017/01/03 12:12
 */
public interface RpcSerializable<T> {
    T decode(byte[] value, Class<T> clazz);

    byte[] encode(T value);
}
