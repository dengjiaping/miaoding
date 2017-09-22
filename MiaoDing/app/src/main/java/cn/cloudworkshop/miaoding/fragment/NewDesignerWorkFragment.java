package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.constant.Constant;

/**
 * Author：binge on 2017-09-15 17:25
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewDesignerWorkFragment extends BaseFragment {

    @BindView(R.id.tab_works)
    SlidingTabLayout tabWorks;
    @BindView(R.id.vp_works)
    ViewPager vpWorks;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_works_designer, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }


    /**
     * 加载视图
     */
    private void initView() {

        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        titleList.add("腔调");
        titleList.add("设计师");
        fragmentList.add(WorksFragment.newInstance());
        fragmentList.add(DesignerFragment.newInstance());

        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getChildFragmentManager(),
                fragmentList, titleList);
        vpWorks.setOffscreenPageLimit(titleList.size());
        vpWorks.setAdapter(adapter);

        tabWorks.setViewPager(vpWorks);
        tabWorks.setCurrentTab(0);

    }


    public static NewDesignerWorkFragment newInstance() {
        Bundle args = new Bundle();
        NewDesignerWorkFragment fragment = new NewDesignerWorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}