package com.bus.data.http;

import com.bus.data.request.BusHttpRequest;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * @author caroline
 * @date 2018/7/30
 */

public interface BusNetInterface {
    @POST("/api/")
    Observable<Object> queryRouteAroundHttpRequest(@Body RequestBody request);
}
