package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.FlipAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.DesignerWorksBean;
import cn.cloudworkshop.miaoding.bean.NewDesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.flipview.FlipView;
import cn.cloudworkshop.miaoding.utils.DialogUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-04-21 10:24
 * Email：1993911441@qq.com
 * Describe：腔调成品
 */
public class DesignerWorksFragment extends BaseFragment {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.flip_view)
    FlipView flipView;
    Unbinder unbinder;
    @BindView(R.id.view_header_line)
    View viewHeaderLine;

    private List<DesignerWorksBean.DataBeanX.DataBean> designerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer_works, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {
        tvHeaderTitle.setText("腔调");
        imgHeaderBack.setVisibility(View.GONE);
        viewHeaderLine.setVisibility(View.GONE);

        OkHttpUtils.get()
                .url(Constant.DESIGNER_WORKS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        DialogUtils.showDialog(getActivity(), new DialogUtils.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                initData();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DesignerWorksBean designerBean = GsonUtils.jsonToBean(response, DesignerWorksBean.class);
                        if (designerBean.getData().getData() != null) {
                            designerList.addAll(designerBean.getData().getData());
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        FlipAdapter adapter = new FlipAdapter(getActivity(), designerList);
        flipView.setAdapter(adapter);
        if (designerList.size() > 1) {
            flipView.flipTo(designerList.size() - 1);
        }

    }

    public static DesignerWorksFragment newInstance() {
        Bundle args = new Bundle();
        DesignerWorksFragment fragment = new DesignerWorksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
