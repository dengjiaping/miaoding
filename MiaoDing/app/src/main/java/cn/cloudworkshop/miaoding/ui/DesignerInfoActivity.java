package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import cn.cloudworkshop.miaoding.bean.DesignerInfoBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.MyLinearLayoutManager;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-25 11:07
 * Email：1993911441@qq.com
 * Describe：设计师详情
 */
public class DesignerInfoActivity extends BaseActivity {
    @BindView(R.id.img_designer_back)
    ImageView imgBack;
    @BindView(R.id.img_share_designer)
    ImageView imgShare;
    @BindView(R.id.img_designer_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_designer_nickname)
    TextView tvNickname;
    @BindView(R.id.rv_designer_works)
    RecyclerView rvWorks;
    @BindView(R.id.tv_works_num)
    TextView tvWorksNum;
    @BindView(R.id.tv_sale_num)
    TextView tvSaleNum;
    @BindView(R.id.tv_collection_num)
    TextView tvCollectNum;
    @BindView(R.id.img_null_works)
    ImageView imgNullWorks;

    //设计师id
    private String id;
    private DesignerInfoBean designerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_info);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.DESIGNER_DETAILS)
                .addParams("uid", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        designerBean = GsonUtils.jsonToBean(response, DesignerInfoBean.class);
                        if (designerBean.getData() != null) {
                            initView();
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {
        Glide.with(getApplicationContext())
                .load(Constant.HOST + designerBean.getData().getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgHead);
        tvNickname.setTypeface(DisplayUtils.setTextType(this));
        tvNickname.setText(designerBean.getData().getName());
        tvWorksNum.setTypeface(DisplayUtils.setTextType(this));
        tvWorksNum.setText(designerBean.getData().getGoods_num() + "");
        tvSaleNum.setTypeface(DisplayUtils.setTextType(this));
        tvSaleNum.setText(designerBean.getData().getSale_num() + "");
        tvCollectNum.setTypeface(DisplayUtils.setTextType(this));
        tvCollectNum.setText(designerBean.getData().getCollect_num() + "");

        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(this);
        linearLayoutManager.setScrollEnabled(false);
        rvWorks.setLayoutManager(linearLayoutManager);

        if (designerBean.getData().getGoods_list() != null && designerBean.getData().getGoods_list().size() > 0) {
            CommonAdapter<DesignerInfoBean.DataBean.GoodsListBean> adapter = new CommonAdapter<DesignerInfoBean
                    .DataBean.GoodsListBean>(DesignerInfoActivity.this, R.layout.listitem_designer_works
                    , designerBean.getData().getGoods_list()) {
                @Override
                protected void convert(ViewHolder holder, DesignerInfoBean.DataBean.GoodsListBean goodsListBean, int position) {
                    Glide.with(DesignerInfoActivity.this)
                            .load(Constant.HOST + designerBean.getData().getAvatar())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into((ImageView) holder.getView(R.id.img_designer_avatar));

                    TextView tvDesigner = holder.getView(R.id.tv_designer);
                    tvDesigner.setTypeface(DisplayUtils.setTextType(DesignerInfoActivity.this));
                    tvDesigner.setText(designerBean.getData().getName());

                    holder.setText(R.id.tv_sale_time, goodsListBean.getC_time());

                    TextView tvWorks = holder.getView(R.id.tv_sale_works);
                    tvWorks.setTypeface(DisplayUtils.setTextType(DesignerInfoActivity.this));
                    tvWorks.setText(goodsListBean.getName());
                    Glide.with(DesignerInfoActivity.this)
                            .load(Constant.HOST + goodsListBean.getThumb())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into((ImageView) holder.getView(R.id.img_sale_works));

                }
            };
            rvWorks.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(DesignerInfoActivity.this, WorksDetailActivity.class);
                    intent.putExtra("id", String.valueOf(designerBean.getData().getGoods_list().get(position).getId()));
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

        } else {
            imgNullWorks.setVisibility(View.VISIBLE);
        }


    }

    private void getData() {
        id = getIntent().getStringExtra("id");
    }

    @OnClick({R.id.img_designer_back, R.id.img_share_designer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_designer_back:
                finish();
                break;
            case R.id.img_share_designer:
                ShareUtils.showShare(this, Constant.HOST + designerBean.getData().getAvatar(),
                        designerBean.getData().getName(), designerBean.getData().getContent(),
                        Constant.DESIGNER_SHARE + "?id=" + id + "&token=" +
                                SharedPreferencesUtils.getString(this, "token"));
                break;
        }
    }
}
