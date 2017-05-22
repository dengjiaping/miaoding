package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;

/**
 * Author：Libin on 2016/9/20 14:01
 * Email：1993911441@qq.com
 * Describe：衣物志详情
 */
public class ClothMaterialInfo extends BaseActivity {
    @BindView(R.id.img_material_back)
    ImageView imgMaterialBack;
    @BindView(R.id.img_material_share)
    ImageView imgMaterialShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloth_material);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_material_back)
    public void onClick() {
        finish();
    }
}
