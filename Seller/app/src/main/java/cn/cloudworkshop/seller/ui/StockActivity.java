package cn.cloudworkshop.seller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
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
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.bean.StockBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.GsonUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-09-19 14:13
 * Email：1993911441@qq.com
 * Describe：库存查询
 */
public class StockActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rv_stock)
    LRecyclerView rvStock;

    private List<StockBean.ListBean.DataBean> stockList = new ArrayList<>();
    //页数
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private StockBean stockBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("库存查询");
        initData();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isRefresh = true;
        page = 1;
        initData();
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.STOCK)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        stockBean = GsonUtils.jsonToBean(response, StockBean.class);
                        if (stockBean.getList().getData() != null && stockBean.getList().getData().size() > 0) {
                            if (isRefresh) {
                                stockList.clear();
                            }
                            stockList.addAll(stockBean.getList().getData());
                            if (isLoadMore || isRefresh) {
                                rvStock.refreshComplete(0);
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isRefresh = false;
                            isLoadMore = false;

                        } else {
                            RecyclerViewStateUtils.setFooterViewState(StockActivity.this, rvStock,
                                    0, LoadingFooter.State.NoMore, null);
                        }
                    }
                });
    }

    private void initView() {
        if (stockBean.getType() == 1) {
            imgHeaderShare.setVisibility(View.VISIBLE);
            imgHeaderShare.setImageResource(R.mipmap.icon_add);
        }

        rvStock.setLayoutManager(new LinearLayoutManager(this));
        CommonAdapter<StockBean.ListBean.DataBean> adapter = new CommonAdapter<StockBean.ListBean
                .DataBean>(this, R.layout.listitem_stock, stockList) {
            @Override
            protected void convert(ViewHolder holder, StockBean.ListBean.DataBean dataBean, int position) {
                holder.setText(R.id.tv_fabric_number, dataBean.getPinhao());
                holder.setText(R.id.tv_fabric_name, dataBean.getName());
                holder.setText(R.id.tv_fabric_stock, dataBean.getStorage());
                holder.setText(R.id.tv_fabric_consume, dataBean.getUse_up());
            }
        };

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rvStock.setAdapter(mLRecyclerViewAdapter);

        //刷新
        rvStock.setOnRefreshListener(new OnRefreshListener() {
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
        rvStock.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(StockActivity.this, rvStock, 0,
                        LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();

            }
        });

        if (stockBean.getType() == 1) {
            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    stockBean.getList().getData().get(position).setIntroduce("wwwwdd");
                    stockBean.getList().getData().get(position).setImg_max("/uploads/img/2017061511061897981009.jpg");
                    stockBean.getList().getData().get(position).setImg_min("/uploads/img/2017061511061897981009.jpg");
                    Intent intent = new Intent(StockActivity.this, AddStockActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "edit");
                    bundle.putSerializable("edit", stockBean.getList().getData().get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    @OnClick({R.id.img_header_back, R.id.img_header_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                Intent intent = new Intent(StockActivity.this, AddStockActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", "add");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
