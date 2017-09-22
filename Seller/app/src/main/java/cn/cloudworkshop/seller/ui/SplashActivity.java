package cn.cloudworkshop.seller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.LogUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import cn.cloudworkshop.seller.utils.TitleBarUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/11/9 09:33
 * Email：1993911441@qq.com
 * Describe：启动页
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarUtils.setNoTitleBar(this);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLogin();
            }
        }, 2000);
    }


    /**
     * 检测是否登录
     */
    private void isLogin() {
        String token = SharedPreferencesUtils.getStr(this, "token");
        if (!TextUtils.isEmpty(token)) {
            OkHttpUtils.get()
                    .url(Constant.CHECK_LOGIN)
                    .addParams("token", token)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                } else {
                                    SharedPreferencesUtils.deleteStr(SplashActivity.this, "token");
                                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                }
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }

}
