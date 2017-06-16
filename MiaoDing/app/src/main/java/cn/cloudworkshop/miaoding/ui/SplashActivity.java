package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.utils.TitleBarUtils;

/**
 * Author：Libin on 2016/11/9 09:33
 * Email：1993911441@qq.com
 * Describe：启动页
 */
public class SplashActivity extends BaseActivity {
    //    @BindView(R.id.video_view)
//    VideoView videoView;
//    @BindView(R.id.tv_next)
//    TextView tvNext;
    @BindView(R.id.img_splash)
    ImageView imgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TitleBarUtils.setNoTitleBar(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        imgSplash.setImageResource(R.mipmap.icon_splash_bg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        }, 2500);


//        //首次进入，播放视频
//        boolean user_first = SharedPreferencesUtils.getBoolean(this, "first", true);
//        if (user_first) {
//            //第一次启动
//            tvNext.setVisibility(View.VISIBLE);
//            SharedPreferencesUtils.saveBoolean(this, "first", false);
//            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.app_video));
//            videoView.requestFocus();
//            //播放
//            videoView.start();
//            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//            });
//        } else {
//            imgSplash.setImageResource(R.mipmap.icon_splash_logo);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//            }, 2500);
//        }
    }

//    @OnClick(R.id.tv_next)
//    public void onClick() {
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        finish();
//        startActivity(intent);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
