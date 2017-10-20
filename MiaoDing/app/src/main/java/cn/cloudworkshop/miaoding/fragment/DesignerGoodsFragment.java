package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.DesignerInfoBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.WorksDetailActivity;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;

/**
 * Author：Libin on 2017-06-08 15:52
 * Email：1993911441@qq.com
 * Describe：设计师作品
 */
public class DesignerGoodsFragment extends BaseFragment {

    @BindView(R.id.rv_member_rights)
    RecyclerView rvWorks;
    @BindView(R.id.sv_no_works)
    NestedScrollView svNoWorks;

    private Unbinder unbinder;

    private List<DesignerInfoBean.DataBean.GoodsListBean> worksList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_member, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        return view;
    }

    private void initView() {
        if (worksList != null && worksList.size() > 0) {
            rvWorks.setLayoutManager(new LinearLayoutManager(getActivity()));
            CommonAdapter<DesignerInfoBean.DataBean.GoodsListBean> adapter = new CommonAdapter
                    <DesignerInfoBean.DataBean.GoodsListBean>(getActivity(), R.layout.listitem_works, worksList) {
                @Override
                protected void convert(ViewHolder holder, DesignerInfoBean.DataBean.GoodsListBean
                        goodsListBean, int position) {

                    RelativeLayout relativeLayout = holder.getView(R.id.rl_layout_goods);
                    ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
                    int widthPixels = DisplayUtils.getMetrics(getActivity()).widthPixels;
                    layoutParams.width = (int) ((widthPixels - DisplayUtils.dp2px(getActivity(), 24)));
                    layoutParams.height = layoutParams.width * 1038 / 696;
                    relativeLayout.setLayoutParams(layoutParams);


                    SimpleDraweeView imgWorks = holder.getView(R.id.img_designer);
                    imgWorks.setImageURI(Constant.HOST + goodsListBean.getThumb());
                    TextView tvTitle = holder.getView(R.id.tv_works_title);
                    tvTitle.setTypeface(DisplayUtils.setTextType(getActivity()));
                    tvTitle.setText(goodsListBean.getName());
                    holder.setText(R.id.tv_works_time, goodsListBean.getC_time());
                }

            };
            rvWorks.setAdapter(adapter);
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    if (!TextUtils.isEmpty(worksList.get(position).getId() + "")) {
                        Intent intent = new Intent(getActivity(), WorksDetailActivity.class);
                        intent.putExtra("id", String.valueOf(worksList.get(position).getId()));
                        startActivity(intent);
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            svNoWorks.setVisibility(View.VISIBLE);
        }

    }

    private void getData() {
        Bundle bundle = getArguments();
        worksList = bundle.getParcelableArrayList("works_list");

    }

    public static DesignerGoodsFragment newInstance(List<DesignerInfoBean.DataBean.GoodsListBean> dataList) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("works_list", (ArrayList<? extends Parcelable>) dataList);
        DesignerGoodsFragment fragment = new DesignerGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
