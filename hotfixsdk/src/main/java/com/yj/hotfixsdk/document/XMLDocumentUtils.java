package com.yj.hotfixsdk.document;

import android.os.Environment;

import com.yj.hotfixsdk.builder.hotfix.file.HotFixFileUtils;
import com.yj.hotfixsdk.document.data.BaseDocData;
import com.yj.hotfixsdk.document.impl.XMLDocumentImpl;
import com.yj.hotfixsdk.log.GLogger;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangjian on 2019/2/28.
 */

public class XMLDocumentUtils {

    public static <D extends BaseDocData> boolean createDocument(String xmlName, String xmlParentFileName,D d, Class<D> dClass) {

        if (xmlName == null || "".equals(xmlName)) {
            return false;
        }

        if (!xmlName.endsWith(".xml")) {

            xmlName = xmlName + ".xml";
        }

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator +
                xmlParentFileName + File.separator +
                xmlName);
        if (file.exists()) {
            HotFixFileUtils.delete(file.getParentFile().getAbsolutePath());
        }
        try {

            File parentFile = file.getParentFile();
            if(parentFile == null){

                parentFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator +
                        xmlParentFileName);
            }
            parentFile.mkdirs();
            GLogger.e("mkdirs 目录 ： " + parentFile.getAbsolutePath() + " 成功");
            file.createNewFile();
            GLogger.e("createNewFile file ： " + file.getAbsolutePath() + " 成功");
        } catch (IOException e) {

            GLogger.e("createNewFile file ： " + file.getAbsolutePath() + " 失败");
        }
        IXMLDocument<D> dxmlDocument = new XMLDocumentImpl();
        return dxmlDocument.createDocument(file, d, dClass);
    }

    public static <D extends BaseDocData> D parseDocument(String xmlName, String xmlParentFileName ,Class<D> dClass) {

        return parseAbsoluteDocument(xmlName,
                Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + xmlParentFileName,
                dClass);
    }

    public static <D extends BaseDocData> D parseAbsoluteDocument(String xmlName, String xmlParentFilePath,Class<D> dClass) {

        if (xmlName == null || "".equals(xmlName)) {
            return null;
        }
        if (!xmlName.endsWith(".xml")) {

            xmlName = xmlName + ".xml";
        }
        File file = new File(xmlParentFilePath + File.separator +
                xmlName);
        if (!file.exists()) {
            GLogger.e("file : " + file.getAbsolutePath() +" !!!!!!!!xml文件不存在");
            return null;
        }
        IXMLDocument<D> dxmlDocument = new XMLDocumentImpl();
        return dxmlDocument.parseDocument(file, dClass);
    }

}
