package cn.cloudworkshop.miaoding.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CheckUpdateBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.fragment.CustomGoodsFragment;
import cn.cloudworkshop.miaoding.fragment.DesignerWorksFragment;
import cn.cloudworkshop.miaoding.fragment.HomepageFragment;
import cn.cloudworkshop.miaoding.fragment.MyCenterFragment;

import cn.cloudworkshop.miaoding.fragment.NewCustomGoodsFragment;
import cn.cloudworkshop.miaoding.service.DownloadService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.FragmentTabUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/31 10:50
 * Email：1993
 * Describe:主界面
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.rgs_main)
    RadioGroup mRgs;
    private List<Fragment> fragmentList = new ArrayList<>();
    private CheckUpdateBean updateBean;
    private DownloadService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        checkUpdate();
        isLogin();
        submitClientId();
        getData();

    }


    /**
     * 推送，提交设备id
     */
    private void submitClientId() {
        String clientId = SharedPreferencesUtils.getString(this, "client_id");
        if (clientId != null) {

            OkHttpUtils.get()
                    .url(Constant.CLIENT_ID)
                    .addParams("content", 1 + "")
                    .addParams("device_id", clientId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                        }
                    });
        }
    }


    /**
     * 检测是否登录
     */
    private void isLogin() {
        if (SharedPreferencesUtils.getString(this, "token") != null) {
            OkHttpUtils.get()
                    .url(Constant.CHECK_LOGIN)
                    .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            SharedPreferencesUtils.deleteString(MainActivity.this, "token");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code == 10001) {
                                        SharedPreferencesUtils.deleteString(MainActivity.this, "token");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {

        OkHttpUtils.get()
                .url(Constant.CHECK_UPDATE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        updateBean = GsonUtils.jsonToBean(response, CheckUpdateBean.class);
                        MyApplication.loginBg = updateBean.getData().getLogin_img();
                        MyApplication.serverPhone = updateBean.getData().getKf_tel();
                        MyApplication.userAgreement = updateBean.getData().getReg_agreement();
                        MyApplication.measureAgreement = updateBean.getData().getLt_agreement();
                        if (updateBean.getData().getVersion().getAndroid() != null &&
                                Integer.valueOf(updateBean.getData().getVersion().getAndroid()
                                        .getVersion()) > getVersionCode()) {
                            MyApplication.updateUrl = updateBean.getData().getDownload_url();
                            MyApplication.updateContent = updateBean.getData().getVersion().getAndroid()
                                    .getRemark();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,
                                    R.style.AlertDialog);
                            dialog.setTitle("检测到新版本，请更新");
                            dialog.setMessage(updateBean.getData().getVersion().getAndroid().getRemark());
                            //为“确定”按钮注册监听事件
                            dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadFile();
                                }
                            });
                            //为“取消”按钮注册监听事件
                            dialog.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.create();
                            dialog.show();
                        }
                    }
                });
    }

    /**
     * 下载文件
     */
    private void downloadFile() {
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(MyApplication.updateUrl));

        request.setTitle("云工场");
        request.setDescription("正在下载");
        // 设置下载可见
        request.setVisibleInDownloadsUi(true);
        //下载完成后通知栏可见
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //当前网络状态
        ConnectivityManager mConnectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("MOBILE")) {
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
            }
        }
        // 设置下载位置
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                "CloudWorkshop/yungongchang.apk");
        request.setDestinationUri(Uri.fromFile(file));
        service = new DownloadService(file);
        registerReceiver(service, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        manager.enqueue(request);
    }

    /**
     * 获取版本号
     */

    private int getVersionCode() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? pi.versionCode : 0;

    }

    private void getData() {
        ((RadioButton) mRgs.getChildAt(getIntent().getIntExtra("fragid", 0))).setChecked(true);
    }

    /**
     * 加载Fragment
     */
    public void initView() {

        fragmentList.add(HomepageFragment.newInstance());
        fragmentList.add(NewCustomGoodsFragment.newInstance());
        fragmentList.add(DesignerWorksFragment.newInstance());
        fragmentList.add(MyCenterFragment.newInstance());
        new FragmentTabUtils(getSupportFragmentManager(),
                fragmentList, R.id.main_fragment_container, mRgs);

    }

    @Override
    protected void onDestroy() {
        if (service != null) {
            unregisterReceiver(service);
        }
        super.onDestroy();
    }
}
