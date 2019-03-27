package com.yj.hotfixsdk.builder.hotfix.file;

import com.yj.hotfixsdk.log.GLogger;

import java.io.File;

/**
 * Created by yangjian on 2019/3/1.
 */

public class HotFixFileUtils {

    public static File getXmlFile(File file, String xmlName) {

        if (file == null || xmlName == null || "".equals(xmlName)) {
            return null;
        }

        File[] fileList = file.listFiles();

        if (fileList == null || fileList.length <= 0) {
            return null;
        }
        for (File file1 : fileList) {

            if (file1.exists() && file1.getName().endsWith(".xml") &&
                    file1.getName().contains(xmlName)) {

                return file1;
            }
        }
        return null;
    }


    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                GLogger.d("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                GLogger.e("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            GLogger.e("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            GLogger.e("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            GLogger.e("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            GLogger.d("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
