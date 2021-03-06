package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.CustomItemBean;
import cn.cloudworkshop.miaoding.bean.EmbroideryBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ActivityManagerUtils;
import cn.cloudworkshop.miaoding.utils.CharacterUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：Libin on 2016/10/13 11:47
 * Email：1993911441@qq.com
 * Describe：个性化定制界面
 */

public class EmbroideryActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.rv_embroidery_position)
    RecyclerView rvEmbroideryPosition;
    @BindView(R.id.rv_embroidery_font)
    RecyclerView rvEmbroideryFont;
    @BindView(R.id.et_embroidery_content)
    EditText etEmbroideryContent;
    @BindView(R.id.rv_embroidery_color)
    RecyclerView rvEmbroideryColor;
    @BindView(R.id.scroll_embroidery)
    ScrollView scrollView;
    @BindView(R.id.tv_confirm_embroidery)
    TextView tvPreview;
    @BindView(R.id.img_load_error)
    ImageView imgLoadError;
    private EmbroideryBean embroideryBean;
    //当前绣花位置
    private int flowerPosition = 0;
    //当前绣花颜色
    private int flowerColor = 0;
    //当前字体颜色
    private int flowerFont = 0;
    //商品分类
    private int classifyId;

    private CustomItemBean customItemBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embroidery);
        ButterKnife.bind(this);

        ActivityManagerUtils.getInstance().addActivity(this);
        tvHeaderTitle.setText("个性绣花");
        tvHeaderNext.setVisibility(View.VISIBLE);
        tvHeaderNext.setText("跳过");
        getData();
        initData();
    }

    /**
     * 商品信息
     */
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        classifyId = bundle.getInt("classify_id");
        customItemBean = (CustomItemBean) bundle.getSerializable("tailor");
    }

    /**
     * 获取网络数据
     */
    private void initData() {
        OkHttpUtils.get()
                .url(Constant.EMBROIDERY_CUSTOMIZE)
                .addParams("goods_id", customItemBean.getId())
                .addParams("phone_type", "6")
                .addParams("price_type", customItemBean.getPrice_type())
                .addParams("classify_id", String.valueOf(classifyId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        imgLoadError.setBackgroundColor(Color.WHITE);
                        imgLoadError.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        imgLoadError.setVisibility(View.GONE);
                        embroideryBean = GsonUtils.jsonToBean(response, EmbroideryBean.class);
                        if (embroideryBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }


    /**
     * 加载视图
     */
    private void initView() {
        tvPreview.setEnabled(false);
        //绣花位置
        rvEmbroideryPosition.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final CommonAdapter<EmbroideryBean.DataBean.PositionBean> positionAdapter
                = new CommonAdapter<EmbroideryBean.DataBean.PositionBean>(EmbroideryActivity.this,
                R.layout.listitem_embroidery_position, embroideryBean.getData().getPosition()) {
            @Override
            protected void convert(ViewHolder holder, EmbroideryBean.DataBean.PositionBean positionBean, int position) {
                CircleImageView imgPosition = holder.getView(R.id.img_embroidery_position);
                TextView tvPosition = holder.getView(R.id.tv_embroidery_position);
                Glide.with(EmbroideryActivity.this)
                        .load(Constant.IMG_HOST + positionBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgPosition);
                tvPosition.setText(positionBean.getName());
                isAllSelect();
                if (flowerPosition == position) {
                    tvPosition.setTextColor(ContextCompat.getColor(EmbroideryActivity.this,
                            R.color.dark_gray_22));
                    imgPosition.setBorderColor(ContextCompat.getColor(EmbroideryActivity.this,
                            R.color.light_gray_3d));
                } else {
                    tvPosition.setTextColor(ContextCompat.getColor(EmbroideryActivity.this,
                            R.color.light_gray_7a));
                    imgPosition.setBorderColor(ContextCompat.getColor(EmbroideryActivity.this,
                            R.color.light_gray_97));
                }
            }
        };

        rvEmbroideryPosition.setAdapter(positionAdapter);

        positionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                flowerPosition = holder.getLayoutPosition();
                positionAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //绣花颜色
        rvEmbroideryColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final CommonAdapter<EmbroideryBean.DataBean.ColorBean> colorAdapter = new
                CommonAdapter<EmbroideryBean.DataBean.ColorBean>(EmbroideryActivity.this,
                R.layout.listitem_embroidery_color, embroideryBean.getData().getColor()) {
            @Override
            protected void convert(ViewHolder holder, EmbroideryBean.DataBean.ColorBean colorBean, int position) {
                CircleImageView imgColor = holder.getView(R.id.img_embroidery_color);
                CircleImageView imgMask = holder.getView(R.id.img_color_mask);
                Glide.with(EmbroideryActivity.this)
                        .load(Constant.IMG_HOST + colorBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgColor);
                isAllSelect();
                if (flowerColor == position) {
                    imgMask.setVisibility(View.VISIBLE);
                } else {
                    imgMask.setVisibility(View.GONE);
                }

            }

        };
        rvEmbroideryColor.setAdapter(colorAdapter);

        colorAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ToastUtils.showToast(EmbroideryActivity.this, embroideryBean.getData().getColor()
                        .get(position).getName());
                flowerColor = holder.getLayoutPosition();
                colorAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //绣花字体
        rvEmbroideryFont.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        final CommonAdapter<EmbroideryBean.DataBean.FontBean> fontAdapter = new CommonAdapter
                <EmbroideryBean.DataBean.FontBean>(EmbroideryActivity.this,
                R.layout.listitem_embroidery_font, embroideryBean.getData().getFont()) {
            @Override
            protected void convert(ViewHolder holder, EmbroideryBean.DataBean.FontBean positionBean, int position) {

                ImageView imgFont = holder.getView(R.id.img_embroidery_font);
                TextView tvFont = holder.getView(R.id.tv_embroidery_font);
                Glide.with(EmbroideryActivity.this)
                        .load(Constant.IMG_HOST + positionBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgFont);
                tvFont.setText(positionBean.getName());

                isAllSelect();
                if (position == flowerFont) {
                    judgeInputWords();
                    tvFont.setTextColor(ContextCompat.getColor(EmbroideryActivity.this, R.color.dark_gray_22));
                    imgFont.setBackgroundResource(R.drawable.bound_corner_3d);
                } else {
                    tvFont.setTextColor(ContextCompat.getColor(EmbroideryActivity.this, R.color.light_gray_7a));
                    imgFont.setBackgroundResource(R.drawable.bound_corner_aa);
                }

            }
        };
        rvEmbroideryFont.setAdapter(fontAdapter);
        fontAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                flowerFont = holder.getLayoutPosition();

                if (position == 0) {
                    etEmbroideryContent.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    etEmbroideryContent.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                fontAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        etEmbroideryContent.setFilters(new InputFilter[]{filter});


        etEmbroideryContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollScrollView();
                return false;
            }
        });

        etEmbroideryContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                selectionStart = etEmbroideryContent.getSelectionStart();
                selectionEnd = etEmbroideryContent.getSelectionEnd();
                if (temp.length() > 12) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etEmbroideryContent.setText(editable);
                    etEmbroideryContent.setSelection(tempSelection);//设置光标在最后
                }

                judgeInputWords();
                isAllSelect();
            }
        });

    }

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.equals(" ") || source.toString().contentEquals("\n")) return "";
            else return null;
        }
    };


    /**
     * 输入中英文判断
     */
    private void judgeInputWords() {
        if (etEmbroideryContent.getText().toString().trim().length() > 0) {
            //1：英文
            if (embroideryBean.getData().getFont().get(flowerFont).getIs_english() == 1) {
                if (!CharacterUtils.inputEnglish(EmbroideryActivity.this,
                        etEmbroideryContent.getText().toString().trim())) {
                    etEmbroideryContent.setText(null);
                }
            } else {
                if (!CharacterUtils.inputChinese(EmbroideryActivity.this,
                        etEmbroideryContent.getText().toString().trim())) {
                    etEmbroideryContent.setText(null);
                }
            }
        }
    }

    /**
     * 所有部件是否完善
     */
    private void isAllSelect() {
        boolean flag = embroideryBean.getData().getPosition().get(flowerPosition).getName() != null
                && embroideryBean.getData().getColor().get(flowerColor).getName() != null
                && embroideryBean.getData().getFont().get(flowerFont).getName() != null
                && etEmbroideryContent.getText().toString().trim().length() > 0;
        tvPreview.setEnabled(flag);
    }

    /**
     * 使ScrollView指向底部
     */
    private void scrollScrollView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, scrollView.getHeight());
            }
        }, 300);
    }

    @OnClick({R.id.img_header_back, R.id.tv_header_next, R.id.tv_confirm_embroidery,R.id.img_load_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_header_next:
                nextStep(false);
                break;
            case R.id.tv_confirm_embroidery:
                nextStep(true);
                break;
            case R.id.img_load_error:
                initData();
                break;

        }
    }


    /**
     * 预览、跳过
     *
     * @param isEmbroidery 是否绣花
     */
    private void nextStep(boolean isEmbroidery) {
        Intent intent = new Intent(this, CustomResultActivity.class);
        Bundle bundle = new Bundle();
        if (isEmbroidery) {
            //个性绣花
            String sb = "位置:" + embroideryBean.getData().getPosition().get(flowerPosition).getName()
                    + ";颜色:" + embroideryBean.getData().getColor().get(flowerColor).getName()
                    + ";字体:" + embroideryBean.getData().getFont().get(flowerFont).getName()
                    + ";文字:" + etEmbroideryContent.getText().toString() + ";";
            customItemBean.setDiy_contet(sb);
        }

        bundle.putSerializable("tailor", customItemBean);

        intent.putExtras(bundle);
        startActivity(intent);

    }


}
