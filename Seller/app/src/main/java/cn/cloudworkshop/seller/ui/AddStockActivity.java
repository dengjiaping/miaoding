package cn.cloudworkshop.seller.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.bean.StockBean;
import cn.cloudworkshop.seller.constant.Constant;
import cn.cloudworkshop.seller.utils.PermissionUtils;
import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import cn.cloudworkshop.seller.utils.ToastUtils;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author：binge on 2017-09-19 14:47
 * Email：1993911441@qq.com
 * Describe：新增库存
 */
public class AddStockActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.et_fabric_number)
    EditText etFabricNumber;
    @BindView(R.id.et_fabric_name)
    EditText etFabricName;
    @BindView(R.id.et_fabric_stock)
    EditText etFabricStock;
    @BindView(R.id.et_fabric_consume)
    EditText etFabricConsume;
    @BindView(R.id.img_fabric_large)
    ImageView imgFabricLarge;
    @BindView(R.id.rv_fabric_large)
    RecyclerView rvFabricLarge;
    @BindView(R.id.img_fabric_small)
    ImageView imgFabricSmall;
    @BindView(R.id.rv_fabric_small)
    RecyclerView rvFabricSmall;
    @BindView(R.id.et_fabric_info)
    EditText etFabricInfo;
    @BindView(R.id.tv_add_fabric)
    TextView tvAddFabric;
    @BindView(R.id.view_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.img_large_fabric)
    ImageView imgLargeFabric;
    @BindView(R.id.img_small_fabric)
    ImageView imgSmallFabric;


    private CommonAdapter photoAdapter1;
    private CommonAdapter photoAdapter2;
    private ArrayList<String> selectedPhotos1 = new ArrayList<>();
    private ArrayList<String> selectedPhotos2 = new ArrayList<>();
    private int currentItem = 0;


    //add:新增库存 alert:编辑库存
    private String type;
    private StockBean.ListBean.DataBean stockBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
        ButterKnife.bind(this);
        getData();
        initView();

    }

    private void getData() {
        type = getIntent().getExtras().getString("type");
    }

    private void initView() {
        loadingView.setIndicator(new BallSpinFadeLoaderIndicator());
        loadingView.setIndicatorColor(Color.GRAY);

        switch (type) {
            case "add":
                tvHeaderTitle.setText("新增库存");
                break;
            case "edit":
                tvHeaderTitle.setText("编辑库存");
                stockBean = (StockBean.ListBean.DataBean) getIntent().getExtras().getSerializable("edit");

                etFabricNumber.setText(stockBean.getPinhao());
                etFabricName.setText(stockBean.getName());
                etFabricStock.setText(stockBean.getStorage());
                etFabricConsume.setText(stockBean.getUse_up());
                etFabricInfo.setText(stockBean.getIntroduce());
                Glide.with(this)
                        .load(Constant.HOST + stockBean.getImg_max())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgLargeFabric);
                Glide.with(this)
                        .load(Constant.HOST + stockBean.getImg_min())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgSmallFabric);
                break;
        }
    }


    @OnClick({R.id.img_header_back, R.id.img_fabric_large, R.id.img_fabric_small, R.id.tv_add_fabric})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_fabric_large:
                photoAdapter1 = initPhotos(rvFabricLarge, selectedPhotos1, 1);
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos1)
                        .start(this);
                currentItem = 1;
                break;
            case R.id.img_fabric_small:
                photoAdapter2 = initPhotos(rvFabricSmall, selectedPhotos2, 2);
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos2)
                        .start(this);
                currentItem = 2;
                break;
            case R.id.tv_add_fabric:
                saveFabric();
                break;
        }
    }


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
            new PermissionUtils(this).showPermissionDialog("打开相机");
        }
    }


    private CommonAdapter<String> initPhotos(RecyclerView recyclerView, final ArrayList<String> selectedPhotos, final int index) {
        recyclerView.setLayoutManager(new LinearLayoutManager(AddStockActivity.this, LinearLayoutManager.HORIZONTAL, false));
        CommonAdapter<String> adapter = new CommonAdapter<String>(AddStockActivity.this, R.layout.listitem_picker_photo, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

                Uri uri = Uri.fromFile(new File(s));

                boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(AddStockActivity.this);

                if (canLoadImage) {
                    Glide.with(mContext)
                            .load(uri)
                            .centerCrop()
                            .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                            .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into((ImageView) holder.getView(R.id.img_picker_photo));
                }
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                PhotoPreview.builder()
                        .setPhotos(selectedPhotos)
                        .setCurrentItem(position)
                        .start(AddStockActivity.this);
                currentItem = index;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }


    /**
     * 保存面料
     */
    private void saveFabric() {
        if (TextUtils.isEmpty(etFabricNumber.getText().toString().trim())
                || TextUtils.isEmpty(etFabricName.getText().toString().trim())
                || TextUtils.isEmpty(etFabricStock.getText().toString().trim())
                || TextUtils.isEmpty(etFabricConsume.getText().toString().trim())
                || TextUtils.isEmpty(etFabricInfo.getText().toString().trim())
                || selectedPhotos1.size() == 0
                || selectedPhotos2.size() == 0) {
            ToastUtils.showToast(this, "请输入完整的面料信息");
        } else {
            loadingView.smoothToShow();
            tvAddFabric.setEnabled(false);
            File file1 = new File(selectedPhotos1.get(0));
            File file2 = new File(selectedPhotos2.get(0));

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("img_max", file1.getName(), RequestBody.create(MediaType.parse("image/png"), file1));
            builder.addFormDataPart("img_min", file2.getName(), RequestBody.create(MediaType.parse("image/png"), file2));

            builder.addFormDataPart("token", SharedPreferencesUtils.getStr(AddStockActivity.this, "token"));
            builder.addFormDataPart("pinhao", etFabricNumber.getText().toString().trim());
            builder.addFormDataPart("name", etFabricName.getText().toString().trim());
            builder.addFormDataPart("storage", etFabricStock.getText().toString().trim());
            builder.addFormDataPart("use_up", etFabricConsume.getText().toString().trim());
            builder.addFormDataPart("introduce", etFabricInfo.getText().toString().trim());

            if (type != null && type.equals("edit")) {
                builder.addFormDataPart("id", stockBean.getId() + "");
            }


            MultipartBody requestBody = builder.build();
            //构建请求
            Request request = new Request.Builder()
                    .url(Constant.ADD_STOCK)//地址
                    .post(requestBody)//添加请求体
                    .build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("code");
                        final String msg = jsonObject.getString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAddFabric.setEnabled(true);
                                loadingView.smoothToHide();
                                ToastUtils.showToast(AddStockActivity.this, msg);
                            }
                        });

                        if (code == 1) {
                            finish();
                        }

                    } catch (JSONException e) {

                    }
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE ||
                requestCode == PhotoPreview.REQUEST_CODE)) {
            if (currentItem == 1) {
                List<String> photos1 = null;
                if (data != null) {
                    photos1 = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                selectedPhotos1.clear();
                if (photos1 != null) {
                    selectedPhotos1.addAll(photos1);
                    photoAdapter1.notifyDataSetChanged();
                }
                imgLargeFabric.setVisibility(View.GONE);

            } else if (currentItem == 2) {
                List<String> photos2 = null;
                if (data != null) {
                    photos2 = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                selectedPhotos2.clear();
                if (photos2 != null) {
                    selectedPhotos2.addAll(photos2);
                    photoAdapter2.notifyDataSetChanged();
                }
                imgSmallFabric.setVisibility(View.GONE);

            }
        }
    }

}
