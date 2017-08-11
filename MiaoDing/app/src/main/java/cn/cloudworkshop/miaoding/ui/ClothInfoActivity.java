
//package cn.cloudworkshop.customtailor.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.cloudworkshop.customtailor.R;
//import cn.cloudworkshop.customtailor.base.BaseActivity;
//import cn.cloudworkshop.customtailor.utils.swipecardview.SwipeFlingAdapterView;
//
///**
// * Author：Libin on 2016/8/31 11:11
// * Email：1993911441@qq.com
// * Describe：衣物志
// */
//public class ClothInfoActivity extends BaseActivity implements SwipeFlingAdapterView.onFlingListener,
//        SwipeFlingAdapterView.OnItemClickListener {
//    @BindView(R.id.img_header_back)
//    ImageView imgHeaderBack;
//    @BindView(R.id.tv_header_title)
//    TextView tvHeaderTitle;
//    @BindView(R.id.img_header_share)
//    ImageView imgHeaderShare;
//    @BindView(R.id.swipe_view)
//    SwipeFlingAdapterView swipeView;
//    @BindView(R.id.tv_current_page)
//    TextView tvCurrentPage;
//    @BindView(R.id.rgs_cloth_info)
//    RadioGroup rgsClothInfo;
//
//    private InnerAdapter adapter;
//    private ArrayList<Integer> itemList;
//    private int count = 1;
//    private int[] imgStr = {R.mipmap.goods_picture1, R.mipmap.goods_picture2,
//            R.mipmap.goods_picture3, R.mipmap.goods_picture4, R.mipmap.goods_picture1
//            , R.mipmap.goods_picture2, R.mipmap.goods_picture3, R.mipmap.goods_picture3};
//
//    private String[] typeStr = {"纽扣", "花纹", "门襟", "面料", "袖口", "领子"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cloth_info);
//        ButterKnife.bind(this);
//
//        initView();
//        initData();
//
//    }
//
//    private void initView() {
//        tvHeaderTitle.setText("衣物志");
//        tvCurrentPage.setText(1 + "/" + imgStr.length);
//        ((RadioButton) rgsClothInfo.getChildAt(0)).setChecked(true);
//
//        swipeView.setFlingListener(this);
//        swipeView.setOnItemClickListener(this);
//
//        adapter = new InnerAdapter();
//        swipeView.setAdapter(adapter);
//
//    }
//
//    private void initData() {
//        itemList = new ArrayList<>();
//        for (int i = 0; i < imgStr.length; i++) {
//            itemList.add(imgStr[i]);
//        }
//        adapter.addAll(itemList);
//    }
//
//    @OnClick(R.id.img_header_back)
//    public void onClick() {
//        finish();
//    }
//
//
//    @Override
//    public void onItemClicked(MotionEvent event, View v, Object dataObject) {
//        Intent intent = new Intent(ClothInfoActivity.this, ClothMaterialInfo.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void removeFirstObjectInAdapter() {
//        adapter.remove(0);
//
//    }
//
//    @Override
//    public void onLeftCardExit(Object dataObject) {
//        count++;
//
//        if (count % itemList.size() == 0) {
//            tvCurrentPage.setText(itemList.size() + "/" + itemList.size());
//        } else {
//            tvCurrentPage.setText(count % itemList.size() + "/" + itemList.size());
//        }
//
//    }
//
//    @Override
//    public void onRightCardExit(Object dataObject) {
//        count++;
//        if (count % itemList.size() == 0) {
//            tvCurrentPage.setText(itemList.size() + "/" + itemList.size());
//        } else {
//            tvCurrentPage.setText(count % itemList.size() + "/" + itemList.size());
//        }
//    }
//
//    @Override
//    public void onAdapterAboutToEmpty(int itemsInAdapter) {
//        if (itemsInAdapter == 3) {
//            initData();
//        }
//
//    }
//
//    @Override
//    public void onScroll(float progress, float scrollXProgress) {
//
//    }
//
//
//    private class InnerAdapter extends BaseAdapter {
//
//        ArrayList<Integer> objs;
//
//        public InnerAdapter() {
//            objs = new ArrayList<>();
//        }
//
//        public void addAll(Collection<Integer> collection) {
//            if (isEmpty()) {
//                objs.addAll(collection);
//                notifyDataSetChanged();
//            } else {
//                objs.addAll(collection);
//            }
//
//        }
//
//
//        public boolean isEmpty() {
//            return objs.isEmpty();
//        }
//
//        public void remove(int index) {
//            if (index > -1 && index < objs.size()) {
//                objs.remove(index);
//                notifyDataSetChanged();
//            }
//        }
//
//
//        @Override
//        public int getCount() {
//            return objs.size();
//        }
//
//        @Override
//        public Object getItem(int currentPos) {
//            if (objs == null || objs.size() == 0) return null;
//            return objs.get(currentPos);
//        }
//
//        @Override
//        public long getItemId(int currentPos) {
//            return currentPos;
//        }
//
//        // TODO: getView
//        @Override
//        public View getView(int currentPos, View convertView, ViewGroup parent) {
//            MyViewHolder holder;
//
//            if (convertView == null) {
//                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_card_item, parent, false);
//                holder = new MyViewHolder();
//                convertView.setTag(holder);
//                holder.portraitView = (ImageView) convertView.findViewById(R.id.card_image_view);
//
//            } else {
//                holder = (MyViewHolder) convertView.getTag();
//            }
//            holder.portraitView.setImageResource(objs.get(currentPos));
//
//            return convertView;
//        }
//
//    }
//
//    private static class MyViewHolder {
//        ImageView portraitView;
//    }
//}
//
//
