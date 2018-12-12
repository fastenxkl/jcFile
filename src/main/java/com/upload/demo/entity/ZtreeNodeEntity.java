/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ZtreeNodeEntity
 * Author:   00056929
 * Date:     2018/9/29 15:53
 * Description: 文件夹树节点实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈文件夹树节点实体类〉
 *
 * @author care.xu
 * @create 2018/9/29
 * @since 1.0.0
 */
public class ZtreeNodeEntity {
    private String id;
    private String pid;
    private String name;
    private String path;
    private boolean parent;
    private boolean fileFlag;
    private int nodeLev;
    private int size;
    private List<ZtreeNodeEntity> children;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public List<ZtreeNodeEntity> getChildren() {
        return children;
    }
    public void setChildren(List<ZtreeNodeEntity> children) {
        this.children = children;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isParent() {
        return parent;
    }
    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public int getNodeLev() {
        return nodeLev;
    }

    public void setNodeLev(int nodeLev) {
        this.nodeLev = nodeLev;
    }

    public boolean isFileFlag() {
        return fileFlag;
    }

    public void setFileFlag(boolean fileFlag) {
        this.fileFlag = fileFlag;
    }

    @Override
    public String toString() {
        return "ZtreeNodeEntity{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", parent=" + parent +
                ", fileFlag=" + fileFlag +
                ", nodeLev=" + nodeLev +
                ", size=" + size +
                ", children=" + children +
                '}';
    }
}
