package com.bus.data.request;

import com.bus.util.AES7PaddingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import framework.util.Loger;
import okhttp3.RequestBody;

/**
 * Created by caroline on 2018/7/30.
 */

public final class BusHttpRequest extends AbstractRequestModel {
    private JSONObject jsonObject = new JSONObject();

    public BusHttpRequest put(String name, String value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BusHttpRequest put(String name, int value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BusHttpRequest put(String name, boolean value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BusHttpRequest put(String name, double value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BusHttpRequest put(String name, long value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public BusHttpRequest put(String name, Object value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public RequestBody toRequestBody() {
        JSONObject requestJson = AES7PaddingUtil.toAES7Padding(jsonObject);
        StringBuilder sb = new StringBuilder();
        Iterator<String> keys = requestJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                sb.append(key).append("=").append(requestJson.get(key).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (keys.hasNext()) {
                sb.append("&");
            }
        }
        String request = sb.toString();
        Loger.INSTANCE.i("request Json:" + requestJson.toString());
        Loger.INSTANCE.i("request Url:" + request);

        return RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), request);
    }
}
