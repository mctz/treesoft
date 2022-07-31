package org.springframework.base.system.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/utils/EncodingDetect.class */
public class EncodingDetect {
    public static String getJavaEncode(File file) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(file)];
        return fileCode;
    }

    public static String getJavaEncode(String filePath) {
        BytesEncodingDetect s = new BytesEncodingDetect();
        String fileCode = BytesEncodingDetect.javaname[s.detectEncoding(new File(filePath))];
        return fileCode;
    }

    public static void readFile(String file, String code) {
        String str;
        BufferedReader fr;
        String readLine;
        String line;
        if (code != null) {
            try {
                if (!"".equals(code)) {
                    str = code;
                    String myCode = str;
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), myCode);
                    fr = new BufferedReader(read);
                    int flag = 1;
                    while (true) {
                        readLine = fr.readLine();
                        line = readLine;
                        if (readLine == null || line.trim().length() <= 0) {
                            break;
                        }
                        if (flag == 1) {
                            line = line.substring(1);
                            flag++;
                        }
                        System.out.println(line);
                    }
                    fr.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            } catch (IOException e2) {
                e2.printStackTrace();
                return;
            }
        }
        str = "UTF8";
        String myCode2 = str;
        InputStreamReader read2 = new InputStreamReader(new FileInputStream(file), myCode2);
        fr = new BufferedReader(read2);
        int flag2 = 1;
        while (true) {
            readLine = fr.readLine();
            line = readLine;
            if (readLine == null) {
                break;
            }
            break;
            System.out.println(line);
        }
        fr.close();
    }
}
