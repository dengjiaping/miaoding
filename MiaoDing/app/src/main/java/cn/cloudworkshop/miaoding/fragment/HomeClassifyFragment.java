package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.HomepageTabBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.HomepageDetailActivity;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-06 10:00
 * Email：1993911441@qq.com
 * Describe：搭配、潮流、风尚
 */
public class HomeClassifyFragment extends BaseFragment {
    @BindView(R.id.rv_recommend)
    LRecyclerView rvRecommend;
    private Unbinder unbinder;
    private List<HomepageTabBean.DataBeanX.DataBean> itemList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    //当前页
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;
    private int type;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initData();
        return view;
    }

    private void getData() {
        type = getArguments().getInt("tags_id");
    }

    /**
     * 加载数据
     */
    private void initData() {
        OkHttpUtils
                .get()
                .url(Constant.HOMEPAGE_TAB_LIST)
                .addParams("tags_id", type + "")
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        HomepageTabBean homepageTabBean = GsonUtils.jsonToBean(response, HomepageTabBean.class);
                        if (homepageTabBean.getData() != null && homepageTabBean.getData().getData().size() > 0) {
                            if (isRefresh) {
                                itemList.clear();
                            }
                            itemList.addAll(homepageTabBean.getData().getData());
                            if (isLoadMore || isRefresh) {
                                rvRecommend.refreshComplete();
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isLoadMore = false;
                            isRefresh = false;

                        } else {
                            RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(),
                                    rvRecommend, 0, LoadingFooter.State.TheEnd, null);
                        }
                    }
                });

    }

    /**
     * 加载视图
     */
    private void initView() {
        rvRecommend.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        CommonAdapter<HomepageTabBean.DataBeanX.DataBean> adapter = new CommonAdapter<HomepageTabBean
                .DataBeanX.DataBean>(getParentFragment().getActivity(), R.layout.listitem_homepage, itemList) {
            @Override
            protected void convert(ViewHolder holder, HomepageTabBean.DataBeanX.DataBean dataBean, int position) {
                holder.setVisible(R.id.view_homepage1, false);
                holder.setVisible(R.id.view_homepage2, true);
                Glide.with(getParentFragment().getActivity())
                        .load(Constant.HOST + dataBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_home_item));
                holder.setText(R.id.tv_recommend_title, dataBean.getTitle());
                holder.setText(R.id.tv_recommend_content, dataBean.getSub_title());
            }

        };


        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rvRecommend.setAdapter(mLRecyclerViewAdapter);
        rvRecommend.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rvRecommend.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


        rvRecommend.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                initData();
            }
        });

        rvRecommend.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(), rvRecommend,
                        0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                homepageLog( itemList.get(position).getTags_name());
                Intent intent = new Intent(getParentFragment().getActivity(), HomepageDetailActivity.class);
                intent.putExtra("url", Constant.HOMEPAGE_INFO + "?content=1&id=" +
                        itemList.get(position).getId());
                intent.putExtra("title", itemList.get(position).getTitle());
                intent.putExtra("content", itemList.get(position).getContent());
                intent.putExtra("img_url", itemList.get(position).getImg());
                intent.putExtra("share_url", Constant.HOMEPAGE_SHARE + "?content=1&id="
                        + itemList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


    }

    public static HomeClassifyFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("tags_id", type);
        HomeClassifyFragment fragment = new HomeClassifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 首页跟踪
     */
    private void homepageLog(String module_name) {
        long time = DateUtils.getCurrentTime() - MyApplication.homeEnterTime;
        OkHttpUtils.post()
                .url(Constant.HOMEPAGE_LOG)
                .addParams("token", SharedPreferencesUtils.getString(getParentFragment().getActivity(), "token"))
                .addParams("time", time + "")
                .addParams("p_module_name", "首页")
                .addParams("module_name", module_name)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        LogUtils.log("homepage");
                    }
                });

    }
}
