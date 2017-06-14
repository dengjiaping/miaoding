package cn.cloudworkshop.miaoding.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;

/**
 * Author：binge on 2017-06-14 16:42
 * Email：1993911441@qq.com
 * Describe：扫描二维码
 */
public class ScanCodeActivity extends BaseActivity implements ZBarView.Delegate {
    @BindView(R.id.zbar_view)
    ZBarView zbarView;
    @BindView(R.id.img_scan_back)
    ImageView imgScanBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        zbarView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbarView.startCamera();
        zbarView.showScanRect();
        zbarView.startSpot();
    }

    @Override
    protected void onStop() {
        zbarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zbarView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        if (result != null) {
            String[] split = result.split("\\?");
            if (split[1] != null) {
                String[] split1 = split[1].split("&");
                if (split1[0] != null && split1[1] != null) {
                    String goods_id = split1[0].split("=")[1];
                    String goods_type = split1[1].split("=")[1];
                    if (goods_id != null && goods_type != null) {
                        Intent intent = null;
                        if (goods_type.equals("1")) {
                            intent = new Intent(this, CustomGoodsActivity.class);
                        } else if (goods_type.equals("2")) {
                            intent = new Intent(this, WorksDetailActivity.class);
                        }

                        intent.putExtra("id", goods_id);
                        finish();
                        startActivity(intent);
                    }
                }
            }

        }

        zbarView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @OnClick(R.id.img_scan_back)
    public void onViewClicked() {
        finish();
    }
}
