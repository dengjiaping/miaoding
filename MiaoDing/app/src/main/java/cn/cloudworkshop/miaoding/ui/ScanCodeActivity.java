package cn.cloudworkshop.miaoding.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yanzhenjie.zbar.camera.CameraPreview;
import com.yanzhenjie.zbar.camera.ScanCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;

/**
 * Author：binge on 2017-06-15 08:48
 * Email：1993911441@qq.com
 * Describe：扫描二维码
 */
public class ScanCodeActivity extends BaseActivity {
    @BindView(R.id.capture_preview)
    CameraPreview capturePreview;
    @BindView(R.id.capture_crop_view)
    RelativeLayout captureCropView;
    @BindView(R.id.capture_scan_line)
    ImageView captureScanLine;
    @BindView(R.id.img_scan_back)
    ImageView imgScanBack;


    private ValueAnimator mScanAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        capturePreview.setScanCallback(new ScanCallback() {
            @Override
            public void onScanResult(String content) {
                if (content != null) {
                    vibrator();
                    String[] split = content.split("\\?");
                    if (split.length > 1 && split[1] != null) {
                        String[] split1 = split[1].split("&");
                        if (split1.length > 1 && split1[0] != null && split1[1] != null) {
                            String goods_id = split1[0].split("=")[1];
                            String goods_type = split1[1].split("=")[1];
                            if (goods_id != null && goods_type != null) {
                                switch (goods_type) {
                                    case "1":
                                        toGoodsDetail(CustomGoodsActivity.class, goods_id);
                                        break;
                                    case "2":
                                        toGoodsDetail(NewWorksActivity.class, goods_id);
                                        break;
                                    default:
                                        ToastUtils.showToast(ScanCodeActivity.this, "仅支持本平台商品");
                                        finish();
                                        break;
                                }

                            } else {
                                ToastUtils.showToast(ScanCodeActivity.this, "仅支持本平台商品");
                                finish();
                            }
                        } else {
                            ToastUtils.showToast(ScanCodeActivity.this, "仅支持本平台商品");
                            finish();
                        }
                    } else {
                        ToastUtils.showToast(ScanCodeActivity.this, "仅支持本平台商品");
                        finish();
                    }

                } else {
                    ToastUtils.showToast(ScanCodeActivity.this, "仅支持本平台商品");
                    finish();
                }
            }


        });
    }

    /**
     * @param cls
     * @param goodsId 跳转商品详情
     */
    private void toGoodsDetail(Class<? extends Activity> cls, String goodsId) {
        Intent intent = new Intent(ScanCodeActivity.this, cls);
        intent.putExtra("id", goodsId);
        startActivity(intent);
        finish();
    }


    /**
     * 振动
     */
    private void vibrator() {
        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(100);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mScanAnimator != null) {
            if (capturePreview.start()) {
                mScanAnimator.start();
            }
        }
    }

    @Override
    public void onPause() {
        // Must be called here, otherwise the camera should not be released properly.
        stopScan();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScanAnimator.cancel();
        capturePreview.stop();
    }

    /**
     * Stop scan.
     */
    private void stopScan() {
        mScanAnimator.cancel();
        capturePreview.stop();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mScanAnimator == null) {
            int height = captureCropView.getMeasuredHeight() - 25;
            mScanAnimator = ObjectAnimator.ofFloat(captureScanLine, "translationY", 0F, height).setDuration(3000);
            mScanAnimator.setInterpolator(new LinearInterpolator());
            mScanAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mScanAnimator.setRepeatMode(ValueAnimator.REVERSE);

            if (capturePreview.start()) {
                mScanAnimator.start();
            }
        }
    }


    @OnClick(R.id.img_scan_back)
    public void onViewClicked() {
        finish();
    }
}
