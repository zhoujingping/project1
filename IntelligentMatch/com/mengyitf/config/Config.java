package com.mengyitf.config;

import com.mengyitf.model.Log;

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
    private Log log = new Log();
    public Config(){}

    /*构造函数-是否设置文件路径*/
    public Config(Boolean isSetFilePath){
        if (isSetFilePath){
            setFilePath();
        }
    }

    /*设置文件路径*/
    private void setFilePath(){
        File confFile = new File(new File("").getAbsolutePath()+"\\config\\conf");
        try {
            /*判断配置文件是否存在*/
            if (!confFile.exists()){
                confFile.createNewFile();
                log.writeLog("您的配置文件是空的:"+confFile.getAbsolutePath());
                
                System.exit(1);
            }
            /*开始读文件*/
            BufferedReader br = new BufferedReader(new FileReader(confFile));
            String str;
            String info[];
            int row = 0;
            while ((str = br.readLine())!=null){
                row++;
                info = str.split(" ");
                /*判断本行是属于什么类型*/
                switch (info[0].trim()){
                    /*输入文件路径*/
                    case "importfile":{
                        if (info[1].trim().equals(" ")){
                            log.writeLog("您的配置文件输入路径存在空格("+row+"):"+str);
                            System.exit(1);
                        }
                        File importfile = new File(info[1]);
                        if (!importfile.exists()){
                            log.writeLog("您的输入文件不存在("+row+"):"+info[1]);
                        }
                        importfilePath = info[1];
                        log.writeLog("已设置输入文件:"+info[1]);
                        break;
                    }
                    /*导出文件路径*/
                    case "outputfile":{
                        if (info[1].trim().equals(" ")){
                            log.writeLog("您的配置文件输出路径存在空格("+row+"):"+str);
                            
                            System.exit(1);
                        }
                        outputfilePath = info[1];
                        log.writeLog("已设置输出文件:"+info[1]);
                        break;
                    }
                    /*优先级*/
                    case "priority":{
                        for (int i = 1;i < info.length;i++){
                            priority.add(info[i]);
                            log.writeLog("已设置优先级:"+info[i]);
                        }
                    }
                }
            }
            br.close();
            if (importfilePath == null){
                log.writeLog("您的输入文件路径是空的");
            }
            if (outputfilePath == null){
                log.writeLog("您的导出文件路径是空的");
            }
            if (priority.size()==0){
                log.writeLog("您的优先级是空的");
            }
        }catch (IOException ioe){
            log.writeLog("您的配置文件是空的:"+confFile.getAbsolutePath());
            
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
