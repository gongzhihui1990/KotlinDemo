package com.xilai.express;

import framework.app.BaseApp;
import framework.util.Loger;

/**
 *
 * @author caroline
 * @date 2018/7/30
 */

public class App extends BaseApp {
    @Override
    public void initApk() {
        Loger.INSTANCE.w("initApk success");
    }
}
