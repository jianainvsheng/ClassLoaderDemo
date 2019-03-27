package com.gome.widget.window.helper;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gome.widget.window.builder.BasePopupBuilder;
import com.gome.widget.window.popup.BasePopupWindow;

/**
 * Created by yangjian on 2019/3/6.
 */

public abstract class BasePopupHelper<B extends BasePopupBuilder> {

    private B mBuilder;

    private View mContentView;

    public View attachView(B builder, ViewGroup parentView,BasePopupWindow decorWindow) {

        if (builder == null) {
            throw new NullPointerException(this.getClass().getCanonicalName() + " attach 方法中 builder 为空");
        }

        this.mBuilder = builder;
        return onCreateView(parentView, builder,decorWindow);
    }

    View onCreateView(ViewGroup parentView, B builder,BasePopupWindow decorWindow){

        mContentView = LayoutInflater.from(parentView.getContext())
                .inflate(onCreateViewLayoutId() , parentView ,false);
        onBuilder(mContentView,builder,decorWindow);
        return mContentView;
    }

    /**
     * layout id
     * @return
     */
    public abstract @LayoutRes int onCreateViewLayoutId();

    /**
     * 绑定数据
     * @param view
     * @param builder
     */
    public abstract void onBuilder(View view , B builder,BasePopupWindow decorWindow);


    /**
     * 添加目标到popup中
     * @param decorWindow
     */
    public abstract void createPopupWindow(BasePopupWindow decorWindow);

    /**
     * 展示
     * @param decorWindow
     */
    public abstract void showPopupWindow(BasePopupWindow decorWindow);

    /**
     * popup消失
     */
    public abstract int hidePopupWindowDuration();

    /**
     * 是否可以隐藏popupwindow
     * @return
     */
    public abstract boolean canNotHidePopupWindow();

    /**
     * 是否可以展示popupWindow
     * @return
     */
    public abstract boolean canNotShowPopupWindow();

    public B getBuilderData() {

        return mBuilder;
    }

    public View getContentView(){

        return mContentView;
    }
}
