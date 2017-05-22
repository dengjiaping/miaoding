package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.bean.HomepageItemBean;
import cn.cloudworkshop.miaoding.ui.HomepageDetailActivity;
import cn.cloudworkshop.miaoding.adapter.SectionedRVAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.BannerBean;
import cn.cloudworkshop.miaoding.bean.HomepageListBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.NetworkImageHolderView;
import okhttp3.Call;

/**
 * Author：Libin on 2016/10/14 14:26
 * Email：1993911441@qq.com
 * Describe：首页
 */

public class HomepageFragment extends BaseFragment implements SectionedRVAdapter.Sectionizer {
    @BindView(R.id.rv_homepage)
    LRecyclerView mRecyclerView;
    private Unbinder unbinder;
    private List<List<HomepageListBean.DataBean>> itemList;
    private List<BannerBean.DataBean> bannerList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<HomepageItemBean> dataList = new ArrayList<>();
    //当前页
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 加载广告栏数据
     */
    private void initData() {

        OkHttpUtils
                .get()
                .url(Constant.HOMEPAGE_BANNER)
                .addParams("type", "2")
                .addParams("tid", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        bannerList.clear();
                        BannerBean bannerEntity = GsonUtils.jsonToBean(response, BannerBean.class);
                        bannerList.addAll(bannerEntity.getData());
                        initListData();
                    }
                });
    }


    /**
     * 加载list数据
     */
    private void initListData() {
        OkHttpUtils
                .get()
                .url(Constant.HOMEPAGE_LIST)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        HomepageListBean listEntity = GsonUtils.jsonToBean(response, HomepageListBean.class);
                        if (listEntity.getData() != null && listEntity.getData().size() > 0) {
                            itemList = new ArrayList<>();
                            if (listEntity.getData() != null) {
                                itemList.addAll(listEntity.getData());
                                for (int i = 0; i < itemList.size(); i++) {
                                    for (int j = 0; j < itemList.get(i).size(); j++) {
                                        dataList.add(new HomepageItemBean(Constant.HOST
                                                + itemList.get(i).get(j).getImg(),
                                                Constant.HOMEPAGE_INFO + "?type=1&id="
                                                        + itemList.get(i).get(j).getId(),
                                                itemList.get(i).get(j).getP_time(),
                                                itemList.get(i).get(j).getImg_list(),
                                                itemList.get(i).get(j).getTitle(),
                                                itemList.get(i).get(j).getName(),
                                                itemList.get(i).get(j).getId()));
                                    }
                                }

                                if (isLoadMore || isRefresh) {
                                    mLRecyclerViewAdapter.notifyDataSetChanged();
                                } else {
                                    initView();
                                }
                                isLoadMore = false;
                                if (isRefresh) {
                                    mRecyclerView.refreshComplete();
                                }
                                isRefresh = false;
                            }
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView,
                                    0, LoadingFooter.State.TheEnd, null);
                        }
                    }
                });
    }


    /**
     * 加载视图
     */
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyAdapter myAdapter = new MyAdapter();
        SectionedRVAdapter sectionedRecyclerViewAdapter = new SectionedRVAdapter(getActivity(),
                R.layout.listitem_homepage_title, R.id.tv_list_title, myAdapter, this);
        sectionedRecyclerViewAdapter.setSections(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(sectionedRecyclerViewAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter.addHeaderView(initBanner());

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
                isRefresh = true;
                dataList.clear();
                page = 1;

                initData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(getActivity(), mRecyclerView,
                        0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initListData();
            }
        });
    }

    /**
     * 加载广告栏
     */
    private View initBanner() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.listitem_homepage_header, null);
        final ConvenientBanner banner = (ConvenientBanner) view.findViewById(R.id.banner);
        banner.startTurning(4000);
        List<String> bannerImg = new ArrayList<>();
        for (int i = 0; i < bannerList.size(); i++) {
            bannerImg.add(bannerList.get(i).getImg());
        }

        banner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, bannerImg)
                //设置两个点图片作为翻页指示器
                .setPageIndicator(new int[]{R.drawable.dot_black, R.drawable.dot_white})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (bannerList.size() < 2) {
                    banner.stopTurning();
                }
            }
        });


        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), HomepageDetailActivity.class);
                intent.putExtra("url", bannerList.get(position).getLink());
                intent.putExtra("title", bannerList.get(position).getTitle());
                intent.putExtra("content", "");
                intent.putExtra("img_url", Constant.HOST + bannerList.get(position).getImg());
                intent.putExtra("share_url", bannerList.get(position).getShare_link());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getSectionTitle(Object object) {
        return ((HomepageItemBean) object).time;
    }

    //--------------- MyViewHolder-----------------------
    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
//        public RecyclerView recyclerview;
//        ImageView arrow;

        MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_home_item);
//            recyclerview = (RecyclerView) itemView.findViewById(R.id.rv_item);
//            arrow = (ImageView) itemView.findViewById(R.id.img_list_arrow);
        }
    }


    //-------------------Adapter----------------------------
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.listitem_homepage, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Glide.with(getActivity())
                    .load(dataList.get(position).img)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MobclickAgent.onEvent(getActivity(), "banner");
                    Intent intent = new Intent(getActivity(), HomepageDetailActivity.class);
                    intent.putExtra("url", dataList.get(position).link);
                    intent.putExtra("title", dataList.get(position).title);
                    intent.putExtra("content", dataList.get(position).type);
                    intent.putExtra("img_url", dataList.get(position).img);
                    intent.putExtra("share_url", Constant.HOMEPAGE_SHARE + "?type=1&id="
                            + dataList.get(position).id);
                    startActivity(intent);
                }
            });

//            if (dataList.get(position).imgList.size() == 0 || dataList.get(position).imgList == null) {
//                holder.arrow.setVisibility(View.GONE);
//            }
//
//            if (position == 0) {
//                holder.recyclerview.setVisibility(View.VISIBLE);
//                holder.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),
//                        LinearLayoutManager.HORIZONTAL, false));
//                holder.recyclerview.setAdapter(new MyRecyclerViewAdapter(getActivity(),
//                        dataList.get(position).imgList, dataList.get(position).link,
//                        dataList.get(position).title, dataList.get(position).type, dataList.get(position).img
//                        , Constant.HOMEPAGE_SHARE + "?type=1" + "&id=" + dataList.get(position).id));
//            } else {
//                if (!dataList.get(position).time.equals(dataList.get(position - 1).time)) {
//                    holder.recyclerview.setVisibility(View.VISIBLE);
//                    holder.arrow.setVisibility(View.VISIBLE);
//                    holder.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),
//                            LinearLayoutManager.HORIZONTAL, false));
//                    holder.recyclerview.setAdapter(new MyRecyclerViewAdapter(getActivity(),
//                            dataList.get(position).imgList, dataList.get(position).link,
//                            dataList.get(position).title, dataList.get(position).type, dataList.get(position).img
//                            , Constant.HOMEPAGE_SHARE + "?type=1" + "&id=" + dataList.get(position).id));
//                } else {
//                    holder.recyclerview.setVisibility(View.GONE);
//                    holder.arrow.setVisibility(View.GONE);
//                }
//            }
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    public static HomepageFragment newInstance() {
        Bundle args = new Bundle();
        HomepageFragment fragment = new HomepageFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
