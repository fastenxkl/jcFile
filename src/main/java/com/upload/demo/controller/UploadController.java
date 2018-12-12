/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UploadController
 * Author:   00056929
 * Date:     2018/9/25 16:03
 * Description: 上传测试控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.upload.demo.entity.FileEntity;
import com.upload.demo.entity.HostEntity;
import com.upload.demo.entity.UserEntity;
import com.upload.demo.entity.ZtreeNodeEntity;
import com.upload.demo.util.FtpUtils;
import com.upload.demo.util.SmbFileUtil;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.upload.demo.util.CopyFileUtil.MESSAGE;

/**
 * 〈一句话功能简述〉<br> 
 * 〈上传测试控制类〉
 *
 * @author care.xu
 * @create 2018/9/25
 * @since 1.0.0
 */
@Controller
@RequestMapping("/upload")
@SpringBootConfiguration
public class UploadController {

    private static Logger logger=Logger.getLogger(UploadController.class);
    private File file;

    private String fileFileName;
    private String chunk; // 当前第几个分片
    private String chunks;// 总分片个数
    private String size;// 单个文件的总大小

    private String custNo;
    @Autowired
    private UserEntity userEntity;

    @Autowired
    private Environment environment;

    @Autowired
    private FileEntity fileEntity;


    //ftp信息
    private List<HostEntity> sendHostList;

    private String targetHostName = "";
    private Integer targetPort = 21;
    private String targetUserName = "";
    private String targetPassword = "";
    private String targetFilePath = "";



    @RequestMapping("/login")
    public String login(){
        return "page/login";
    }
    @RequestMapping("/test")
    public String test(){
        return "page/test";
    }
    @RequestMapping("/main")
    public String toMain(ModelMap model) {
        getFtpParam();
        model.addAttribute("targetHostName",targetHostName);
        model.addAttribute("targetFilePath",targetFilePath);
        model.addAttribute("sendHostList",sendHostList);
        return "page/main";
    }

    @RequestMapping("/upload")
    public String toDefault(ModelMap model) {
        getFtpParam();
        model.addAttribute("targetHostName",targetHostName);
        model.addAttribute("targetFilePath",targetFilePath);
        model.addAttribute("sendHostList",sendHostList);
        return "page/upload";
    }

    @RequestMapping(value = "/checkUser",produces = {"application/json;charset=utf-8"})
    public void checkUser(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map para = new HashMap();
        Map resultMap = new HashMap();
        String userName = String.valueOf(request.getParameter("userName"));
        String password = String.valueOf(request.getParameter("password"));
        if (!userName.equals("admin")) {
            resultMap.put("errorMsg", "用戶不存在");
        } else if (!password.equals("123456")) {
            resultMap.put("errorMsg", userName + "用户名或者密码不正确");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(resultMap));

    }


    @RequestMapping("/uploadFiles")
    public
    Map<String,String> uploadFiles(@RequestParam("file") MultipartFile[] fileUploads ,
                       @RequestParam(value = "custNo", required = false) String custNo,
                       @RequestParam(value = "chipVersion", required = false) String chipVersion,
                       @RequestParam(value = "batchNo", required = false) String batchNo,
                       @RequestParam(value = "fileDir", required = false) String fileDir,
                       @RequestParam(value = "selectHost", required = false) String selectHost,
                       @RequestParam(value = "path", required = false) String path,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"".equals(custNo)) {
            fileEntity.setCustNo(custNo);
        }
        if (!"".equals(chipVersion)) {
            fileEntity.setChipVersion(chipVersion);
        }
        if (!"".equals(batchNo)) {
            fileEntity.setBatchNo(batchNo);
        }
        if (!"".equals(fileDir)) {
            fileEntity.setFileDir(fileDir);
        }
        if (!"".equals(selectHost)) {
            fileEntity.setSelectHost(selectHost);
        }
        String dataState = "";
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String,String> map1 = new HashMap<>();
        //从xml获取相关ftp信息
        HostEntity hostEntity = new HostEntity();
        for (int i=0;i<sendHostList.size();i++) {
            if (sendHostList.get(i).getHostName().equals(selectHost)) {
                hostEntity = sendHostList.get(i);
            }
        }
        if (hostEntity.getHostName() == null) {
            map1.put("fileState","failed");
            map1.put("message","上传异常");
            return null;
        }
        //初始化ftp
        FtpUtils ftpUtils = new FtpUtils(hostEntity.getHostName(), new Integer(hostEntity.getPort()), hostEntity.getUserName(), hostEntity.getPassword());
        String ftpParentPath = "";
        FtpUtils targetFtpUtils = new FtpUtils(targetHostName, targetPort, targetUserName, targetPassword);
        String targetFtpParentPath = "";
        //TODO
        if (path.indexOf("//") > -1) {
            ftpParentPath = path.substring(path.indexOf("//")+1,path.length());
        } else {
            ftpParentPath = fileDir + "/"  + custNo +
                    "/"  + chipVersion + "/"  + batchNo;
        }

        targetFtpParentPath = targetFilePath + "/"  + custNo +
                "/"  + chipVersion + "/"  + batchNo;


        if (fileUploads.length > 0) {
            for (int i=0;i<fileUploads.length;i++){
                String fileName = fileUploads[i].getOriginalFilename();

                try {
                    String url = "smb://" + hostEntity.getUserName() + ":" + hostEntity.getPassword() + "@" + hostEntity.getHostName() + "/" + ftpParentPath;
                    SmbFileUtil smbFileUtil = SmbFileUtil.getInstance(url);
                    smbFileUtil.uploadFile(fileName,new BufferedInputStream(fileUploads[i].getInputStream()));
//                    ftpUtils.uploadFile(ftpParentPath,fileName,fileUploads[i].getInputStream());
                    targetFtpUtils.uploadFile(targetFtpParentPath,fileName,fileUploads[i].getInputStream());
                    map1.put("fileState","success");
                } catch (IOException e) {
                    e.printStackTrace();
                    map1.put("fileState","failed");
                    map1.put("message","上传异常");
                }
            }
        }
        response.getWriter().write(JSONObject.toJSONString(map1));
        return null;

    }

    @RequestMapping("queryAndDelete")
    public String queryAndDelete(ModelMap model,
    @RequestParam(value = "custNo", required = false) String custNo,
    @RequestParam(value = "chipVersion", required = false) String chipVersion){
        model.addAttribute("targetHostName",targetHostName);
        model.addAttribute("targetFilePath",targetFilePath);
        model.addAttribute("sendHostList",sendHostList);
        model.addAttribute("fileEntity",fileEntity);
        return "page/delete";
    }

    @RequestMapping("/deleteFiles")
    public String deleteFiles(@RequestParam(value = "filePathList[]", required = false) List<String> filePathList,
                                HttpServletRequest requests, HttpServletResponse response) throws IOException {
        SmbFile smbFile = null;
        int deleteNum = 0;
        Map<String,String> map = new HashMap<>();
        try {
        for (int i=0;i < filePathList.size(); i++) {
            smbFile = new SmbFile(filePathList.get(i));
            if(smbFile.exists()){
                smbFile.delete();
                System.out.println(filePathList.get(i) + "删除文件成功！");
                deleteNum ++;
            }
        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            map.put("error","删除异常");
        } catch (SmbException e) {
            e.printStackTrace();
            map.put("error","删除异常");
        }
        if (deleteNum == filePathList.size()) {
            map.put("success","you have success deleted " + deleteNum + " file(s)!!！");
//        } else {
//            map.put("message");
        }
        response.getWriter().write(JSONObject.toJSONString(map));
        return null;
    }

    @RequestMapping("/uploadFile")
    public Map<String,String> uploadFile(@RequestParam("file") MultipartFile[] fileUploads ,
                            @RequestParam(value = "pathDir", required = false) String pathDir,
                            @RequestParam(value = "custNo", required = false) String custNo,
                            @RequestParam(value = "chipVersion", required = false) String chipVersion,
                            @RequestParam(value = "batchNo", required = false) String batchNo,
                            HttpServletRequest request, HttpServletResponse response){
        String dataState = "";
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String,String> map1 = new HashMap<>();
        //初始化ftp
        FtpUtils ftpUtils = new FtpUtils(targetHostName, targetPort, targetUserName, targetPassword);
        String ftpParentPath = "/home/MappingStorage/SIPMapping/carexu";
//        ftpParentPath = ftpParentPath + File.separator + pathDir +File.separator + custNo +
//                File.separator + chipVersion + File.separator + batchNo;
        ftpParentPath = ftpParentPath + "/" + pathDir + "/"  + custNo +
                "/"  + chipVersion + "/"  + batchNo;

        if (fileUploads.length > 0) {
            for (int i=0;i<fileUploads.length;i++){
                String fileName = fileUploads[i].getOriginalFilename();

                //设定文件夹路径
//                SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd" );//按天建立文件夹
//                String fileDir = "D://" + "fileUpload" + File.separator + sFormat.format(Calendar.getInstance().getTime())+ new Random().nextInt(1000);
//
//                String filePath = fileDir + File.separator + fileName;
//                boolean uploadState = copyFile(fileUploads[i],filePath);
//                if (!uploadState){
//                    request.setAttribute("state","1");
//                    map1.put("fileState","failed")
//                    return map1;
//                }

                try {
                    ftpUtils.uploadFile(ftpParentPath,fileName,fileUploads[i].getInputStream());
                    map1.put("fileState","success");
                    response.getWriter().write(JSONObject.toJSONString(map1));
                } catch (IOException e) {
                    e.printStackTrace();
                    map1.put("fileState","failed");
                    map1.put("message","上传异常");
                }
            }
        }

        return null;

    }

    public boolean copyFile(MultipartFile fileUpload, String filePath){

        boolean uploadState = true;

        File file = new File(filePath);
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        if (file.exists()) {
            // 如果目标文件存在并允许覆盖
            //TODO
            new File(filePath).delete();
            /*if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }*/
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!file.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!file.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    MESSAGE="复制文件失败：创建目标文件所在目录失败";
                    logger.error(MESSAGE);
                    uploadState = false;
                }
            }

        }

        try
        {
            in = fileUpload.getInputStream();
            if (!in.equals(null)){
                out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];

                while ((byteread = in.read(buffer)) != -1) {
                    out.write(buffer, 0, byteread);
                    out.flush();
                }
            }


            out.close();
            in.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return uploadState;
    }

    /*@RequestMapping("/queryFileDir")
    public void queryFileDir(HttpServletRequest request, HttpServletRequest response){
        FtpUtils ftpUtils = new FtpUtils(targetHostName, targetPort, targetUserName, targetPassword);
        ftpUtils.initFtpClient();
        FtpUtils ftpUtilsQuery = new FtpUtils(targetHostName, targetPort, targetUserName, targetPassword);
        ftpUtilsQuery.initFtpClient();
        try {
            String[] fileStrings = ftpUtilsQuery.ftpClient.listNames(ftpParentPath);
            for (String f:fileStrings){
                System.out.println("fileStrings:"+f);
            }
            FTPFile[] files = ftpUtilsQuery.ftpClient.listFiles(ftpParentPath);
            for (FTPFile fs:files){
                System.out.println("files:"+ fs.toString());
            }
            ftpUtilsQuery.ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    /***
     * 获取xml中ftp参数
     */
    /**
     public void getFtpParam(){
        sendHostName = environment.getProperty("send.hostName");
        sendPort = Integer.valueOf(environment.getProperty("send.port"));
        sendUserName = environment.getProperty("send.userName");
        sendPassword = environment.getProperty("send.password");

        sendFilePath = new HashMap();
        for (int i = 1; i <= new Integer(environment.getProperty("send.filePath.max")); i++) {
            sendFilePath.put(i,environment.getProperty("send.filePath." + i));
        }
        targetHostName = environment.getProperty("target.hostName");
        targetPort = Integer.valueOf(environment.getProperty("target.port"));
        targetUserName = environment.getProperty("target.userName");
        targetPassword = environment.getProperty("target.password");
        targetFilePath = environment.getProperty("target.filePath");
    }
     */

    public void getFtpParam() {
        sendHostList = new ArrayList<>();
        HostEntity hostEntity;
        Map filePath;
        for (int i = 1;i <= new Integer(environment.getProperty("sendList"));i++) {
            hostEntity = new HostEntity();
            hostEntity.setHostIndex(i+"");
            hostEntity.setHostName(environment.getProperty("send" + i + ".hostName"));
            hostEntity.setPort(environment.getProperty("send" + i + ".port"));
            hostEntity.setUserName(environment.getProperty("send" + i + ".userName"));
            hostEntity.setPassword(environment.getProperty("send" + i + ".password"));
            filePath = new HashMap();
            for (int j = 1;j <= new Integer(environment.getProperty("send" + i +".filePath.max"));j++) {
                filePath.put(j,environment.getProperty("send" + i + ".filePath." + j));
            }
            hostEntity.setFilePath(filePath);
            sendHostList.add(hostEntity);
        }

        targetHostName = environment.getProperty("target.hostName");
        targetPort = Integer.valueOf(environment.getProperty("target.port"));
        targetUserName = environment.getProperty("target.userName");
        targetPassword = environment.getProperty("target.password");
        targetFilePath = environment.getProperty("target.filePath");
    }

    /**
     * 目录文件查询
     * @param ztreeNodeEntity
     * @param smbFileParent
     */
    public void findDir(ZtreeNodeEntity ztreeNodeEntity, SmbFile smbFileParent) {
        SmbFile smbFile = null;
        try {
            SmbFile[] test = smbFileParent.listFiles();
            List<ZtreeNodeEntity> ztreeNodeEntityList = new ArrayList<ZtreeNodeEntity>();
            for (int i=0;i<test.length;i++) {
                SmbFile smbFileTemp = test[i];
                ZtreeNodeEntity ztreeNodeEntityTmp = new ZtreeNodeEntity();
                ztreeNodeEntityTmp.setPid(ztreeNodeEntity.getId());
                ztreeNodeEntityTmp.setId(smbFileTemp.getUncPath());
                ztreeNodeEntityTmp.setPath(smbFileTemp.getPath());
                ztreeNodeEntityTmp.setNodeLev(ztreeNodeEntity.getNodeLev()+1);
                if (smbFileTemp.isDirectory()){
                    ztreeNodeEntityTmp.setFileFlag(false);
                    ztreeNodeEntityTmp.setParent(true);
                    ztreeNodeEntityTmp.setName(smbFileTemp.getName().substring(0,smbFileTemp.getName().length()-1));
                    ztreeNodeEntityList.add(ztreeNodeEntityTmp);
//                    findDir(ztreeNodeEntityTmp, smbFileTemp);
                } else if (smbFileTemp.isFile()) {
                    ztreeNodeEntityTmp.setFileFlag(true);
                    ztreeNodeEntityTmp.setParent(false);
                    ztreeNodeEntityTmp.setName(smbFileTemp.getName());
                    ztreeNodeEntityList.add(ztreeNodeEntityTmp);
                    System.out.println(smbFileTemp.getPath());
                }

            }
            ztreeNodeEntity.setChildren(ztreeNodeEntityList);
        } catch (SmbException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/queryFile")
    @ResponseBody

    public ZtreeNodeEntity getDir(@RequestParam(value = "hostIp") String hostIp,
                                  @RequestParam(value = "path") String path) throws MalformedURLException, SmbException {
//        path = "smb://mp:cd890890@172.17.255.93//mapping/test/";

//        path = "smb://" + sendUserName + ":" + sendPassword + "@" + sendHostName + "/" +path +"/";
        HostEntity hostEntity = new HostEntity();
        for (int i=0;i<sendHostList.size();i++) {
            if (sendHostList.get(i).getHostName().equals(hostIp)) {
                hostEntity = sendHostList.get(i);
            }
        }
        path = spiltSuffix(path) + "/";
        if (path.indexOf("smb") == -1 || path.indexOf("//") == -1 ) {
            path = path.substring(path.indexOf("//") +1,path.length());
            path = "smb://" + hostEntity.getUserName() + ":" + hostEntity.getPassword() + "@" + hostIp + "/" +path;
        }
            System.out.println(path);
        ZtreeNodeEntity ztreeNodeEntity = new ZtreeNodeEntity();
        SmbFile smbFileParent = new SmbFile(path);
        ztreeNodeEntity.setId(smbFileParent.getUncPath());
        ztreeNodeEntity.setPath(path);
        ztreeNodeEntity.setNodeLev(0);
        if (smbFileParent.isDirectory()){
            ztreeNodeEntity.setFileFlag(false);
            ztreeNodeEntity.setName(smbFileParent.getUncPath());
//            ztreeNodeEntity.setName(smbFileParent.getName().substring(0,smbFileParent.getName().length()-1));
        } else {
            ztreeNodeEntity.setFileFlag(true);
            ztreeNodeEntity.setName(smbFileParent.getName());
        }
        ztreeNodeEntity.setParent(true);
        findDir(ztreeNodeEntity, smbFileParent);
        System.out.println(ztreeNodeEntity.toString());
        return ztreeNodeEntity;
    }

    public String spiltSuffix(String str){
        if (str.endsWith("/") || str.endsWith("\\")){
            str = str.substring(0,str.length()-1);
            spiltSuffix(str);
        }
        return str;
    }

    @RequestMapping("/createDir")
    @ResponseBody
    public String createDir(@RequestParam(value = "hostIp") String hostIp,
                            @RequestParam(value = "path") String path,
                            @RequestParam(value = "fileName") String fileName,
                            HttpServletResponse response) throws IOException {
        HostEntity hostEntity = new HostEntity();
        Map<String,String> map = new HashMap<>();
        for (int i=0;i<sendHostList.size();i++) {
            if (sendHostList.get(i).getHostName().equals(hostIp)) {
                hostEntity = sendHostList.get(i);
            }
        }
        path = spiltSuffix(path) + "/";
        if (path.indexOf("smb") == -1 || path.indexOf("//") == -1 ) {
            path = path.substring(path.indexOf("//") +1,path.length());
            path = "smb://" + hostEntity.getUserName() + ":" + hostEntity.getPassword() + "@" + hostIp + "/" +path;
        }
        String url = path + fileName;
        SmbFileUtil smbFileUtil = SmbFileUtil.getInstance(url);
        boolean createState = smbFileUtil.makeDir();
        if (createState) {
            map.put("success","文件夹 \""+fileName+"\" 创建成功！");
        } else {
            map.put("message","文件夹 \""+fileName+"\" 创建失败！");
        }
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONObject.toJSONString(map));
        return null;
    }

    public static void main(String[] args) throws SmbException, MalformedURLException {
//        String url = "smb://" + sendUserName + ":" + sendPassword + "@" + sendHostName + "/" + ftpParentPath;
//        String path = "smb://mp:cd890890@172.17.255.93//mapping/test/";
        String path = "smb://mp:cd890890@172.17.255.93//mapping/test/1/1/1/1/";
//        ZtreeNodeEntity ztreeNodeEntity = new ZtreeNodeEntity();
//        SmbFile smbFileParent = new SmbFile(path);
//        ztreeNodeEntity.setId(smbFileParent.getUncPath());
//        ztreeNodeEntity.setPath(path);
//        ztreeNodeEntity.setNodeLev(0);
//        ztreeNodeEntity.setName(smbFileParent.getName());
//        ztreeNodeEntity.setParent(true);
////        findDir(ztreeNodeEntity, smbFileParent);
//        System.out.println(ztreeNodeEntity);

        SmbFileUtil smbFileUtil = SmbFileUtil.getInstance(path + "xkl");
        smbFileUtil.makeDir();


    }

    @RequestMapping("/toQuery")
    public String toQuery() {
        return "page/queryFile";
    }
}
