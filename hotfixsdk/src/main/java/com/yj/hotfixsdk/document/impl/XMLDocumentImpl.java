package com.yj.hotfixsdk.document.impl;

import com.yj.hotfixsdk.document.IXMLDocument;
import com.yj.hotfixsdk.document.data.BaseDocData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by yangjian on 2019/2/27.
 * 只支持对象 不支持泛型为列表
 */

public class XMLDocumentImpl<D extends BaseDocData> implements IXMLDocument<D> {

    @Override
    public boolean createDocument(File file, D d, Class<D> dClass) {

        if (d == null || file == null || !file.exists()) {
            return false;
        }

        if (dClass == null) {
            return false;
        }

        Field[] fields = dClass.getFields();

        if (fields == null) {
            return false;
        }

        try {
            // 初始化xml解析工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // 创建DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 创建Document
            Document doc = builder.newDocument();
            // standalone用来表示该文件是否呼叫其它外部的文件。若值是 ”yes” 表示没有呼叫外部文件
            doc.setXmlStandalone(true);
            doc.setXmlVersion("1.0");
            // 创建一个根节点
            // 说明: doc.createElement("元素名")、element.setAttribute("属性名","属性值")、element.setTextContent("标签间内容")

            Element rootEle = doc.createElement(d.root);
            doc.appendChild(rootEle);
            Element classEle = doc.createElement(dClass.getCanonicalName());
            rootEle.appendChild(classEle);
            for (Field field : fields) {
                if (field.getName() == null ||
                        "".equals(field.getName()) ||
                        field.getName().equals(d.root)) {
                    break;
                }
                Element fieldEle = doc.createElement(field.getName());
                Object value = field.get(d);
                if (value instanceof String) {

                    fieldEle.setTextContent((String) value);
                } else if (value instanceof Integer) {
                    fieldEle.setTextContent(value + "");
                } else if (value instanceof Long) {
                    fieldEle.setTextContent(value + "");
                } else if (value instanceof Float) {
                    fieldEle.setTextContent(value + "");
                } else if (value instanceof Double) {
                    fieldEle.setTextContent(value + "");
                }
                classEle.appendChild(fieldEle);
            }

            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);

            //export string
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transFormer.transform(domSource, new StreamResult(bos));

            FileOutputStream out = new FileOutputStream(file);
            StreamResult xmlResult = new StreamResult(out);
            transFormer.transform(domSource, xmlResult);
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    @Override
    public D parseDocument(File file, Class<D> dClass) {

        if (file == null || !file.exists()) {
            return null;
        }

        if (dClass == null) {
            return null;
        }

        Field[] fields = dClass.getFields();

        if (fields == null) {
            return null;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName(dClass.getCanonicalName());
            if (nodeList.getLength() < 1) {
                return null;
            }
            Element classEle = (Element) nodeList.item(0);
            D data = dClass.newInstance();
            NodeList fieldNodeList = classEle.getChildNodes();

            Field[] classFields = dClass.getFields();
            if (classFields == null || classFields.length <= 0) {

                return null;
            }
            for (int i = 0; i < fieldNodeList.getLength(); i++) {
                Node childNode = fieldNodeList.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childEle = (Element) childNode;
                    String fieldName = childEle.getNodeName();
                    for (Field field : classFields) {
                        if (field.getName().equals(fieldName)) {
                            field.setAccessible(true);
                            String dataString = childEle.getTextContent();
                            if (field.getType() == long.class) {
                                try {
                                    long datalong = Long.parseLong(dataString);
                                    field.set(data, datalong);
                                } catch (Exception e) {
                                }
                            } else if (field.getType() == String.class) {
                                field.set(data, dataString);
                            } else if (field.getType() == int.class) {
                                try {
                                    int dataint = Integer.parseInt(dataString);
                                    field.set(data, dataint);
                                } catch (Exception e) {
                                }
                            } else if (field.getType() == float.class) {
                                try {
                                    float datafloat = Float.parseFloat(dataString);
                                    field.set(data, datafloat);
                                } catch (Exception e) {
                                }
                            } else if (field.getType() == double.class) {
                                try {
                                    double datadouble = Double.parseDouble(dataString);
                                    field.set(data, datadouble);
                                } catch (Exception e) {
                                }
                            }

                        }
                    }
                }
            }
            return data;
        } catch (Exception e) {

            return null;
        }
    }
}
