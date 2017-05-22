package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.fragment.CouponFragment;

/**
 * Author：binge on 2016/12/15 16:40
 * Email：1993911441@qq.com
 * Describe：优惠券
 */
public class CouponActivity extends BaseActivity {

    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tab_my_coupon)
    SlidingTabLayout tabCoupon;
    @BindView(R.id.vp_my_coupon)
    ViewPager vpCoupon;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;


    private List<String> titleList;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycoupon);
        ButterKnife.bind(this);
        initData();
    }


    /**
     * 加载数据
     */
    private void initData() {
        tvHeaderTitle.setText("优惠券");
        imgHeaderShare.setVisibility(View.VISIBLE);
        imgHeaderShare.setImageResource(R.mipmap.icon_member_rule);

        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add("未使用");
        titleList.add("已使用");
        titleList.add("已失效");

        fragmentList.add(CouponFragment.newInstance(1));
        fragmentList.add(CouponFragment.newInstance(2));
        fragmentList.add(CouponFragment.newInstance(-1));

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
        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        vpCoupon.setOffscreenPageLimit(titleList.size());
        vpCoupon.setAdapter(adapter);
        tabCoupon.setViewPager(vpCoupon);
        tabCoupon.setCurrentTab(0);

    }

    @OnClick({R.id.img_header_back, R.id.img_header_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                Intent intent = new Intent(this, CouponRuleActivity.class);
                intent.putExtra("type", "coupon");
                startActivity(intent);
                break;
        }
    }

}
