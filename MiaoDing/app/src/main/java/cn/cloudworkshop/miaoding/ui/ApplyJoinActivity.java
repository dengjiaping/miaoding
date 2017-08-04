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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.photopicker.PhotoPickerAdapter;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ImageEncodeUtils;
import cn.cloudworkshop.miaoding.utils.PhoneNumberUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.photopicker.RecyclerItemClickListener;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/24.
 * 提交入驻申请
 */

public class ApplyJoinActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.et_apply_name)
    EditText etApplyName;
    @BindView(R.id.et_apply_phone)
    EditText etApplyPhone;
    @BindView(R.id.et_apply_qq)
    EditText etApplyQq;
    @BindView(R.id.et_apply_email)
    EditText etApplyEmail;
    @BindView(R.id.img_apply_works)
    ImageView imgApplyWorks;
    @BindView(R.id.rv_apply_works)
    RecyclerView rvApplyWorks;
    @BindView(R.id.img_apply_company)
    ImageView imgApplyCompany;
    @BindView(R.id.rv_apply_company)
    RecyclerView rvApplyCompany;
    @BindView(R.id.tv_submit_apply)
    TextView tvSubmitApply;
    @BindView(R.id.view_loading)
    AVLoadingIndicatorView loadingView;

    private PhotoPickerAdapter photoAdapter1;
    private PhotoPickerAdapter photoAdapter2;
    private ArrayList<String> selectedPhotos1 = new ArrayList<>();
    private ArrayList<String> selectedPhotos2 = new ArrayList<>();
    private int currentClickId1 = -1;
    private int currentClickId2 = -2;
    private int id;

    private String imgList1;
    private String imgList2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_join);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);

        tvHeaderTitle.setText("申请入驻");
        loadingView.setIndicator(new BallSpinFadeLoaderIndicator());
        loadingView.setIndicatorColor(Color.GRAY);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OkHttpUtils.post()
                    .url(Constant.APPLY_JOIN)
                    .addParams("token", SharedPreferencesUtils.getStr(ApplyJoinActivity.this, "token"))
                    .addParams("name", etApplyName.getText().toString().trim())
                    .addParams("phone", etApplyPhone.getText().toString().trim())
                    .addParams("weixin", etApplyQq.getText().toString().trim())
                    .addParams("email", etApplyEmail.getText().toString().trim())
                    .addParams("img_list1", imgList1)
                    .addParams("img_list2", imgList2)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //监听申请入驻事件
                            MobclickAgent.onEvent(ApplyJoinActivity.this, "apply_join");
                            loadingView.smoothToHide();
                            Intent intent = new Intent(ApplyJoinActivity.this, AppointmentActivity.class);
                            intent.putExtra("type", "apply_join");
                            finish();
                            startActivity(intent);
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
        if (requestCode == PhotoPicker.REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            new PermissionUtils(this).showPermissionDialog("打开相机");
        }
    }


    private PhotoPickerAdapter initView(RecyclerView recyclerView, final ArrayList<String> selectedPhotos) {

        PhotoPickerAdapter photoAdapter = new PhotoPickerAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(ApplyJoinActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(photoAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(selectedPhotos)
                        .setCurrentItem(position)
                        .start(ApplyJoinActivity.this);
            }
        }));

        return photoAdapter;
    }

    @OnClick({R.id.img_header_back, R.id.img_apply_works, R.id.img_apply_company, R.id.tv_submit_apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.img_apply_works:
                photoAdapter1 = initView(rvApplyWorks, selectedPhotos1);
                PhotoPicker.builder()
                        .setPhotoCount(4)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos1)
                        .start(this);
                currentClickId1 = view.getId();
                break;
            case R.id.img_apply_company:
                photoAdapter2 = initView(rvApplyCompany, selectedPhotos2);
                PhotoPicker.builder()
                        .setPhotoCount(2)
                        .setShowCamera(true)
                        .setSelected(selectedPhotos2)
                        .start(this);
                currentClickId2 = view.getId();
                break;
            case R.id.tv_submit_apply:
                submitApply();
                break;
        }
        id = view.getId();
    }

    /**
     * 提交申请
     */
    private void submitApply() {
        if (TextUtils.isEmpty(etApplyName.getText().toString().trim())
                || TextUtils.isEmpty(etApplyQq.getText().toString().trim())
                || TextUtils.isEmpty(etApplyEmail.getText().toString().trim())
                || !PhoneNumberUtils.judgePhoneNumber(etApplyPhone.getText().toString().trim())
                || selectedPhotos1.size() == 0
                || selectedPhotos2.size() == 0) {
            ToastUtils.showToast(this, "请按要求填写个人信息");
        } else {
            loadingView.smoothToShow();
            tvSubmitApply.setEnabled(false);
            new Thread(myRunnable).start();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            if (id == currentClickId1) {
                List<String> photos1 = null;
                if (data != null) {
                    photos1 = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                selectedPhotos1.clear();
                if (photos1 != null) {
                    selectedPhotos1.addAll(photos1);
                    photoAdapter1.notifyDataSetChanged();
                }


            } else if (id == currentClickId2) {
                List<String> photos2 = null;
                if (data != null) {
                    photos2 = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                selectedPhotos2.clear();
                if (photos2 != null) {
                    selectedPhotos2.addAll(photos2);
                    photoAdapter2.notifyDataSetChanged();
                }
            }
        }
    }


    /**
     * 开启线程处理图片
     */
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            imgList1 = ImageEncodeUtils.encodeFile(selectedPhotos1);
            imgList2 = ImageEncodeUtils.encodeFile(selectedPhotos2);
            handler.sendEmptyMessage(1);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(myRunnable);
    }
}
