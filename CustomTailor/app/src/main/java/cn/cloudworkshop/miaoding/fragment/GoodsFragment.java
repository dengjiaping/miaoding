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
import cn.cloudworkshop.miaoding.bean.GoodsTitleBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;


/**
 * Author：Libin on 2016/8/8 12:29
 * Email：1993911441@qq.com
 * Describe：商品
 */
public class GoodsFragment extends BaseFragment {

    @BindView(R.id.tab_goods_fragment)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_goods_fragment)
    ViewPager viewPager;
    Unbinder unbinder;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();

        return view;
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.GOODS_TITLE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        GoodsTitleBean titleEntity = GsonUtils.jsonToBean(response, GoodsTitleBean.class);
                        if (titleEntity.getData() != null && titleEntity.getData().size() > 0) {
                            for (int i = 0; i < titleEntity.getData().size(); i++) {
                                titleList.add(titleEntity.getData().get(i).getName());
                                fragmentList.add(GoodsInfoFragment.newInstance(titleEntity.getData()
                                        .get(i).getId()));
                            }
                            initView();
                        }
                    }
                });
    }


    /**
     * 加载视图
     */
    protected void initView() {

        if (titleList.size() <= 5) {
            tabLayout.setTabSpaceEqual(true);
        } else {
            tabLayout.setTabSpaceEqual(false);
        }

        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getChildFragmentManager(),
                fragmentList, titleList);
        viewPager.setOffscreenPageLimit(titleList.size());
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        adapter.notifyDataSetChanged();
    }

    public static GoodsFragment newInstance() {
        Bundle args = new Bundle();
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
