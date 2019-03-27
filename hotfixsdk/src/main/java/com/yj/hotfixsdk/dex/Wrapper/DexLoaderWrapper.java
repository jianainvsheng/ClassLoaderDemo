package com.yj.hotfixsdk.dex.Wrapper;

import android.content.Context;

import com.yj.hotfixsdk.dex.IDexLoader;
import com.yj.hotfixsdk.dex.impl.DexLoaderImpl;
import com.yj.hotfixsdk.log.GLogger;

import java.io.File;

/**
 * Created by yangjian on 2019/2/26.
 */

public class DexLoaderWrapper implements IDexLoader {

    private IDexLoader mDexLoader;

    public DexLoaderWrapper(IDexLoader dexLoader){

        this.mDexLoader = dexLoader;
    }

    public boolean loaderDexPath(Context context, File... patchFilesDir){

        for(File file : patchFilesDir){
            boolean isloadSucess =  mDexLoader.onDexLoader(file);
            if(!isloadSucess){
                GLogger.e("加载file : " + file.getAbsolutePath() + " 下面的dex文件失败");
                return false;
            }
        }

        if(!isDexFileExists()){
            GLogger.e("所有的文件夹中不存在dex文件");
            return false;
        }

        try {
            mDexLoader.onDexInject(context);
            return true;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onDexLoader(File dexPath) {
        return mDexLoader.onDexLoader(dexPath);
    }

    @Override
    public boolean isDexFileExists() {
        return mDexLoader.isDexFileExists();
    }

    @Override
    public void onDexInject(Context context) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {

        mDexLoader.onDexInject(context);
    }

    @Override
    public void onDexOutFileName(String outDexFileName) {

        mDexLoader.onDexOutFileName(outDexFileName);
    }

    @Override
    public String getDexOutFilePath(Context context) {

        return mDexLoader.getDexOutFilePath(context);
    }
}
