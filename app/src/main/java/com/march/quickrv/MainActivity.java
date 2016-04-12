package com.march.quickrv;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.march.quickrvlibs.RvHFAdapter;
import com.march.quickrvlibs.RvLoadMoreAdapter;
import com.march.quickrvlibs.RvQuickAdapter;
import com.march.quickrvlibs.RvViewHolder;
import com.march.quickrvlibs.helper.GridDividerDecoration;
import com.march.quickrvlibs.helper.RvConvertor;
import com.march.quickrvlibs.model.RvQuickModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private Context self = MainActivity.this;
    RvLoadMoreAdapter rvLoadMoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rvquick_activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);


        //内置类适配测试
        String[] strs = new String[]{"a", "a", "a", "a", "a", "a", "a", "a", "a", "a"};
        RvQuickAdapter<RvQuickModel> adapter =
                new RvQuickAdapter<RvQuickModel>(this, RvConvertor.convert(strs)) {
                    @Override
                    public void bindData4View(RvViewHolder holder, RvQuickModel data, int pos, int type) {
                        String s = data.<String>get();
                    }
                };
        Demo[] demoarr = new Demo[]{};
        RvQuickAdapter<Demo> adapter1 = new RvQuickAdapter<Demo>(this, demoarr) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {

            }
        };


        final List<Demo> demos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            demos.add(new Demo(i, i + " <- this is"));
        }

        RvQuickAdapter rvQuickAdapter;

        //单类型适配
//        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos, R.layout.rvquick_item_a) {
//            @Override
//            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
//                holder.setText(R.id.item_a_tv, data.title);
//            }
//        };


        // 设置item监听事件
//        rvQuickAdapter.setClickListener(new OnRecyclerItemClickListener<RvViewHolder>() {
//            @Override
//            public void onItemClick(int pos, RvViewHolder holder) {
//                Toast.makeText(MainActivity.this, "点击" + pos, Toast.LENGTH_SHORT).show();
//            }
//        });
//        //设置item长按事件
//        rvQuickAdapter.setLongClickListener(new OnRecyclerItemLongClickListener<RvViewHolder>() {
//            @Override
//            public void onItemLongClick(int pos, RvViewHolder holder) {
//                Toast.makeText(MainActivity.this, "长按" + pos, Toast.LENGTH_SHORT).show();
//            }
//        });

        //多类型适配
        rvQuickAdapter = new RvQuickAdapter<Demo>(MainActivity.this, demos) {
            @Override
            public void bindData4View(RvViewHolder holder, Demo data, int pos, int type) {
                if (type == 0)
                    holder.setText(R.id.item_a_tv, data.title);
                else
                    holder.setText(R.id.item_b_tv, data.title + "   type2");
            }

        };

        rvQuickAdapter.addType(0, R.layout.rvquick_item_a).addType(1, R.layout.rvquick_item_b);

        final RvHFAdapter rvHFQuickAdapter = new RvHFAdapter(rvQuickAdapter){
            @Override
            public void bindLisAndData4Footer(RvViewHolder footer) {
                super.bindLisAndData4Footer(footer);
            }

            @Override
            public void bindLisAndData4Header(RvViewHolder header) {
                super.bindLisAndData4Header(header);
            }
        };


//        rvQuickAdapter.addHeader(LayoutInflater.from(this).inflate(R.layout.rvquick_header, null));
//        rvQuickAdapter.addFooter(LayoutInflater.from(this).inflate(R.layout.rvquick_footer, null));
        rvHFQuickAdapter.addHeader(R.layout.rvquick_header);
        rvHFQuickAdapter.addFooter(R.layout.rvquick_footer);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));


        //你可以使用shape自定义分割线样式
        Drawable line = ContextCompat.getDrawable(self, R.drawable.shape_line);
//        new GridDividerDecoration(self,line);
//        new LinearDividerDecoration(self, LinearDividerDecoration.VERTICAL_LIST, line)
        recyclerView.addItemDecoration(new GridDividerDecoration(self, line));

        rvLoadMoreAdapter = new RvLoadMoreAdapter(rvHFQuickAdapter, new RvLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                for (int i = 0; i < 50; i++) {
                    demos.add(new Demo(i, i + " <- new"));
                }
                rvLoadMoreAdapter.notifyDataSetChanged();
                rvLoadMoreAdapter.finishLoad();
            }
        });
        recyclerView.setAdapter(rvLoadMoreAdapter);
    }
}