package cn.cloudworkshop.seller.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
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
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.bean.DistributeBean;
import cn.cloudworkshop.seller.bean.StoreBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.service.DownloadService;
import cn.cloudworkshop.seller.utils.GsonUtils;
import cn.cloudworkshop.seller.utils.PermissionUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import cn.cloudworkshop.seller.utils.ToastUtils;
import okhttp3.Call;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Author：Libin on 2016/11/9 09:33
 * Email：1993911441@qq.com
 * Describe：主界面
 */
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.tv_goods)
    TextView tvGoods;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.tv_stock)
    TextView tvStock;
    @BindView(R.id.tv_distribute)
    TextView tvDistribute;
    @BindView(R.id.tv_set)
    TextView tvSet;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_turnover)
    TextView tvTurnover;


    //读写权限
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.tv_log_out)
    TextView tvLogOut;
    //是否检测更新
    private boolean isCheckUpdate = true;
    //下载服务
    private BroadcastReceiver downloadService;
    private StoreBean storeBean;
    //退出应用
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        writeToStorage();
        initData();
        checkUpdate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
        OkHttpUtils.get()
                .url(Constant.HOMEPAGE)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        storeBean = GsonUtils.jsonToBean(response, StoreBean.class);
                        if (storeBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {
        tvOrderNum.setText(storeBean.getData().getOrder_count() + "");
        tvTurnover.setText("¥" + new DecimalFormat("0.00").format(storeBean.getData().getOrder_sum()));
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {


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
                + File.separator + "miaoding", "miaoding.apk");
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

    @OnClick({R.id.tv_order, R.id.tv_goods, R.id.tv_wallet, R.id.tv_stock, R.id.tv_distribute, R.id.tv_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_order:
                startActivity(new Intent(this, OrderActivity.class));
                break;
            case R.id.tv_goods:
                break;
            case R.id.tv_wallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
            case R.id.tv_stock:
                startActivity(new Intent(this, StockActivity.class));
                break;
            case R.id.tv_distribute:
                startActivity(new Intent(this, DistributeActivity.class));
                break;
            case R.id.tv_set:
                startActivity(new Intent(this, SetUpActivity.class));
                break;
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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new PermissionUtils(this).showPermissionDialog("读写内存");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

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
    protected void onDestroy() {
        if (downloadService != null) {
            unregisterReceiver(downloadService);
        }
        super.onDestroy();
    }

    @OnClick(R.id.tv_log_out)
    public void onViewClicked() {
        logOut();
    }

    /**
     * 退出登录
     */
    private void logOut() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setTitle("退出登录");
        dialog.setMessage("您确定要退出登录吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.get()
                        .url(Constant.LOG_OUT)
                        .addParams("token", SharedPreferencesUtils.getStr(MainActivity.this, "token"))
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
                                        SharedPreferencesUtils.deleteStr(MainActivity.this, "token");
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        });
        //为“取消”按钮注册监听事件
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }

}
