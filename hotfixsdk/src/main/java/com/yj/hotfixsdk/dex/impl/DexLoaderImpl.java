package com.yj.hotfixsdk.dex.impl;

import android.content.Context;
import com.yj.hotfixsdk.dex.IDexLoader;
import com.yj.hotfixsdk.log.GLogger;

import org.w3c.dom.Element;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by yangjian on 2019/2/26.
 */

public class DexLoaderImpl implements IDexLoader{

    private static final String DEX_SUFFIX = ".dex";
    private static final String APK_SUFFIX = ".apk";
    private static final String JAR_SUFFIX = ".jar";
    private static final String ZIP_SUFFIX = ".zip";
    public static final String DEX_FILE_OUT= "optimize_dex";
    private String outPathName = DEX_FILE_OUT;
    private HashSet<File> loadedDex = new HashSet<>();

    @Override
    public boolean onDexLoader(File dexPath) {

        if(dexPath == null || !dexPath.exists()){
            GLogger.e("onDexLoader 中 dexpath 为空" + " dexPath : " + dexPath.getAbsolutePath());
            return false;
        }
        File[] files = dexPath.listFiles();
        GLogger.e("onDexLoader 存放的目录为  " + dexPath.getAbsolutePath() + "个数为 " + files.length);
        if(files == null || files.length <= 0){

            GLogger.e("onDexLoader 文件下为空 没有文件 " + dexPath.getAbsolutePath());
            return false;
        }
        for(File file : files){
            GLogger.e("检测目录  " + dexPath.getAbsolutePath() + "下的文件  " + file.getAbsolutePath());
            checkFileNeedLoader(file);
        }
        return true;
    }

    @Override
    public boolean isDexFileExists() {
        return (loadedDex !=null && loadedDex.size() > 0);
    }

    public boolean checkFileNeedLoader(File file) {
        if (file.getName().startsWith("classes") &&
                (file.getName().endsWith(DEX_SUFFIX)
                        || file.getName().endsWith(APK_SUFFIX)
                        || file.getName().endsWith(JAR_SUFFIX)
                        || file.getName().endsWith(ZIP_SUFFIX))) {
            GLogger.d("添加dex到集合中 "+ file.getAbsolutePath());
            loadedDex.add(file);// 存入集合
            return true;
        }
        GLogger.e("文件 " + file.getAbsolutePath() +" 不符合checkFileNeedLoader 的要求");
        return false;
    }

    @Override
    public void onDexInject(Context context) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {

        String optimizeDir = context.getFilesDir().getAbsolutePath() + File.separator + outPathName + File.separator + "classfile";// data/data/包名/files/optimize_dex（这个必须是自己程序下的目录）
        GLogger.e("dex 的解析目录路径 为 ： " + optimizeDir);
        File fopt = new File(optimizeDir);
        if(!fopt.exists()){
            fopt.mkdirs();
        }
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        if(loadedDex.size() > 0){
            for (File dexFile : loadedDex){
                DexClassLoader dexClassLoader = new DexClassLoader(
                        dexFile.getAbsolutePath(),
                        fopt.getAbsolutePath()
                        ,null,
                        pathClassLoader
                );
                //合并两个classloader 中的pathlist 的Element[] (dexElements)
                Object dexPathList = getField(dexClassLoader, BaseDexClassLoader.class,"pathList");
                Object pathPathList = getField(pathClassLoader,BaseDexClassLoader.class,"pathList");
                Object dexElement = getField(dexPathList,dexPathList.getClass(),"dexElements");
                Object pathElement = getField(pathPathList,pathPathList.getClass(),"dexElements");
                Object bineArray = combineArray(dexElement,pathElement);

                //赋值
                Object pathList = getField(pathClassLoader,BaseDexClassLoader.class,"pathList");
                setField(pathList,pathList.getClass(),"dexElements",bineArray);
            }
        }
        //结束
        onDexInjectOver();
    }

    @Override
    public void onDexOutFileName(String outPath) {

        outPathName = (outPath == null || "".equals(outPath)) ? outPathName : outPath;
    }

    @Override
    public String getDexOutFilePath(Context context) {
        return context.getFilesDir().getAbsolutePath() + File.separator + outPathName;
    }

    private void onDexInjectOver() {

        loadedDex.clear();
    }

    /**
     * 反射给对象中的属性重新赋值
     * @param obj  赋值目标类的实例
     * @param cl   类的class
     * @param field 赋值类中的属性
     * @param value 赋值类中的属性值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cl.getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }


    /**
     * 反射得到对象中的属性值
     * @param obj  目标class实例
     * @param cl   class
     * @param field 目标class的属性
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    /**
     * 数组合并
     * @param arrayLhs
     * @param arrayRhs
     * @return
     */
    private Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> componentType = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);// 得到左数组长度（补丁数组）
        int j = Array.getLength(arrayRhs);// 得到原dex数组长度
        int k = i + j;// 得到总数组长度（补丁数组+原dex数组）
        Object result = Array.newInstance(componentType, k);// 创建一个类型为componentType，长度为k的新数组
        System.arraycopy(arrayLhs, 0, result, 0, i);
        System.arraycopy(arrayRhs, 0, result, i, j);
        return result;
    }
}
