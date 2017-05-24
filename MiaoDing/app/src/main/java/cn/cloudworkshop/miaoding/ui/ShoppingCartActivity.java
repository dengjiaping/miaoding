package cn.cloudworkshop.miaoding.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CartDetailsBean;
import cn.cloudworkshop.miaoding.bean.RecommendBean;
import cn.cloudworkshop.miaoding.bean.ShoppingCartBean;
import cn.cloudworkshop.miaoding.bean.TailorItemBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/31 11:02
 * Email：1993911441@qq.com
 * Describe：购物袋
 */
public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rv_goods_cart)
    RecyclerView rvGoodsCart;
    @BindView(R.id.tv_my_bag)
    TextView tvMyBag;
    @BindView(R.id.rl_null_bag)
    RelativeLayout rlNullBag;
    @BindView(R.id.ll_cart_goods)
    LinearLayout llCartGoods;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.checkbox_all_select)
    CheckBox checkboxAllSelect;
    @BindView(R.id.tv_total_counts)
    TextView tvTotalCounts;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_goods_buy)
    TextView tvGoodsBuy;
    @BindView(R.id.rv_cart_recommend)
    RecyclerView rvRecommend;
    private CommonAdapter<ShoppingCartBean.DataBean> adapter;
    private List<ShoppingCartBean.DataBean> dataList;

    //编辑状态
    private boolean flag;
    public static ShoppingCartActivity cartActivity;
    private RecommendBean recommendBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        cartActivity = this;
        tvHeaderTitle.setText("购物袋");
        tvHeaderNext.setText("编辑");

        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /**
     * 获取网络数据
     */
    private void initData() {
        checkboxAllSelect.setChecked(true);
        OkHttpUtils.get()
                .url(Constant.SHOPPING_CART)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ShoppingCartBean shoppingCartBean = GsonUtils.jsonToBean(response,
                                ShoppingCartBean.class);
                        dataList = new ArrayList<>();
                        if (shoppingCartBean.getData().size() == 0 || shoppingCartBean.getData() == null) {
                            nullCart();
                        } else {
                            rlNullBag.setVisibility(View.GONE);
                            llCartGoods.setVisibility(View.VISIBLE);
                            tvHeaderNext.setVisibility(View.VISIBLE);

                            dataList.addAll(shoppingCartBean.getData());

                            initView();
                        }
                    }
                });
    }

    /**
     * 空购物车
     */
    private void nullCart() {
        rlNullBag.setVisibility(View.VISIBLE);
        llCartGoods.setVisibility(View.GONE);
        tvHeaderNext.setVisibility(View.GONE);
        OkHttpUtils.get()
                .url(Constant.GOODS_RECOMMEND)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        recommendBean = GsonUtils.jsonToBean(response, RecommendBean.class);
                        if (recommendBean.getData().getData() != null) {
                            recommendGoods();
                        }
                    }
                });
    }

    /**
     * 推荐商品
     */
    private void recommendGoods() {
        rvRecommend.setLayoutManager(new GridLayoutManager(ShoppingCartActivity.this, 2));
        CommonAdapter<RecommendBean.DataBeanX.DataBean> adapter = new CommonAdapter<RecommendBean
                .DataBeanX.DataBean>(this,
                R.layout.listitem_goods_recommend, recommendBean.getData().getData()) {
            @Override
            protected void convert(ViewHolder holder, RecommendBean.DataBeanX.DataBean dataBean,
                                   int position) {
                Glide.with(ShoppingCartActivity.this)
                        .load(Constant.HOST + dataBean.getThumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_goods_recommend));
            }
        };
        rvRecommend.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent;
                if (recommendBean.getData().getData().get(position).getType() == 1) {
                    intent = new Intent(ShoppingCartActivity.this, CustomGoodsActivity.class);
                } else {
                    intent = new Intent(ShoppingCartActivity.this, WorksDetailActivity.class);
                }

                intent.putExtra("id", String.valueOf(recommendBean.getData().getData().get(position)
                        .getGoods_id()));
                startActivity(intent);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }


    /**
     * 加载视图
     */
    private void initView() {
        getTotalPrice();
        getTotalCount();

        rvGoodsCart.setLayoutManager(new LinearLayoutManager(ShoppingCartActivity.this));
        adapter = new CommonAdapter<ShoppingCartBean.DataBean>(ShoppingCartActivity.this,
                R.layout.listitem_shopping_cart, dataList) {
            @Override
            protected void convert(final ViewHolder holder, final ShoppingCartBean.DataBean dataBean,
                                   final int position) {
                Glide.with(ShoppingCartActivity.this)
                        .load(Constant.HOST + dataBean.getGoods_thumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_item_goods));
                TextView tvGoodsName = holder.getView(R.id.tv_goods_name);
                tvGoodsName.setTypeface(DisplayUtils.setTextType(mContext));
                tvGoodsName.setText(dataBean.getGoods_name());
                switch (dataList.get(position).getGoods_type()) {
                    case 2:
                        holder.setText(R.id.tv_goods_content, dataBean.getSize_content());
                        break;
                    default:
                        holder.setText(R.id.tv_goods_content, "定制款");
                        break;
                }


                holder.setText(R.id.tv_goods_price, "¥" + new DecimalFormat("#0.00").format(dataBean.getPrice()));
                holder.setText(R.id.tv_goods_count, "x" + dataBean.getNum() + "");
                holder.setVisible(R.id.ll_cart_edit, flag);
                holder.setVisible(R.id.tv_goods_content, !flag);
                holder.setVisible(R.id.tv_goods_price, !flag);
                holder.setVisible(R.id.tv_goods_count, !flag);
                holder.setText(R.id.tv_cart_count, dataList.get(position).getNum() + "");
                final CheckBox checkBox = holder.getView(R.id.checkbox_goods_select);
                checkBox.setOnCheckedChangeListener(null);
                holder.setChecked(R.id.checkbox_goods_select, dataBean.getIs_select());

                final TextView tvReduce = holder.getView(R.id.tv_cart_reduce);
                tvReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dataBean.getNum() > 1) {
                            changeCartCount(position, dataBean.getNum() - 1);
                        }
                    }
                });

                final TextView tvAdd = holder.getView(R.id.tv_cart_add);
                tvAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeCartCount(position, dataBean.getNum() + 1);
                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        dataBean.setIs_select(b);
                        checkBox.setChecked(b);
                        int count = 0;
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getIs_select()) {
                                count++;
                            }
                        }
                        if (count == dataList.size()) {
                            checkboxAllSelect.setChecked(true);
                        } else {
                            checkboxAllSelect.setChecked(false);
                        }
                        getTotalCount();
                        getTotalPrice();
                    }
                });
            }
        };
        rvGoodsCart.setAdapter(adapter);

        checkboxAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    ((CheckBox) view).setChecked(true);
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.get(i).setIs_select(true);
                    }
                } else {
                    ((CheckBox) view).setChecked(false);
                    for (int i = 0; i < dataList.size(); i++) {
                        dataList.get(i).setIs_select(false);
                    }
                }
                adapter.notifyDataSetChanged();
                getTotalCount();
                getTotalPrice();

            }
        });


        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (tvHeaderNext.getText().toString().equals("编辑")) {
                    switch (dataList.get(position).getGoods_type()) {
                        case 1:
                            cartToTailorInfo(position);
                            break;
                        case 2:
                            Intent intent = new Intent(ShoppingCartActivity.this, WorksDetailActivity.class);
                            intent.putExtra("id", String.valueOf(dataList.get(position).getGoods_id()));
                            startActivity(intent);
                            break;
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<Integer> itemList = new ArrayList<>();
                itemList.add(dataList.get(position).getId());
                deleteGoods(itemList);
                return false;
            }
        });

    }

    /**
     * 购物车跳转定制详情
     */
    private void cartToTailorInfo(int position) {
        OkHttpUtils.get()
                .url(Constant.CART_TO_TAILOR)
                .addParams("token", SharedPreferencesUtils.getString(ShoppingCartActivity.this, "token"))
                .addParams("car_id", dataList.get(position).getId() + "")
                .addParams("phone_type", 3 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CartDetailsBean cartDetails = GsonUtils.jsonToBean(response, CartDetailsBean.class);

                        if (cartDetails.getData() != null) {
                            Intent intent = new Intent(ShoppingCartActivity.this, CustomResultActivity.class);
                            Bundle bundle = new Bundle();
                            TailorItemBean tailorBean = new TailorItemBean();
                            tailorBean.setId(cartDetails.getData().getId() + "");
                            tailorBean.setGoods_name(cartDetails.getData().getGoods_name());
                            tailorBean.setPrice(new DecimalFormat("#0.00").format(cartDetails
                                    .getData().getPrice()));
                            tailorBean.setImg_url(cartDetails.getData().getGoods_thumb());
                            tailorBean.setSpec_ids(cartDetails.getData().getSpec_ids());
                            tailorBean.setSpec_content(cartDetails.getData().getSpec_content());


                            List<TailorItemBean.ItemBean> itemList = new ArrayList<>();
                            for (int i = 0; i < cartDetails.getData().getImg_list().size(); i++) {
                                TailorItemBean.ItemBean itemBean = new TailorItemBean.ItemBean();
                                itemBean.setImg(cartDetails.getData().getImg_list().get(i).getImg_c());
                                itemBean.setPosition_id(cartDetails.getData().getImg_list().get(i)
                                        .getPosition_id());
                                itemList.add(itemBean);
                            }

                            //图片
                            tailorBean.setItemBean(itemList);
                            //面料
                            tailorBean.setFabric_id(cartDetails.getData().getMianliao_id());
                            //部件id
                            tailorBean.setSpec_ids(cartDetails.getData().getSpec_ids());
                            //部件名称
                            tailorBean.setSpec_content(cartDetails.getData().getSpec_content());
                            bundle.putSerializable("tailor", tailorBean);

                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    }
                });
    }

    /**
     * @param position 改变购物车数量
     */
    private void changeCartCount(final int position, final int counts) {
        OkHttpUtils.post()
                .url(Constant.CART_COUNT)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("car_id", dataList.get(position).getId() + "")
                .addParams("num", counts + "")
                .addParams("type", "1")
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
                                dataList.get(position).setNum(counts);
                                adapter.notifyDataSetChanged();
                                getTotalPrice();
                                getTotalCount();
                            } else {
                                Toast.makeText(ShoppingCartActivity.this, "库存不足", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    /**
     * 删除购物车
     */
    private void deleteGoods(final List<Integer> itemList) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setTitle("删除宝贝");
        dialog.setMessage("您确定要删除该宝贝吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < itemList.size(); i++) {

                    if (i == itemList.size() - 1) {
                        sb.append(itemList.get(i));
                    } else {
                        sb.append(itemList.get(i)).append(",");
                    }

                }

                OkHttpUtils.post()
                        .url(Constant.DELETE_CART)
                        .addParams("token", SharedPreferencesUtils.getString(ShoppingCartActivity.this, "token"))
                        .addParams("car_id", sb.toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    for (int j = 0; j < itemList.size(); j++) {
                                        if (dataList.get(i).getId() == (itemList.get(j))) {
                                            dataList.remove(i);
                                            adapter.notifyItemRemoved(i);
                                            adapter.notifyItemRangeChanged(i, adapter.getItemCount());
                                        }
                                    }
                                }
                                if (dataList.size() == 0) {
                                    nullCart();
                                } else {
                                    getTotalCount();
                                    getTotalPrice();
                                }
                                Toast.makeText(ShoppingCartActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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


    @OnClick({R.id.img_header_back, R.id.tv_my_bag, R.id.tv_header_next, R.id.tv_goods_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_goods_buy:
                if (tvGoodsBuy.getText().toString().equals("删除")) {
                    if (getTotalCount() != 0) {
                        deleteGoods(getSelected());
                    } else {
                        Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getTotalCount() != 0) {
                        buyGoods();
                    } else {
                        Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.tv_my_bag:
                Intent intent = new Intent(ShoppingCartActivity.this, MainActivity.class);
                intent.putExtra("fragid", 1);
                finish();
                startActivity(intent);
                break;
            case R.id.tv_header_next:
                if (tvHeaderNext.getText().toString().equals("编辑")) {
                    flag = true;
                    adapter.notifyDataSetChanged();
                    tvHeaderNext.setText("确定");
                    tvGoodsBuy.setText("删除");
                } else if (tvHeaderNext.getText().toString().equals("确定")) {
                    flag = false;
                    adapter.notifyDataSetChanged();
                    tvHeaderNext.setText("编辑");
                    getTotalCount();
                }
                break;
        }
    }


    /**
     * 下单
     */

    private void buyGoods() {
        Intent intent = new Intent(this, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        bundle.putString("cart_id", getCartIds());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @return 已选
     */
    private List<Integer> getSelected() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getIs_select()) {
                list.add(dataList.get(i).getId());
            }
        }

        return list;
    }


    /**
     * 已选择商品Id
     */
    private String getCartIds() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getIs_select()) {
                if (i == dataList.size() - 1) {
                    sb.append(dataList.get(i).getId());
                } else {
                    sb.append(dataList.get(i).getId()).append(",");
                }
            }
        }
        return sb.toString();
    }


    /**
     * 获取总价格
     */
    public void getTotalPrice() {
        float sum = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getIs_select()) {
                sum += dataList.get(i).getPrice() * dataList.get(i).getNum();
            }
        }
        tvTotalPrice.setText("¥" + new DecimalFormat("#0.00").format(sum));
    }


    /**
     * 总数量
     */
    public int getTotalCount() {
        int selectCount = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getIs_select()) {
                selectCount += dataList.get(i).getNum();
            }
        }
        if (tvHeaderNext.getText().toString().trim().equals("确定")) {
            tvGoodsBuy.setText("删除");
        } else if (tvHeaderNext.getText().toString().trim().equals("编辑")) {
            tvGoodsBuy.setText("下单(" + selectCount + ")");
        }
        return selectCount;
    }
}
