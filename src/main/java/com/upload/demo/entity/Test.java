/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Test
 * Author:   00056929
 * Date:     2018/12/25 9:41
 * Description: test
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.upload.demo.entity;

import javafx.print.Collation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈test〉
 *
 * @author care.xu
 * @create 2018/12/25
 * @since 1.0.0
 */
public class Test {

    public static void main(String[] args) {
        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("12");
        List<String> li = new ArrayList<String>(Arrays.asList("test","no2"));
        li.add("no3");
        List<String> newLi = Collections.unmodifiableList(li);
        li.add("no4");
//        newLi.add("no5");
        System.out.println(li.toString());
        System.out.println(newLi.toString());
    }

}
