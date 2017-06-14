package cn.cloudworkshop.miaoding.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ImageEncodeUtils;

/**
 * Author：binge on 2017/2/9 11:34
 * Email：1993911441@qq.com
 * Describe：优惠券使用规则
 */
public class CouponRuleActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_user_rule)
    ImageView imgUserRule;
    @BindView(R.id.img_user_rule1)
    ImageView imgUserRule1;
    @BindView(R.id.img_user_rule2)
    ImageView imgUserRule2;
    private String title;
    private String imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_rule);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        title = getIntent().getStringExtra("title");
        imgUrl = getIntent().getStringExtra("img_url");
    }

    private void initView() {
        tvHeaderTitle.setText(title);
        Glide.with(getApplicationContext())
                .load(Constant.HOST + imgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        InputStream inputStream = ImageEncodeUtils.bitmap2InputStream(resource);
                        try {
                            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, true);
                            int width = decoder.getWidth();
                            int height = decoder.getHeight();
                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            Rect rect = new Rect();


                            rect.set(0, 0, width, height / 3);
                            Bitmap bm = decoder.decodeRegion(rect, opts);
                            imgUserRule.setImageBitmap(bm);

                            rect.set(0, height / 3, width, height / 3 * 2);
                            Bitmap bm1 = decoder.decodeRegion(rect, opts);
                            imgUserRule1.setImageBitmap(bm1);

                            rect.set(0, height / 3 * 2, width, height);
                            Bitmap bm2 = decoder.decodeRegion(rect, opts);
                            imgUserRule2.setImageBitmap(bm2);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @OnClick(R.id.img_header_back)
    public void onClick() {
        finish();
    }
}
