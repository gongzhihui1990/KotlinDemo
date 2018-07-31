package com.xilai.express.delivery.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.bus.data.http.BusNetApi;
import com.bus.data.request.BusHttpRequest;
import com.bus.util.AES7PaddingUtil;
import com.xilai.express.delivery.R;

import org.jetbrains.annotations.NotNull;

import framework.rx.ProgressObserverImplementation;
import framework.rx.RxHelper;
import framework.util.Loger;

/**
 * @author caroline
 * @date 2018/7/30
 */

public class BusApiFragment extends BaseFragment {
    @Override
    public int getLayout() {
        return R.layout.fragment_bus_api;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        RxHelper.INSTANCE.bindOnNull(Observable.just(true).map(new Function<Boolean, Object>() {
//                    @Override
//                    public Object apply(Boolean aBoolean) throws Exception {
//                        BusHttpRequest request =new BusHttpRequest().put("m","line_nearby")
//                                .put("radius","500")
//                                .put("latitude_baidu","120.298208")
//                                .put("longitude_baidu","31.538309");
//                        return BusNetApi.getInstance().queryRouteAroundHttpRequest(request);
//                    }
//                }));




        findViewById(R.id.path).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] datas = new String[]{
                        "rzxYHa3O8pCv8ZnIrWmeu2tDkGvJAHxplwknvP/27RRlJn0aXO5WYPB5gv5CyPdvHf1rnEg8jVrqpOt2tnegHg==",
                        "eanoReUOuxNR1WI5J/Io//FkLJd52BJgZ2CJprGyRsuoBUUhhcKDiLd8UoEP6ywRyrZ5xqI3eVFXWrDBFHthQhwhYjjYDlnj4OUZbi20U9wCZlqbKBvjRiowFVg1OCaLdPfc780vJqO80ocu63QiWw==",
                        "1v%2B20aaG5XkA81MDqdrkAoVfiNUA0086qDUK80nzgk3DrrH4%2FekUE8Nt3j%2BYbAPCbfPGcxXNkyKt%0AG1paUjuqGQNzCoJvmI8yQfWt5G%2FaCMN0Csx3dzvyJ7ZzHcd%2Bq866SbOG3wTv7QZ0hF61TfEcrYDF%0AWSlyvy73bd1lSbp1IHoG%2B%2FfzOk%2BwVtdvhtJckAYL%0A",
                        "BtE2Jp%2BnsVXMZxPMtCKvcrhJty1WKzZOxK7HLxGDj1hSpnh%2Fpxt1tHcLj%2FaA7LmOK%2FKGTFG6BzD3%0AowJDl8XIUSBz%2Bd%2B2a35ISU%2BedFornPsUnqjEKAjGJCjvYMBErHpWF8ndedCNw62hoSIK0J6mH61Q%0AzULwpRBvtp2%2BYafxDTUFF76B%2BSjkXeAbI2l88Ju1%0A"
                };
                for (String data : datas) {
                    AES7PaddingUtil.reverse(data);
                }
            }
        });
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomLa = (int) (Math.random() * 10);
                int randomLo = (int) (Math.random() * 10);
                //120.318294,31.554834
                BusHttpRequest request = new BusHttpRequest().
                        put("m", "line_nearby").
                        put("radius", "1500").
                        put("longitude_baidu", "120.31829" + randomLa).
                        put("latitude_baidu", "31.55483" + randomLo);

                RxHelper.INSTANCE.bindOnUI(BusNetApi.getInstance().queryRouteAroundHttpRequest(request), new ProgressObserverImplementation<Object>(BusApiFragment.this) {

                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        Loger.INSTANCE.e(o.toString());
                    }

                    @Override
                    public void onError(@NotNull Throwable t) {
                        super.onError(t);
                        t.printStackTrace();
                    }
                }.setShow(false));
            }
        });

    }
}
