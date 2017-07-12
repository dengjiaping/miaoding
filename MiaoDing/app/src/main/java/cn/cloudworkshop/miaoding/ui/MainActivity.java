package cn.cloudworkshop.miaoding.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import cn.cloudworkshop.miaoding.bean.AppIconBean;
import cn.cloudworkshop.miaoding.bean.AppIndexBean;
import cn.cloudworkshop.miaoding.bean.GuideBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.fragment.DesignerWorksFragment;
import cn.cloudworkshop.miaoding.fragment.HomepageFragment;
import cn.cloudworkshop.miaoding.fragment.MyCenterFragment;
import cn.cloudworkshop.miaoding.fragment.NewCustomGoodsFragment;
import cn.cloudworkshop.miaoding.service.DownloadService;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.NewFragmentTabUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author：binge on 2017-06-21 10:35
 * Email：1993911441@qq.com
 * Describe：首页
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tab_main)
    TabLayout tabMain;

    //fragment
    private List<Fragment> fragmentList = new ArrayList<>();
    private NewFragmentTabUtils fragmentUtils;
    //下载服务
    private DownloadService service;
    //退出应用
    private long exitTime = 0;
    //是否检测更新
    private boolean isCheckUpdate = true;

    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AppIconBean iconBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        writeToStorage();
        initIcon();
        checkUpdate();
        isLogin();
        submitClientId();
    }


    /**
     * 加载app icon
     */
    private void initIcon() {
        OkHttpUtils.get()
                .url(Constant.APP_ICON)
                .addParams("type", "2")
                .addParams("is_android", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        iconBean = GsonUtils.jsonToBean(response, AppIconBean.class);
                        if (iconBean.getData() != null && iconBean.getData().size() > 0) {
                            initView();
                        }
                    }
                });
    }


    /**
     * 推送，提交设备id
     */
    private void submitClientId() {
        String clientId = SharedPreferencesUtils.getString(this, "client_id");
        if (clientId != null) {
            OkHttpUtils.get()
                    .url(Constant.CLIENT_ID)
                    .addParams("type", "1")
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
        if (isCheckUpdate) {
            OkHttpUtils.get()
                    .url(Constant.APP_INDEX)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onResponse(String response, int id) {
                            final AppIndexBean appIndexBean = GsonUtils.jsonToBean(response, AppIndexBean.class);
                            MyApplication.loginBg = appIndexBean.getData().getLogin_img();
                            MyApplication.serverPhone = appIndexBean.getData().getKf_tel();
                            MyApplication.userAgreement = appIndexBean.getData().getUser_manual();
                            if (appIndexBean.getData().getVersion().getAndroid() != null &&
                                    Integer.valueOf(appIndexBean.getData().getVersion().getAndroid()
                                            .getVersion()) > getVersionCode()) {
                                MyApplication.updateUrl = appIndexBean.getData().getDownload_url();
                                MyApplication.updateContent = appIndexBean.getData().getVersion()
                                        .getAndroid().getRemark();
                                //取消检测更新
                                isCheckUpdate = false;
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,
                                        R.style.AlertDialog);
                                dialog.setTitle("检测到新版本，请更新");
                                dialog.setMessage(appIndexBean.getData().getVersion().getAndroid().getRemark());
                                //为“确定”按钮注册监听事件
                                dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadFile(appIndexBean.getData().getDownload_url());
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

    }

    /**
     * 下载文件
     */
    private void downloadFile(String url) {
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("妙定");
        request.setDescription("正在下载");
        // 设置下载可见
        request.setVisibleInDownloadsUi(true);
        //下载完成后通知栏可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
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
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "CloudWorkshop", "miaoding.apk");
        if (file.exists()) {
            file.delete();
        }
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


    /**
     * 加载Fragment
     */
    public void initView() {
        fragmentList.add(HomepageFragment.newInstance());
        fragmentList.add(NewCustomGoodsFragment.newInstance());
        fragmentList.add(DesignerWorksFragment.newInstance());
        fragmentList.add(MyCenterFragment.newInstance());
        fragmentUtils = new NewFragmentTabUtils(this, getSupportFragmentManager(), fragmentList,
                R.id.frame_container, tabMain, iconBean.getData());

    }

    /**
     * 首次进入，推荐注册
     */
    private void isFirstIn() {
        //默认首次
        boolean isFirst = SharedPreferencesUtils.getBoolean(this, "first_in", true);
        if (isFirst && TextUtils.isEmpty(SharedPreferencesUtils.getString(this, "token"))) {
            OkHttpUtils.get()
                    .url(Constant.GUIDE_IMG)
                    .addParams("id", "5")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GuideBean guideBean = GsonUtils.jsonToBean(response, GuideBean.class);
                            if (guideBean.getData().getImg_urls() != null && guideBean.getData()
                                    .getImg_urls().size() > 0) {

                                SharedPreferencesUtils.saveBoolean(MainActivity.this, "first_in", false);
                                View popupView = getLayoutInflater().inflate(R.layout.ppw_register, null);
                                final PopupWindow mPopupWindow = new PopupWindow(popupView,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                                mPopupWindow.setTouchable(true);
                                mPopupWindow.setFocusable(true);
                                mPopupWindow.setOutsideTouchable(true);
                                mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
                                mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

                                View viewRegister = popupView.findViewById(R.id.view_register);
                                View viewCancel = popupView.findViewById(R.id.view_cancel);
                                ImageView imgRegister = (ImageView) popupView.findViewById(R.id.img_register);

                                Glide.with(MainActivity.this)
                                        .load(Constant.HOST + guideBean.getData().getImg_urls().get(0))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(imgRegister);
                                viewRegister.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    }
                                });
                                viewCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                    }
                                });
                            }
                        }
                    });
        }
    }


    /**
     * 读写权限
     */
    @AfterPermissionGranted(111)
    private void writeToStorage() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "本应用需要使用存储权限", 111, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new PermissionUtils(this).showPermissionDialog("读写内存");
        }
    }


    @Override
    protected void onDestroy() {
        if (service != null) {
            unregisterReceiver(service);
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            isFirstIn();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();

                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int currentPage = intent.getIntExtra("page", 0);
        fragmentUtils.setCurrentFragment(currentPage);
    }
}
