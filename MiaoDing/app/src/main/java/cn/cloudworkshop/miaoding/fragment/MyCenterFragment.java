package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.AboutUsActivity;
import cn.cloudworkshop.miaoding.ui.AppointmentActivity;
import cn.cloudworkshop.miaoding.ui.CollectionActivity;
import cn.cloudworkshop.miaoding.ui.CouponActivity;
import cn.cloudworkshop.miaoding.ui.DressingTestActivity;
import cn.cloudworkshop.miaoding.ui.InviteFriendActivity;
import cn.cloudworkshop.miaoding.ui.JoinUsActivity;
import cn.cloudworkshop.miaoding.ui.LoginActivity;
import cn.cloudworkshop.miaoding.ui.ApplyMeasureActivity;
import cn.cloudworkshop.miaoding.ui.MemberCenterActivity;
import cn.cloudworkshop.miaoding.ui.MessageCenterActivity;
import cn.cloudworkshop.miaoding.ui.MyOrderActivity;
import cn.cloudworkshop.miaoding.ui.NewClothInfoActivity;
import cn.cloudworkshop.miaoding.ui.SetUpActivity;
import cn.cloudworkshop.miaoding.ui.ShoppingCartActivity;
import cn.cloudworkshop.miaoding.utils.ContactService;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
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
    @BindView(R.id.tv_mycenter_order)
    TextView tvMycenterOrder;
    @BindView(R.id.tv_mycenter_cart)
    TextView tvMycenterCart;
    @BindView(R.id.tv_mycenter_collection)
    TextView tvMycenterCollection;
    @BindView(R.id.tv_mycenter_test)
    TextView tvMycenterTest;
    @BindView(R.id.tv_mycenter_measure)
    TextView tvMycenterMeasure;
    @BindView(R.id.tv_mycenter_consult)
    TextView tvMycenterConsult;
    @BindView(R.id.tv_mycenter_cloth)
    TextView tvMycenterCloth;
    @BindView(R.id.tv_mycenter_designer)
    TextView tvMycenterDesigner;
    @BindView(R.id.tv_center_name)
    TextView tvCenterName;
    @BindView(R.id.tv_mycenter_invite)
    TextView tvMycenterInvite;
    @BindView(R.id.tv_mycenter_coupon)
    TextView tvCoupon;
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
    RelativeLayout rlMycenter;

    private Unbinder unbinder;
    private String imgUrl;
    private String name;
    //消息数量
    private int msgCount;
    //是否预约
    private int isOrdered;
    //未读消息提醒
    BadgeView badgeView;
    private String icoGrade;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycenter, container, false);
        unbinder = ButterKnife.bind(this, view);
        badgeView = new BadgeView(getActivity());
        if (TextUtils.isEmpty(SharedPreferencesUtils.getString(getActivity(), "token"))) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("log_in", "center");
            intent.putExtra("page_name", "个人中心");
            startActivity(intent);
        } else {
            rlMycenter.setVisibility(View.VISIBLE);
            initData();
        }
        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {
        OkHttpUtils.get()
                .url(Constant.USER_INFO)
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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            name = jsonObject1.getString("name");
                            imgUrl = jsonObject1.getString("avatar");
                            isOrdered = jsonObject1.getInt("is_yuyue");

                            msgCount = jsonObject1.getInt("unread_message_num");
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("user_grade");
                            icoGrade = jsonObject2.getString("img");

                            initView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    protected void initView() {

        tvCenterName.setText(name);
        tvCenterName.setTypeface(DisplayUtils.setTextType(getActivity()));

        Glide.with(getActivity())
                .load(Constant.HOST + imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgIcon);
        Glide.with(getActivity())
                .load(Constant.HOST + icoGrade)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgCenterGrade);

        if (msgCount > 0) {
            badgeView.setVisibility(View.VISIBLE);
            badgeView.setTargetView(imgMessage);
            badgeView.setBackgroundResource(R.drawable.btn_red_bg);
            badgeView.setTextSize(8);
            if (msgCount < 99) {
                badgeView.setBadgeCount(msgCount);
            } else {
                badgeView.setText("99+");
            }
        } else {
            badgeView.setVisibility(View.GONE);
        }
    }

    /**
     * 刷新页面
     */
    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SharedPreferencesUtils.getString(getActivity(), "token"))) {
            rlMycenter.setVisibility(View.GONE);
        } else {
            rlMycenter.setVisibility(View.VISIBLE);
            initData();
        }
    }


    public static MyCenterFragment newInstance() {
        Bundle args = new Bundle();
        MyCenterFragment fragment = new MyCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.tv_mycenter_order, R.id.tv_mycenter_cart, R.id.tv_mycenter_collection,
            R.id.tv_mycenter_test, R.id.tv_mycenter_measure, R.id.tv_mycenter_consult,
            R.id.tv_mycenter_cloth, R.id.tv_mycenter_designer, R.id.tv_mycenter_invite,
            R.id.rl_msg_center, R.id.tv_mycenter_coupon, R.id.rl_set_center, R.id.rl_user_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_mycenter_order:
                Intent order = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(order);
                break;
            case R.id.tv_mycenter_cart:
                Intent cart = new Intent(getActivity(), ShoppingCartActivity.class);
                startActivity(cart);
                break;
            case R.id.tv_mycenter_collection:
                Intent collection = new Intent(getActivity(), CollectionActivity.class);
                startActivity(collection);
                break;
            case R.id.tv_mycenter_test:
                Intent test = new Intent(getActivity(), DressingTestActivity.class);
                startActivity(test);
                break;
            case R.id.tv_mycenter_measure:

//                startActivity(new Intent(getActivity(), CameraActivity.class));

                if (isOrdered == 1) {
                    Intent intent = new Intent(getActivity(), AppointmentActivity.class);
                    intent.putExtra("content", "appoint");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ApplyMeasureActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_mycenter_consult:
                ContactService.contactService(getActivity());
                break;
            case R.id.tv_mycenter_cloth:
                Intent cloth = new Intent(getActivity(), NewClothInfoActivity.class);
                startActivity(cloth);
                break;
            case R.id.tv_mycenter_designer:
                Intent designer = new Intent(getActivity(), JoinUsActivity.class);
                startActivity(designer);
                break;
            case R.id.rl_set_center:
                Intent setUp = new Intent(getActivity(), SetUpActivity.class);
                startActivity(setUp);
                break;
            case R.id.tv_mycenter_invite:
                Intent invite = new Intent(getActivity(), InviteFriendActivity.class);
                startActivity(invite);
                break;
            case R.id.tv_mycenter_coupon:
                startActivity(new Intent(getActivity(), CouponActivity.class));
                break;
            case R.id.rl_msg_center:
                startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                break;
            case R.id.rl_user_center:
                startActivity(new Intent(getActivity(), MemberCenterActivity.class));
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
