package com.mengyitf;

import com.mengyitf.config.Config;
import com.mengyitf.model.Log;

import java.io.*;
import java.util.Random;

/**
 * Created by feifei on 2017/10/24.
 */
public class CreateInportFile {
    Config config = null;
    private int dnum = 20;  // 部门数
    private int snum = 300; // 学生人数
    private int snum2 = 10; // 部门上限10到15个
    private Log log = new Log();
    public CreateInportFile(Config config){
        this.config = config;
        createInportFile();
    }

    private void createInportFile() {
        try {
            File file = new File(config.getImportfilePath());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String[] week = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
            Random random = new Random();
            int num1,num2 = 132000,num3,num4;
            // 生成兴趣类别30-50种
            num1 = random.nextInt(20)+30;
            String[] is = new String[num1];
            String[] dn = new String[dnum];
            int[] dc = new int[dnum];
            for (int i=0;i<dnum;i++){
                num1 = random.nextInt(2) + 2;
                char[] cs = new char[num1];
                for (int j = 0;j<num1;j++){
                    cs[j] = getRandomChar();
                }
                is[i] = String.valueOf(cs);
                if (is[i]==null||is[i].equals("null"))i--;
            }
            bw.write("Department");
            bw.newLine();
            for (int i=0;i<dnum;i++){
                num1 = random.nextInt(2) + 2;
                char[] cs = new char[num1];
                for (int j = 0;j<num1;j++){
                    cs[j] = getRandomChar();
                    if (String.valueOf(cs).equals("null")||String.valueOf(cs)==null)j--;
                }
                bw.write("DepartmentName: \""+String.valueOf(cs)+"\"");
                dn[i] = String.valueOf(cs);
                bw.newLine();
                num2+=random.nextInt(5) + num1;
                bw.write("DepartmentCode: \""+num2+"\"");
                dc[i] = num2;
                bw.newLine();
                num1 = random.nextInt(15-snum2) + snum2+1;
                bw.write("StudentLimit: "+num1);
                bw.newLine();
                bw.write("Characteristics:");
                num1 = random.nextInt(6) + 1;
                for (int j=0;j<num1;j++){
                    num3 = random.nextInt(is.length);
                    while (is[num3]==null||is[num3].equals("null"))num3 = random.nextInt(is.length);
                    bw.write(" \""+is[num3]+"\"");
                }
                bw.newLine();
                bw.write("RoutineActivityTime:");
                num1 = random.nextInt(3) + 2;
                for (int j=0;j<num1;j++){
                    int hour = random.nextInt(1000)%16+9;
                    num4 = random.nextInt(8);
                    if (num4 > 2){
                        bw.write(" \"Day/"+hour+":"+(random.nextInt(1000)%60)+"-"+(hour+random.nextInt(1000)%3+1)+":"+(random.nextInt(1000)%60)+"\"");
                    }else {
                        num3 = random.nextInt(1000)%7;
                        bw.write(" \"Week/"+week[num3]+"/"+hour+":"+(random.nextInt(1000)%60)+"-"+(hour+random.nextInt(1000)%3+1)+":"+(random.nextInt(1000)%60)+"\"");
                    }
                }
                bw.newLine();
                bw.write("/");
                bw.newLine();
            }
            num2 = 1702000;
            bw.write("Student");
            bw.newLine();
            for (int i=0;i<snum;i++){
                num1 = random.nextInt(2) + 2;
                char[] cs = new char[num1];
                for (int j = 0;j<num1;j++){
                    cs[j] = getRandomChar();
                    if (String.valueOf(cs).equals("null")||String.valueOf(cs)==null)j--;
                }
                bw.write("StudentName: \""+String.valueOf(cs)+"\"");
                bw.newLine();
                num2+=random.nextInt(5) + num1;
                bw.write("StudentCode: \""+num2+"\"");
                bw.newLine();
                bw.write("GradePoint: "+(random.nextDouble()*6+4));
                bw.newLine();
                bw.write("Interest:");
                num1 = random.nextInt(6) + 1;
                for (int j=0;j<num1;j++){
                    num3 = random.nextInt(is.length);
                    while (is[num3]==null||is[num3].equals("null"))num3 = random.nextInt(is.length);
                    bw.write(" \""+is[num3]+"\"");
                }
                bw.newLine();
                bw.write("DepartmentWishes:");
                num3 = random.nextInt(4)+1;
                long k = 1048575;
                for (int j=0;j<num3;){
                    num1 = random.nextInt(20);
                    num4 = random.nextInt(8);
                    if((k>>num1)%2>0){
                        if (num4>2){
                            bw.write(" \""+dc[num1]+"/"+dn[num1]+"\"");
                        }else {
                            bw.write(" \""+dn[num1]+"\"");
                        }
                        k = k - (1<<num1);
                        j++;
                    }
                }
                bw.newLine();
                bw.write("FreeTime:");
                num1 = random.nextInt(4)+1;
                num4 = random.nextInt(8);
                for (int j=0;j<num1;j++){
                    int hour = random.nextInt(1000)%16+9;
                    if (num4 > 2){
                        bw.write(" \"Day/"+hour+":"+(random.nextInt(1000)%60)+"-"+(hour+random.nextInt(1000)%3+1)+":"+(random.nextInt(1000)%60)+"\"");
                    }else {
                        num3 = random.nextInt(1000)%7;
                        bw.write(" \"Week/"+week[num3]+"/"+hour+":"+(random.nextInt(1000)%60)+"-"+(hour+random.nextInt(1000)%3+1)+":"+(random.nextInt(1000)%60)+"\"");
                    }
                }
                bw.newLine();
                bw.write("/");
                bw.newLine();
            }
            bw.close();
            log.writeLog("已生成文件:"+config.getImportfilePath());
        }catch (FileNotFoundException fnfe){
            try{
                new File(config.getImportfilePath()).createNewFile();
                createInportFile();
            }catch (IOException ioe){
                log.writeLog(ioe);
                System.exit(1);
            }
        }catch (IOException ioe){
            log.writeLog(ioe);
        }
    }

    private static char getRandomChar() {
        String str = "";
        int hightPos;
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }
        return str.charAt(0);
    }

}
