package holder.sdk.com.hotfixdemo.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yj.hotfixsdk.builder.hotfix.HotFixBuilder;

/**
 * Created by yangjian on 2019/2/26.
 */

public class FixApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HotFixBuilder.onCreate()
                .setDebugLogger(true)
                .setDexParentFileName("hotfix_demo")
                .setXmlFileName("hotfixxml")
                .initBuilder(this);
    }
}
