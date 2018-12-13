package com.upload.demo.util;

/**
 * Created by gavin on 15/12/9.
 * 解密类，与JCOA同步
 */


public class DigestUtil {

    public static String passwordDeEncrypt(String password){
        String result="";
        String rc1="";
        String temp1;
        int l,k;

        String PKey="0231042620191022290301120823283206301118152524132717071605210914";
        if(password.length()!=33){
            result=password;
        }else{
            l=password.charAt(password.length()-1)-88;
            for(int i=1;i<=l;i++){
                temp1=PKey.substring(i*2-2,i*2);
                int mm=Integer.parseInt(temp1);
                temp1=password.substring(mm-1,mm);
                mm=(int)temp1.toCharArray()[0];
                k=126-mm+40;
                rc1=rc1+(char)k;
            }

            int slength=rc1.length();
            for(int j=0;j<slength;j=j+2){
                temp1=rc1.substring(j,j+2);
                k=hexToInt(temp1);
                result=result+(char)k;
                //System.out.println(result);
            }

        }

        return result;
    }

    public static int hexToInt(String str){
        int res=0;
        int mm=0;
        int k=1,w=1;
        for(int i=0;i<str.length();i++){
            //mm=Integer.parseInt(String.valueOf(str.charAt(i)));
            mm=str.charAt(i);
            if(mm>=48&&mm<=57){
                k=mm-48;
            }else if(mm>=65&&mm<=70){
                k=mm-55;
            }else{
                k=0;
            }
            res=res*16+k;

            //res = res*16  + Integer.parseInt(String.valueOf(str.charAt(i)), 16);
        }
        return res;
    }
    public static void main(String[] args){
        String str="123456789ABCDE";
        System.out.println(str.substring(str.length() - 2, str.length()));
        System.out.println(Integer.parseInt("E", 16));

        System.out.println(passwordDeEncrypt("123456"));
        System.out.println("00007965   施宾:"+passwordDeEncrypt("srso.scs,str((`\\+emo?tvCksSssmcql"));
        System.out.println("00013310   徐丽B:"+passwordDeEncrypt("prtpTs[pLpsmvhsm,vap;oarpq4ppvcop"));
        System.out.println("00010854   吴炜:"+passwordDeEncrypt("srnpIsms.ssq[yt+monsfpp=eedssusrn"));
        System.out.println("00000716   许金华:"+passwordDeEncrypt("sssszr>tHspoa^p*qtvsEvb1tuJrsutun"));
        System.out.println("C0009041   夏雪:"+passwordDeEncrypt("ssvsFs}sYssom^sYUpqsVqtsou-ssnqpp"));
        System.out.println("00017782   彭泽良:"+passwordDeEncrypt("srvpjs(sfrsuax8`^tup5vsZS`Usrvcql"));
        System.out.println("00017782   彭泽良:"+passwordDeEncrypt("ssqsbsHs9sspq*s;Aqpshutsqm+sssqup"));
        System.out.println("B0000050   彭泽良:"+passwordDeEncrypt("srup=?UsWo[t=B<q,-qpors_ps]stQerh"));
        System.out.println("00017782   苏一青:"+passwordDeEncrypt("pqoqts1quosq.=<?7nsr*u`w^ndsopeul"));
        System.out.println("00013138   彭泽良:"+passwordDeEncrypt("sqmo7s:s+ssnv>sx@tuoHutrrnHssvmup"));
        System.out.println("00047222   彭泽良:"+passwordDeEncrypt("sqqp5sQs0sspMwtsRtqoFqs,unQsspevn"));
        System.out.println("00049237   彭泽良:"+passwordDeEncrypt("srqpNsPsussuT4/pKratLpugScBssscpl"));
        System.out.println("00031261   杨时波:"+passwordDeEncrypt("sqtqCsVsctsqN5s}ystpaanvss<ssumnn"));
        System.out.println("B0000311   汪胜军:"+passwordDeEncrypt("sqtoss.s6stsK{tAouep5ur*us^sspoqn"));
        System.out.println("00035199   王琛:"+passwordDeEncrypt("sqqqtstsDosvvIsesuuoeuqsvussstuov"));
        System.out.println("00033393   康鹏:"+passwordDeEncrypt("sqvp4<|sBsDt_,DDg*spuns\\E`)ts8tah"));
        System.out.println("B0001192   周峰:"+passwordDeEncrypt("stuqms/smostuk<j+purxes^|uhssqurl"));
    }
}
