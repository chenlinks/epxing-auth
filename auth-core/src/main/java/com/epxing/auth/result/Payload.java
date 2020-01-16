package com.epxing.auth.result;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author chenling
 * @date 2020/1/15 16:13
 * @since V1.0.0
 */
public class Payload<T> implements Serializable {

    private static final long serialVersionUID = -1549643581827130116L;
    private T payload;
    private String code = "0";
    private String msg = "ok";
    private JsonFilterConfig mapper;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Payload() {
    }

    public Payload(T payload) {
        this.payload = payload;
    }

    public Payload(T payload, String code, String msg) {
        this.payload = payload;
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getPayload() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        JsonFilterConfig var10000 = this.mapper;
        return (T) JsonFilterConfig.filteredWriter(this.payload);
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        try {
            String json = this.payload != null ? this.objectMapper.writeValueAsString(this) : "{payload=" + null + ",code=" + this.code + ",msg=" + this.msg + "}";
            return this.payload != null ? ((Map) this.objectMapper.readValue(json, Map.class)).toString() : json;
        } catch (IOException var2) {
            var2.printStackTrace();
            return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
        }
    }
}
