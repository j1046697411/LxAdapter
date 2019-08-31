package com.zfy.adapter.x;

import android.support.annotation.LayoutRes;

/**
 * CreateAt : 2019-08-30
 * Describe :
 *
 * @author chendong
 */
public class TypeOpts {

    public            int viewType = Lx.VIEW_TYPE_DEFAULT; // 数据类型
    @LayoutRes public int layoutId; // 布局资源
    public            int spanSize = Lx.SPAN_NONE; // 跨越行数

    public boolean enableClick     = true; // 是否允许点击事件
    public boolean enableLongPress = false; // 是否允许长按事件
    public boolean enableDbClick   = false; // 是否允许双击事件

    public            boolean enableDrag      = false; // 是否允许拖动
    public            boolean enableSwipe     = false; // 是否允许滑动
    public            boolean enablePin       = false; // 钉住，支持悬停效果

    public interface TypeOptsSetter {
        void set(TypeOpts opts);
    }

    private TypeOpts() {

    }

    public static TypeOpts make(TypeOptsSetter setter) {
        TypeOpts typeOpts = new TypeOpts();
        setter.set(typeOpts);
        return typeOpts;
    }

    public static TypeOpts make(int viewType, int layoutId) {
        TypeOpts typeOpts = new TypeOpts();
        typeOpts.viewType = viewType;
        typeOpts.layoutId = layoutId;
        return typeOpts;
    }


    public static TypeOpts make(int layoutId) {
        TypeOpts typeOpts = new TypeOpts();
        typeOpts.viewType = Lx.VIEW_TYPE_DEFAULT;
        typeOpts.layoutId = layoutId;
        return typeOpts;
    }

}