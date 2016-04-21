package test11.mylocation;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by 123 on 2016/3/28.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
