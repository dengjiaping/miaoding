package cn.cloudworkshop.miaoding.fragment;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.JazzyPagerAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.DesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.view.JazzyViewPager;
import okhttp3.Call;

/**
 * Author：Libin on 2016/9/22 15:20
 * Email：1993911441@qq.com
 * Describe：设计师界面
 */
public class DesignerFragment extends BaseFragment {

    @BindView(R.id.vp_designer_works)
    JazzyViewPager jazzyViewPager;
    private List<DesignWorksBean.DataBean.ItemBean> designerList = new ArrayList<>();
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 获取网络数据
     */
    private void initData() {
        OkHttpUtils.get()
                .url(Constant.DESIGNER_WORKS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DesignWorksBean designerBean = GsonUtils.jsonToBean(response, DesignWorksBean.class);
                        if (designerBean.getData().getData() != null) {
                            designerList.addAll(designerBean.getData().getData());
                            initView();
                        }
                    }
                });
    }


    /**
     * 加载视图
     */
    protected void initView() {
        jazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        jazzyViewPager.setFadeEnabled(true);
        JazzyPagerAdapter adapter = new JazzyPagerAdapter(getActivity(), jazzyViewPager, designerList);
        jazzyViewPager.setAdapter(adapter);
        if (designerList.size() > 2) {
            jazzyViewPager.setCurrentItem(designerList.size() - 2);
        } else {
            jazzyViewPager.setCurrentItem(0);
        }

    }

    public static DesignerFragment newInstance() {
        Bundle args = new Bundle();
        DesignerFragment fragment = new DesignerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
