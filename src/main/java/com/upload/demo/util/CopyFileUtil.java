package com.upload.demo.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by gavin on 2016/11/1.
 */
public class CopyFileUtil {

    private static Logger logger=Logger.getLogger(CopyFileUtil.class);
    public static String MESSAGE = "0";

    public static String delFile(String fileName){
        File delFile=new File(fileName);
        // 判断源文件是否存在
        if (!delFile.exists()) {
            MESSAGE = "文件：" + delFile + "不存在！";
            return MESSAGE;
        }
        if(delFile.delete()){
            MESSAGE = "文件：" + delFile + "删除成功！";
        }
        return MESSAGE;
    }
    public static String delFile(File file){
        // 判断源文件是否存在
        if (!file.exists()) {
            MESSAGE = "文件：" + file.getName() + "不存在！";
            return MESSAGE;
        }
        if(file.delete()){
            MESSAGE = "文件：" + file.getName() + "删除成功！";
        }
        return MESSAGE;
    }
    /**
     * 复制单个文件
     *
     * @param srcFileName  待复制的文件名
     * @param destFileName 目标文件名
     * @param overlay      如果目标文件存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static String copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        MESSAGE = "0";
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            MESSAGE = "源文件：" + srcFileName + "不存在！";
            return MESSAGE;
        } else if (!srcFile.isFile()) {
            MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
            return MESSAGE;
        }

        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    MESSAGE="复制文件失败：创建目标文件所在目录失败";
                    logger.error(MESSAGE);
                    return MESSAGE;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return MESSAGE;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制整个目录的内容
     *
     * @param srcDirName  待复制目录的目录名
     * @param destDirName 目标目录名
     * @param overlay     如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static String copyDirectory(String srcDirName, String destDirName,
                                        boolean overlay) {
        MESSAGE = "0";
        // 判断源目录是否存在
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
            return MESSAGE;
        } else if (!srcDir.isDirectory()) {
            MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
            return MESSAGE;
        }

        // 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File destDir = new File(destDirName);
        // 如果目标文件夹存在
        if (destDir.exists()) {
            // 如果允许覆盖则删除已存在的目标目录
            if (overlay) {
                new File(destDirName).delete();
            } else {
                MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
                return MESSAGE;
            }
        } else {
            // 创建目的目录
           logger.info(destDir+"目的目录不存在，准备创建。。。");
            if (!destDir.mkdirs()) {
               logger.error("复制目录失败：创建目的目录失败！");
                return "复制目录失败：创建目的目录失败！";
            }
        }

        String result  = "";
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 复制文件
            if (files[i].isFile()) {
                result = CopyFileUtil.copyFile(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!"0".equals(result)){
                    break;
                }

            } else if (files[i].isDirectory()) {
                result = CopyFileUtil.copyDirectory(files[i].getAbsolutePath(),
                        destDirName + files[i].getName(), overlay);
                if (!"0".equals(result)){
                    break;
                }
            }
        }
        if (!"0".equals(result)) {
            MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
            return MESSAGE;
        } else {
            return MESSAGE;
        }
    }

    public static void main(String[] args) {
        String srcDirName = "D:\\ftpdata\\LocalUser\\stripMapFile\\processingMap\\";
        String destDirName = "D:\\ftpdata\\LocalUser\\stripMapFile\\mapHis\\version";
//        CopyFileUtil.copyDirectory(srcDirName, destDirName, true);
        File destFile = new File("D:\\ftpdata\\LocalUser\\stripMapFile\\processingMap\\ABG123129-12319.093.txt");
        String fileName=destFile.getName();
        System.out.println(destFile.getName());
        System.out.println(destFile.getAbsoluteFile());
        System.out.println(destFile.getAbsolutePath());
        System.out.println(fileName.substring(0,fileName.lastIndexOf(".")));
        System.out.println(fileName.substring(fileName.lastIndexOf(".")+1));

    }
}
