package com.zfy.light.sample;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.march.common.exts.ListX;
import com.march.common.exts.ToastX;
import com.zfy.adapter.LightAdapter;
import com.zfy.adapter.LightHolder;
import com.zfy.adapter.assistant.SlidingSelectLayout;
import com.zfy.adapter.collections.LightDiffList;
import com.zfy.adapter.model.LightView;
import com.zfy.component.basic.mvx.mvp.app.MvpActivity;
import com.zfy.component.basic.mvx.mvp.app.MvpV;
import com.zfy.light.sample.entity.SingleTypeEntity;

import butterknife.BindView;

/**
 * CreateAt : 2018/11/9
 * Describe :
 *
 * @author chendong
 */
@MvpV(layout = R.layout.selector_activity)
public class SelectorTestActivity extends MvpActivity {

    @BindView(R.id.content_rv) RecyclerView        mRecyclerView;
    @BindView(R.id.ssl)        SlidingSelectLayout mSlidingSelectLayout;

    private LightDiffList<SingleTypeEntity> mData;
    private LightAdapter<SingleTypeEntity>  mAdapter;

    @Override
    public void init() {
        mData = new LightDiffList<>();
        mAdapter = new LightAdapter<SingleTypeEntity>(getContext(), mData, R.layout.item_selector) {
            @Override
            public void onBindView(LightHolder holder, SingleTypeEntity data, int pos) {
                holder.setText(R.id.desc_tv, (data.id % 4 == 0) ? "不允许选中" : data.title);
            }
        };
        mAdapter.header().addHeaderView(LightView.from(R.layout.desc_header), (holder, position) -> {
            holder.setText(R.id.desc_tv, Values.getSelectorDesc())
                    .setCallback(R.id.cover_iv, new GlideCallback(Utils.randomImage()))
                    .setClick(R.id.action_fab, v -> {
                        ToastX.show("选中了 " + mAdapter.selector().getResults().size() + " 个");
                    });
        });
        mAdapter.selector().setMultiSelector((holder, pos, data) -> {
            holder.setChecked(R.id.checkbox, data.isSelected());
        });
        mAdapter.selector().setOnSelectListener(data -> {
            if (data.id % 4 == 0) {
                ToastX.show("此项不允许选中");
                return false;
            }
            return true;
        });
        mAdapter.setClickCallback((holder, pos, data) -> {
            mAdapter.selector().toggleItem(data);
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter);
        mData.update(ListX.range(100, index -> new SingleTypeEntity(index, "title " + index, "desc " + index)));

    }

}