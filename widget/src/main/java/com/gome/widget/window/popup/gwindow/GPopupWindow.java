package com.gome.widget.window.popup.gwindow;

import android.content.Context;

import com.gome.widget.window.builder.BasePopupBuilder;
import com.gome.widget.window.helper.BasePopupHelper;
import com.gome.widget.window.popup.BasePopupWindow;

/**
 * Created by yangjian on 2019/3/6.
 */

public class GPopupWindow<B extends BasePopupBuilder<B>,H extends BasePopupHelper<B>> extends BasePopupWindow<B,H>{


    public GPopupWindow(Context context, B builder, H helper) {

        super(context, builder, helper);
    }

    public static <B extends BasePopupBuilder<B>,
            H extends BasePopupHelper<B>> B createPopupWindow(Context context,Class<B> bClass,Class<H> hClass){

        try {
            B bObject = bClass.newInstance();
            H hObject = hClass.newInstance();
            new GPopupWindow<>(context,bObject,hObject);
            return bObject;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <H extends BasePopupHelper<BasePopupBuilder>> BasePopupBuilder createPopupWindow(Context context,Class<H> hClass){

       return createPopupWindow(context,BasePopupBuilder.class,hClass);
    }
}
