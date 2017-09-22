package cn.cloudworkshop.seller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import cn.cloudworkshop.seller.bean.WalletBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.GsonUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-09-12 15:39
 * Email：1993911441@qq.com
 * Describe：钱包
 */
public class WalletActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_wallet_money)
    TextView tvWalletMoney;
    @BindView(R.id.tv_sell_money)
    TextView tvSellMoney;
    @BindView(R.id.tv_reward_money)
    TextView tvRewardMoney;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.img_draw_cash)
    ImageView imgDrawCash;
    private WalletBean walletBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("钱包");
        initData();
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.WALLET)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        walletBean = GsonUtils.jsonToBean(response, WalletBean.class);
                        if (walletBean.getData() != null) {
                            initView();
                        }
                    }
                });

    }

    private void initView() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        tvWalletMoney.setText("¥" + decimalFormat.format(walletBean.getData().getMoney()));
        tvSellMoney.setText("¥" + decimalFormat.format(walletBean.getData().getMoney()));
        tvRewardMoney.setText("¥" + decimalFormat.format(walletBean.getData().getFenxiao()));
        tvTotalMoney.setText("¥" + decimalFormat.format(walletBean.getData().getCash()));
    }

    @OnClick({R.id.img_header_back, R.id.img_draw_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_draw_cash:
                break;
        }
    }
}
