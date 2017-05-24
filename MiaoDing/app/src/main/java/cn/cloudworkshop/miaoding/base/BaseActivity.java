package cn.cloudworkshop.miaoding.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.umeng.analytics.MobclickAgent;

import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.service.GeTuiIntentService;
import cn.cloudworkshop.miaoding.service.GeTuiService;

/**
 * Author：Libin on 2016/7/7 16:41
 * Email：1993911441@qq.com
 * Describe：Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //个推
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
        //友盟统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - MyApplication.exitTime1) < 800) {
                MyApplication.getInstance().appExit();
            }

            if ((System.currentTimeMillis() - MyApplication.exitTime) > 2000) {
                MyApplication.exitTime = System.currentTimeMillis();
                finish();
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                MyApplication.exitTime1 = System.currentTimeMillis();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @return 字体大小固定
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
