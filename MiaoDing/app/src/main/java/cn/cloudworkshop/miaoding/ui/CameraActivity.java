/*

package cn.cloudworkshop.miaoding.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DetectRectangles;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.ImageEncodeUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CustomCameraView;
import cn.cloudworkshop.miaoding.view.SensorView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
*
        *Author：binge on 2017/3/6 12:17
        *Email：1993911441

@qq.com
 *Describe：拍照


public class CameraActivity extends BaseActivity implements SensorEventListener {

    @BindView(R.id.img_position)
    ImageView imgPosition;
    @BindView(R.id.bubble_sensor)
    SensorView bubbleSensor;
    @BindView(R.id.img_cancel_take)
    ImageView imgCancelTake;
    @BindView(R.id.tv_take_again)
    TextView tvTakeAgain;
    @BindView(R.id.tv_take_next)
    TextView tvTakeNext;
    @BindView(R.id.custom_camera_view)
    CustomCameraView cameraView;
    @BindView(R.id.img_take_photo)
    ImageView imgTakePhoto;
    @BindView(R.id.view_loading)
    AVLoadingIndicatorView loadingView;

    private boolean mInitialized = false;
    private float mLastX = 0;
    private float mLastY = 0;

    private float mLastZ = 0;

    //照片保存路径
    private String[] photoArray = new String[4];

    //背景图片
    private int[] positionArray = {R.mipmap.positive, R.mipmap.left, R.mipmap.back, R.mipmap.right};
    //拍照次数
    private int count = 0;
    //传感器
    private SensorManager mSensorManager;

    private boolean canTakePhoto = true;
    //比例
    private String[] scaleStr = new String[4];
    //A4纸左上角Y轴坐标
    private String[] yPosition = new String[4];


    private float userHeight;
    private Thread myThread;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    break;

                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingView.smoothToHide();
            switch (msg.what) {
                case 0:
                    Toast.makeText(CameraActivity.this, "拍摄失败，请重拍", Toast.LENGTH_SHORT).show();
                    tvTakeAgain.setVisibility(View.GONE);
                    tvTakeNext.setVisibility(View.GONE);
                    canTakePhoto = true;
                    break;
                case 1:
                    Toast.makeText(CameraActivity.this, "拍摄成功，下一步", Toast.LENGTH_SHORT).show();
                    tvTakeAgain.setVisibility(View.VISIBLE);
                    tvTakeNext.setVisibility(View.VISIBLE);
                    canTakePhoto = false;
                    if (count == 3) {
                        tvTakeNext.setText("提交");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明导航栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        getData();

//        ViewGroup.LayoutParams layoutParams = viewStroke.getLayoutParams();
//        layoutParams.width = (int) DisplayUtils.dp2px(this, 200);
//        layoutParams.height = (int) DisplayUtils.dp2px(this, (float) (userHeight * 2.05));
//        viewStroke.setLayoutParams(layoutParams);

        loadingView.setIndicator(new BallSpinFadeLoaderIndicator());
        loadingView.setIndicatorColor(Color.GRAY);

    }

    private void getData() {
        userHeight = getIntent().getFloatExtra("height", 0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        //初始化OpenCV
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        cameraView.setOnTakeFinish(new CustomCameraView.OnTakeFinish() {
            @Override
            public void takeFinish(Bitmap bitmap) {
                Map<String, String> map = DetectRectangles.findRectangles(CameraActivity.this, bitmap,
                        Integer.parseInt(etBlockSize.getText().toString()),
                        Double.parseDouble(etDelta.getText().toString()));
                if (map == null) {
                    loadingView.smoothToHide();
                    Toast.makeText(CameraActivity.this, "拍摄失败，请重拍", Toast.LENGTH_SHORT).show();
                    tvTakeAgain.setVisibility(View.GONE);
                    tvTakeNext.setVisibility(View.GONE);
                    canTakePhoto = true;
                } else {
                    scaleStr[count] = map.get("scale");
                    yPosition[count] = map.get("y");


                    loadingView.smoothToHide();
                    Toast.makeText(CameraActivity.this, "拍摄成功，下一步", Toast.LENGTH_SHORT).show();
                    tvTakeAgain.setVisibility(View.VISIBLE);
                    tvTakeNext.setVisibility(View.VISIBLE);
                    canTakePhoto = false;
                    if (count == 3) {
                        tvTakeNext.setText("提交");
                    }
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @OnClick({R.id.img_cancel_take, R.id.tv_take_again, R.id.img_take_photo, R.id.tv_take_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_cancel_take:
                finish();
                break;
            case R.id.tv_take_again:
                tvTakeAgain.setVisibility(View.GONE);
                tvTakeNext.setVisibility(View.GONE);
                canTakePhoto = true;
                break;
            case R.id.img_take_photo:
                loadingView.smoothToShow();
                canTakePhoto = false;
                photoArray[count] = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/CloudWorkshop/camera" + count + ".jpg";
                cameraView.takePicture(photoArray[count]);
                break;
            case R.id.tv_take_next:
                if (count < 3) {
                    count++;
                    imgPosition.setImageResource(positionArray[count]);
                    tvTakeAgain.setVisibility(View.GONE);
                    tvTakeNext.setVisibility(View.GONE);
                    canTakePhoto = true;
                } else {
                    submitData();
                }
                break;
        }
    }

    private void submitData() {
        loadingView.smoothToShow();

        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < photoArray.length; i++) {
            if (i < photoArray.length - 1) {
                sb2.append(scaleStr[i]).append(",");
                sb3.append(yPosition[i]).append(",");
            } else {
                sb2.append(scaleStr[i]);
                sb3.append(yPosition[i]);
            }
        }

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (int i = 0; i < photoArray.length; i++) {
            File file = new File(photoArray[i]);
            builder.addFormDataPart("img" + i, file.getName(),
                    RequestBody.create(MediaType.parse("image/png"), file));
        }


        builder.addFormDataPart("token", SharedPreferencesUtils.getStr(this, "token"));
        builder.addFormDataPart("scale", sb2.toString());
        builder.addFormDataPart("y_position", sb3.toString());
        builder.addFormDataPart("height", String.valueOf(userHeight));


        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(Constant.TAKE_PHOTO)//地址
                .post(requestBody)//添加请求体
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingView.smoothToHide();
                        Toast.makeText(CameraActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        // 获取手机触发event的传感器的类型
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ORIENTATION:
                //绕Z轴旋转
                float zAngle = values[0];
                //绕X轴旋转
                //-180 -- 0，前0，后-180
                float verAngle = values[1];
                //绕Y轴旋转
                //-90 --  90，  左90，右-90
                float horAngle = values[2];

                int width = DisplayUtils.getMetrics(this).widthPixels;
                int height = DisplayUtils.getMetrics(this).heightPixels;

                //手机俯仰
                bubbleSensor.verX = 0;
                if (verAngle >= -180 && verAngle <= 0) {
                    bubbleSensor.verY = height * Math.abs(verAngle) / 180 -
                            bubbleSensor.verBubble.getHeight() / 2;
                }

                //左右旋转
                if (horAngle >= -90 && horAngle <= 90) {
                    bubbleSensor.horX = width / 2 - width / 2 * horAngle / 90 -
                            bubbleSensor.horBubble.getWidth() / 2;
                }
                bubbleSensor.horY = height - bubbleSensor.horBubble.getHeight() - 300;
                bubbleSensor.postInvalidate();


                //偏移角度
                if (verAngle > -100 && verAngle < -80 && horAngle > -10 && horAngle < 10 && canTakePhoto) {
                    imgTakePhoto.setVisibility(View.VISIBLE);
                } else {
                    imgTakePhoto.setVisibility(View.GONE);
                }

                //聚焦
                if (!mInitialized) {
                    mLastX = zAngle;
                    mLastY = verAngle;
                    mLastZ = horAngle;
                    mInitialized = true;
                }

                float deltaX = Math.abs(mLastX - zAngle);
                float deltaY = Math.abs(mLastY - verAngle);
                float deltaZ = Math.abs(mLastZ - horAngle);

                if (deltaX > 0.3 || deltaY > 0.3 || deltaZ > 0.3) {
                    if (CustomCameraView.camera != null) {
                        if (null != myThread) {
                            myThread.interrupt();
                        }
                        myThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                cameraView.setFocus();
                            }
                        });
                        myThread.start();
                    }
                }

                mLastX = zAngle;
                mLastY = verAngle;
                mLastZ = horAngle;

                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
*/
