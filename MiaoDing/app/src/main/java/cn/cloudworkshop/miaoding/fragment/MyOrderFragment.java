package cn.cloudworkshop.miaoding.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.OrderInfoBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.AfterSalesActivity;
import cn.cloudworkshop.miaoding.ui.LogisticsActivity;
import cn.cloudworkshop.miaoding.ui.MainActivity;
import cn.cloudworkshop.miaoding.ui.MyOrderActivity;
import cn.cloudworkshop.miaoding.ui.OrderDetailActivity;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.PayOrderUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/9/17 15:57
 * Email：1993911441@qq.com
 * Describe：我的订单子界面
 */
public class MyOrderFragment extends BaseFragment {
    Unbinder unbinder;

    @BindView(R.id.tv_my_order)
    TextView tvMyOrder;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.ll_null_order)
    LinearLayout llNullOrder;

    private CommonAdapter<OrderInfoBean.DataBean> adapter;
    private List<OrderInfoBean.DataBean> dataList = new ArrayList<>();
    //订单状态
    private int orderStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_goods, container, false);
        unbinder = ButterKnife.bind(this, view);

        getData();
        initData();
        return view;
    }

    private void getData() {
        Bundle bundle = getArguments();
        orderStatus = bundle.getInt("order");
    }


    /**
     * 获取订单数据
     */
    private void initData() {
        //是否售后
        int isAfterSale;
        if (orderStatus == 4) {
            isAfterSale = 1;
        } else {
            isAfterSale = 0;
        }
        OkHttpUtils.get()
                .url(Constant.MY_ORDER)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .addParams("status", orderStatus + "")
                .addParams("sh_status", isAfterSale + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        OrderInfoBean entity = GsonUtils.jsonToBean(response, OrderInfoBean.class);
                        if (entity.getData() != null && entity.getData().size() > 0) {
                            dataList.addAll(entity.getData());
                            initView();
                        } else {
                            llNullOrder.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    protected void initView() {
        rvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommonAdapter<OrderInfoBean.DataBean>(getActivity(), R.layout.listitem_order, dataList) {
            @Override
            protected void convert(final ViewHolder holder, final OrderInfoBean.DataBean dataBean,
                                   final int position) {

                holder.setText(R.id.tv_order_number, dataBean.getOrder_no());

                if (dataBean.getList() != null && dataBean.getList().size() > 0) {
                    Glide.with(getActivity())
                            .load(Constant.HOST + dataBean.getList().get(0).getGoods_thumb())
                            .into((ImageView) holder.getView(R.id.img_order_info));
                    TextView tvGoodsName = holder.getView(R.id.tv_order_name);
                    tvGoodsName.setText(dataBean.getList().get(0).getGoods_name());
                    tvGoodsName.setTypeface(DisplayUtils.setTextType(mContext));
                    switch (dataBean.getList().get(0).getGoods_type()) {
                        case 2:
                            holder.setText(R.id.tv_order_content, dataBean.getList().get(0).getSize_content());
                            break;
                        default:
                            holder.setText(R.id.tv_order_content, "定制款");
                            break;
                    }
                    holder.setText(R.id.tv_order_count, "共" + dataBean.getList().get(0).getNum() + "件商品");
                }


                holder.setText(R.id.tv_order_price, "¥" + dataBean.getMoney());

                switch (dataList.get(position).getStatus()) {
                    case 1:
                        holder.setVisible(R.id.tv_after_sale, false);
                        holder.setVisible(R.id.tv_order_control, true);
                        holder.setVisible(R.id.tv_order_pay, true);
                        holder.setText(R.id.tv_order_status, "待付款");
                        holder.setText(R.id.tv_order_control, "取消订单");
                        holder.setText(R.id.tv_order_pay, "付款");

                        break;
                    case 2:
                        holder.setVisible(R.id.tv_after_sale, false);
                        holder.setVisible(R.id.tv_order_control, true);
                        holder.setVisible(R.id.tv_order_pay, false);
                        holder.setText(R.id.tv_order_status, "待发货");
                        holder.setText(R.id.tv_order_control, "提醒发货");
                        break;
                    case 3:
                        holder.setVisible(R.id.tv_after_sale, false);
                        holder.setVisible(R.id.tv_order_control, true);
                        holder.setVisible(R.id.tv_order_pay, true);
                        holder.setText(R.id.tv_order_status, "已发货");
                        holder.setText(R.id.tv_after_sale, "售后服务");
                        holder.setText(R.id.tv_order_control, "查看物流");
                        holder.setText(R.id.tv_order_pay, "确认收货");
                        break;
                    case 4:
                        holder.setVisible(R.id.tv_after_sale, false);
                        holder.setVisible(R.id.tv_order_control, true);
                        holder.setVisible(R.id.tv_order_pay, true);
                        holder.setText(R.id.tv_order_status, "已完成");
                        holder.setText(R.id.tv_after_sale, "售后服务");
                        holder.setText(R.id.tv_order_control, "查看物流");
                        holder.setText(R.id.tv_order_pay, "评价");
                        break;
                    case -2:
                        holder.setVisible(R.id.tv_after_sale, false);
                        holder.setVisible(R.id.tv_order_control, true);
                        holder.setVisible(R.id.tv_order_pay, false);
                        holder.setText(R.id.tv_order_status, "已取消");
                        holder.setText(R.id.tv_order_control, "删除订单");
                        break;
                }

                holder.getView(R.id.tv_order_control).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (dataList.get(position).getStatus()) {
                            case 1:
                                cancelOrder(dataList.get(position).getId());
                                break;
                            case 2:
                                Toast.makeText(getActivity(), "已提醒商家发货，请耐心等待",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                            case 4:
                                Intent intent = new Intent(getActivity(), LogisticsActivity.class);
                                intent.putExtra("number", dataList.get(position).getEms_no());
                                intent.putExtra("company", dataList.get(position).getEms_com());
                                intent.putExtra("company_name", dataList.get(position).getEms_com_name());
                                intent.putExtra("img_url", dataList.get(position).getList().get(0)
                                        .getGoods_thumb());
                                startActivity(intent);
                                break;
                            case -2:
                                deleteOrder(dataList.get(position).getId(), position);
                                break;
                        }
                    }
                });
                holder.getView(R.id.tv_order_pay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (dataList.get(position).getStatus()) {
                            case 1:
                                PayOrderUtils payOrderUtil = new PayOrderUtils(getActivity(),
                                        dataBean.getMoney(), dataList.get(position).getId() + "");
                                payOrderUtil.payMoney();
                                break;
                            case 3:
                                confirmReceive(dataList.get(position).getId());
                                break;
                            case 4:

                                break;

                        }
                    }
                });
                holder.getView(R.id.tv_after_sale).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AfterSalesActivity.class);
                        intent.putExtra("order_id", dataList.get(position).getId());
                        startActivity(intent);
                    }
                });

            }
        };

        rvGoods.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("id", dataList.get(position).getId() + "");
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }


    /**
     * 确认收货
     */
    private void confirmReceive(final int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        dialog.setTitle("确认收货");
        dialog.setMessage("您要确认收货吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.get()
                        .url(Constant.CONFIRM_RECEIVE)
                        .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                        .addParams("order_id", id + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {


                            }

                            @Override
                            public void onResponse(String response, int id) {
                                MobclickAgent.onEvent(getActivity(), "trade_success");
                                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                                intent.putExtra("page", orderStatus);
                                getActivity().finish();
                                getActivity().startActivity(intent);
                                Toast.makeText(getActivity(), "交易完成，祝您购物愉快！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        //为“取消”按钮注册监听事件
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }


    /**
     * 删除订单
     */
    private void deleteOrder(final int id, final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        dialog.setTitle("删除订单");
        dialog.setMessage("您确定要删除订单吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.get()
                        .url(Constant.DELETE_ORDER)
                        .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                        .addParams("order_id", id + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code == 1) {
                                        dataList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                        if (dataList.size() == 0 && orderStatus == 0) {
                                            llNullOrder.setVisibility(View.VISIBLE);
                                        }
                                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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


    /**
     * 取消订单
     */
    private void cancelOrder(final int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        dialog.setTitle("取消订单");
        dialog.setMessage("您确定要取消订单吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.get()
                        .url(Constant.CANCEL_ORDER)
                        .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                        .addParams("order_id", id + "")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
//                                dataList.clear();
//                                initGoods();
//                                adapter.notifyDataSetChanged();
                                MobclickAgent.onEvent(getActivity(), "cancel_order");
                                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                                intent.putExtra("page", orderStatus);
                                getActivity().finish();
                                getActivity().startActivity(intent);
                                Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show();
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

    public static MyOrderFragment newInstance(int orderStatus) {
        Bundle args = new Bundle();
        args.putInt("order", orderStatus);
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_my_order)
    public void onClick() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("fragid", 1);
        getActivity().finish();
        startActivity(intent);
    }
}
