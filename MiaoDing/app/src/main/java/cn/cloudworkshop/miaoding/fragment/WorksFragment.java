package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import cn.cloudworkshop.miaoding.ui.WorksDetailActivity;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 11:29
 * Email：1993911441@qq.com
 * Describe：
 */
public class WorksFragment extends BaseFragment {

    @BindView(R.id.rv_designer_goods)
    LRecyclerView rvWorks;
    Unbinder unbinder;
    private List<NewDesignWorksBean.ListBean.DataBean> worksList = new ArrayList<>();

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
                .addParams("page", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        NewDesignWorksBean worksBean = GsonUtils.jsonToBean(response, NewDesignWorksBean.class);
                        if (worksBean.getList().getData() != null && worksBean.getList().getData().size() > 0) {
                            if (isRefresh) {
                                worksList.clear();
                            }
                            worksList.addAll(worksBean.getList().getData());
                            if (isRefresh || isLoadMore) {
                                rvWorks.refreshComplete();
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isRefresh = false;
                            isLoadMore = false;
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(),
                                    rvWorks, 0, LoadingFooter.State.TheEnd, null);
                        }
                    }
                });
    }

    private void initView() {
        rvWorks.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        CommonAdapter<NewDesignWorksBean.ListBean.DataBean> adapter = new CommonAdapter<NewDesignWorksBean
                .ListBean.DataBean>(getParentFragment().getActivity(), R.layout.listitem_works, worksList) {
            @Override
            protected void convert(ViewHolder holder, NewDesignWorksBean.ListBean.DataBean itemBean, int position) {
                Glide.with(getParentFragment().getActivity())
                        .load(Constant.HOST + itemBean.getThumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_designer));
                holder.setText(R.id.tv_works_title, itemBean.getName());
                holder.setText(R.id.tv_works_time, DateUtils.getDate("yyyy-MM-dd", itemBean.getC_time()));
            }
        };

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rvWorks.setAdapter(mLRecyclerViewAdapter);
        rvWorks.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rvWorks.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!TextUtils.isEmpty(worksList.get(position).getId() + "")) {
                    Intent intent = new Intent(getParentFragment().getActivity(), WorksDetailActivity.class);
                    intent.putExtra("id", String.valueOf(worksList.get(position).getId() + ""));
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        //刷新
        rvWorks.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                initData();
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
