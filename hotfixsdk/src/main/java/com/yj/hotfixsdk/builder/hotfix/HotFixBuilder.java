package com.yj.hotfixsdk.builder.hotfix;

import android.content.Context;
import android.os.Environment;

import com.yj.hotfixsdk.builder.BaseHotBuilder;
import com.yj.hotfixsdk.builder.hotfix.file.HotFixFileUtils;
import com.yj.hotfixsdk.dex.IDexLoader;
import com.yj.hotfixsdk.dex.Wrapper.DexLoaderWrapper;
import com.yj.hotfixsdk.dex.impl.DexLoaderImpl;
import com.yj.hotfixsdk.document.XMLDocumentUtils;
import com.yj.hotfixsdk.log.GLogger;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangjian on 2019/3/1.
 */

public class HotFixBuilder extends BaseHotBuilder<HotFixBuilder, HotBuilderData,DexLoaderWrapper> {

    private HotFixBuilder() throws NullPointerException, InstantiationException, IllegalAccessException {
        super();
    }

    @Override
    public void builder(Context context, HotBuilderData data,DexLoaderWrapper dexWrapLoader) {

        data.mDexParentFileName = (data.mDexParentFileName == null || "".equals(data.mDexParentFileName)) ? DexLoaderImpl.DEX_FILE_OUT : data.mDexParentFileName;
        data.mDexInFilePath = (data.mDexInFilePath == null || "".equals(data.mDexInFilePath)) ? Environment.getExternalStorageDirectory().getAbsolutePath() : data.mDexInFilePath;
        File inDexfile = new File(data.mDexInFilePath + File.separator + data.mDexParentFileName);
        if (!inDexfile.exists()) {
            inDexfile.mkdirs();
        }
        GLogger.d("加载dex的目录为 " + inDexfile.getAbsolutePath());
        dexWrapLoader.onDexOutFileName(data.mDexParentFileName);
        String outDexPath = dexWrapLoader.getDexOutFilePath(context);
        GLogger.d("输出dex的目录为 " + outDexPath);
        File outDexFile = new File(outDexPath);
        if (!outDexFile.exists()) {
            outDexFile.mkdirs();
        }

        File xmlInFile = HotFixFileUtils.getXmlFile(inDexfile, getBuilderData().mXmlName);
        File xmlOutFile = HotFixFileUtils.getXmlFile(outDexFile, getBuilderData().mXmlName);

        if (xmlInFile == null) {
            GLogger.d("加载dex的目录为空");
            GLogger.d("加载dex输出目录中文件到classloader中");
            if (dexWrapLoader.loaderDexPath(context, outDexFile)) {
                GLogger.d("加载成功");
            } else {
                GLogger.d("加载失败");
            }
        } else {
            if (xmlOutFile == null) {
                //拷贝
                try {
                    FileUtils.copyDirectory(inDexfile, outDexFile);
                    //最好进行md5 处理  看拷贝文件是否成功 保证完整性 这只是demo不做那么详细
                } catch (IOException e) {
                    //
                    GLogger.e("copy file fail !!!!!!!!!!!!");
                }
            } else {
                //比较
                XmlDocuData inXmlData = XMLDocumentUtils.parseDocument(
                        getBuilderData().mXmlName,
                        data.mDexParentFileName,
                        XmlDocuData.class);

                XmlDocuData outXmlData = XMLDocumentUtils.parseAbsoluteDocument(
                        getBuilderData().mXmlName,
                        dexWrapLoader.getDexOutFilePath(context),
                        XmlDocuData.class);
                if (outXmlData == null) {
                    if (inXmlData == null) {
                        GLogger.d("没有检测到要加载的dex文件");
                        GLogger.d("inXmlData文件不存在 outXml不存在");
                        return;
                    } else {

                        GLogger.d("inXmlData文件存在 但是outXml不存在");
                        try {
                            FileUtils.copyDirectory(inDexfile, outDexFile);
                            //最好进行md5 处理  看拷贝文件是否成功 保证完整性 这只是demo不做那么详细
                        } catch (IOException e) {
                            //
                            GLogger.e("copy file fail !!!!!!!!!!!!");
                        }
                        if (dexWrapLoader.loaderDexPath(context, outDexFile)) {
                            GLogger.d("加载成功");
                        } else {
                            GLogger.d("加载失败");
                        }
                    }
                } else {
                    if (inXmlData == null) {
                        GLogger.d("inXmlData文件不存在 但是outXml存在");
                        if (dexWrapLoader.loaderDexPath(context, outDexFile)) {
                            GLogger.d("加载成功");
                        } else {
                            GLogger.d("加载失败");
                        }
                    } else {
                        GLogger.d("inXmlData文件存在 outXml存在");
                        long inDexVersionCode = inXmlData.versonCode;
                        long outDexVersionCode = outXmlData.versonCode;
                        long inDexLast = inXmlData.lastUpdated;
                        long outDexLast = outXmlData.lastUpdated;
                        GLogger.d("indexDoc ===  inDexVersionCode : " + inDexVersionCode +
                                " inDexLast : " + inDexLast + " inXmlData.version : " + inXmlData.version);

                        GLogger.d("outXmlData ===  outDexVersionCode : " + outDexVersionCode +
                                " outDexLast : " + outDexLast + " outXmlData.version : " + outXmlData.version);

                        GLogger.d("inDexfile 目录下的文件个数: " +  inDexfile.listFiles().length);
                        GLogger.d("outDexFile 目录下的文件个数: " + outDexFile.listFiles().length);

                        if ((inDexVersionCode > outDexVersionCode &&
                                !inXmlData.version.equals(outXmlData.version) &&
                                inDexLast > outDexLast) ||
                                inDexfile.listFiles().length >
                                        outDexFile.listFiles().length) {
                            try {
                                FileUtils.deleteDirectory(outDexFile);
                            } catch (IOException e) {
                                //e.printStackTrace();
                                GLogger.e("outDexFile file delete fail !!!!!!!!!!!!");
                            }
                          //  outDexFile.delete();
                            try {
                                FileUtils.copyDirectory(inDexfile, outDexFile);
                                //最好进行md5 处理  看拷贝文件是否成功 保证完整性 这只是demo不做那么详细
                            } catch (IOException e) {
                                //
                                GLogger.e("copy file fail !!!!!!!!!!!!");
                            }
                        }
                        if (dexWrapLoader.loaderDexPath(context, outDexFile)) {
                            GLogger.d("加载成功");
                        } else {
                            GLogger.d("加载失败");
                        }
                    }
                }
            }
        }
    }

    /***
     * 输出和加进来dex的文件目录名称
     * @param dexParentFileName
     * @return
     */
    public HotFixBuilder setDexParentFileName(String dexParentFileName) {

        getBuilderData().mDexParentFileName = dexParentFileName;
        return this;
    }


    /**
     * 加载dex目录之前的路径 默认为手机的SD卡
     */
    public HotFixBuilder setDexInFilePath(String dexInFilePath) {

        getBuilderData().mDexInFilePath = dexInFilePath;
        return this;
    }

    /**
     * 加载xml目录名称
     */
    public HotFixBuilder setXmlFileName(String xmlName) {

        getBuilderData().mXmlName = (xmlName == null || "".equals(xmlName)) ? getBuilderData().mXmlName : xmlName;
        return this;
    }

    /**
     * 是否展示log
     */
    public HotFixBuilder setDebugLogger(boolean isShowLog) {

        GLogger.setLogDebug(isShowLog);
        return this;
    }

    public static HotFixBuilder onCreate() {

        try {
            return new HotFixBuilder();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
