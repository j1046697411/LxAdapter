package com.zfy.adapter.callback;

import com.zfy.adapter.LightHolder;
import com.zfy.adapter.model.Extra;

/**
 * CreateAt : 2018/11/5
 * Describe : 事件回调
 *
 * @author chendong
 */
public interface EventCallback<D> {

    void call(LightHolder holder, D data, Extra extra);
}