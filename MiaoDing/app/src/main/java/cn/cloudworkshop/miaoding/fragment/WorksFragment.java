package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.DesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.WorksDetailActivity;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-06-08 11:29
 * Email：1993911441@qq.com
 * Describe：
 */
public class WorksFragment extends BaseFragment {
    @BindView(R.id.rv_member_rights)
    RecyclerView rvWorks;
    private Unbinder unbinder;
    private List<DesignWorksBean.DataBean.ItemBean> worksList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_member, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    /**
     * 加载数据
     */
    private void initData() {

        OkHttpUtils.get()
                .url(Constant.DESIGNER_WORKS)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DesignWorksBean worksBean = GsonUtils.jsonToBean(response, DesignWorksBean.class);
                        if (worksBean.getData().getData() != null) {
                            worksList.addAll(worksBean.getData().getData());
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        rvWorks.setLayoutManager(new LinearLayoutManager(getParentFragment().getActivity()));
        CommonAdapter<DesignWorksBean.DataBean.ItemBean> adapter = new CommonAdapter<DesignWorksBean
                .DataBean.ItemBean>(getParentFragment().getActivity(), R.layout.listitem_designer, worksList) {
            @Override
            protected void convert(ViewHolder holder, DesignWorksBean.DataBean.ItemBean itemBean, int position) {
                Glide.with(getParentFragment().getActivity())
                        .load(Constant.HOST + itemBean.getImg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) holder.getView(R.id.img_designer));
            }
        };
        rvWorks.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (!TextUtils.isEmpty(worksList.get(position).getGoods_id())) {
                    Intent intent = new Intent(getParentFragment().getActivity(), WorksDetailActivity.class);
                    intent.putExtra("id", String.valueOf(worksList.get(position).getGoods_id()));
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    public static WorksFragment newInstance() {
        Bundle args = new Bundle();
        WorksFragment fragment = new WorksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
