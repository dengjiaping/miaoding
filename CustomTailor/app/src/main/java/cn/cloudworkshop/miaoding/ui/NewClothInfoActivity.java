package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.fragment.ClothInfoFragment;

/**
 * Author：Libin on 2016/9/22 09:18
 * Email：1993911441@qq.com
 * Describe：衣物志
 */
public class NewClothInfoActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.tab_cloth_info)
    TabLayout tabClothInfo;
    @BindView(R.id.vp_cloth_info)
    ViewPager vpClothInfo;


    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_info_new);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        titleList.add("纽扣");
        titleList.add("面料");
        titleList.add("门襟");
        titleList.add("袖口");
        titleList.add("领子");
        titleList.add("花纹");

        for (int i = 0; i < titleList.size(); i++) {
            fragmentList.add(ClothInfoFragment.newInstance());
        }
    }

    private void initView() {
        tvHeaderTitle.setText("衣物志");

        tabClothInfo.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabClothInfo.setSelectedTabIndicatorColor(getResources().getColor(R.color.dark_gray_22));
        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpClothInfo.setOffscreenPageLimit(3);
        vpClothInfo.setAdapter(adapter);
        tabClothInfo.setupWithViewPager(vpClothInfo);
    }

    @OnClick(R.id.img_header_back)
    public void onClick() {
        finish();
    }
}



