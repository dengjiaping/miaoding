package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;

/**
 * Author：binge on 2017-06-09 10:46
 * Email：1993911441@qq.com
 * Describe：
 */
public class TestActivity extends BaseActivity{
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.text);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ItemAdapter());
    }


    private class ItemAdapter extends RecyclerView.Adapter<TextHolder>{

        @Override
        public TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TextHolder(View.inflate(TestActivity.this,android.R.layout.simple_list_item_1,null));
        }

        @Override
        public void onBindViewHolder(TextHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }
    class TextHolder extends RecyclerView.ViewHolder{

        private TextView mText;
        public TextHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(android.R.id.text1);
        }

        void bind(){
            mText.setText(String.format("Item %s",getAdapterPosition()));
        }
    }
}
