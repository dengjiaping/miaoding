package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.ui.ClothMaterialInfo;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.utils.GridDividerItemDecoration;

/**
 * Author：Libin on 2016/9/24 15:40
 * Email：1993911441@qq.com
 * Describe：衣物志子界面
 */
public class ClothInfoFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    private List<Integer> imgList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        imgList = new ArrayList<>();
    }

    protected void initView() {
        rvGoods.setLayoutManager(new GridLayoutManager(getActivity(),2));
        GridDividerItemDecoration dividerItemDecoration = new GridDividerItemDecoration(getActivity(),
                GridDividerItemDecoration.GRID_DIVIDER_VERTICAL);

        rvGoods.addItemDecoration(dividerItemDecoration);
        CommonAdapter<Integer> adapter = new CommonAdapter<Integer>(getActivity(),R.layout.listitem_cloth_info,imgList) {
            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {

            }
        };
        rvGoods.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(getActivity(),ClothMaterialInfo.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public static ClothInfoFragment newInstance() {

        Bundle args = new Bundle();
        ClothInfoFragment fragment = new ClothInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
