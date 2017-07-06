package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.SectionedRVAdapter;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.HomepageItemBean;
import cn.cloudworkshop.miaoding.bean.NewHomepageBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.DesignerDetailActivity;
import cn.cloudworkshop.miaoding.ui.HomepageDetailActivity;
import cn.cloudworkshop.miaoding.ui.JoinUsActivity;
import cn.cloudworkshop.miaoding.ui.LoginActivity;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.NetworkImageHolderView;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-15 09:52
 * Email：1993911441@qq.com
 * Describe：
 */
public class HomeRecommendFragment extends BaseFragment implements SectionedRVAdapter.Sectionizer {
    @BindView(R.id.rv_recommend)
    LRecyclerView mRecyclerView;
    private Unbinder unbinder;
    private List<List<NewHomepageBean.DataBean>> itemList;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<HomepageItemBean> dataList = new ArrayList<>();
    private NewHomepageBean homepageBean;
    //当前页
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_recommend, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }


    /**
     * 加载数据
     */
    private void initData() {
        OkHttpUtils
                .get()
                .url(Constant.NEW_HOMEPAGE_LIST)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        homepageBean = GsonUtils.jsonToBean(response, NewHomepageBean.class);
                        if (homepageBean != null && homepageBean.getData().size() > 0) {

                            itemList = new ArrayList<>();
                            if (isRefresh) {
                                dataList.clear();
                            }
                            itemList.addAll(homepageBean.getData());
                            for (int i = 0; i < itemList.size(); i++) {
                                for (int j = 0; j < itemList.get(i).size(); j++) {
                                    dataList.add(new HomepageItemBean(Constant.HOST
                                            + itemList.get(i).get(j).getImg(),
                                            Constant.HOMEPAGE_INFO + "?content=1&id="
                                                    + itemList.get(i).get(j).getId(),
                                            itemList.get(i).get(j).getP_time(),
                                            itemList.get(i).get(j).getImg_list(),
                                            itemList.get(i).get(j).getTitle(),
                                            itemList.get(i).get(j).getTag_name(),
                                            itemList.get(i).get(j).getSub_title(),
                                            itemList.get(i).get(j).getId()));
                                }
                            }

                            if (isLoadMore || isRefresh) {
                                mRecyclerView.refreshComplete();
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {

                                initView();
                            }
                            isLoadMore = false;
                            isRefresh = false;

                        } else {
                            RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(),
                                    mRecyclerView, 0, LoadingFooter.State.TheEnd, null);
                        }
                    }
                });

    }


    /**
     * 加载视图
     */
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        HomeRecommendFragment.MyAdapter myAdapter = new HomeRecommendFragment.MyAdapter();
        SectionedRVAdapter sectionedRecyclerViewAdapter = new SectionedRVAdapter
                (getParentFragment().getActivity(), R.layout.listitem_homepage_title, R.id.tv_info_title, myAdapter, this);
        sectionedRecyclerViewAdapter.setSections(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(sectionedRecyclerViewAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mLRecyclerViewAdapter.addHeaderView(initHeader());

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                initData();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(getParentFragment().getActivity(),
                        mRecyclerView, 0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });
    }


    /**
     * 加载头部
     */
    private View initHeader() {
        View view = LayoutInflater.from(getParentFragment().getActivity()).inflate
                (R.layout.fragment_homepage_header, null);
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (homepageBean.getLunbo() != null && homepageBean.getLunbo().size() > 0) {
            final ConvenientBanner banner = (ConvenientBanner) view.findViewById(R.id.banner_recommend);
            banner.startTurning(4000);
            final List<String> bannerImg = new ArrayList<>();
            for (int i = 0; i < homepageBean.getLunbo().size(); i++) {
                bannerImg.add(homepageBean.getLunbo().get(i).getImg());
            }

            banner.setPages(
                    new CBViewHolderCreator<NetworkImageHolderView>() {
                        @Override
                        public NetworkImageHolderView createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, bannerImg)
                    //设置两个点图片作为翻页指示器
                    .setPageIndicator(new int[]{R.drawable.indicator_normal, R.drawable.indicator_focus})
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
                    if (homepageBean.getLunbo().size() < 2) {
                        banner.stopTurning();
                    }
                }
            });


            banner.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    homepageLog("banner");
                    //banner点击事件统计
                    MobclickAgent.onEvent(getParentFragment().getActivity(), "banner");
                    switch (homepageBean.getLunbo().get(position).getBanner_type()) {
                        //咨询页webview
                        case 1:
                            Intent intent = new Intent(getParentFragment().getActivity(), HomepageDetailActivity.class);
                            intent.putExtra("url", Constant.HOST + homepageBean.getLunbo().get(position).getLink());
                            intent.putExtra("title", homepageBean.getLunbo().get(position).getTitle());
                            intent.putExtra("content", "");
                            intent.putExtra("img_url", Constant.HOST + homepageBean.getLunbo().get(position).getImg());
                            intent.putExtra("share_url", Constant.HOST + homepageBean.getLunbo().get(position).getShare_link());
                            startActivity(intent);
                            break;
                        //设计师申请入驻
                        case 2:
                            startActivity(new Intent(getParentFragment().getActivity(), JoinUsActivity.class));
                            break;
                    }

                }
            });
        }


        if (homepageBean.getDesigner_list() != null) {
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_recommend_designer);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            final CommonAdapter<NewHomepageBean.DesignerListBean> adapter = new CommonAdapter
                    <NewHomepageBean.DesignerListBean>(getParentFragment().getActivity(),
                    R.layout.listitem_recommend_designer, homepageBean.getDesigner_list()) {


                @Override
                protected void convert(ViewHolder holder, NewHomepageBean.DesignerListBean designerListBean
                        , int position) {
                    //recyclerView横向,分为三个item
                    CardView cardView = holder.getView(R.id.cv_recommend_designer);
                    ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                    int widthPixels = DisplayUtils.getMetrics(getParentFragment().getActivity()).widthPixels;
                    layoutParams.width = (int) ((widthPixels - DisplayUtils.dp2px(getParentFragment().getActivity(), 18)) / 3);
                    cardView.setLayoutParams(layoutParams);

                    SimpleDraweeView imageView = holder.getView(R.id.img_recommend_designer);
                    TextView tvName = holder.getView(R.id.tv_name_designer);
                    tvName.setTypeface(DisplayUtils.setTextType(getParentFragment().getActivity()));
                    tvName.setText(designerListBean.getName());
                    holder.setText(R.id.tv_info_designer, designerListBean.getTag());
                    imageView.setImageURI(Constant.HOST + designerListBean.getAvatar());
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (!homepageBean.getDesigner_list().get(position).getName().equals("虚位以待")) {
                        Intent intent = new Intent(getParentFragment().getActivity(), DesignerDetailActivity.class);
                        intent.putExtra("id", homepageBean.getDesigner_list().get(position).getId() + "");
                        startActivity(intent);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            ImageView imgTitle = (ImageView) view.findViewById(R.id.img_designer_title);
            imgTitle.setVisibility(View.GONE);
        }

        return view;
    }


    @Override
    public String getSectionTitle(Object object) {
        return ((HomepageItemBean) object).time;
    }

    //--------------- MyViewHolder-----------------------
    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView tvTitle;
        public TextView tvContent;


        MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.img_homepage_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_recommend_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_recommend_content);
        }
    }


    //-------------------Adapter----------------------------
    private class MyAdapter extends RecyclerView.Adapter<HomeRecommendFragment.MyViewHolder> {

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public HomeRecommendFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.listitem_homepage, parent, false);

            return new HomeRecommendFragment.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HomeRecommendFragment.MyViewHolder holder, final int position) {
            holder.tvTitle.setText(dataList.get(position).title);
            holder.tvContent.setText(dataList.get(position).type + " · " + dataList.get(position).content);
            Glide.with(getParentFragment().getActivity())
                    .load(dataList.get(position).img)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    homepageLog(dataList.get(position).type);

                    Intent intent = new Intent(getParentFragment().getActivity(), HomepageDetailActivity.class);
                    intent.putExtra("url", dataList.get(position).link);
                    intent.putExtra("title", dataList.get(position).title);
                    intent.putExtra("content", dataList.get(position).content);
                    intent.putExtra("img_url", dataList.get(position).img);
                    intent.putExtra("share_url", Constant.HOMEPAGE_SHARE + "?content=1&id=" +
                            dataList.get(position).id);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

    }

    public static HomeRecommendFragment newInstance() {
        Bundle args = new Bundle();
        HomeRecommendFragment fragment = new HomeRecommendFragment();
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
