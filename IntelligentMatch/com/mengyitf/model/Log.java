package com.mengyitf.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mengyitf on 2017/10/25.
 */
public class Log {
    private BufferedWriter bw;
    public Log(){
        try {
            File file = new File(new File("").getAbsolutePath()+"\\log.txt");
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file,true));
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }
    public <T> void writeLog(T str){
        try {
            bw.write(str.toString());
            bw.newLine();
            bw.flush();
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }
    public void close(){
        try {
            bw.close();
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }
}
