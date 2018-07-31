package com.bus.data.http;

import com.bus.data.request.BusHttpRequest;

import io.reactivex.Observable;
import retrofit2.http.Body;

/**
 *
 * @author caroline
 * @date 2018/7/30
 */

public interface BusApi {

    Observable<Object> queryRouteAroundHttpRequest(@Body BusHttpRequest request);
}
