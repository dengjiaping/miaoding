package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.NewMemberBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：binge on 2017-05-05 18:42
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewMemberCenterActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.progress_grade)
    ProgressBar progressGrade;
    @BindView(R.id.tv_total_score)
    TextView tvTotalScore;
    @BindView(R.id.tv_member_growth)
    TextView tvMemberGrowth;
    @BindView(R.id.tv_member_more)
    TextView tvMemberMore;
    @BindView(R.id.img_member_icon)
    CircleImageView imgMember;
    @BindView(R.id.tv_member_nickname)
    TextView tvMemberName;
    @BindView(R.id.img_member_grade)
    ImageView imgGrade;
    @BindView(R.id.tv_member_rights)
    TextView tvMemberRight;
    @BindView(R.id.rv_member_power)
    RecyclerView rvMemberGrade;
    @BindView(R.id.tv_check_more)
    TextView tvCheckMore;

    private NewMemberBean memberBean;
    //    private List<List<MemberBean.DataBean.UserPrivilegeBean>> dataList = new ArrayList<>();
//    private CommonAdapter<MemberBean.DataBean.UserPrivilegeBean> adapter;
    //会员成长值
    private int credit;
    private PopupWindow mPopupWindow;
    private String userBirthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_center_new);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("会员俱乐部");
        imgHeaderShare.setVisibility(View.VISIBLE);

        initData();
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.MEMBER_CENTER)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        memberBean = GsonUtils.jsonToBean(response, NewMemberBean.class);
                        if (memberBean.getData() != null) {
                            userBirthDay = memberBean.getData().getUser_info().getBirthday();
                            initView();
                        }
                    }
                });

    }

    private void initView() {
        Glide.with(getApplicationContext())
                .load(Constant.HOST + memberBean.getData().getUser_info().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgMember);
        tvMemberName.setText(memberBean.getData().getUser_info().getName());
        Glide.with(getApplicationContext())
                .load(Constant.HOST + memberBean.getData().getUser_info().getUser_grade().getImg2())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgGrade);
        tvMemberGrowth.setText(memberBean.getData().getUser_info().getUser_grade().getName());

        credit = (int) Float.parseFloat(memberBean.getData().getUser_info().getCredit());

        float maxCredit = Float.parseFloat(memberBean.getData().getUser_info().getUser_grade().getMax_credit());
        progressGrade.setProgress((int) (credit / maxCredit * 100));
        tvMemberGrowth.setText(credit + "");
        tvTotalScore.setText("/" + (int) maxCredit);

        tvMemberRight.setText("当前所享权限  (" + memberBean.getData().getUser_info().getUser_grade().getName() + ")");

        rvMemberGrade.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter<NewMemberBean.DataBean.UserPrivilegeBean> adapter = new CommonAdapter
                <NewMemberBean.DataBean.UserPrivilegeBean>(NewMemberCenterActivity.this,
                R.layout.listitem_member_rights, memberBean.getData().getUser_privilege()) {
            @Override
            protected void convert(ViewHolder holder, NewMemberBean.DataBean.UserPrivilegeBean
                    userPrivilegeBean, int position) {
                Glide.with(NewMemberCenterActivity.this)
                        .load(Constant.HOST + userPrivilegeBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_rights_icon));
                holder.setText(R.id.tv_rights_name, userPrivilegeBean.getName());

            }
        };
        rvMemberGrade.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                showGift(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    /**
     * @param position 礼包详情
     */
    private void showGift(final int position) {
        final View popupView = getLayoutInflater().inflate(R.layout.ppw_receive_gift, null);
        mPopupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        DisplayUtils.setBackgroundAlpha(this, 0.5f);
        TextView tvTitle = (TextView) popupView.findViewById(R.id.tv_gift_title);
        CircleImageView imgGift = (CircleImageView) popupView.findViewById(R.id.img_gift);
        final TextView tvInfo = (TextView) popupView.findViewById(R.id.tv_gift_info);
        final TextView tvReceive = (TextView) popupView.findViewById(R.id.tv_receive_gift);

        Glide.with(this)
                .load(Constant.HOST + memberBean.getData().getUser_privilege().get(position).getImg())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgGift);

        tvTitle.setText(memberBean.getData().getUser_privilege().get(position).getName());
        switch (memberBean.getData().getUser_privilege().get(position).getIs_get()) {
            case 1:
                tvInfo.setText(memberBean.getData().getUser_privilege().get(position).getDesc());
                tvReceive.setText("关闭");
                tvReceive.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
                break;
            case 2:
                if (userBirthDay != null && !userBirthDay.equals("null")) {
                    tvInfo.setText(memberBean.getData().getUser_privilege().get(position).getDesc());
                    tvReceive.setText("领取");
                    tvReceive.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
                } else {
                    tvInfo.setText("·完善生日资料后，过生日时可获惊喜生日礼包一份\r\n·礼包领取有效期：生日前三天及后四天");
                    tvReceive.setText("去设置");
                    tvReceive.setTextColor(ContextCompat.getColor(this, R.color.dark_red));
                }
                break;
            case 3:
                tvInfo.setText(memberBean.getData().getUser_privilege().get(position).getDesc());
                tvReceive.setText("领取");
                tvReceive.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
                break;
        }

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DisplayUtils.setBackgroundAlpha(NewMemberCenterActivity.this, 1.0f);
            }
        });

        tvReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (memberBean.getData().getUser_privilege().get(position).getIs_get()) {
                    case 1:
                        mPopupWindow.dismiss();
                        break;
                    case 2:
                        if (userBirthDay != null && !userBirthDay.equals("null")) {
                            receiveGift(Constant.BIRTHDAY_GIFT);
                        } else {
                            Intent intent = new Intent(NewMemberCenterActivity.this, SetUpActivity.class);
                            intent.putExtra("set_birthday", true);
                            mPopupWindow.dismiss();
                            startActivityForResult(intent, 1);
                        }
                        break;
                    case 3:
                        receiveGift(Constant.UPGRADE_GIFT);
                        break;
                }
            }
        });
    }

    /**
     * @param giftType 领取礼包
     */
    private void receiveGift(String giftType) {
        OkHttpUtils.post()
                .url(giftType)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            Toast.makeText(NewMemberCenterActivity.this, msg, Toast.LENGTH_SHORT).show();
                            mPopupWindow.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.img_header_back, R.id.img_header_share, R.id.tv_member_more, R.id.tv_check_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                startActivity(new Intent(this, MemberRuleActivity.class));
                break;
            case R.id.tv_member_more:
                Intent intent = new Intent(this, MemberGrowthActivity.class);
                intent.putExtra("value", credit + "");
                startActivity(intent);
                break;
            case R.id.tv_check_more:
                startActivity(new Intent(this,MemberRightActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            userBirthDay = data.getStringExtra("birthday");
        }
    }
}
