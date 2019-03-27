package com.yj.hotfixsdk.builder;

import android.content.Context;

import com.yj.hotfixsdk.builder.data.BaseBuilderData;
import com.yj.hotfixsdk.dex.IDexLoader;
import com.yj.hotfixsdk.dex.Wrapper.DexLoaderWrapper;
import com.yj.hotfixsdk.dex.impl.DexLoaderImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yangjian on 2019/3/1.
 */

public abstract class BaseHotBuilder<H extends BaseHotBuilder<H,D,E>,D extends BaseBuilderData,E extends DexLoaderWrapper> {

    private D mData;

    private IDexLoader mDexLoader;

    protected BaseHotBuilder() throws NullPointerException , InstantiationException ,IllegalAccessException{

        Class<D> dClass = getEventClass(1);
        if(dClass == null){
            throw new NullPointerException("BaseHotBuilder 构造中传入的data class 为 null ");
        }
        mDexLoader = new DexLoaderImpl();
        mData = dClass.newInstance();
    }
    protected BaseHotBuilder(Class<D> dClass) throws NullPointerException , InstantiationException ,IllegalAccessException{

        if(dClass == null){
            throw new NullPointerException("BaseHotBuilder 构造中传入的data class 为 null ");
        }
        mData = dClass.newInstance();
        mDexLoader = new DexLoaderImpl();
    }

    public void initBuilder(Context context){

        Class<E> eClass = getEventClass(2);
        if(eClass == null){
            throw new NullPointerException("绑定的dexloader加载器为空");
        }

        Class[] parameterTypes={IDexLoader.class};
        try {
            Constructor constructor = eClass.getConstructor(parameterTypes);
            Object[] parameters={mDexLoader};
            E e = (E) constructor.newInstance(parameters);
            builder(context,getBuilderData(),e);
        } catch (Exception e) {
            throw new NullPointerException("IDexLoader 创建失败");
        }
    }

    public abstract void builder(Context context ,D data,E dexWrapLoader);


    public H setVersion(String version){

        mData.version = version;

        return (H) this;
    }

    public H setVersionCode(long code){

        mData.versonCode = code;
        return (H) this;
    }

    public D getBuilderData() {
        return mData;
    }

    /**
     * 如果该方法为空 请使用第二个构造
     * @return
     */
    public <T> Class<T>  getEventClass(int position){

        Type type = getClass().getGenericSuperclass();
        if(type == null){
            return null;
        }
        if(type.toString().contains("<")
                && type.toString().contains(">")){
            Type[] tClass = ((ParameterizedType)getClass().
                    getGenericSuperclass()).getActualTypeArguments();
            if(tClass != null
                    && tClass.length >= 3
                    && tClass[1].toString().contains("class")){
                Class<T> tCls = (Class<T>) tClass[position];
                return tCls;
            }
        }
        return null;
    }
}
