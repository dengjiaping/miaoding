package cn.cloudworkshop.miaoding.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.cloudworkshop.miaoding.ui.GoodsDetailsActivity;
import cn.cloudworkshop.miaoding.adapter.MyFocusResizeAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.GoodsListBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.FocusResizeScrollListener;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;


/**
 * Author：Libin on 2016/8/29 14:40
 * Email：1993911441@qq.com
 * Describe：商品界面
 */
public class GoodsInfoFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    //当前页面
    private int page = 1;
    //商品id
    private int id;
    //加载更多
    private boolean isLoadMore;
    private MyFocusResizeAdapter myFocusResizeAdapter;

    private List<GoodsListBean.DataBean.itemDataBean> goodsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initData();
        return view;
    }


    /**
     * 获取网络数据
     */
    private void initData() {
        OkHttpUtils
                .get()
                .url(Constant.GOODS_LIST)
                .addParams("type", String.valueOf(1))
                .addParams("classify_id", String.valueOf(id))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        GoodsListBean listEntity = GsonUtils.jsonToBean(response, GoodsListBean.class);
                        if (listEntity.getData().getData() != null && listEntity.getData().getData().size() > 0) {
                            if (isLoadMore) {
                                goodsList.addAll(listEntity.getData().getData());
                                myFocusResizeAdapter.notifyDataSetChanged();
                                isLoadMore = false;
                            } else {
                                goodsList.clear();
                                goodsList.addAll(listEntity.getData().getData());
                                initView();
                            }

                        }
                    }
                });

    }


    /**
     * 加载视图
     */
    protected void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());
        createCustomAdapter(rvGoods, linearLayoutManager);
    }


    /**
     * 创建适配器
     */
    private void createCustomAdapter(RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager) {
        myFocusResizeAdapter = new MyFocusResizeAdapter(getParentFragment().getActivity(),
                (int) getResources().getDimension(R.dimen.custom_item_height));
        myFocusResizeAdapter.addItems(goodsList);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(myFocusResizeAdapter);
            recyclerView.addOnScrollListener(new FocusResizeScrollListener<>(myFocusResizeAdapter, linearLayoutManager));


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastVisiblePosition >= linearLayoutManager.getItemCount() - 1 && dy > 0) {
                        isLoadMore = true;
                        page++;
                        initData();
                    }
                }
            });

            myFocusResizeAdapter.setOnItemClickListener(new MyFocusResizeAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getParentFragment().getActivity(), GoodsDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(goodsList.get(position).getId()));
                    bundle.putString("img_url", goodsList.get(position).getThumb());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static GoodsInfoFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void getData() {
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
    }
}
