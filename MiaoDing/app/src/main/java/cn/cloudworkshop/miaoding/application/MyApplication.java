package cn.cloudworkshop.miaoding.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.FrescoImageLoader;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Author：Libin on 2016/7/7 17:41
 * Email：1993911441@qq.com
 * Describe：application全局对象
 */
public class MyApplication extends Application {
    private static MyApplication application;
    private static Stack<Activity> activityStack;
    //更新链接
    public static String updateUrl;
    //更新内容
    public static String updateContent;
    public static long exitTime;
    public static long exitTime1;
    //登录背景
//    public static String loginBg;
    //客服电话
    public static String serverPhone;
    //用户协议
    public static String userAgreement;
    //量体协议
    public static String measureAgreement;
    //订单号
    public static String orderId;
    //首页时间
    public static long homeEnterTime;
    //是否检测更新
    public static boolean isCheckUpdate = true;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        Fresco.initialize(application);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(new Cache(new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "CloudWorkshop/Cache"), 1024 * 1024 * 100))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        Unicorn.init(this, "e98a79aca99f25ebf9bacbc8c334b76b", options(), new FrescoImageLoader(application));

    }


    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
        } catch (Exception ignored) {
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * @return 七鱼配置
     */
    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        UICustomization uiCustomization = new UICustomization();
        uiCustomization.leftAvatar = "";
        uiCustomization.rightAvatar = Constant.HOST + SharedPreferencesUtils.getString(this, "avatar");
        options.uiCustomization = uiCustomization;
        return options;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
