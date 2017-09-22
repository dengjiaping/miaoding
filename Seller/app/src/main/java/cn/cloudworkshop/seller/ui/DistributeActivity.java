package cn.cloudworkshop.seller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.bean.DistributeBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.GsonUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-09-20 13:49
 * Email：1993911441@qq.com
 * Describe：分销
 */
public class DistributeActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_invite_num)
    TextView tvInviteNum;
    @BindView(R.id.tv_invite_money)
    TextView tvInviteMoney;
    private DistributeBean distributeBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribute);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("分销管理");
        initData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    private void initData() {

        OkHttpUtils.get()
                .url(Constant.DISTRIBUTE)
                .addParams("token", SharedPreferencesUtils.getStr(this,"token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        distributeBean = GsonUtils.jsonToBean(response,DistributeBean.class);
                        if (distributeBean.getData() != null){
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        tvInviteNum.setText(distributeBean.getData().getUser_num()+"");
        tvInviteMoney.setText(new DecimalFormat("#0.00").format(distributeBean.getData().getReward_money()));

    }

    @OnClick(R.id.img_header_back)
    public void onViewClicked() {
        finish();
    }
}
