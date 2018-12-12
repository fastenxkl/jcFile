/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: SmbFileUtil
 * Author:   00056929
 * Date:     2018/10/9 15:03
 * Description: 共享文件夹工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.util;

import com.upload.demo.entity.ZtreeNodeEntity;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import jcifs.util.LogStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈共享文件夹工具类〉
 *
 * @author care.xu
 * @create 2018/10/9
 * @since 1.0.0
 */
public class SmbFileUtil {
    private static LogStream log = LogStream.getInstance();
    private String url = "";
    private SmbFile smbFile = null;
    private SmbFileOutputStream smbOut = null;
    private static SmbFileUtil smbFileUtil = null; //共享文件协议

    public static synchronized SmbFileUtil getInstance(String url) {
        if (smbFileUtil == null) {
            return new SmbFileUtil(url);
        }
        return smbFileUtil;
    }

    /**
     * @param url 服务器路径
     */
    private SmbFileUtil(String url) {
        this.url = url;
        this.init();
    }

    public void init() {
        try {
            log.println("开始连接...url：" + this.url);
            smbFile = new SmbFile(this.url);
            smbFile.connect();
            log.println("连接成功...url：" + this.url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.print(e);
        } catch (IOException e) {
            e.printStackTrace();
            log.print(e);
        }
    }


    /**
     * 上传文件到服务器
     */
    public int uploadFile(File file) {
        int flag = -1;
        BufferedInputStream bf = null;
        try {
            smbOut = new SmbFileOutputStream(this.url + "/" + file.getName(), false);
            bf = new BufferedInputStream(new FileInputStream(file));
            byte[] bt = new byte[8192];
            int n = bf.read(bt);
            while (n != -1) {
                this.smbOut.write(bt, 0, n);
                this.smbOut.flush();
                n = bf.read(bt);
            }
            flag = 0;
            log.println("文件传输结束...");
        } catch (SmbException e) {
            e.printStackTrace();
            log.println(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.println(e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.println("找不到主机...url：" + this.url);
        } catch (IOException e) {
            e.printStackTrace();
            log.println(e);
        } finally {
            try {
                if (null != this.smbOut) {
                    this.smbOut.close();
                }
                if (null != bf) {
                    bf.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * 上传文件到服务器
     */
    public int uploadFile(String fileName, BufferedInputStream fileInputStream) {
        int flag = -1;
        BufferedInputStream bf = null;
        SmbFile remoteFile = null;
        try {
            remoteFile = new SmbFile(this.url);
            if(!remoteFile.exists()){
                //创建远程文件夹
                remoteFile.mkdirs();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        try {
            smbOut = new SmbFileOutputStream(this.url + "/" + fileName, false);
//            bf = new BufferedInputStream(new FileInputStream(file));
            bf = fileInputStream;
            byte[] bt = new byte[8192];
            int n = bf.read(bt);
            while (n != -1) {
                this.smbOut.write(bt, 0, n);
                this.smbOut.flush();
                n = bf.read(bt);
            }
            flag = 0;
            log.println("文件传输结束...");
        } catch (SmbException e) {
            e.printStackTrace();
            log.println(e);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.println(e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            log.println("找不到主机...url：" + this.url);
        } catch (IOException e) {
            e.printStackTrace();
            log.println(e);
        } finally {
            try {
                if (null != this.smbOut) {
                    this.smbOut.close();
                }
                if (null != bf) {
                    bf.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return flag;
    }

    public boolean deleteFile (String url) {
        SmbFile smbFile= null;
        try {
            smbFile = new SmbFile(url);
            if(smbFile.exists()){
                smbFile.delete();
                System.out.println(url + "删除文件成功！");
                return true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getFileList(String path){
        try {
            SmbFile smbFile = new SmbFile(path);
            System.out.println(smbFile.list());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SmbException, MalformedURLException {
        String path = "smb://mp:cd890890@172.17.255.93//mapping/test/";
        ZtreeNodeEntity ztreeNodeEntity = new ZtreeNodeEntity();
        SmbFile smbFileParent = new SmbFile(path);
        ztreeNodeEntity.setId(smbFileParent.getUncPath());
        ztreeNodeEntity.setPath(path);
        ztreeNodeEntity.setNodeLev(0);
        ztreeNodeEntity.setName(smbFileParent.getName());
        ztreeNodeEntity.setParent(true);
        findDir(ztreeNodeEntity, smbFileParent);
        System.out.println(ztreeNodeEntity);

    }

    public static void findDir(ZtreeNodeEntity ztreeNodeEntity,SmbFile smbFileParent) {
        SmbFile smbFile = null;
        try {
            SmbFile[] test = smbFileParent.listFiles();
            List<ZtreeNodeEntity> ztreeNodeEntityList = new ArrayList<ZtreeNodeEntity>();
            for (int i=0;i<test.length;i++) {
                SmbFile smbFileTemp = test[i];
                ZtreeNodeEntity ztreeNodeEntityTmp = new ZtreeNodeEntity();
                ztreeNodeEntityTmp.setPid(ztreeNodeEntity.getId());
                ztreeNodeEntityTmp.setPath(smbFileTemp.getPath());
                ztreeNodeEntityTmp.setName(smbFileTemp.getName());
                ztreeNodeEntityTmp.setNodeLev(ztreeNodeEntity.getNodeLev()+1);
                if (smbFileTemp.isDirectory()){
                    ztreeNodeEntityTmp.setParent(true);
                    ztreeNodeEntityList.add(ztreeNodeEntityTmp);
                    findDir(ztreeNodeEntityTmp, smbFileTemp);
                } else if (smbFileTemp.isFile()) {
                    ztreeNodeEntityTmp.setParent(false);
                    ztreeNodeEntityList.add(ztreeNodeEntityTmp);
                    System.out.println(smbFileTemp.getPath());
                }

            }
            ztreeNodeEntity.setChildren(ztreeNodeEntityList);
        } catch (SmbException e) {
            e.printStackTrace();
        }

    }

    public boolean makeDir(){
        try {
            if (!smbFile.exists()) {
                smbFile.mkdir();
                System.out.println( "创建文件成功！");
                return true;
            }
        }  catch (SmbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
