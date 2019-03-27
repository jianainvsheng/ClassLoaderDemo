package com.gome.widget.window.decorchild;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.gome.widget.window.IDecorWindow;
import com.gome.widget.window.windowenum.WindowEnum;
import com.volley.library.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangjian on 2019/3/6.
 */

public class DecorChildView extends RelativeLayout implements IDecorWindow{

    private ViewGroup mDecorView;

    private IDecorWindow mDecorWindow;

    private Map<Class<?> , DecorChildRecord> mapViews = new HashMap<>();

    public DecorChildView(Context context) {
        super(context);
        this.mDecorView = ((Activity)context).findViewById(android.R.id.content);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        setLayoutParams(params);
        this.mDecorView.addView(this,mDecorView.getChildCount());
        this.mDecorView.setTag(R.id.decor_view_window_id,this);
        this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public void attach(IDecorWindow popupWindow) {

        this.mDecorWindow = popupWindow;
        this.mDecorWindow.attach(this);
    }

    @Override
    public void showPopupWindow(View view,Class<?> cls,int level) {

        if(isPopupWindowShow(cls)){
            return;
        }
        if(this.mDecorView.indexOfChild(this) == -1){
            this.mDecorView.addView(this,mDecorView.getChildCount());
        }else if(this.mDecorView.indexOfChild(this) != mDecorView.getChildCount() -1){
            this.mDecorView.removeView(this);
            this.mDecorView.addView(this,mDecorView.getChildCount());
        }
        if(mapViews.containsKey(cls)){

            DecorChildRecord record = mapViews.get(cls);
            removeView(record.mDecorChildView);
        }
        if(view.getParent() != null){
            ViewGroup parentView = (ViewGroup) view.getParent();
            parentView.removeView(view);
        }
        ViewGroup.LayoutParams viewGroupParams = view.getLayoutParams();

        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if(viewGroupParams != null){
            width = viewGroupParams.width;
            height = viewGroupParams.height;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width,
                height);
        view.setLayoutParams(params);
        DecorChildRecord record = new DecorChildRecord();
        record.mLevel = level;
        record.mDecorChildView = view;
        record.mCls = cls;
        int childSize = getChildCount();
        if(childSize <= 0){
            level = 0;
        }else{
            for(int i = childSize - 1 ; i >= 0 ; i--){
                View lastView = getChildAt(i);
                DecorChildRecord lastChildRecord = (DecorChildRecord) lastView.getTag(R.id.decor_view_child_id);
                if(lastChildRecord.isLevelLess(record)){
                    level = i + 1;
                    break;
                }
                if(i == 0){
                    level = 0;
                }
            }
        }
        if(level > getChildCount()){
            level = getChildCount();
        }
        view.setTag(R.id.decor_view_child_id,record);
        addView(view,level);
        mapViews.put(cls,record);

        StringBuffer buffer = new StringBuffer();
        for(Map.Entry<Class<?>, DecorChildRecord> entry : mapViews.entrySet()){
            buffer.append("key : " + entry.getKey() +
            " value : " + entry.getValue().mDecorChildView + " level :" +
                    entry.getValue().mLevel);
        }
        System.out.println("=========map : " + buffer.toString());
    }

    @Override
    public void hidePopupWindow(Class<?> cls) {

        if(!isPopupWindowShow(cls)){
            return;
        }
        DecorChildRecord record = mapViews.get(cls);
        removeView(record.mDecorChildView);
        mapViews.remove(record);
    }

    @Override
    public ViewGroup getDecorView() {
        return this;
    }

    @Override
    public boolean isPopupWindowShow(Class<?> cls) {

        if(!mapViews.containsKey(cls)){
            return false;
        }
        DecorChildRecord record = mapViews.get(cls);
        if(indexOfChild(record.mDecorChildView) == -1){
            mapViews.remove(cls);
            return false;
        }
        return true;
    }

    @Override
    public int getViewCurentLevel(Class<?> cls) {
        if(mapViews.containsKey(cls)){
            DecorChildRecord record = mapViews.get(cls);
            return indexOfChild(record.mDecorChildView);
        }
        return -1;
    }

    @Override
    public int getViewLevel(Class<?> cls) {
        if(mapViews.containsKey(cls)){
            DecorChildRecord record = mapViews.get(cls);
            return record.mLevel;
        }
        return -1;
    }

    class DecorChildRecord{

        public View mDecorChildView;

        public int mLevel = -1;

        public Class<?> mCls;

        public boolean isLevelLess(DecorChildRecord record){

            if(this.mLevel < 0 ||
                    this.mLevel < record.mLevel){
                return true;
            }
            if(this.mLevel == record.mLevel){

                throw new NullPointerException(record.mCls.getSimpleName() + " 设置的level : " + record.mLevel

                     + "与 " + this.mCls.getSimpleName() + "设置的 level ： "+ this.mLevel + "一样"
                );
            }
            return false;
        }
    }
}
