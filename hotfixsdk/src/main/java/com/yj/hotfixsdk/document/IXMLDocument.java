package com.yj.hotfixsdk.document;

import com.yj.hotfixsdk.document.data.BaseDocData;

import java.io.File;

/**
 * Created by yangjian on 2019/2/27.
 */

public interface IXMLDocument<D extends BaseDocData> {

    /**
     * 生成xml文件  目前只支持泛型类中的基本数据类型 对象 数组或者集合不支持
     */
    boolean createDocument(File file, D d,Class<D> dClass);

    /**
     * 解析xml文件 目前只支持泛型类中的基本数据类型 对象 数组或者集合不支持
     */
    D parseDocument(File file,Class<D> dClass);

}
