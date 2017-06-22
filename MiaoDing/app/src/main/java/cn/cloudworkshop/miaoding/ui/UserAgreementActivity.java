package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseActivity;

/**
 * Author：binge on 2017/2/10 16:16
 * Email：1993911441@qq.com
 * Describe：用户协议
 */
public class UserAgreementActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        type = getIntent().getStringExtra("content");
    }

    private void initView() {
        switch (type){
            case "measure":
                tvHeaderTitle.setText("量体协议");
//                tvAgreement.setText(MyApplication.measureAgreement);
                break;
        }

    }

    @OnClick(R.id.img_header_back)
    public void onClick() {
        finish();
    }
}
