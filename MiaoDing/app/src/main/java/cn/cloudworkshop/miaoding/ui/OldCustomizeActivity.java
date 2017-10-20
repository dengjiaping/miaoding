package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import cn.cloudworkshop.miaoding.utils.LoadErrorUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：Libin on 2016/8/31 09:24
 * Email：1993911441@qq.com
 * Describe：定制页面（当前版）
 */
public class OldCustomizeActivity extends BaseActivity {

    @BindView(R.id.rv_tailor_cloth)
    RecyclerView rvTailor;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.rgs_recommend_type)
    RadioGroup rgsRecommendType;
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

    //部件
    private List<String> typeList = new ArrayList<>();
    //部件名称 标题
    private List<String> titleList = new ArrayList<>();
    //正反面
    private List<Integer> positionList = new ArrayList<>();
    //小图
    private List<List<String>> itemList = new ArrayList<>();
    //长按图
    private List<List<String>> checkedList = new ArrayList<>();
    //大图
    private List<List<String>> largeList = new ArrayList<>();
    //部件名称
    private List<List<String>> nameList = new ArrayList<>();
    //部件id
    private List<List<Integer>> idList = new ArrayList<>();

    //所有部件是否选择完毕
    Map<Integer, Integer> checkedMap = new TreeMap<>();
    //部件名称
    Map<Integer, String> nameMap = new TreeMap<>();
    //部件id
    Map<Integer, Integer> idMap = new TreeMap<>();
    //部件类别
    Map<Integer, String> typeMap = new TreeMap<>();
    //部件正反面
    Map<Integer, String> positionMap = new TreeMap<>();


    private CommonAdapter<String> adapter;

    private float x1 = 0;
    private float x2 = 0;
    private TailorBean.DataBean dataBean;

    private String id;
    private String shop_id;
    private String market_id;
    private String goodsName;
    private String imgUrl;
    private String price;
    private String priceType;
    private int classifyId;
    private String logId;
    private long goodsTime;
    //进入页面时间
    private long enterTime;

    private AnimationDrawable animation;
    //选择部件位置
    private int index = 0;
    //选择子部件
    private int itemIndex;
    //纽扣名称
    private String buttonName;
    //首次选择
    private boolean firstSelect = true;
    private String default_img;


    private CommonAdapter<String> itemAdapter;
    private GuideBean guideBean;
    //首次进入引导
    private boolean isFirstEntry;

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
     * 加载视图
     */
    private void initView() {
        tvHeaderTitle.setText("选版型");
        animation = (AnimationDrawable) imgTailorIcon.getDrawable();
        ((RadioButton) rgsSelectOrientation.getChildAt(0)).setChecked(true);

        for (int i = 0; i < typeList.size(); i++) {
            ImageView img1 = new ImageView(this);
            img1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img2 = new ImageView(this);
            img2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView img3 = new ImageView(this);
            img3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            switch (positionList.get(i)) {
                case 1:
                    Glide.with(getApplicationContext())
                            .load(Constant.HOST + largeList.get(i).get(0))
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img1);
                    break;
                case 2:
                    Glide.with(getApplicationContext())
                            .load(Constant.HOST + largeList.get(i).get(0))
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(img2);
                    break;
                case 3:
                    if (positionList.contains(3)) {
                        rgsSelectOrientation.getChildAt(2).setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load(Constant.HOST + largeList.get(i).get(0))
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(img3);
                    }
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
                        x1 = motionEvent.getRawX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getRawX();
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

        rvTailor.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        adapter = new CommonAdapter<String>(OldCustomizeActivity.this, R.layout.listitem_custom_parts, typeList) {
            @Override
            protected void convert(ViewHolder holder, String imgUrl, int position) {
                Glide.with(OldCustomizeActivity.this)
                        .load(Constant.HOST + imgUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_tailor_item));

            }

        };
        rvTailor.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //选择正反面
                ((RadioButton) rgsSelectOrientation.getChildAt(positionList.get(position) - 1)).setChecked(true);
                tvHeaderTitle.setText(titleList.get(position));
                index = position;

                imgLargeMaterial.setVisibility(View.GONE);
                rvTailorButton.setVisibility(View.GONE);

                rvTailorItem.setVisibility(View.VISIBLE);
                rvTailorItem.setLayoutManager(new GridLayoutManager(OldCustomizeActivity.this, 1,
                        GridLayoutManager.HORIZONTAL, false));
                itemAdapter = new CommonAdapter<String>(OldCustomizeActivity.this,
                        R.layout.listitem_custom_parts, itemList.get(index)) {
                    @Override
                    protected void convert(ViewHolder holder, String imgUrl, int position) {
                        Glide.with(OldCustomizeActivity.this)
                                .load(Constant.HOST + imgUrl)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into((ImageView) holder.getView(R.id.img_tailor_item));

                    }
                };
                rvTailorItem.setAdapter(itemAdapter);

                if (isFirstEntry && guideBean.getData().getImg_urls().get(1) != null) {
                    imgGuide.setVisibility(View.VISIBLE);
                    Glide.with(OldCustomizeActivity.this)
                            .load(Constant.HOST + guideBean.getData().getImg_urls().get(1))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(imgGuide);
                    isFirstEntry = false;
                    SharedPreferencesUtils.saveBoolean(OldCustomizeActivity.this, "tailor_guide", false);
                }


                itemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                        if (firstSelect && index != 0) {
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

                        typeMap.put(index, titleList.get(index));
                        nameMap.put(index, nameList.get(index).get(position));
                        idMap.put(index, idList.get(index).get(position));

                        if (dataBean.getSpec_list().get(index).getList().get(position)
                                .getNotmatch_spec_ids() != null &&
                                dataBean.getSpec_list().get(index).getList().get(position)
                                        .getNotmatch_spec_ids().length() > 0) {
                            noMatchSpec();
                        }


                        CircleImageView img = (CircleImageView) rvTailor.findViewHolderForAdapterPosition(index)
                                .itemView.findViewById(R.id.img_tailor_item);
                        Glide.with(OldCustomizeActivity.this)
                                .load(Constant.HOST + itemList.get(index).get(position))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(img);

                        View itemBg = rvTailor.findViewHolderForAdapterPosition(index).itemView
                                .findViewById(R.id.view_tailor_item);
                        itemBg.setVisibility(View.VISIBLE);

                        if (titleList.get(index).contains("面料")) {
                            for (int i = 0; i < dataBean.getSpec_list().get(index).getList().size(); i++) {
                                if (dataBean.getSpec_list().get(index).getList().get(i).getId() == idMap.get(index)) {
                                    default_img = dataBean.getSpec_list().get(index).getList().get(i).getMianliao_img();
                                    break;
                                }
                            }
                        }

                        //选择正反面
                        ((RadioButton) rgsSelectOrientation.getChildAt(positionList.get(index) - 1)).setChecked(true);

                        switch (positionList.get(index)) {
                            case 1:
                                ImageView positiveImg = (ImageView) rlPositiveTailor.getChildAt(index);
                                Glide.with(OldCustomizeActivity.this)
                                        .load(Constant.HOST + largeList.get(index).get(position))
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(positiveImg);

                                positionMap.put(index, largeList.get(index).get(position));

                                imgTailorIcon.setVisibility(View.GONE);
                                animation.stop();
                                rvTailorButton.setVisibility(View.GONE);

                                break;
                            case 2:
                                ImageView backImg = (ImageView) rlBackTailor.getChildAt(index);
                                Glide.with(OldCustomizeActivity.this)
                                        .load(Constant.HOST + largeList.get(index).get(position))
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(backImg);
                                positionMap.put(index, largeList.get(index).get(position));

//                                if (nameList.get(index).get(position).contains("袖")) {
//                                    if (nameList.get(index).get(position).contains("法式")) {
//                                        for (int i = 0; i < dataBean.getSpec_list().get(index).getList().size(); i++) {
//                                            if (dataBean.getSpec_list().get(index).getList().get(i).getId() == idMap.get(index)) {
//                                                itemIndex = i;
//                                            }
//                                        }

//                                        imgTailorIcon.setVisibility(View.VISIBLE);
//                                        animation.start();
//                                        rvTailorButton.setVisibility(View.GONE);
//                                        ToastUtils.showToast(OldCustomizeActivity.this, "您选择了法式袖，请挑选扣子");
//                                    } else {
//                                        imgTailorIcon.setVisibility(View.GONE);
//                                        animation.stop();
//                                        rvTailorButton.setVisibility(View.GONE);
//                                        buttonName = "";
//                                    }
//                                }

                                break;
                            case 3:
                                ImageView inSideImg = (ImageView) rlInsideTailor.getChildAt(index);
                                Glide.with(OldCustomizeActivity.this)
                                        .load(Constant.HOST + largeList.get(index).get(position))
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(inSideImg);
                                positionMap.put(index, largeList.get(index).get(position));

                                imgTailorIcon.setVisibility(View.GONE);
                                animation.stop();
                                rvTailorButton.setVisibility(View.GONE);

                                break;
                        }

                        ToastUtils.showToast(OldCustomizeActivity.this, nameList.get(index).get(position));

                        if (imgLargeMaterial.getVisibility() == View.VISIBLE) {
                            imgLargeMaterial.setVisibility(View.GONE);
                        }

                        checkedMap.put(index, position);

                        isAllSelect(checkedMap);

                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        Glide.with(OldCustomizeActivity.this)
                                .load(Constant.HOST + checkedList.get(index).get(position))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(imgLargeMaterial);
                        if (imgLargeMaterial.getVisibility() == View.GONE) {
                            imgLargeMaterial.setVisibility(View.VISIBLE);

                        }
                        return false;
                    }
                });


                rvTailorItem.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            imgLargeMaterial.setVisibility(View.GONE);
                        }
                        return true;
                    }
                });


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        for (int i = 0; i < dataBean.getSpec_templets_recommend().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setButtonDrawable(null);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(dataBean.getSpec_templets_recommend().get(i).getName());
            radioButton.setTextSize(14);
            radioButton.setPadding(10, 10, 10, 10);
            radioButton.setEms(1);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setBackgroundResource(R.drawable.rgs_customize_color);
            radioButton.setTextColor(ContextCompat.getColor(this, R.color.rgs_customize_position));

            rgsRecommendType.addView(radioButton);
        }


        rgsRecommendType.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        imgLargeMaterial.setVisibility(View.GONE);
                        rvTailorItem.setVisibility(View.GONE);
                        rvTailorButton.setVisibility(View.GONE);
                        imgTailorIcon.setVisibility(View.GONE);
                        animation.stop();

                        for (int j = 0; j < rgsRecommendType.getChildCount(); j++) {
                            RadioButton rBtn = (RadioButton) radioGroup.getChildAt(j);
                            if (rBtn.getId() == i) {
                                recommendType(j);
                                tvHeaderNext.setVisibility(View.VISIBLE);
                                tvHeaderNext.setText("下一步");
                            }
                        }
                    }
                }
        );
    }

    /**
     * 不可搭配
     */
    private void noMatchSpec() {

        itemList.clear();
        idList.clear();
        nameList.clear();
        checkedList.clear();
        largeList.clear();

        List<String> noMatchIds = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : idMap.entrySet()) {
            for (int i = 0; i < dataBean.getSpec_list().size(); i++) {
                for (int j = 0; j < dataBean.getSpec_list().get(i).getList().size(); j++) {
                    if (entry.getValue() == dataBean.getSpec_list().get(i).getList().get(j).getId()) {
                        if (dataBean.getSpec_list().get(i).getList().get(j).getNotmatch_spec_ids() != null) {
                            String[] split = dataBean.getSpec_list().get(i).getList().get(j)
                                    .getNotmatch_spec_ids().split(",");
                            for (int k = 0; k < split.length; k++) {
                                noMatchIds.add(split[k]);
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < dataBean.getSpec_list().size(); i++) {
            List<String> imgItem = new ArrayList<>();
            List<String> imgCheck = new ArrayList<>();
            List<String> imgLarge = new ArrayList<>();
            List<Integer> goodsId = new ArrayList<>();
            List<String> goodsName = new ArrayList<>();
            for (int j = 0; j < dataBean.getSpec_list().get(i).getList().size(); j++) {
                if (!noMatchIds.contains(String.valueOf(dataBean.getSpec_list().get(i)
                        .getList().get(j).getId()))) {
                    imgItem.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_a());
                    imgCheck.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_b());
                    imgLarge.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_c());
                    goodsId.add(dataBean.getSpec_list().get(i).getList().get(j).getId());
                    goodsName.add(dataBean.getSpec_list().get(i).getList().get(j).getName());
                }
            }
            itemList.add(imgItem);
            checkedList.add(imgCheck);
            largeList.add(imgLarge);
            idList.add(goodsId);
            nameList.add(goodsName);
        }
    }


    /**
     * 推荐款
     */
    private void recommendType(int k) {
        typeList.clear();
        for (int i = 0; i < dataBean.getSpec_templets_recommend().get(k).getList().size(); i++) {
            typeList.add(dataBean.getSpec_templets_recommend().get(k).getList().get(i).getImg_a());
            nameMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getName());
            idMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getId());
            typeMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getSpec_name());

            switch (dataBean.getSpec_templets_recommend().get(k).getList().get(i).getPosition_id()) {
                case 1:
                    ImageView positiveImg = (ImageView) rlPositiveTailor.getChildAt(i);
                    Glide.with(OldCustomizeActivity.this)
                            .load(Constant.HOST + dataBean.getSpec_templets_recommend()
                                    .get(k).getList().get(i).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(positiveImg);
                    positionMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getImg_c());
                    break;
                case 2:
                    ImageView backImg = (ImageView) rlBackTailor.getChildAt(i);
                    Glide.with(OldCustomizeActivity.this)
                            .load(Constant.HOST + dataBean.getSpec_templets_recommend().get(k)
                                    .getList().get(i).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(backImg);
                    positionMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getImg_c());
//                    if (dataBean.getSpec_templets_recommend().get(k).getList().get(i).getName().contains("袖")) {
//                        if (dataBean.getSpec_templets_recommend().get(k).getList().get(i).getName().contains("法式")) {
//                            ToastUtils.showToast(this, "您选择了法式袖，请挑选扣子");
//                            imgTailorIcon.setVisibility(View.VISIBLE);
//                            rvTailorButton.setVisibility(View.GONE);
//                            animation.start();
//                        } else {
//                            imgTailorIcon.setVisibility(View.GONE);
//                            animation.stop();
//                            rvTailorButton.setVisibility(View.GONE);
//                            buttonName = "";
//                        }
//                    }


                    break;
                case 3:
                    ImageView insideImg = (ImageView) rlInsideTailor.getChildAt(i);
                    Glide.with(OldCustomizeActivity.this)
                            .load(Constant.HOST + dataBean.getSpec_templets_recommend().get(k)
                                    .getList().get(i).getImg_c())
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(insideImg);
                    positionMap.put(i, dataBean.getSpec_templets_recommend().get(k).getList().get(i).getImg_c());
                    break;
            }
        }

        adapter.notifyDataSetChanged();
    }


    /**
     * 部件是否都选择
     *
     * @param map
     */
    private void isAllSelect(Map<Integer, Integer> map) {
        if (map.size() == itemList.size()) {
            tvHeaderNext.setVisibility(View.VISIBLE);
            tvHeaderNext.setText("下一步");
        }
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
                                Glide.with(OldCustomizeActivity.this)
                                        .load(Constant.HOST + guideBean.getData().getImg_urls().get(0))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .into(imgGuide);
                            }
                        }
                    });
        }
        OkHttpUtils.get()
                .url(Constant.TAILOR_INFO)
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
                        loadData();
                    }
                });

    }

    private void loadData() {
        for (int i = 0; i < dataBean.getSpec_list().size(); i++) {
            typeList.add(dataBean.getSpec_list().get(i).getImg());
            titleList.add(dataBean.getSpec_list().get(i).getSpec_name());
            positionList.add(dataBean.getSpec_list().get(i).getPosition_id());
            List<String> imgItem = new ArrayList<>();
            List<String> imgCheck = new ArrayList<>();
            List<String> imgLarge = new ArrayList<>();
            List<Integer> goodsId = new ArrayList<>();
            List<String> goodsName = new ArrayList<>();
            for (int j = 0; j < dataBean.getSpec_list().get(i).getList().size(); j++) {
                imgItem.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_a());
                imgCheck.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_b());
                imgLarge.add(dataBean.getSpec_list().get(i).getList().get(j).getImg_c());
                goodsId.add(dataBean.getSpec_list().get(i).getList().get(j).getId());
                goodsName.add(dataBean.getSpec_list().get(i).getList().get(j).getName());
            }
            itemList.add(imgItem);
            checkedList.add(imgCheck);
            largeList.add(imgLarge);
            idList.add(goodsId);
            nameList.add(goodsName);

        }
        initView();
    }

    @OnClick({R.id.img_header_back, R.id.tv_header_next, R.id.rl_positive_tailor,R.id.img_load_error,
            R.id.img_large_material, R.id.img_tailor_icon, R.id.img_tailor_reset, R.id.img_tailor_guide})
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
            case R.id.img_large_material:
                imgLargeMaterial.setVisibility(View.GONE);
                break;
            case R.id.img_tailor_icon:
                selectButton();
                break;
            case R.id.img_tailor_reset:
                resetTailor();
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
    private void resetTailor() {
        typeList.clear();
        titleList.clear();
        positionList.clear();
        itemList.clear();
        checkedList.clear();
        largeList.clear();
        nameList.clear();
        idList.clear();
        checkedMap.clear();
        nameMap.clear();
        idMap.clear();
        typeMap.clear();
        positionMap.clear();

        buttonName = "";
        firstSelect = true;
        tvHeaderNext.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        rvTailorItem.setVisibility(View.GONE);
        rvTailorButton.setVisibility(View.GONE);
        imgReset.setVisibility(View.GONE);
        rlPositiveTailor.removeAllViews();
        rlBackTailor.removeAllViews();
        rlInsideTailor.removeAllViews();

        loadData();
    }


    /**
     * 定制完成
     */
    private void nextStep() {

        if (idMap.size() > 0 && typeMap.size() > 0 && nameMap.size() > 0 && positionMap.size() > 0) {
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

            //是否选择法式袖扣
//            boolean isSelectButton = false;
//
//            for (Map.Entry<Integer, String> entry : nameMap.entrySet()) {
//                if (entry.getValue().contains("法式") && !buttonName.equals("")) {
//                    isSelectButton = true;
//                }
//            }

            //部件
            List<CustomItemBean.ItemBean> itemList = new ArrayList<>();
            StringBuilder sbIds = new StringBuilder();
            StringBuilder sbContent = new StringBuilder();

            for (Map.Entry<Integer, Integer> entry : idMap.entrySet()) {
                sbIds.append(entry.getValue()).append(",");
            }

            for (int i = 0; i < nameList.size(); i++) {
                sbContent.append(typeMap.get(i))
                        .append(":")
                        .append(nameMap.get(i))
                        .append(";");
            }

            for (int i = 0; i < positionList.size(); i++) {
                CustomItemBean.ItemBean itemBean = new CustomItemBean.ItemBean();
                itemBean.setImg(positionMap.get(i));
                itemBean.setPosition_id(positionList.get(i));
                itemList.add(itemBean);
            }

//
//            if (isSelectButton) {
//                sbContent.append("法式袖扣子:")
//                        .append(buttonName)
//                        .append(";");
//            }

            customItemBean.setDefault_img(default_img);
            customItemBean.setItemBean(itemList);
            customItemBean.setSpec_ids(sbIds.deleteCharAt(sbIds.length() - 1).toString());
            customItemBean.setSpec_content(sbContent.toString());
            bundle.putSerializable("tailor", customItemBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 选择纽扣
     */
    private void selectButton() {
        animation.stop();
        imgTailorIcon.setVisibility(View.GONE);
        rvTailorButton.setVisibility(View.VISIBLE);
        rvTailorButton.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean.ChildBean> buttonAdapter = new
                CommonAdapter<TailorBean.DataBean.SpecListBean.ListBean.ChildBean>(this,
                        R.layout.listitem_custom_parts, dataBean.getSpec_list()
                        .get(index).getList().get(itemIndex).getChild_list()) {
                    @Override
                    protected void convert(ViewHolder holder, TailorBean.DataBean.SpecListBean
                            .ListBean.ChildBean childBean, int position) {
                        Glide.with(OldCustomizeActivity.this)
                                .load(Constant.HOST + dataBean.getSpec_list().get(index).getList()
                                        .get(itemIndex).getChild_list().get(position).getImg_a())
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
                if (imgLargeMaterial.getVisibility() == View.VISIBLE) {
                    imgLargeMaterial.setVisibility(View.GONE);
                }

                ToastUtils.showToast(OldCustomizeActivity.this, dataBean.getSpec_list().get(index).getList()
                        .get(itemIndex).getChild_list().get(position).getName());
                buttonName = dataBean.getSpec_list().get(index).getList().get(itemIndex)
                        .getChild_list().get(position).getName();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                Glide.with(OldCustomizeActivity.this)
                        .load(Constant.HOST + dataBean.getSpec_list().get(index).getList()
                                .get(itemIndex).getChild_list().get(position).getImg_b())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgLargeMaterial);
                if (imgLargeMaterial.getVisibility() == View.GONE) {
                    imgLargeMaterial.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

}