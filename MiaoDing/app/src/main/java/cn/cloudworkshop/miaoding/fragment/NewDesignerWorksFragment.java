package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.constant.Constant;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 11:25
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewDesignerWorksFragment extends BaseFragment {


    @BindView(R.id.tab_designer_works)
    SlidingTabLayout tabDesignerWorks;
    @BindView(R.id.vp_designer_works)
    ViewPager vpDesignerWorks;
    @BindView(R.id.img_designer_works)
    ImageView imgDesignerWorks;
    @BindView(R.id.app_bar_works)
    AppBarLayout appBar;


    private Unbinder unbinder;
    private String imgUrl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer_works_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();

        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {


        OkHttpUtils.get()
                .url(Constant.DESIGNER_TITLE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            imgUrl = data.getString("img");
                            initView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initView() {
        Glide.with(getActivity())
                .load(Constant.HOST + imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDesignerWorks);
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
