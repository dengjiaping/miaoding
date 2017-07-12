package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.GoodsFragmentAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.DesignerInfoBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.fragment.DesignerFragment;
import cn.cloudworkshop.miaoding.fragment.DesignerGoodsFragment;
import cn.cloudworkshop.miaoding.fragment.DesignerStoryFragment;
import cn.cloudworkshop.miaoding.fragment.WorksFragment;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 15:26
 * Email：1993911441@qq.com
 * Describe：设计师详情
 */
public class DesignerDetailActivity extends BaseActivity {
    @BindView(R.id.img_designer_icon)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_designer_name)
    TextView tvName;
    @BindView(R.id.tv_designer_nick)
    TextView tvNickName;
    @BindView(R.id.tv_designer_introduce)
    TextView tvIntroduce;
    @BindView(R.id.tab_designer)
    SlidingTabLayout tabDesigner;
    @BindView(R.id.vp_designer)
    ViewPager vpDesigner;
    @BindView(R.id.img_back_designer)
    ImageView imgBack;
    @BindView(R.id.img_designer_share)
    ImageView imgShare;

    //设计师id
    private String id;
    private DesignerInfoBean designerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_detail);
        ButterKnife.bind(this);

        getData();
        initData();

    }


    private void getData() {
        id = getIntent().getStringExtra("id");
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.DESIGNER_DETAILS)
                .addParams("uid", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        designerBean = GsonUtils.jsonToBean(response, DesignerInfoBean.class);
                        if (designerBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }

    private void initView() {

        Glide.with(getApplicationContext())
                .load(Constant.HOST+designerBean.getData().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgAvatar);
        tvName.setText(designerBean.getData().getName());
        tvNickName.setText(designerBean.getData().getTag());
        tvIntroduce.setText(designerBean.getData().getIntroduce());

        List<String> titleList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        titleList.add("故事");
        titleList.add("作品");
        fragmentList.add(DesignerStoryFragment.newInstance(designerBean.getData().getStory()));
        fragmentList.add(DesignerGoodsFragment.newInstance(designerBean.getData().getGoods_list()));

        GoodsFragmentAdapter adapter = new GoodsFragmentAdapter(getSupportFragmentManager(),
                fragmentList, titleList);
        vpDesigner.setOffscreenPageLimit(titleList.size());
        vpDesigner.setAdapter(adapter);
        tabDesigner.setViewPager(vpDesigner);
        tabDesigner.setCurrentTab(0);
    }


    @OnClick({R.id.img_back_designer, R.id.img_designer_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back_designer:
                finish();
                break;
            case R.id.img_designer_share:
                ShareUtils.showShare(this, Constant.HOST + designerBean.getData().getAvatar(),
                        designerBean.getData().getName(), designerBean.getData().getContent(),
                        Constant.DESIGNER_SHARE + "?id=" + id + "&token=" +
                                SharedPreferencesUtils.getString(this, "token"));
                break;
        }
    }
}
