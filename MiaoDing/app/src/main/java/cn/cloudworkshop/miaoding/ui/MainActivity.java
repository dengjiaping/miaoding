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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.AppIconBean;
import cn.cloudworkshop.miaoding.bean.AppIndexBean;
import cn.cloudworkshop.miaoding.bean.GuideBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.fragment.CustomGoodsFragment;
import cn.cloudworkshop.miaoding.fragment.DesignerWorksFragment;
import cn.cloudworkshop.miaoding.fragment.HomepageFragment;
import cn.cloudworkshop.miaoding.fragment.MyCenterFragment;
import cn.cloudworkshop.miaoding.service.DownloadService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.FragmentTabUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author：Libin on 2017-06-21 10:35
 * Email：1993911441@qq.com
 * Describe：首页
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.tab_main)
    TabLayout tabMain;
    @BindView(R.id.img_load_error)
    ImageView imgLoadingError;

    //fragment
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentTabUtils fragmentUtils;
    //下载服务
    private DownloadService downloadService;
    //退出应用
    private long exitTime = 0;
    //是否检测更新
    private boolean isCheckUpdate = true;
    //读写权限
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AppIconBean iconBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        storagePermiss();
        checkUpdate1();

//        initIcon();
//        checkUpdate();

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
                        imgLoadingError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        imgLoadingError.setVisibility(View.GONE);
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
        String clientId = SharedPreferencesUtils.getStr(this, "client_id");
        if (clientId != null) {
            Map<String, String> map = new HashMap<>();
            map.put("device_id", clientId);

            String token = SharedPreferencesUtils.getStr(this, "token");
            if (!TextUtils.isEmpty(token)) {
                map.put("token", token);
            }

            OkHttpUtils.post()
                    .url(Constant.CLIENT_ID)
                    .params(map)
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
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code == 10001) {
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "uid");
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "token");
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "username");
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "avatar");
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "phone");
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
    private void checkUpdate1() {
            OkHttpUtils.get()
                    .url(Constant.APP_INDEX)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            final AppIndexBean appIndexBean = GsonUtils.jsonToBean(response, AppIndexBean.class);
                            MyApplication.serverPhone = appIndexBean.getData().getKf_tel();
                            MyApplication.userAgreement = appIndexBean.getData().getUser_manual();
                            MyApplication.cobbler_banner = appIndexBean.getData().getCobbler_banner();
                            MyApplication.loginBg = appIndexBean.getData().getLogin_img();
                            if (appIndexBean.getData().getVersion().getAndroid() != null &&
                                    Integer.valueOf(appIndexBean.getData().getVersion().getAndroid()
                                            .getVersion()) > getVersionCode()) {
                                MyApplication.updateUrl = appIndexBean.getData().getDownload_url();
                                MyApplication.updateContent = appIndexBean.getData().getVersion()
                                        .getAndroid().getRemark();

                                View popupView = getLayoutInflater().inflate(R.layout.ppw_check_update, null);
                                final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                if (!isFinishing()){
                                    mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                                }

                                DisplayUtils.setBackgroundAlpha(MainActivity.this, true);

                                TextView tvContent = (TextView) popupView.findViewById(R.id.tv_update_content);
                                final TextView tvUpdate = (TextView) popupView.findViewById(R.id.tv_confirm_update);

                                tvContent.setText(appIndexBean.getData().getVersion().getAndroid().getRemark());

                                tvUpdate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        tvUpdate.setText("正在下载");
                                        tvUpdate.setEnabled(false);
                                        downloadFile(appIndexBean.getData().getDownload_url());
                                    }
                                });
                            }else {
                                initIcon();
                            }
                        }
                    });

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
                            MyApplication.serverPhone = appIndexBean.getData().getKf_tel();
                            MyApplication.userAgreement = appIndexBean.getData().getUser_manual();
                            MyApplication.cobbler_banner = appIndexBean.getData().getCobbler_banner();
                            MyApplication.loginBg = appIndexBean.getData().getLogin_img();
                            if (appIndexBean.getData().getVersion().getAndroid() != null &&
                                    Integer.valueOf(appIndexBean.getData().getVersion().getAndroid()
                                            .getVersion()) > getVersionCode()) {
                                MyApplication.updateUrl = appIndexBean.getData().getDownload_url();
                                MyApplication.updateContent = appIndexBean.getData().getVersion()
                                        .getAndroid().getRemark();
                                //取消检测更新
                                isCheckUpdate = false;
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this,
                                        R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                                dialog.setTitle("检测到新版本，请更新");
                                dialog.setMessage(appIndexBean.getData().getVersion().getAndroid().getRemark());
                                //确定
                                dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadFile(appIndexBean.getData().getDownload_url());
                                    }
                                });
                                //取消
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

        downloadService = new DownloadService(file);
        registerReceiver(downloadService, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
        fragmentList.add(CustomGoodsFragment.newInstance());
        fragmentList.add(DesignerWorksFragment.newInstance());
        fragmentList.add(MyCenterFragment.newInstance());
        fragmentUtils = new FragmentTabUtils(this, getSupportFragmentManager(), fragmentList,
                R.id.frame_container, tabMain, iconBean.getData());

    }

    /**
     * 首次进入，推荐注册
     */
    private void isFirstIn() {
        //默认首次
        boolean isFirst = SharedPreferencesUtils.getBoolean(this, "first_in", true);
        if (isFirst && TextUtils.isEmpty(SharedPreferencesUtils.getStr(this, "token"))) {
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
                                if (!isFinishing()) {
                                    mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                                }
                                View viewRegister = popupView.findViewById(R.id.view_register);
                                View viewCancel = popupView.findViewById(R.id.view_cancel);
                                ImageView imgRegister = (ImageView) popupView.findViewById(R.id.img_register);

                                Glide.with(MainActivity.this)
                                        .load(Constant.IMG_HOST + guideBean.getData().getImg_urls().get(0))
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
    private void storagePermiss() {
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
        int currentPage = getIntent().getIntExtra("page", 0);
        if (fragmentUtils != null && !isFinishing()) {
            fragmentUtils.setCurrentFragment(currentPage);
        }
    }

    @Override
    protected void onDestroy() {
        if (downloadService != null) {
            unregisterReceiver(downloadService);
        }
        super.onDestroy();
    }

    @OnClick(R.id.img_load_error)
    public void onViewClicked() {
        initIcon();
    }
}
