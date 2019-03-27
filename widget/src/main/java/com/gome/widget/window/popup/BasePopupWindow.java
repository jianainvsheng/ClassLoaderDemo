package com.gome.widget.window.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gome.widget.window.IDecorWindow;
import com.gome.widget.window.builder.BasePopupBuilder;
import com.gome.widget.window.decorchild.DecorChildView;
import com.gome.widget.window.helper.BasePopupHelper;
import com.gome.widget.window.windowenum.WindowEnum;
import com.volley.library.R;

/**
 * Created by yangjian on 2019/3/6.
 */

public class BasePopupWindow<B extends BasePopupBuilder<B>, H extends BasePopupHelper<B>>
        implements IDecorWindow {

    private IDecorWindow mDecorWindow;

    private B mBuilder;

    private H mHelper;

    public BasePopupWindow(Context context, B builder, H helper) {

        this.mBuilder = builder;
        this.mHelper = helper;
        IDecorWindow decorChildView = findDecorChild(context);
        decorChildView.attach(this);
        this.mBuilder.attach(this);
    }

    @Override
    public void attach(IDecorWindow popupWindow) {
        this.mDecorWindow = popupWindow;
    }

    @Override
    public void showPopupWindow(View view, Class<?> cls, int level) {

        mDecorWindow.showPopupWindow(view, cls, level);
    }

    @Override
    public void hidePopupWindow(Class<?> cls) {
        mDecorWindow.hidePopupWindow(cls);
    }

    public void showPopupWindow(View longitudinalView) {

        showPopupWindow(longitudinalView, mBuilder.getLevel());
    }

    public void showPopupWindow(View longitudinalView, int level) {
        if (longitudinalView == null || isPopupWindowShow() || mHelper.canNotShowPopupWindow()) {
            return;
        }
        final int[] startPosition = new int[2];
        longitudinalView.getLocationOnScreen(startPosition);
        final View popupWindowView = this.mHelper.attachView(mBuilder, getDecorView(), this);
        if (popupWindowView == null) {
            throw new NullPointerException(this.getClass().getCanonicalName() + " 获取加载的popupWindwoView 为空");
        }
        final int viewWidth = longitudinalView.getMeasuredWidth();
        final int[] endPosition = new int[2];
        getDecorView().getLocationOnScreen(endPosition);
        WindowEnum windowEnum = mBuilder.getWindowEnum();
        windowEnum = windowEnum == null ? WindowEnum.WINDOW_LEFT : windowEnum;
        final WindowEnum wEnum = windowEnum;
        popupWindowView.setVisibility(View.INVISIBLE);
        showPopupWindow(popupWindowView, mHelper.getClass(), level);
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) popupWindowView.getLayoutParams();
        popupWindowView.post(new Runnable() {
            @Override
            public void run() {
                mHelper.createPopupWindow(BasePopupWindow.this);
                int longitudinalY = startPosition[1] + mBuilder.getOffY();
                int longitudinalX = startPosition[0] + mBuilder.getOffX();
                int popupWidth = popupWindowView.getMeasuredWidth();
                if (wEnum == WindowEnum.WINDOW_LEFT) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = longitudinalX;
                } else if (wEnum == WindowEnum.WINDOW_CENTER) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = (longitudinalX > (popupWidth - viewWidth) / 2) ? longitudinalX - (popupWidth - viewWidth) / 2 : 0;

                } else if (wEnum == WindowEnum.WINDOW_RIGHT) {
                    params.topMargin = longitudinalY;
                    params.leftMargin = longitudinalX + viewWidth - popupWidth < endPosition[0] ? endPosition[0] : longitudinalX + viewWidth - popupWidth;
                }
                popupWindowView.setLayoutParams(params);
                mHelper.showPopupWindow(BasePopupWindow.this);
                popupWindowView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hidePopupWindow() {
        if (mHelper != null && mHelper.canNotHidePopupWindow()) {
            return;
        }
        if (isPopupWindowShow(mHelper.getClass())) {
            int duration = mHelper.hidePopupWindowDuration();
            if (duration > 0) {
                this.mHelper.getContentView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hidePopupWindow(mHelper.getClass());
                    }
                }, duration);
            } else {
                hidePopupWindow(mHelper.getClass());
            }
        }
    }

    @Override
    public ViewGroup getDecorView() {
        return this.mDecorWindow.getDecorView();
    }

    @Override
    public boolean isPopupWindowShow(Class<?> cls) {
        return mDecorWindow.isPopupWindowShow(cls);
    }

    @Override
    public int getViewCurentLevel(Class<?> cls) {
        return mDecorWindow.getViewCurentLevel(cls);
    }

    /**
     * 获取当前控件实际层级
     * @return
     */
    public int getViewCurentLevel(){

        return getViewCurentLevel(mHelper.getClass());
    }

    /**
     * 获取控件设置的层级
     * @return
     */
    public int getViewLevel() {

        return getViewLevel(mHelper.getClass());
    }

    @Override
    public int getViewLevel(Class<?> cls) {
        return mDecorWindow.getViewLevel(cls);
    }

    public boolean isPopupWindowShow() {
        return isPopupWindowShow(mHelper.getClass());
    }

    private IDecorWindow findDecorChild(Context context) {
        Activity activity = (Activity) context;
        View decorView = activity.findViewById(android.R.id.content);
        Object decorObject = decorView.getTag(R.id.decor_view_window_id);
        if (decorObject == null) {
            return new DecorChildView(context);
        } else {
            return (IDecorWindow) decorObject;
        }
    }
}
