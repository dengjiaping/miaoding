package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.MemberRightBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-05-08 09:55
 * Email：1993911441@qq.com
 * Describe：
 */
public class MemberRightActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.rv_member_right)
    RecyclerView rvMemberRight;
    private MemberRightBean memberBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_right);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("会员权益");
        initData();
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.MEMBER_RIGHTS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        memberBean = GsonUtils.jsonToBean(response, MemberRightBean.class);
                        if (memberBean.getData() != null && memberBean.getData().size() > 0) {
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        rvMemberRight.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<MemberRightBean.DataBean> adapter = new CommonAdapter<MemberRightBean.DataBean>
                (MemberRightActivity.this, R.layout.listitem_member_center, memberBean.getData()) {
            @Override
            protected void convert(ViewHolder holder, MemberRightBean.DataBean dataBean, int position) {
                holder.setText(R.id.tv_member_right, dataBean.getName() + "  权益");
                RecyclerView rvGift = holder.getView(R.id.rv_member_grade);
                rvGift.setLayoutManager(new LinearLayoutManager(MemberRightActivity.this,
                        LinearLayoutManager.HORIZONTAL, false));
                CommonAdapter<MemberRightBean.DataBean.GradePrivilegeBean> giftAdapter = new
                        CommonAdapter<MemberRightBean.DataBean.GradePrivilegeBean>(
                                MemberRightActivity.this, R.layout.listitem_member_rights, memberBean
                                .getData().get(position).getGrade_privilege()) {
                            @Override
                            protected void convert(ViewHolder holder, MemberRightBean.DataBean
                                    .GradePrivilegeBean gradePrivilegeBean, int position) {
                                Glide.with(MemberRightActivity.this)
                                        .load(Constant.HOST + gradePrivilegeBean.getImg())
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into((ImageView) holder.getView(R.id.img_rights_icon));
                                holder.setText(R.id.tv_rights_name, gradePrivilegeBean.getName());
                            }
                        };
                rvGift.setAdapter(giftAdapter);
            }
        };
        rvMemberRight.setAdapter(adapter);

    }

    @OnClick(R.id.img_header_back)
    public void onViewClicked() {
        finish();
    }
}
