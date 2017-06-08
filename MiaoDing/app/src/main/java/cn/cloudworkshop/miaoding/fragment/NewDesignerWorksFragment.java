package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.FlipAdapter;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.DesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 11:25
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewDesignerWorksFragment extends BaseFragment {

    @BindView(R.id.img_designer_works)
    ImageView imgDesignerWorks;
    @BindView(R.id.tv_designer_works)
    TextView tvDesignerWorks;
    @BindView(R.id.tab_designer_works)
    SlidingTabLayout tabDesignerWorks;
    @BindView(R.id.vp_designer_works)
    ViewPager vpDesignerWorks;
    private Unbinder unbinder;

    private List<DesignWorksBean.DataBean.ItemBean> designerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_designer_works, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 加载数据
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

    private void initView() {
        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        titleList.add("腔调");
        titleList.add("设计师");
        fragmentList.add(WorksFragment.newInstance());
        fragmentList.add(DesignerFragment.newInstance());

        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getChildFragmentManager(),
                fragmentList, titleList);
        vpDesignerWorks.setOffscreenPageLimit(titleList.size());
        vpDesignerWorks.setAdapter(adapter);
        tabDesignerWorks.setViewPager(vpDesignerWorks);
        tabDesignerWorks.setCurrentTab(0);

    }


    public static NewDesignerWorksFragment newInstance() {
        Bundle args = new Bundle();
        NewDesignerWorksFragment fragment = new NewDesignerWorksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
