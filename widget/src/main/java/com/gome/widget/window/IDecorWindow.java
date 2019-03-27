package com.gome.widget.window;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yangjian on 2019/3/6.
 */

public interface IDecorWindow {

    /**
     * 初始化
     * @param popupWindow
     */
    void attach(IDecorWindow popupWindow);

    /**
     * 展示popouwindow 向下展示
     * @param view
     * @param cls
     * @param level
     */
    void showPopupWindow(View view,Class<?> cls,int level);

    /**
     * 隐藏view
     */
    void hidePopupWindow(Class<?> cls);

    /**
     * 加载PopupWindow的控件
     * @return
     */
    ViewGroup getDecorView();

    /**
     * window是否展示
     * @param cls 以class 作为区分是否是同一个window
     * @return
     */
    boolean isPopupWindowShow(Class<?> cls);

    /**
     * 获取层级 当前实际的层级
     * @param cls
     * @return
     */
    int getViewCurentLevel(Class<?> cls);


    /**
     * 获取层级 设置的层级 默认为-1
     * @param cls
     * @return
     */
    int getViewLevel(Class<?> cls);
}
