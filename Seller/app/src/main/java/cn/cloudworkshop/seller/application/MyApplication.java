package cn.cloudworkshop.seller.application;

import android.Manifest;
import android.app.Application;
import android.os.Environment;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author：binge on 2017-09-14 16:04
 * Email：1993911441@qq.com
 * Describe：
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "miaoding/Cache");
            builder.cache(new Cache(file, 1024 * 1024 * 100));
        }
        OkHttpClient okHttpClient = builder.connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}