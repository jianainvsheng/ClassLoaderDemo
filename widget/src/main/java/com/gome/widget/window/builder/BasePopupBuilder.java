package com.gome.widget.window.builder;
import android.view.View;
import com.gome.widget.window.popup.BasePopupWindow;
import com.gome.widget.window.popup.gwindow.GPopupWindow;
import com.gome.widget.window.windowenum.WindowEnum;
/**
 * Created by yangjian on 2019/3/6.
 */

public class BasePopupBuilder<B extends BasePopupBuilder<B>> {

    private BasePopupWindow mPopupWindow;

    private Object mData;

    private int mType;

    private int mLevel = -1;

    private int offX = 0;

    private int offY = 0;

    private WindowEnum mWindowEnum = WindowEnum.WINDOW_LEFT;

    public B setData(Object data) {
        this.mData = data;
        return (B) this;
    }

    public B setType(int type) {

        this.mType = type;
        return (B) this;
    }

    public B setWindowEnum(WindowEnum windowEnum) {

        this.mWindowEnum = windowEnum;
        return (B) this;
    }

    public B setLevel(int mLevel) {
        this.mLevel = mLevel;
        return (B)this;
    }

    public int getLevel() {
        return mLevel;
    }

    public B setOffY(int offY) {
        this.offY = offY;
        return (B)this;
    }

    public B setOffX(int offX) {
        this.offX = offX;
        return (B)this;
    }


    public int getOffY() {
        return offY;
    }

    public int getOffX() {
        return offX;
    }

    public int getType() {
        return mType;
    }

    public Object getData() {
        return mData;
    }

    public WindowEnum getWindowEnum() {
        return mWindowEnum;
    }

    public GPopupWindow showPopupView(View longitudinalView) {
        this.mPopupWindow.showPopupWindow(longitudinalView,getLevel());
        return (GPopupWindow) mPopupWindow;
    }

    public void attach(BasePopupWindow popupWindow) {

        this.mPopupWindow = popupWindow;
    }
}
