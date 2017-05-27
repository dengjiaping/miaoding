package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.HomeClassifyBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-06 09:38
 * Email：1993911441@qq.com
 * Describe：主页
 */
public class HomepageFragment extends BaseFragment {
    @BindView(R.id.tab_homepage)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_homepage)
    ViewPager viewPager;
    private Unbinder unbinder;

    private HomeClassifyBean tabBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }


    /**
     * 加载tab
     */
    private void initData() {
        OkHttpUtils.post()
                .url(Constant.HOMEPAGE_TAB)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        tabBean = GsonUtils.jsonToBean(response,HomeClassifyBean.class);
                        if (tabBean.getData() != null){
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeRecommendFragment.newInstance());
        titleList.add("推荐");
        for (int i = 0; i < tabBean.getData().size(); i++) {
            fragmentList.add(HomeClassifyFragment.newInstance(tabBean.getData().get(i).getId()));
            titleList.add(tabBean.getData().get(i).getName());
        }

        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getChildFragmentManager(),
                fragmentList, titleList);
        viewPager.setOffscreenPageLimit(titleList.size());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCurrentTab(0);

    }

    public static HomepageFragment newInstance() {
        Bundle args = new Bundle();
        HomepageFragment fragment = new HomepageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
