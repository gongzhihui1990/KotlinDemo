package com.bus.data.request;

import com.bus.data.BaseModel;
import com.google.gson.Gson;

import okhttp3.RequestBody;

/**
 * @author caroline
 * @date 2018/2/22
 */

public class AbstractRequestModel extends BaseModel {

    public RequestBody toRequestBody() {
        String route = new Gson().toJson(this);
        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
    }
}
