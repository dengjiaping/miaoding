package cn.cloudworkshop.miaoding.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CollectionBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author: Libin on 2016/9/1 13:17
 * Email：1993911441@qq.com
 * Describe：个人收藏
 */
public class CollectionActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rv_my_collection)
    LRecyclerView rvMyCollection;
    @BindView(R.id.ll_null_collection)
    LinearLayout llNullCollection;
    @BindView(R.id.tv_my_collection)
    TextView tvMyCollection;

    private List<CollectionBean.DataBean> itemList = new ArrayList<>();

    //页数
    private int page = 1;
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("我的收藏");
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        itemList.clear();
        page = 1;
        initData();
    }


    /**
     * 获取网络数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.COLLECTION)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CollectionBean collection = GsonUtils.jsonToBean(response, CollectionBean.class);
                        if (collection.getData() != null && collection.getData().size() > 0) {
                            if (isRefresh) {
                                itemList.clear();
                            }
                            itemList.addAll(collection.getData());
                            if (isLoadMore || isRefresh) {
                                rvMyCollection.refreshComplete();
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isRefresh = false;
                            isLoadMore = false;
                            llNullCollection.setVisibility(View.GONE);
                            rvMyCollection.setVisibility(View.VISIBLE);

                        } else {
                            RecyclerViewStateUtils.setFooterViewState(CollectionActivity.this,
                                    rvMyCollection, 0, LoadingFooter.State.TheEnd, null);
                            if (page == 1) {
                                rvMyCollection.setVisibility(View.GONE);
                                llNullCollection.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {

        final GridLayoutManager mLayoutManager = new GridLayoutManager(CollectionActivity.this, 2);
        rvMyCollection.setLayoutManager(mLayoutManager);
        CommonAdapter<CollectionBean.DataBean> adapter = new CommonAdapter<CollectionBean.DataBean>(CollectionActivity.this,
                R.layout.listitem_collection, itemList) {
            @Override
            protected void convert(ViewHolder holder, CollectionBean.DataBean itemBean, int position) {
                Glide.with(CollectionActivity.this)
                        .load(Constant.HOST + itemBean.getThumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_collection_goods));
                holder.setText(R.id.tv_collection_name, itemBean.getName());
//                holder.setText(R.id.tv_collection_price, "¥" + itemBean.getPrice2());
            }
        };

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rvMyCollection.setAdapter(mLRecyclerViewAdapter);
        rvMyCollection.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        rvMyCollection.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


        //刷新
        rvMyCollection.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                initData();
            }
        });
        //加载更多
        rvMyCollection.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(CollectionActivity.this, rvMyCollection,
                        0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent;
                if (itemList.get(position).getType() == 1) {
                    intent = new Intent(CollectionActivity.this, CustomGoodsActivity.class);
                } else {
                    intent = new Intent(CollectionActivity.this, WorksDetailActivity.class);
                }

                intent.putExtra("id", String.valueOf(itemList.get(position).getCid()));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                cancelCollection(itemList.get(position).getId());
            }
        });


    }


    /**
     * 取消收藏
     */
    private void cancelCollection(final int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setTitle("取消收藏");
        dialog.setMessage("您确定要取消收藏？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OkHttpUtils.get()
                        .url(Constant.CANCEL_COLLECTION)
                        .addParams("token", SharedPreferencesUtils.getString(CollectionActivity.this, "token"))
                        .addParams("id", "" + id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                itemList.clear();
                                page = 1;
                                initData();
                            }
                        });
            }
        });
        //为“取消”按钮注册监听事件
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据实际情况编写相应代码。
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }


    @OnClick({R.id.img_header_back, R.id.tv_my_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_my_collection:
                Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                intent.putExtra("fragid", 1);
                finish();
                startActivity(intent);
                break;
        }
    }
}
