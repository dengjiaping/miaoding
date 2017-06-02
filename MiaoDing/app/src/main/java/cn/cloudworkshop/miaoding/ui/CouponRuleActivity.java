package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;

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
    private String type;
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
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        imgUrl = getIntent().getStringExtra("img_url");
    }

    private void initView() {
        switch (type) {
            case "coupon_rule":
                tvHeaderTitle.setText("使用规则");
                imgUserRule.setImageResource(R.mipmap.icon_coupon_rule);
                break;
            case "member_rule":
                tvHeaderTitle.setText(title);
                Glide.with(getApplicationContext())
                        .load(Constant.HOST + imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgUserRule);
                break;
            case "invite_rule":
                tvHeaderTitle.setText(title);
                Glide.with(getApplicationContext())
                        .load(Constant.HOST + imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgUserRule);
                break;

        }
    }

    @OnClick(R.id.img_header_back)
    public void onClick() {
        finish();
    }
}
