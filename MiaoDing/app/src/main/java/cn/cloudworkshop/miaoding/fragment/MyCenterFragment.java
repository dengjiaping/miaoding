package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.UserInfoBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.ApplyMeasureActivity;
import cn.cloudworkshop.miaoding.ui.AppointmentActivity;
import cn.cloudworkshop.miaoding.ui.CollectionActivity;
import cn.cloudworkshop.miaoding.ui.CouponActivity;
import cn.cloudworkshop.miaoding.ui.DressingResultActivity;
import cn.cloudworkshop.miaoding.ui.DressingTestActivity;
import cn.cloudworkshop.miaoding.ui.JoinUsActivity;
import cn.cloudworkshop.miaoding.ui.LoginActivity;
import cn.cloudworkshop.miaoding.ui.MemberCenterActivity;
import cn.cloudworkshop.miaoding.ui.MessageCenterActivity;
import cn.cloudworkshop.miaoding.ui.MyOrderActivity;
import cn.cloudworkshop.miaoding.ui.SetUpActivity;
import cn.cloudworkshop.miaoding.ui.ShoppingCartActivity;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.BadgeView;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/8 12:30
 * Email：1993911441@qq.com
 * Describe：个人中心
 */
public class MyCenterFragment extends BaseFragment {

    @BindView(R.id.img_user_icon)
    CircleImageView imgIcon;
    @BindView(R.id.tv_center_name)
    TextView tvCenterName;
    @BindView(R.id.img_center_message)
    ImageView imgMessage;
    @BindView(R.id.rl_msg_center)
    RelativeLayout rlMsgCenter;
    @BindView(R.id.img_center_grade)
    ImageView imgCenterGrade;
    @BindView(R.id.rl_set_center)
    RelativeLayout rlSetCenter;
    @BindView(R.id.rl_user_center)
    RelativeLayout rlCenterUser;
    @BindView(R.id.rl_mycenter)
    RelativeLayout rlCenter;
    @BindView(R.id.rv_center)
    RecyclerView rvCenter;

    private Unbinder unbinder;
    //未读消息提醒
    BadgeView badgeView;

    private UserInfoBean userInfoBean;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycenter, container, false);
        unbinder = ButterKnife.bind(this, view);
        badgeView = new BadgeView(getActivity());
        return view;
    }

    /**
     * 刷新页面
     */
    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SharedPreferencesUtils.getString(getActivity(), "token"))) {
            rlCenter.setVisibility(View.GONE);
        } else {
            rlCenter.setVisibility(View.VISIBLE);
            initData();
        }
    }

    /**
     * @param hidden Fragment显示隐藏监听
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (TextUtils.isEmpty(SharedPreferencesUtils.getString(getActivity(), "token"))) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("log_in", "center");
                intent.putExtra("page_name", "我的");
                startActivity(intent);
            }
        }
    }


    /**
     * 加载数据
     */
    private void initData() {
        OkHttpUtils.get()
                .url(Constant.USER_INFO)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .addParams("is_android", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        userInfoBean = GsonUtils.jsonToBean(response, UserInfoBean.class);
                        if (userInfoBean.getData() != null && userInfoBean.getIcon_list() != null
                                && userInfoBean.getIcon_list().size() > 0) {
                            initView();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    protected void initView() {

        tvCenterName.setText(userInfoBean.getData().getName());
        tvCenterName.setTypeface(DisplayUtils.setTextType(getActivity()));

        Glide.with(getActivity())
                .load(Constant.HOST + userInfoBean.getData().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgIcon);
        Glide.with(getActivity())
                .load(Constant.HOST + userInfoBean.getData().getUser_grade().getImg())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgCenterGrade);

        if (userInfoBean.getData().getUnread_message_num() > 0) {
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setTargetView(imgMessage);
            badgeView.setBackgroundResource(R.drawable.btn_red_bg);
            badgeView.setTextSize(8);
            if (userInfoBean.getData().getUnread_message_num() < 99) {
                badgeView.setBadgeCount(userInfoBean.getData().getUnread_message_num());
            } else {
                badgeView.setText("99+");
            }
        } else {
            badgeView.setVisibility(View.GONE);
        }

        rvCenter.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        CommonAdapter<UserInfoBean.IconListBean> adapter = new CommonAdapter<UserInfoBean.IconListBean>
                (getActivity(), R.layout.listitem_center, userInfoBean.getIcon_list()) {
            @Override
            protected void convert(ViewHolder holder, UserInfoBean.IconListBean iconListBean, int position) {
                Glide.with(getActivity())
                        .load(Constant.HOST + iconListBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_center_item));
                holder.setText(R.id.tv_center_item, iconListBean.getName());
                if (position == 8) {
                    holder.setVisible(R.id.img_invite_surprise, true);
                }
            }
        };
        rvCenter.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), MyOrderActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), CouponActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), CollectionActivity.class));
                        break;
                    case 4:
                        if (userInfoBean.getData().getIs_yuyue() == 1) {
                            Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                            intent.putExtra("content", "appoint_measure");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), ApplyMeasureActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), DressingTestActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), JoinUsActivity.class));
                        break;
                    case 7:
                        ContactService.contactService(getActivity());
                        break;
                    case 8:
                        inviteFriends();
                        break;

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }


    @OnClick({R.id.rl_msg_center, R.id.rl_set_center, R.id.rl_user_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_set_center:
                startActivity(new Intent(getActivity(), SetUpActivity.class));
                break;
            case R.id.rl_msg_center:
                startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                break;
            case R.id.rl_user_center:
                startActivity(new Intent(getActivity(), MemberCenterActivity.class));
                break;

        }
    }

    /**
     * 邀请好友
     */
    private void inviteFriends() {
        OkHttpUtils.get()
                .url(Constant.INVITE_FRIEND)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String url = jsonObject.getString("share_url");
                            JSONObject data = jsonObject.getJSONObject("data");
                            int uid = data.getInt("up_uid");

                            if (uid != 0) {
                                Intent intent = new Intent(getActivity(), DressingResultActivity.class);
                                intent.putExtra("title", "邀请有礼");
                                intent.putExtra("share_title", "邀请有礼");
                                intent.putExtra("share_content", "TA得优惠，你得奖励");
                                intent.putExtra("url", Constant.HOST + url + uid);
                                intent.putExtra("share_url", Constant.INVITE_SHARE + "?id=" + uid);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public static MyCenterFragment newInstance() {
        Bundle args = new Bundle();
        MyCenterFragment fragment = new MyCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
