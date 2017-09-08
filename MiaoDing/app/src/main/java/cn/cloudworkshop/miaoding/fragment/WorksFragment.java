package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.NewDesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.NewWorksActivity;
import cn.cloudworkshop.miaoding.ui.NewWorksDetailActivity;
import cn.cloudworkshop.miaoding.ui.WorksDetailActivity;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 11:29
 * Email：1993911441@qq.com
 * Describe：腔调作品
 */
public class WorksFragment extends BaseFragment {

    @BindView(R.id.rv_designer_goods)
    LRecyclerView rvWorks;
    Unbinder unbinder;
    private List<NewDesignWorksBean.DataBeanX.DataBean> worksList = new ArrayList<>();

    //页面
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_works, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.NEW_DESIGNER_WORKS)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        NewDesignWorksBean worksBean = GsonUtils.jsonToBean(response, NewDesignWorksBean.class);
                        if (worksBean.getData().getData() != null && worksBean.getData().getData().size() > 0) {
                            if (isRefresh) {
                                worksList.clear();
                            }
                            worksList.addAll(worksBean.getData().getData());
                            if (isRefresh || isLoadMore) {
                                rvWorks.refreshComplete(0);
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isRefresh = false;
                            isLoadMore = false;
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(),
                                    rvWorks, 0, LoadingFooter.State.NoMore, null);
                        }
                    }
                });
    }

    private void initView() {
        rvWorks.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        CommonAdapter<NewDesignWorksBean.DataBeanX.DataBean> adapter = new CommonAdapter<NewDesignWorksBean
                .DataBeanX.DataBean>(getParentFragment().getActivity(), R.layout.listitem_works, worksList) {
            @Override
            protected void convert(ViewHolder holder, NewDesignWorksBean.DataBeanX.DataBean itemBean, int position) {
                Glide.with(getParentFragment().getActivity())
                        .load(Constant.HOST + itemBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_designer));
                holder.setText(R.id.tv_works_title, itemBean.getName());
                holder.setText(R.id.tv_works_time, DateUtils.getDate("yyyy-MM-dd", itemBean.getC_time()));
            }
        };

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rvWorks.setAdapter(mLRecyclerViewAdapter);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!TextUtils.isEmpty(worksList.get(position).getId() + "")) {
                    Intent intent = new Intent(getParentFragment().getActivity(), NewWorksActivity.class);
                    intent.putExtra("id", worksList.get(position).getRecommend_goods_ids() + "");
                    startActivity(intent);
                }
            }

        });

        //刷新
        rvWorks.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isRefresh = true;
                        page = 1;
                        initData();
                    }
                }, 1000);
            }
        });

        //加载更多
        rvWorks.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(), rvWorks,
                        0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });


    }

    public static WorksFragment newInstance() {
        Bundle args = new Bundle();
        WorksFragment fragment = new WorksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
