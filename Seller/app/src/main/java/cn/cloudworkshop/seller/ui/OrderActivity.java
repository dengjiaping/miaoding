package cn.cloudworkshop.seller.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.adpter.SectionedRVAdapter;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.bean.OrderBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.GsonUtils;
import cn.cloudworkshop.seller.utils.LogUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import cn.qqtheme.framework.picker.DatePicker;
import okhttp3.Call;

/**
 * Author：binge on 2017-09-15 15:37
 * Email：1993911441@qq.com
 * Describe：订单页面
 */
public class OrderActivity extends BaseActivity implements SectionedRVAdapter.SectionTitle {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rv_order)
    LRecyclerView rvOrder;
    @BindView(R.id.img_no_order)
    ImageView imgNoOrder;

    //日期
    private String date = "";
    //页数
    private int page = 1;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<OrderBean.ListBean> orderList = new ArrayList<>();
    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;
    private SectionedRVAdapter sectionedAdapter;
    private int currentYear;
    private int currentMonth;
    private int currentDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        imgHeaderShare.setVisibility(View.VISIBLE);
        imgHeaderShare.setImageResource(R.mipmap.icon_calendar);
        tvHeaderTitle.setText("订单管理");
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH) + 1;
        currentDay = c.get(Calendar.DAY_OF_MONTH);
        initData();
    }

    private void initData() {

        OkHttpUtils.get()
                .url(Constant.ORDER)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .addParams("date", date)
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        OrderBean orderBean = GsonUtils.jsonToBean(response, OrderBean.class);
                        if (orderBean.getList() != null && orderBean.getList().size() > 0) {
                            if (isRefresh) {
                                orderList.clear();
                            }
                            for (int i = 0; i < orderBean.getList().size(); i++) {
                                for (int j = 0; j < orderBean.getList().get(i).size(); j++) {
                                    OrderBean.ListBean listBean = orderBean.getList().get(i).get(j);
                                    orderList.add(listBean);
                                }
                            }

                            if (isLoadMore || isRefresh) {
                                rvOrder.refreshComplete(0);
                                sectionedAdapter.setSections(orderList);
                                mLRecyclerViewAdapter.notifyDataSetChanged();
                            } else {
                                initView();
                            }
                            isLoadMore = false;
                            isRefresh = false;
                            imgNoOrder.setVisibility(View.GONE);
                            rvOrder.setVisibility(View.VISIBLE);
                        } else {
                            if (isRefresh) {
                                orderList.clear();
                                rvOrder.refreshComplete(0);
                                imgNoOrder.setVisibility(View.VISIBLE);
                                rvOrder.setVisibility(View.GONE);
                                isRefresh = false;

                            } else {
                                RecyclerViewStateUtils.setFooterViewState(OrderActivity.this,
                                        rvOrder, 0, LoadingFooter.State.NoMore, null);
                            }

                        }
                    }
                });
    }

    private void initView() {
        rvOrder.setLayoutManager(new LinearLayoutManager(this));

        CommonAdapter<OrderBean.ListBean> adapter = new CommonAdapter<OrderBean.ListBean>(this,
                R.layout.listitem_order, orderList) {
            @Override
            protected void convert(ViewHolder holder, OrderBean.ListBean listBean, int position) {
                Glide.with(OrderActivity.this)
                        .load(Constant.HOST + listBean.getGoods_thumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_order_goods));
                holder.setText(R.id.tv_order_number, listBean.getOrder_no());
                holder.setText(R.id.tv_goods_name, listBean.getGoods_name());

                switch (listBean.getGoods_type()) {
                    case 2:
                        holder.setText(R.id.tv_goods_type, listBean.getSize_content());
                        break;
                    default:
                        holder.setText(R.id.tv_goods_type, "定制款");
                        break;
                }
                holder.setText(R.id.tv_goods_count, "共" + listBean.getNum() + "件商品");
                holder.setText(R.id.tv_order_price, "¥" + new DecimalFormat("#0.00")
                        .format(Float.parseFloat(listBean.getMoney())));

            }
        };
        sectionedAdapter = new SectionedRVAdapter(this, R.layout.listitem_order_title,
                R.id.tv_order_date, adapter, this);
        sectionedAdapter.setSections(orderList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(sectionedAdapter);
        rvOrder.setAdapter(mLRecyclerViewAdapter);


        rvOrder.setOnRefreshListener(new OnRefreshListener() {
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

        rvOrder.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(OrderActivity.this, rvOrder, 0,
                        LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });
    }

    @OnClick({R.id.img_header_back, R.id.img_header_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_header_share:
                selectDate();
                break;
        }
    }

    @Override
    public String getSectionTitle(Object object) {
        return ((OrderBean.ListBean) object).getDate();
    }

    /**
     * 选择日期
     */
    private void selectDate() {
        DatePicker picker = new DatePicker(this);
        picker.setOffset(2);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        picker.setRangeStart(1900, 1, 1);
        picker.setRangeEnd(year, month, day);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);

        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                isRefresh = true;
                date = year + month + day;
                LogUtils.log(date);
                currentYear = Integer.parseInt(year);
                currentMonth = Integer.parseInt(month);
                currentDay = Integer.parseInt(day);
                page = 1;
                initData();
            }
        });
        picker.show();

    }


}
