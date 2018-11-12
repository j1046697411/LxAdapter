package com.zfy.adapter.items;

import android.support.annotation.LayoutRes;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.common.LightValues;
import com.zfy.adapter.model.ModelType;

/**
 * CreateAt : 2018/11/12
 * Describe :
 *
 * @author chendong
 */
public abstract class SimpleItemAdapter<D> implements IItemAdapter<D> {

    private ModelType mModelType;

    public SimpleItemAdapter() {
        TypeOption option = getClass().getAnnotation(TypeOption.class);
        if (option != null) {
            mModelType = new ModelType(option.type());
            mModelType.spanSize = option.spanSize();
            mModelType.enableClick = option.enableClick();
            mModelType.enableLongPress = option.enableLongPress();
            mModelType.enableDbClick = option.enableDbClick();
            mModelType.enableDrag = option.enableDrag();
            mModelType.enableSwipe = option.enableSwipe();
            mModelType.enablePin = option.enablePin();
        } else {
            mModelType = new ModelType(LightValues.NONE);
            mModelType.type = getModelType();
            configModelType(mModelType);
        }
    }


    public abstract @LayoutRes
    int getLayoutId();

    @Override
    public int getModelType() {
        return mModelType.type;
    }


    @Override
    public void configModelType(ModelType modelType) {
        modelType.type = mModelType.type;
        modelType.layoutId = getLayoutId();
        modelType.spanSize = mModelType.spanSize;
        modelType.enableClick = mModelType.enableClick;
        modelType.enableLongPress = mModelType.enableLongPress;
        modelType.enableDbClick = mModelType.enableDbClick;
        modelType.enableDrag = mModelType.enableDrag;
        modelType.enableSwipe = mModelType.enableSwipe;
        modelType.enablePin = mModelType.enablePin;
    }


    @Override
    public void onBindViewUsePayload(LightHolder holder, D data, int pos, String msg) {

    }

    @Override
    public void onClickEvent(LightHolder holder, D data, int pos) {

    }

    @Override
    public void onLongPressEvent(LightHolder holder, D data, int pos) {

    }

    @Override
    public void onDbClickEvent(LightHolder holder, D data, int pos) {

    }

}