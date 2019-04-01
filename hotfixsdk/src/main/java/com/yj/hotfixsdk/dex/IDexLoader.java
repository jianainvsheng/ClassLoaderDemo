package com.yj.hotfixsdk.dex;

import android.content.Context;
import java.io.File;

/**
 * Created by yangjian on 2019/2/26.
 *
 *  http://androidxref.com/5.0.0_r2/xref/libcore/dalvik/src/main/java/dalvik/system/PathClassLoader.java
 *
 *  http://androidxref.com/5.0.0_r2/xref/libcore/dalvik/src/main/java/dalvik/system/DexClassLoader.java
 *
 *  http://androidxref.com/5.0.0_r2/xref/libcore/dalvik/src/main/java/dalvik/system/BaseDexClassLoader.java
 *
 *  http://androidxref.com/5.0.0_r2/xref/libcore/dalvik/src/main/java/dalvik/system/DexPathList.java
 */

public interface  IDexLoader {

    /**
     *
     * @param dexPath  补丁所在目录
     */
    boolean onDexLoader(File dexPath);

    /**
     * 是否有dex文件存在
     * @return
     */
    boolean isDexFileExists();

    /**
     * 添加补丁
     * @param context apk运行环境
     */

    void onDexInject(Context context) throws NoSuchFieldException, IllegalAccessException,ClassNotFoundException;

    /**
     * 补丁解析dex文件的输出目录名称
     * @param outDexFileName
     */
    void onDexOutFileName(String outDexFileName);

    /**
     * 获取输出dex文件的全路径
     * @param context
     * @return
     */
    String getDexOutFilePath(Context context);
}
