package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.GoodsTitleBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.FragmentTabUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017/3/27 15:43
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewGoodsFragment extends BaseFragment {

    @BindView(R.id.frame_goods)
    FrameLayout frameGoods;
    @BindView(R.id.tab_goods)
    TabLayout tabGoods;

    private Unbinder unbinder;

    private List<GoodsTitleBean.DataBean> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        OkHttpUtils
                .get()
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
                                titleList.add(titleEntity.getData().get(i));
                            }
                            initView();
                        }
                    }
                });


    }

    private void initView() {

        for (int i = 0; i < titleList.size(); i++) {
            tabGoods.addTab(tabGoods.newTab().setText(titleList.get(i).getName()));
            fragmentList.add(NewGoodsInfoFragment.newInstance(titleList.get(i).getId()));
        }

        new FragmentTabUtils(getChildFragmentManager(), fragmentList, R.id.frame_goods, tabGoods);


    }

    public static NewGoodsFragment newInstance() {
        Bundle args = new Bundle();
        NewGoodsFragment fragment = new NewGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
