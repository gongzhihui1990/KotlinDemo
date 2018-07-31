package com.bus.data.http;

import android.content.Context;

import com.bus.data.http.persistentcookiejar.PersistentCookieJar;
import com.bus.data.http.persistentcookiejar.cache.SetCookieCache;
import com.bus.data.http.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import framework.app.BaseApp;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * @author caroline
 */
public abstract class BaseApiRetrofit {

    private final OkHttpClient mClient;

    BaseApiRetrofit() {
        //cache
        File httpCacheDir = new File(BaseApp.Companion.getContext().getCacheDir(), "response");
        // 10 MiB
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(httpCacheDir, cacheSize);
        // cookieJar配置
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApp.Companion.getContext()));
        OkHttpClient.Builder mClientBuilder = new OkHttpClient.Builder();
        // 请求头配置
        Interceptor rewriteHeaderControlInterceptor = chain -> {
            Request request = chain.request().newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").build();
            return chain.proceed(request);
        };
        //OkHttpClient
        mClient = mClientBuilder.
                addInterceptor(rewriteHeaderControlInterceptor).
                addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).
                //设置客户端和服务器建立连接的超时时间
                        connectTimeout(10, TimeUnit.SECONDS).
                //设置客户端上传数据到服务器的超时时间
                        readTimeout(30, TimeUnit.SECONDS).
                //设置客户端从服务器下载响应数据的超时时间。
                        writeTimeout(30, TimeUnit.SECONDS).
                        cache(cache).
                        cookieJar(cookieJar).
                //retryOnConnectionFailure(true).
                        build();
    }

    OkHttpClient getClient() {
        return mClient;
    }

    Boolean clearCookieJar() {
        try {
            PersistentCookieJar cookieJar = (PersistentCookieJar) mClient.cookieJar();
            if (cookieJar != null) {
                cookieJar.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 检查返回结果是否正确
     *
     * @param observable
     * @param <T>
     * @return
     */
//    protected <T extends AbstractResponseModel<BaseModel>> Observable<T> checkResp(Observable<T> observable) {
//        return observable.map(response -> {
//            return response;
//            if (response.isResponseOK()) {
//                return response;
//            }
//            String msg = response.getErrDesc();
//            if ("AUTH09".equals(response.getErrCode())) {
//                throw new IServerAuthException();
//            }
//            throw new IServerException(!TextUtils.isEmpty(msg) ? msg : "服务器返回未知错误" + response.getErrCode());
//        });
//    }

}
