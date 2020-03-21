package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WC {

    private static String func;//功能参数
    private static String filePath;//文件路径

    public static void main(String[] args) throws Exception {

        filePath="C:\\Users\\win10\\Desktop\\测试\\看看\\hello.c";
        //判断是否有足够的参数
        if (args.length<2){
            System.out.println("请输入足够的参数...");
            return;
        }
        func = args[0];
        filePath = args[1];
        //判断文件是否存在
        File file = new File(filePath);
        if (!file.exists()){
            System.out.println("你指定的文件不存在....");
            return;
        }

        switch (func){
            case "-w":countWord(null,filePath);break;
            case "-c":countChar(null,filePath);break;
            case "-l":countLine(null,filePath);break;
            case "-s":countDir(filePath);break;
            case "-a":countComplex(filePath);break;
            default:
                System.out.println("您输入的参数有误");
        }

    }

    public static void  countLine (String fileName,String absolutePath) throws Exception {
        BufferedReader br=new BufferedReader(new FileReader(new File(absolutePath)));
        String line;
        int sum = 0;
        while((line=br.readLine())!=null) {
            sum++;
        }
        System.out.println((fileName==null?"":fileName)+"文件行数为："+sum);
    }

    public static void  countChar(String fileName,String absolutePath) throws Exception {

        BufferedReader br=new BufferedReader(new FileReader(new File(absolutePath)));
        int sum = 0 ;
        String line;
        while ((line = br.readLine()) != null){
            sum+=line.length();
        }
        System.out.println((fileName==null?"":fileName)+"文件字符数为："+sum);

    }

    //统计单词数
    public static void countWord(String fileName,String absolutePath) throws Exception {

        BufferedReader br=new BufferedReader(new FileReader(new File(absolutePath)));
        int sum = 0 ;
        String line;
        String reg = "[\\s]+";
        while((line=br.readLine())!=null) {
            line = line.replaceAll("[/*{}\\.]","");//去掉注释符号及其他字符
            String[] split = line.split(reg);
            if (!"".equals(split[0]))//排除空行影响
            sum += split.length ;
        }
        System.out.println((fileName==null?"":fileName)+"文件单词数为："+sum);
    }

    /**
     * 递归处理目录的子文件
     */
    public static void countDir(String filePath) throws Exception {
        File dir = new File(filePath);
        if (!dir.isDirectory() || !dir.exists()){
            System.out.println("你指定的不是目录");
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            //还有目录，再递归
            if (file.isDirectory()){
                countDir(file.getAbsolutePath());
            }else {
                String name = file.getName();
                String absolutePath = file.getAbsolutePath();
                //调用其他三个方法
                countLine(name,absolutePath);
                countChar(name,absolutePath);
                countWord(name,absolutePath);
            }
        }

    }

    /**
     * 复杂统计
     */
    public static void countComplex(String filePath) throws IOException {
        int spaceNum = 0;//空行
        int annoNum = 0;//注释行
        int codeNum = 0;//代码行
        BufferedReader br=new BufferedReader(new FileReader(new File(filePath)));
        String line;
        String reg = "";
        while ( (line = br.readLine()) != null){
            String s = line.replaceAll("[\\s;]", "");
            if (s.length() == 0){//空行
                spaceNum++;
            }else if (s.length() == 1 && ("{".equals(s) || "}".equals(s)) ){//单字符空行
                spaceNum++;
            }else if ( (s.startsWith("{") || s.startsWith("}") ) && s.contains("//")){//单字符的注释行
                annoNum++;
            }else if (s.startsWith("//")){//单行注释
                annoNum++;
            }else if (s.startsWith("/*") && s.length()>=4 && s.endsWith("*/")){//单行注释
               annoNum++;
            }else if (s.startsWith("/*")){//多行注释
                annoNum++;
                while (true){
                    if ((line = br.readLine()) != null){
                        annoNum++;
                        if (line.endsWith("*/"))break;//多行注释结束
                    }else {
                        break;
                    }
                }
            }else {
                codeNum++;//代码行
            }
        }
        System.out.println("文件空行数为："+spaceNum);
        System.out.println("文件注释行数为："+annoNum);
        System.out.println("文件代码行数为："+codeNum);
    }
}
