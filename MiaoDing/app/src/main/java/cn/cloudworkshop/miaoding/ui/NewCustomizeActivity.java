package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CustomItemBean;
import cn.cloudworkshop.miaoding.bean.GuideBean;
import cn.cloudworkshop.miaoding.bean.TailorBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/31 09:24
 * Email：1993911441@qq.com
 * Describe：定制页面（当前版）
 */
public class NewCustomizeActivity extends BaseActivity {

    @BindView(R.id.rv_tailor_cloth)
    RecyclerView rvTailor;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rv_tailor_item)
    RecyclerView rvTailorItem;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.img_large_material)
    ImageView imgLargeMaterial;
    @BindView(R.id.rl_positive_tailor)
    RelativeLayout rlPositiveTailor;
    @BindView(R.id.rl_back_tailor)
    RelativeLayout rlBackTailor;
    @BindView(R.id.rgs_select_orientation)
    RadioGroup rgsSelectOrientation;
    @BindView(R.id.img_tailor_icon)
    ImageView imgTailorIcon;
    @BindView(R.id.rv_tailor_button)
    RecyclerView rvTailorButton;
    @BindView(R.id.rl_inside_tailor)
    RelativeLayout rlInsideTailor;
    @BindView(R.id.img_tailor_reset)
    ImageView imgReset;
    @BindView(R.id.img_tailor_guide)
    ImageView imgGuide;
    @BindView(R.id.img_load_error)
    ImageView imgLoadError;

    private TailorBean.DataBean dataBean;
    //配件
//    private List<TailorBean.DataBean.SpecListBean> specList = new ArrayList<>();
    private List<TailorBean.DataBean.SpecListBean.ListBean> itemList = new ArrayList<>();
    //稀疏数组
    //选择部件id
    private SparseIntArray itemArray = new SparseIntArray();

    private CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean> itemAdapter;

    //商品id
    private String id;
    //商店id
    private String shop_id;
    //工厂id
    private String market_id;
    //商品名称
    private String goodsName;
    //商品图
    private String imgUrl;
    //价格
    private String price;
    //价格类型
    private String priceType;
    //商品分类
    private int classifyId;
    private String logId;
    private long goodsTime;
    //进入页面时间
    private long enterTime;

    //当前部件位置
    private int currentPart;
    //当前子部件位置
    private int currentItem;
    //是否选择法式袖扣
    private String buttonName;
    //首次选择
    private boolean firstSelect = true;
    //默认面料图片
    private String default_img;
    //选择钮扣动画
    private AnimationDrawable animation;
    //展示图滑动监听
    private float x1 = 0;
    private float x2 = 0;

    private GuideBean guideBean;
    //首次进入引导页面
    private boolean isFirstEntry;
    private boolean isLongPress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailor);
        ButterKnife.bind(this);

        getData();
        initData();
    }

    /**
     * 商品信息
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        shop_id = bundle.getString("shop_id");
        market_id = bundle.getString("market_id");
        goodsName = bundle.getString("goods_name");
        imgUrl = bundle.getString("img_url");
        price = bundle.getString("price");
        priceType = bundle.getString("price_type");
        classifyId = bundle.getInt("classify_id");
        logId = bundle.getString("log_id");
        goodsTime = bundle.getLong("goods_time");
        enterTime = DateUtils.getCurrentTime();
    }


    /**
     * 获取网络数据
     */
    private void initData() {
        isFirstEntry = SharedPreferencesUtils.getBoolean(this, "tailor_guide", true);
        if (isFirstEntry) {
            OkHttpUtils.get()
                    .url(Constant.GUIDE_IMG)
                    .addParams("id", "2")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            guideBean = GsonUtils.jsonToBean(response, GuideBean.class);
                            if (guideBean.getData().getImg_urls() != null && guideBean.getData()
                                    .getImg_urls().size() > 0) {
                                imgGuide.setVisibility(View.VISIBLE);
                                Glide.with(NewCustomizeActivity.this)
                                        .load(Constant.HOST + guideBean.getData().getImg_urls().get(0))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(imgGuide);
                            }
                        }
                    });
        }
        OkHttpUtils.get()
                .url(Constant.CUSTOMIZE)
                .addParams("goods_id", id)
                .addParams("phone_type", "3")
                .addParams("price_type", priceType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        imgLoadError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        imgLoadError.setVisibility(View.GONE);
                        dataBean = GsonUtils.jsonToBean(response, TailorBean.class).getData();
                        if (dataBean.getSpec_list() != null && dataBean.getSpec_list().size() > 0) {
                            initView();
                        }
                    }
                });

    }


    /**
     * 加载视图
     */
    private void initView() {
        tvHeaderTitle.setText("选版型");
        animation = (AnimationDrawable) imgTailorIcon.getDrawable();
        ((RadioButton) rgsSelectOrientation.getChildAt(0)).setChecked(true);

        for (int i = 0; i < dataBean.getSpec_list().size(); i++) {
            ImageView img1 = new ImageView(this);
            img1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img2 = new ImageView(this);
            img2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img3 = new ImageView(this);
            img3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            switch (dataBean.getSpec_list().get(i).getPosition_id()) {
                case 1:
                    Glide.with(getApplicationContext())
                            .load(Constant.HOST + dataBean.getSpec_list().get(i).getList().get(0).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img1);
                    break;
                case 2:
                    Glide.with(getApplicationContext())
                            .load(Constant.HOST + dataBean.getSpec_list().get(i).getList().get(0).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img2);
                    break;
                case 3:
                    rgsSelectOrientation.getChildAt(2).setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(Constant.HOST + dataBean.getSpec_list().get(i).getList().get(0).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img3);

                    break;
            }
            rlPositiveTailor.addView(img1);
            rlBackTailor.addView(img2);
            rlInsideTailor.addView(img3);
        }


        rgsSelectOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                for (int m = 0; m < rgsSelectOrientation.getChildCount(); m++) {
                    RadioButton rBtn = (RadioButton) radioGroup.getChildAt(m);
                    if (rBtn.getId() == i) {
                        switch (m) {
                            case 0:
                                rlPositiveTailor.setVisibility(View.VISIBLE);
                                rlBackTailor.setVisibility(View.GONE);
                                rlInsideTailor.setVisibility(View.GONE);
                                break;
                            case 1:
                                rlBackTailor.setVisibility(View.VISIBLE);
                                rlPositiveTailor.setVisibility(View.GONE);
                                rlInsideTailor.setVisibility(View.GONE);
                                break;
                            case 2:
                                rlInsideTailor.setVisibility(View.VISIBLE);
                                rlPositiveTailor.setVisibility(View.GONE);
                                rlBackTailor.setVisibility(View.GONE);
                                break;
                        }
                    }
                }
            }
        });

        rlPositiveTailor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getX();
                    case MotionEvent.ACTION_UP:
                        if (x1 < x2) {
                            ((RadioButton) rgsSelectOrientation.getChildAt(1)).setChecked(true);
                        }
                        break;
                }
                return true;
            }
        });

        rlBackTailor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getX();
                    case MotionEvent.ACTION_UP:
                        if (x1 > x2) {
                            ((RadioButton) rgsSelectOrientation.getChildAt(0)).setChecked(true);
                        } else if (x1 < x2) {
                            if (rgsSelectOrientation.getChildAt(2).getVisibility() == View.VISIBLE) {
                                ((RadioButton) rgsSelectOrientation.getChildAt(2)).setChecked(true);
                            }
                        }
                        break;
                }
                return true;
            }
        });

        rlInsideTailor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getX();
                    case MotionEvent.ACTION_UP:
                        if (x1 > x2) {
                            ((RadioButton) rgsSelectOrientation.getChildAt(1)).setChecked(true);
                        }
                        break;
                }
                return true;
            }

        });

        //部件
        rvTailor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        CommonAdapter<TailorBean.DataBean.SpecListBean> adapter = new CommonAdapter<TailorBean
                .DataBean.SpecListBean>(NewCustomizeActivity.this,
                R.layout.listitem_custom_parts, dataBean.getSpec_list()) {
            @Override
            protected void convert(ViewHolder holder, TailorBean.DataBean.SpecListBean specListBean, int position) {
                Glide.with(NewCustomizeActivity.this)
                        .load(Constant.HOST + specListBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_tailor_item));
            }
        };
        rvTailor.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //选择正反面
                ((RadioButton) rgsSelectOrientation.getChildAt(dataBean.getSpec_list()
                        .get(position).getPosition_id() - 1)).setChecked(true);
                tvHeaderTitle.setText(dataBean.getSpec_list().get(position).getSpec_name());
                currentPart = position;

                imgLargeMaterial.setVisibility(View.GONE);
                rvTailorButton.setVisibility(View.GONE);
                rvTailorItem.setVisibility(View.VISIBLE);

                noMatchSpec();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //配件
        rvTailorItem.setLayoutManager(new LinearLayoutManager(NewCustomizeActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        itemAdapter = new CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean>(
                NewCustomizeActivity.this, R.layout.listitem_custom_parts, itemList) {
            @Override
            protected void convert(ViewHolder holder, TailorBean.DataBean.SpecListBean.ListBean listBean, int position) {
                Glide.with(NewCustomizeActivity.this)
                        .load(Constant.HOST + listBean.getImg_a())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((CircleImageView) holder.getView(R.id.img_tailor_item));
            }
        };
        rvTailorItem.setAdapter(itemAdapter);

        if (isFirstEntry && guideBean.getData().getImg_urls().get(1) != null) {
            imgGuide.setVisibility(View.VISIBLE);
            Glide.with(NewCustomizeActivity.this)
                    .load(Constant.HOST + guideBean.getData().getImg_urls().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgGuide);
            isFirstEntry = false;
            SharedPreferencesUtils.saveBoolean(NewCustomizeActivity.this,
                    "tailor_guide", false);
        }

        rvTailorItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imgLargeMaterial.setVisibility(View.GONE);
                }
                return false;
            }
        });

        itemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!isLongPress) {
                    currentItem = position;
                    if (firstSelect && currentPart != 0) {
                        for (int i = 0; i < rlPositiveTailor.getChildCount(); i++) {
                            ImageView positiveImg = (ImageView) rlPositiveTailor.getChildAt(i);
                            positiveImg.setImageDrawable(null);
                        }

                        for (int i = 0; i < rlBackTailor.getChildCount(); i++) {
                            ImageView backImg = (ImageView) rlBackTailor.getChildAt(i);
                            backImg.setImageDrawable(null);
                        }

                        for (int i = 0; i < rlInsideTailor.getChildCount(); i++) {
                            ImageView inSideImg = (ImageView) rlInsideTailor.getChildAt(i);
                            inSideImg.setImageDrawable(null);
                        }
                        firstSelect = false;
                    }

                    imgReset.setVisibility(View.VISIBLE);

                    itemArray.put(currentPart, itemList.get(position).getId());


                    CircleImageView img = (CircleImageView) rvTailor.findViewHolderForAdapterPosition(currentPart)
                            .itemView.findViewById(R.id.img_tailor_item);
                    Glide.with(NewCustomizeActivity.this)
                            .load(Constant.HOST + itemList.get(position).getImg_a())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img);

                    View itemBg = rvTailor.findViewHolderForAdapterPosition(currentPart).itemView
                            .findViewById(R.id.view_tailor_item);
                    itemBg.setVisibility(View.VISIBLE);

                    if (dataBean.getSpec_list().get(currentPart).getSpec_name().equals("面料")) {
                        default_img = itemList.get(position).getMianliao_img();
                    }

                    //选择正反面
                    ((RadioButton) rgsSelectOrientation.getChildAt(dataBean.getSpec_list().get(currentPart)
                            .getPosition_id() - 1)).setChecked(true);

                    switch (dataBean.getSpec_list().get(currentPart).getPosition_id()) {
                        case 1:
                            ImageView positiveImg = (ImageView) rlPositiveTailor.getChildAt(currentPart);
                            Glide.with(NewCustomizeActivity.this)
                                    .load(Constant.HOST + itemList.get(position).getImg_c())
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(positiveImg);

                            imgTailorIcon.setVisibility(View.GONE);
                            animation.stop();
                            rvTailorButton.setVisibility(View.GONE);

                            break;
                        case 2:
                            ImageView backImg = (ImageView) rlBackTailor.getChildAt(currentPart);
                            Glide.with(NewCustomizeActivity.this)
                                    .load(Constant.HOST + itemList.get(position).getImg_c())
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(backImg);

//                        if (itemList.get(position).getName().contains("袖")) {
//                            if (itemList.get(position).getName().contains("法式")) {
//
//                                imgTailorIcon.setVisibility(View.VISIBLE);
//                                animation.start();
//                                rvTailorButton.setVisibility(View.GONE);
//                                ToastUtils.showToast(NewCustomizeActivity.this,
//                                        "您选择了法式袖，请挑选扣子");
//                            } else {
//                                imgTailorIcon.setVisibility(View.GONE);
//                                animation.stop();
//                                rvTailorButton.setVisibility(View.GONE);
//                                buttonName = null;
//                            }
//                        }

                            break;
                        case 3:
                            ImageView inSideImg = (ImageView) rlInsideTailor.getChildAt(currentPart);
                            Glide.with(NewCustomizeActivity.this)
                                    .load(Constant.HOST + itemList.get(position).getImg_c())
                                    .fitCenter()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(inSideImg);

                            imgTailorIcon.setVisibility(View.GONE);
                            animation.stop();
                            rvTailorButton.setVisibility(View.GONE);

                            break;
                    }

                    tvHeaderTitle.setText(itemList.get(position).getName());


                    isAllSelect();
                } else {
                    isLongPress = false;
                    if (imgLargeMaterial.getVisibility() == View.VISIBLE) {
                        imgLargeMaterial.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Glide.with(NewCustomizeActivity.this)
                        .load(Constant.HOST + itemList.get(position).getImg_b())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgLargeMaterial);
                isLongPress = true;
                if (imgLargeMaterial.getVisibility() == View.GONE) {
                    imgLargeMaterial.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });


    }

    /**
     * 不可搭配
     */
    private void noMatchSpec() {

        List<String> noMatchIds = new ArrayList<>();
        for (int k = 0; k < itemArray.size(); k++) {
            for (int i = 0; i < dataBean.getSpec_list().size(); i++) {
                for (int j = 0; j < dataBean.getSpec_list().get(i).getList().size(); j++) {
                    if (itemArray.valueAt(k) == dataBean.getSpec_list().get(i).getList().get(j).getId()) {
                        if (dataBean.getSpec_list().get(i).getList().get(j).getNotmatch_spec_ids() != null) {
                            String[] split = dataBean.getSpec_list().get(i).getList().get(j)
                                    .getNotmatch_spec_ids().split(",");
                            noMatchIds.addAll(Arrays.asList(split));
                            break;
                        }
                    }
                }
            }
        }

        itemList.clear();

        for (int j = 0; j < dataBean.getSpec_list().get(currentPart).getList().size(); j++) {
            if (!noMatchIds.contains(String.valueOf(dataBean.getSpec_list().get(currentPart)
                    .getList().get(j).getId()))) {
                itemList.add(dataBean.getSpec_list().get(currentPart).getList().get(j));
            }
        }

        itemAdapter.notifyDataSetChanged();

    }

    /**
     * 部件是否都选择
     *
     * @param
     */
    private void isAllSelect() {
        if (itemArray.size() == dataBean.getSpec_list().size()) {
            tvHeaderNext.setVisibility(View.VISIBLE);
            tvHeaderNext.setText("下一步");
        }
    }


    @OnClick({R.id.img_header_back, R.id.tv_header_next, R.id.rl_positive_tailor, R.id.img_load_error,
           R.id.img_tailor_icon, R.id.img_tailor_reset, R.id.img_tailor_guide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_header_next:
                nextStep();
                break;
            case R.id.rl_positive_tailor:
                imgLargeMaterial.setVisibility(View.GONE);
                break;
            case R.id.img_tailor_icon:
                selectButton();
                break;
            case R.id.img_tailor_reset:
                reselect();
                break;
            case R.id.img_tailor_guide:
                imgGuide.setVisibility(View.GONE);
                break;
            case R.id.img_load_error:
                initData();
                break;

        }
    }

    /**
     * 重置
     */
    private void reselect() {

        itemArray.clear();
        itemList.clear();
        itemAdapter.notifyDataSetChanged();

        buttonName = null;
        firstSelect = true;
        tvHeaderNext.setVisibility(View.GONE);
        rvTailorItem.setVisibility(View.GONE);
        rvTailorButton.setVisibility(View.GONE);
        imgReset.setVisibility(View.GONE);
        rlPositiveTailor.removeAllViews();
        rlBackTailor.removeAllViews();
        rlInsideTailor.removeAllViews();

        initView();
    }


    /**
     * 定制完成
     */
    private void nextStep() {

        Intent intent;
        Bundle bundle = new Bundle();
        if (classifyId == 1 || classifyId == 2) {
            intent = new Intent(this, EmbroideryActivity.class);
            bundle.putInt("classify_id", classifyId);
        } else {
            intent = new Intent(this, EmbroideryActivity.class);
        }

        CustomItemBean customItemBean = new CustomItemBean();
        customItemBean.setId(id);
        if (shop_id != null) {
            customItemBean.setShop_id(shop_id);
        }
        if (market_id != null) {
            customItemBean.setMarket_id(market_id);
        }
        customItemBean.setGoods_name(goodsName);
        customItemBean.setPrice(price);
        customItemBean.setImg_url(imgUrl);
        customItemBean.setPrice_type(priceType);
        customItemBean.setLog_id(logId);
        customItemBean.setGoods_time(goodsTime);
        customItemBean.setDingzhi_time(DateUtils.getCurrentTime() - enterTime);
        customItemBean.setIs_scan(0);


        //部件
        List<CustomItemBean.ItemBean> itemList = new ArrayList<>();
        StringBuilder sbIds = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();


        for (int i = 0; i < itemArray.size(); i++) {
            CustomItemBean.ItemBean itemBean = new CustomItemBean.ItemBean();
            int value = itemArray.valueAt(i);


            sbIds.append(value).append(",");

            for (int j = 0; j < dataBean.getSpec_list().size(); j++) {

                for (int k = 0; k < dataBean.getSpec_list().get(j).getList().size(); k++) {
                    if (value == dataBean.getSpec_list().get(j).getList().get(k).getId()) {
                        sbContent.append(dataBean.getSpec_list().get(j).getSpec_name())
                                .append(":")
                                .append(dataBean.getSpec_list().get(j).getList().get(k).getName())
                                .append(";");
                        itemBean.setPosition_id(dataBean.getSpec_list().get(j).getPosition_id());
                        itemBean.setImg(dataBean.getSpec_list().get(j).getList().get(k).getImg_c());
                        itemList.add(itemBean);
                        break;
                    }
                }
            }

        }


//        if (buttonName != null) {
//            sbContent.append("法式袖扣子:")
//                    .append(buttonName)
//                    .append(";");
//        }

        customItemBean.setDefault_img(default_img);
        customItemBean.setItemBean(itemList);
        customItemBean.setSpec_ids(sbIds.deleteCharAt(sbIds.length() - 1).toString());
        customItemBean.setSpec_content(sbContent.toString());
        bundle.putSerializable("tailor", customItemBean);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * 选择钮扣
     */
    private void selectButton() {
        animation.stop();
        imgTailorIcon.setVisibility(View.GONE);
        rvTailorButton.setVisibility(View.VISIBLE);
        rvTailorButton.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean.ChildBean> buttonAdapter = new
                CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean.ChildBean>(this,
                        R.layout.listitem_custom_parts, itemList.get(currentItem).getChild_list()) {
                    @Override
                    protected void convert(ViewHolder holder, TailorBean.DataBean.SpecListBean
                            .ListBean.ChildBean childBean, int position) {
                        Glide.with(NewCustomizeActivity.this)
                                .load(Constant.HOST + childBean.getImg_a())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into((ImageView) holder.getView(R.id.img_tailor_item));
                    }
                };

        rvTailorButton.setAdapter(buttonAdapter);
        rvTailorButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imgLargeMaterial.setVisibility(View.GONE);
                }
                return false;
            }
        });

        buttonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                if (!isLongPress) {
                    tvHeaderTitle.setText(itemList.get(currentItem).getChild_list().get(position).getName());
                    buttonName = itemList.get(currentItem).getChild_list().get(position).getName();
                } else {
                    isLongPress = false;
                    if (imgLargeMaterial.getVisibility() == View.VISIBLE) {
                        imgLargeMaterial.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Glide.with(NewCustomizeActivity.this)
                        .load(Constant.HOST + itemList.get(currentItem).getChild_list()
                                .get(position).getImg_b())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgLargeMaterial);
                isLongPress = true;
                if (imgLargeMaterial.getVisibility() == View.GONE) {
                    imgLargeMaterial.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

}
