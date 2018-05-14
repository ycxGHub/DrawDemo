package adr.ycx.com.drawdemo;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * 创建日期：2018/5/11
 * 创建者ycx
 */

public class DemoApp extends Application {
    public final static String appid = "FHYBQJL7R04Jf2xaej4Y6Esb-gzGzoHsz";
    public final static String appkey = "sDjhGWRyCaSrHRJEouXY74ex";

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, appid, appkey);
        AVOSCloud.setDebugLogEnabled(true);

    }
}
