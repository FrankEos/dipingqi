package org.harvest.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {
	
	/**
	 * 生成版本信息json文件
	 * @param content
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String writeTxtFile(String content, File fileName) throws Exception {
        RandomAccessFile mm = null;
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
            return "true";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mm != null) {
                mm.close();
            }
        }
        return "false";
    }
	
	/**
	 * 计算文件的md5值
	 */
	protected static char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};  
    protected static MessageDigest messageDigest = null;  
    static{  
        try{  
            messageDigest = MessageDigest.getInstance("MD5");  
        }catch (NoSuchAlgorithmException e) {  
            System.err.println(FileUtil.class.getName()+"初始化失败，MessageDigest不支持MD5Util.");  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * 计算文件的MD5 
     * @param fileName 文件的绝对路径 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(String fileName) throws IOException{  
        File f = new File(fileName);  
        return getFileMD5String(f);  
    }  
      
    /** 
     * 计算文件的MD5，重载方法 
     * @param file 文件对象 
     * @return 
     * @throws IOException 
     */  
    @SuppressWarnings("resource")
    public static String getFileMD5String(File file) throws IOException{  
        FileInputStream in = new FileInputStream(file);  
        FileChannel ch = in.getChannel();  
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
        messageDigest.update(byteBuffer);  
        return bufferToHex(messageDigest.digest());  
    }  
      
    private static String bufferToHex(byte bytes[]) {  
       return bufferToHex(bytes, 0, bytes.length);  
    }  
      
    private static String bufferToHex(byte bytes[], int m, int n) {  
       StringBuffer stringbuffer = new StringBuffer(2 * n);  
       int k = m + n;  
       for (int l = m; l < k; l++) {  
        appendHexPair(bytes[l], stringbuffer);  
       }  
       return stringbuffer.toString();  
    }  
      
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {  
       char c0 = hexDigits[(bt & 0xf0) >> 4];  
       char c1 = hexDigits[bt & 0xf];  
       stringbuffer.append(c0);  
       stringbuffer.append(c1);  
    }  

}
