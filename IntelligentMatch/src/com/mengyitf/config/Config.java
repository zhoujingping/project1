package com.mengyitf.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by feifei on 2017/10/22.
 */
public class Config {
    private String importfilePath = null;  // 导入数据文件路径
    private String outputfilePath = null;  // 到处数据文件路径
    private ArrayList<String> priority = new ArrayList<>(); // 优先级

    public Config(){}

    /*构造函数-是否设置文件路径*/
    public Config(Boolean isSetFilePath){
        if (isSetFilePath) setFilePath();
    }

    /*设置文件路径*/
    private void setFilePath(){
        File confFile = new File("src/config/conf");
        try {
            /*判断配置文件是否存在*/
            if (!confFile.exists()){
                confFile.createNewFile();
                System.out.println("您的配置文件是空的:"+confFile.getAbsolutePath());
                System.exit(1);
            }
            /*开始读文件*/
            BufferedReader br = new BufferedReader(new FileReader(confFile));
            String str;
            String info[];
            while ((str = br.readLine())!=null){
                info = str.split(" ");
                /*判断本行是属于什么类型*/
                switch (info[0].trim()){
                    /*导入文件路径*/
                    case "importfile":{
                        if (info[1].trim().equals(" ")){
                            System.out.println("您的配置文件导入路径存在空格:"+str);
                            System.exit(1);
                        }
                        File importfile = new File(info[1]);
                        if (!importfile.exists()){
                            System.out.println("您的导入文件不存在:"+info[1]);
                            System.exit(1);
                        }
                        importfilePath = info[1];
                        break;
                    }
                    /*导出文件路径*/
                    case "outputfile":{
                        if (info[1].trim().equals(" ")){
                            System.out.println("您的配置文件输出路径存在空格:"+str);
                            System.exit(1);
                        }
                        outputfilePath = info[1];
                        break;
                    }
                    /*优先级*/
                    case "priority":{
                        for (int i = 1;i < info.length;i++){
                            priority.add(info[i]);
                        }

                    }
                }
            }
            br.close();
            if (importfilePath == null){
                System.out.println("您的导入文件路径是空的");
                System.exit(1);
            }
            if (outputfilePath == null){
                System.out.println("您的导出文件路径是空的");
                System.exit(1);
            }
            if (priority.size()==0){
                System.out.println("您的优先级是空的");
                System.exit(1);
            }
        }catch (IOException ioe){
            System.out.println("您的配置文件是空的:"+confFile.getAbsolutePath());
            System.exit(1);
        }
    }

    public String getImportfilePath() {
        return importfilePath;
    }

    public void setImportfilePath(String importfilePath) {
        this.importfilePath = importfilePath;
    }

    public String getOutputfilePath() {
        return outputfilePath;
    }

    public void setOutputfilePath(String outputfilePath) {
        this.outputfilePath = outputfilePath;
    }

    public ArrayList<String> getPriority() {
        return priority;
    }

    public void setPriority(ArrayList<String> priority) {
        this.priority = priority;
    }
}
