package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.photopicker.PhotoPickerAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ImageEncodeUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.photopicker.RecyclerItemClickListener;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import cn.cloudworkshop.miaoding.view.CircleImageView;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 17:53
 * Email：1993911441@qq.com
 * Describe：订单评价
 */
public class EvaluateActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_goods_icon)
    CircleImageView imgGoods;
    @BindView(R.id.tv_name_goods)
    TextView tvGoodsName;
    @BindView(R.id.img_select_picture)
    ImageView imgSelectPicture;
    @BindView(R.id.rv_goods_picture)
    RecyclerView rvGoodsPicture;
    @BindView(R.id.tv_submit_evaluate)
    TextView tvSubmit;
    @BindView(R.id.et_evaluate)
    EditText etEvaluate;
    @BindView(R.id.view_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.tv_type_product)
    TextView tvGoodsType;

    private String orderId;
    private String cartId;
    private String goodsId;
    private String goodsName;
    private String goodsImg;
    private String goodsType;

    private PhotoPickerAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private String imgEncode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void initView() {
        tvHeaderTitle.setText("发表评论");
        Glide.with(getApplicationContext())
                .load(Constant.HOST + goodsImg)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(imgGoods);
        tvGoodsName.setText(goodsName);
        tvGoodsType.setText(goodsType);

        photoAdapter = new PhotoPickerAdapter(this, selectedPhotos);
        rvGoodsPicture.setLayoutManager(new LinearLayoutManager(EvaluateActivity.this,
                LinearLayoutManager.HORIZONTAL, false));
        rvGoodsPicture.setAdapter(photoAdapter);
        rvGoodsPicture.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PhotoPreview.builder()
                                .setPhotos(selectedPhotos)
                                .setCurrentItem(position)
                                .start(EvaluateActivity.this);
                    }
                }));

        loadingView.setIndicator(new BallSpinFadeLoaderIndicator());
        loadingView.setIndicatorColor(Color.GRAY);
    }

    private void getData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("order_id");
        cartId = intent.getStringExtra("cart_id");
        goodsId = intent.getStringExtra("goods_id");
        goodsImg = intent.getStringExtra("goods_img");
        goodsName = intent.getStringExtra("goods_name");
        goodsType = intent.getStringExtra("goods_type");
    }

    @OnClick({R.id.img_header_back, R.id.img_select_picture, R.id.tv_submit_evaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_select_picture:
                PhotoPicker.builder()
                        .setPhotoCount(5)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos)
                        .start(this);
                break;
            case R.id.tv_submit_evaluate:
                submitData();
                break;
        }
    }

    /**
     * 提交评论
     */
    private void submitData() {
        if (!TextUtils.isEmpty(etEvaluate.getText().toString().trim())) {
            loadingView.smoothToShow();
            tvSubmit.setEnabled(false);

            new Thread(myRunnable).start();
        } else {
            ToastUtils.showToast(this, "请填写评论");
        }
    }

    /**
     * 开启线程处理图片
     */
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if (selectedPhotos.size() != 0) {
                imgEncode = ImageEncodeUtils.encodeFile(selectedPhotos);
                handler.sendEmptyMessage(1);
            } else {
                handler.sendEmptyMessage(2);
            }
        }
    };


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Map<String, String> map = new HashMap<>();
            map.put("order_id", orderId);
            map.put("car_id", cartId);
            map.put("goods_id", goodsId);
            map.put("token", SharedPreferencesUtils.getStr(EvaluateActivity.this, "token"));
            map.put("content", etEvaluate.getText().toString().trim());
            if (msg.what == 1) {
                map.put("img_list", imgEncode);
            }

            OkHttpUtils.post()
                    .url(Constant.EVALUATE)
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            loadingView.smoothToHide();
                            ToastUtils.showToast(EvaluateActivity.this, "评价成功！");
                            finish();
                        }
                    });


        }
    };

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PhotoPicker.REQUEST_CODE || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            new PermissionUtils(this).showPermissionDialog("读写内存");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE
                || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
                photoAdapter.notifyDataSetChanged();

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(myRunnable);
    }
}
