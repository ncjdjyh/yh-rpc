package com.neo.yhrpc.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author: neo
 * @FirstInitial: 2019/7/13
 * @Description: ~
 */
public class MessageInput {
    private String type;
    private String requestId;
    private String payload;

    public MessageInput(String type, String requestId, String payload) {
        this.type = type;
        this.requestId = requestId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public <T> T getPayload(Class<T> clazz) {
        if (payload == null) {
            return null;
        }
        return JSON.parseObject(payload, clazz);
    }
}