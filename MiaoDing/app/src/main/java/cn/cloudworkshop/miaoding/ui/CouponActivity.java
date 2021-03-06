package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.fragment.MyCouponFragment;
import okhttp3.Call;

/**
 * Author：Libin on 2016/12/15 16:40
 * Email：1993911441@qq.com
 * Describe：优惠券
 */
public class CouponActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.tab_my_order)
    SlidingTabLayout tabCoupon;
    @BindView(R.id.vp_my_order)
    ViewPager vpCoupon;
    @BindView(R.id.img_load_error)
    ImageView imgLoadError;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private String couponRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        ButterKnife.bind(this);
        initData();
    }


    /**
     * 加载数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.COUPON_RULE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        imgLoadError.setBackgroundColor(Color.WHITE);
                        imgLoadError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        imgLoadError.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            couponRule = jsonObject.getString("introduce_img");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        tvHeaderTitle.setText("优惠券");
        imgHeaderShare.setVisibility(View.VISIBLE);
        imgHeaderShare.setImageResource(R.mipmap.icon_member_rule);

        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add("未使用");
        titleList.add("已使用");
        titleList.add("已失效");

        fragmentList.add(MyCouponFragment.newInstance(1));
        fragmentList.add(MyCouponFragment.newInstance(2));
        fragmentList.add(MyCouponFragment.newInstance(-1));

        initView();
    }

    /**
     * 加载视图
     */
    private void initView() {
        if (titleList.size() <= 5) {
            tabCoupon.setTabSpaceEqual(true);
        } else {
            tabCoupon.setTabSpaceEqual(false);
        }
        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpCoupon.setOffscreenPageLimit(titleList.size());
        vpCoupon.setAdapter(adapter);
        tabCoupon.setViewPager(vpCoupon);
        tabCoupon.setCurrentTab(0);

    }


    @OnClick({R.id.img_header_back, R.id.img_header_share,R.id.img_load_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                if (couponRule != null) {
                    Intent intent = new Intent(this, UserRuleActivity.class);
                    intent.putExtra("title", "使用规则");
                    intent.putExtra("img_url", couponRule);
                    startActivity(intent);
                }
                break;
            case R.id.img_load_error:
                initData();
                break;
        }
    }

}
