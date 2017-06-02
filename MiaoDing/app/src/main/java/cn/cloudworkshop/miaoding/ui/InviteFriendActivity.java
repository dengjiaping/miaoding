package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.InviteBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2016/12/8 11:27
 * Email：1993911441@qq.com
 * Describe：邀请有礼
 */
public class InviteFriendActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.img_invite_code)
    ImageView imgInviteCode;
    @BindView(R.id.tv_activity_rule)
    TextView tvActivityRule;
    @BindView(R.id.tv_invite_count)
    TextView tvInviteCount;
    @BindView(R.id.tv_invite_reward)
    TextView tvInviteReward;
    @BindView(R.id.tv_invite_content)
    TextView tvInviteContent;
    private InviteBean inviteBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
        tvHeaderTitle.setText("邀请有礼");
        imgHeaderShare.setVisibility(View.VISIBLE);

        OkHttpUtils.get()
                .url(Constant.INVITE_FRIEND)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        inviteBean = GsonUtils.jsonToBean(response, InviteBean.class);
                        if (inviteBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {
        tvInviteContent.setText("您可获订单实付金额的"+inviteBean.getData().getRate()+"作为奖励");
        tvInviteCount.setText("您已邀请好友 " + inviteBean.getData().getInvite_num() + " 人");
        tvInviteReward.setText("您已获得奖励RMB " + new DecimalFormat("#0.00")
                .format(Float.parseFloat(inviteBean.getData().getMoney())) + " 元");
        Glide.with(this)
                .load(inviteBean.getData().getEwm())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgInviteCode);
    }

    @OnClick({R.id.img_header_back, R.id.img_header_share, R.id.tv_activity_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                if (inviteBean != null) {
                    ShareUtils.showShare(this, Constant.HOST + SharedPreferencesUtils.getString(this
                            , "icon"), "邀请有礼", "", Constant.INVITE_SHARE + "?id=" + inviteBean
                            .getData().getUp_uid());
                    MobclickAgent.onEvent(InviteFriendActivity.this, "invite_friend");
                }
                break;
            case R.id.tv_activity_rule:
                Intent intent = new Intent(this, CouponRuleActivity.class);
                intent.putExtra("type", "invite_rule");
                intent.putExtra("title","活动规则");
                intent.putExtra("img_url",inviteBean.getImg());
                startActivity(intent);
                break;

        }
    }

}
