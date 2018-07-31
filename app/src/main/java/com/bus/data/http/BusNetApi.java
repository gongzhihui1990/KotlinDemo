package com.bus.data.http;

import com.bus.data.request.BusHttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import framework.util.Loger;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author caroline
 * @date 2018/7/30
 */

public class BusNetApi extends BaseApiRetrofit implements BusApi {

    private static BusNetApi mInstance;

    private BusNetInterface busNetInterface;

    private BusNetApi() {
        super();
        Gson gson = new GsonBuilder().setLenient().create();
        //在构造方法中完成对Retrofit接口的初始化
        HttpUrl httpUrl = HttpUrl.parse("http://api.wxbus.com.cn/");
        Loger.INSTANCE.d("httpUrl:" + httpUrl);
        busNetInterface = new Retrofit.Builder().baseUrl(httpUrl).
                client(getClient()).
                addConverterFactory(GsonConverterFactory.create(gson)).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build().
                create(BusNetInterface.class);
    }

    public static BusNetApi getInstance() {
        if (mInstance == null) {
            synchronized (BusNetApi.class) {
                mInstance = new BusNetApi();
            }
        }
        return mInstance;
    }

    @Override
    public Observable<Object> queryRouteAroundHttpRequest(BusHttpRequest request) {
        return busNetInterface.queryRouteAroundHttpRequest(request.toRequestBody()).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) throws Exception {
                Loger.INSTANCE.w("resp " + o.toString());
                return o;
            }
        });
    }

}
