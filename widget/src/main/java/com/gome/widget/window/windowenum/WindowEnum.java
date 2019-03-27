package com.gome.widget.window.windowenum;

/**
 * Created by yangjian on 2019/3/7.
 */

public enum WindowEnum {

    WINDOW_LEFT(1,"相对控件的左边"),WINDOW_CENTER(2,"相对控件的中间"),WINDOW_RIGHT(3,"相对控件的右边");

    private int mType = -1;
    private String mDesc = "";
    WindowEnum(int type ,String desc) {

        this.mType = type;
        this.mDesc = desc;
    }

    public int getType(){

        return mType;
    }

    public String getDesc() {
        return mDesc;
    }
}
