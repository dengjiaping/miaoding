package com.zbar.lib;

import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.ui.CustomGoodsActivity;
import cn.cloudworkshop.miaoding.ui.WorksDetailActivity;


/**
 * Author：Libin on 2016/7/25 15:06
 * Email：1993911441@qq.com
 * Describe：
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    /**
     * 声音大小
     */
    private static final float BEEP_VOLUME = 0.50f;

    /**
     * 震动周期
     */
    private static final long VIBRATE_DURATION = 200L;
    @BindView(R.id.img_code_back)
    ImageView imgCodeBack;

    /**
     * 返回按钮
     */
    private Button btnBack;

    /**
     * 扫描画面Handler
     */
    private CaptureActivityHandler handler;

    /**
     * 是否有SurfaceView
     */
    private boolean hasSurface;

    /**
     * 计时器
     */
    private InactivityTimer inactivityTimer;

    /**
     * 媒体播放器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 是否播放音频
     */
    private boolean playBeep;

    /**
     * 是否震动音频
     */
    private boolean vibrate;

    /**
     * 截取的x坐标
     */
    private int x = 0;

    /**
     * 截取的y坐标
     */
    private int y = 0;

    /**
     * 截取的区域宽度
     */
    private int cropWidth = 0;

    /**
     * 截取的区域高度
     */
    private int cropHeight = 0;

    /**
     * 扫描区域
     */
    private RelativeLayout captureContainter;

    /**
     * 扫描框布局
     */
    private RelativeLayout captureCropLayout;

    /**
     * 是否截取扫描的二维码图片
     */
    private boolean isNeedCapture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 扫描画面
        setContentView(R.layout.activity_scan_qrcode);
        ButterKnife.bind(this);

        // 初始化 CameraManager
        CameraManager.init(getApplication());

        // 默认没有SurfaceView
        hasSurface = false;

        // 初始化计时器
        inactivityTimer = new InactivityTimer(this);

        // 获取扫描区域
        captureContainter = (RelativeLayout) findViewById(R.id.capture_container);

        // 获取扫描框布局
        captureCropLayout = (RelativeLayout) findViewById(R.id.captureCropLayout);

        // 扫描线
        ImageView captureScanLine = (ImageView) findViewById(R.id.captureScanLine);
        // 扫描线的动画
        TranslateAnimation mAnimation = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE,
                0f, TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
        // 动画持续时间
        mAnimation.setDuration(1500);
        // 无限循环
        mAnimation.setRepeatCount(-1);
        // 来回扫描
        mAnimation.setRepeatMode(Animation.REVERSE);
        // 动画速率：匀速
        mAnimation.setInterpolator(new LinearInterpolator());
        // 设置动画
        captureScanLine.setAnimation(mAnimation);


    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {

        super.onResume();

        // 相机预览控件（SurfaceView）
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capturePreview);

        // SurfaceHolder
        SurfaceHolder surfaceHolder = surfaceView.getHolder();

        // 已经初始化过相机预览控件
        if (hasSurface) {
            // 初始化相机
            initCamera(surfaceHolder);
        } else {
            // 调用接口初始化相机预览控件
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        // 播放声音
        playBeep = true;

        // 铃声
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        // 响铃类型不是标准
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            // 不播放声音
            playBeep = false;
        }

        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Handler退出
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }

        // 相机关闭
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {

        // 计时器停止
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 解码
     *
     * @param qrCodeString 解码后的文字
     */
    public void handleDecode(String qrCodeString) {

        inactivityTimer.onActivity();

        if (qrCodeString != null) {
            String[] split = qrCodeString.split("\\?");
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



    }

    /**
     * 初始化照相机
     *
     * @param surfaceHolder SurfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {

        try {

            // 打开相机
            CameraManager.get().openDriver(surfaceHolder);

            // 预览图
            Point point = CameraManager.get().getCameraResolution();

            // 预览图的宽度，也即camera的分辨率宽度
            int width = point.y;
            // 预览图的高度，也即camera的分辨率高度
            int height = point.x;

            /**************************************************************/
            // x： 预览图中二维码图片的左上顶点x坐标，也就是手机中相机预览中看到的待扫描二维码的位置的x坐标
            // y： 预览图中二维码图片的左上顶点y坐标，也就是手机中相机预览中看到的待扫描二维码的位置的y坐标
            // cropHeight： 预览图中二维码图片的高度
            // cropWidth： 预览图中二维码图片的宽度
            // height：预览图的高度，也即camera的分辨率高度
            // width： 预览图的宽度，也即camera的分辨率宽度
            //
            // captureCropLayout.getLeft()： 布局文件中扫描框的左上顶点x坐标
            // captureCropLayout.getTop() 布局文件中扫描框的左上顶点y坐标
            // captureCropLayout.getHeight()： 布局文件中扫描框的高度
            // captureCropLayout.getWidth()： 布局文件中扫描框的宽度
            // captureContainter.getHeight()：布局文件中相机预览控件的高度
            // captureContainter.getWidth()： 布局文件中相机预览控件的宽度
            //
            // 其中存在这样一个等比例公式：
            //
            // x / width = captureCropLayout.getLeft() /
            // captureContainter.getWidth();
            // y / height = captureCropLayout.getTop() /
            // captureContainter.getHeight();
            // cropWidth / width = captureCropLayout.getWidth() /
            // captureContainter.getWidth();
            // cropHeight / height = captureCropLayout.getHeight() /
            // captureContainter.getHeight();
            //
            // 即：
            //
            // x = captureCropLayout.getLeft() * width /
            // captureContainter.getWidth() ;
            // y = captureCropLayout.getTop() * height /
            // captureContainter.getHeight() ;
            // cropWidth = captureCropLayout.getWidth() * width /
            // captureContainter.getWidth() ;
            // cropHeight = captureCropLayout.getHeight() * height /
            // captureContainter.getHeight() ;
            /**************************************************************/

            // 获取预览图中二维码图片的左上顶点x坐标
            int x = captureCropLayout.getLeft() * width
                    / captureContainter.getWidth();
            // 预览图中二维码图片的左上顶点y坐标
            int y = captureCropLayout.getTop() * height
                    / captureContainter.getHeight();

            // 获取预览图中二维码图片的宽度
            int cropWidth = captureCropLayout.getWidth() * width
                    / captureContainter.getWidth();
            // 预览图中二维码图片的高度
            int cropHeight = captureCropLayout.getHeight() * height
                    / captureContainter.getHeight();

            // 设置
            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);

            // 设置是否需要截图
            setNeedCapture(false);

        } catch (IOException ioe) {
            // 异常处理
            return;
        } catch (RuntimeException e) {
            // 异常处理
            return;
        }

        // 没有Handler新建一个
        if (handler == null) {
            handler = new CaptureActivityHandler(CaptureActivity.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceView已经创建
        if (!hasSurface) {
            // 标记位更改
            hasSurface = true;
            // 初始化相机
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 标记位更改
        hasSurface = false;

    }


    /**
     * 播放声音及震动手机
     */
    private void playBeepSoundAndVibrate() {

        // 允许播放声音及音频播放器已经初始化
        if (playBeep && mediaPlayer != null) {
            // 播放音频
            mediaPlayer.start();
        }

        // 允许震动手机
        if (vibrate) {
            // 震动手机
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            // 设置震动频率
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }



    public Handler getHandler() {
        return handler;
    }

    public boolean isNeedCapture() {
        return isNeedCapture;
    }

    public void setNeedCapture(boolean isNeedCapture) {
        this.isNeedCapture = isNeedCapture;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    @OnClick(R.id.img_code_back)
    public void onViewClicked() {

        finish();
    }
}
