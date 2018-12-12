/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: FileEntity
 * Author:   00056929
 * Date:     2018/11/29 16:57
 * Description: 文件实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈文件实体类〉
 *
 * @author care.xu
 * @create 2018/11/29
 * @since 1.0.0
 */
@Component
public class FileEntity {
    public String custNo;
    public String chipVersion;
    public String batchNo;
    public String fileDir;
    public String selectHost;


    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getChipVersion() {
        return chipVersion;
    }

    public void setChipVersion(String chipVersion) {
        this.chipVersion = chipVersion;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getSelectHost() {
        return selectHost;
    }

    public void setSelectHost(String selectHost) {
        this.selectHost = selectHost;
    }
}
