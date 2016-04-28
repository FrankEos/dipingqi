package org.harvest.crawler.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * file operation
 * @author   zhangyy
 * @version  Ver 1.1
 * @Date	 2010 2010-12-21
 */
public class FileOpertion {

	public final static Logger logger = Logger.getLogger(FileOpertion.class);
	
	/**
	 Return to the system file path separator
	 **/
	public static String getPathSpace() {
		return new Properties(System.getProperties())
				.getProperty("file.separator");
	}
	
	/**
	 Remove the final document delimiter
	 **/
	public static String repaceFilePath(String filePath) {
    	if (filePath.endsWith(File.separator)) {
    		filePath = filePath.substring(0,filePath.lastIndexOf(File.separator));
    	}
		return filePath;
	}

	/**
	 * check the file exsits or not
	 * @param filename
	 * @return
	 */
	public static boolean fileExists(String filename) {
		File f = new File(filename);
		return f.isFile() && f.exists();

	}

	/**
	 * check the directory exsits or not　
	 * @param pathname
	 * @return
	 */
	public static boolean folderExists(String pathname) {
		File f = new File(pathname);
		return f.isDirectory() && f.exists();
	}

	/**
	 * delete file
	 * @param filename
	 * @return
	 */
	public static boolean deleteFile(String filename) {
		File f = new File(filename);
		return f.isFile() && f.delete();
	}

	/**
	 * delete file and directory
	 * @param fileorpath
	 * @return
	 */
	public static boolean deleteFileEx(String fileorpath) {
		File f = new File(fileorpath);
		return f.delete();
	}

	/**
	 * delete file and directory
	 * @param pathname
	 * @return
	 */
	public static boolean deletePath(String pathname) {
		File f = new File(pathname);
		if (!f.isDirectory())
			return false;
		String[] listfile = f.list();
		if (listfile != null)
			for (int i = 0; i < listfile.length; i++) {
				if (!deleteFileEx(pathname + "/" + listfile[i]))
					return false;
			}
		return f.delete();
	}

	/**
	 * delete file and directory with sub diretory　
	 * @param pathname
	 * @return
	 */
	public static boolean deleteTree(String pathname) {
		File f = new File(pathname);
		String[] listfile = f.list();
		if (listfile != null)
			for (int i = 0; i < listfile.length; i++) {
				deleteTree(pathname + "/" + listfile[i]);
			}
		return f.delete();
	}

	/**
	 * change name of directory or file
	 * @param sfilename
	 * @param dfilename
	 * @return
	 */
	public static boolean moveFile(String sfilename, String dfilename) {
		File fs = new File(sfilename);
		File fd = new File(dfilename);
		return fs.renameTo(fd);
	}

	/**
	 * create file
	 * @param filename
	 * @return
	 */
	public static boolean createFile(String filename) {
		File f = new File(filename);
		try {
			return f.createNewFile();
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * create directory
	 */
	public static boolean mkdir(String pathname) {
		File f = new File(pathname);
		try {
			return f.mkdir();
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * create directory（With superiors and on the parent directory does not exist）
	 * @param pathname
	 * @return
	 */
	public static boolean mkdirs(String pathname) {
		File f = new File(pathname);
		if(f!=null) f=f.getParentFile();
		try {
			return f.mkdirs();
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * copy file
	 * @param source
	 * @param desc
	 * @return
	 */
	public static boolean copyFile(String source, String desc) {
		FileInputStream is =null;
		FileOutputStream os=null;
		logger.debug(source+" to "+desc);
		try {
			mkdirs(desc);
			is = new FileInputStream(source);
			os = new FileOutputStream(desc);
			byte[] b = new byte[1024];
			int i =-1;
			while((i=is.read(b))!=-1){
				os.write(b,0,i);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}finally{
			if(is!=null)
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * get size of file
	 * @param filename
	 * @return
	 */
	public static int getFileSize(String filename) {
		try {
			File fl = new File(filename);
			int length = (int) fl.length();
			return length;
		} catch (Exception e) {
			return 0;
		}

	}

	/**
	 * get the file name with not file path
	 * @param filePathName
	 * @return
	 */
	public static String getFileName(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf(47); //  "/"
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		pos = filePathName.lastIndexOf(92); //  "\"
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		else
			return filePathName;
	}

	/**
	 * get the extra file name 
	 */
	public static String getFileExt(String filePathName) {
		int pos = 0;
		pos = filePathName.lastIndexOf('.');
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		else
			return "";

	}
	
	/**
	 * Return the full file path based on the file path and file name
	 * @param dirPath 
	 * @param fileName　
	 * @return
	 */
	public static String getFilePath(String dirPath,String fileName){
		String resultStr = "";
		if (dirPath.lastIndexOf(StringUtil.getPathSpace())!=0)
			resultStr = dirPath.concat(StringUtil.getPathSpace()).concat(fileName);
		else
			resultStr = dirPath.concat(fileName);
		//System.out.print("path="+resultStr);
		return resultStr;
	}
	/**
	 * copy file
	 * @param source
	 * @param desc
	 * @return
	 */
	public static boolean copyFile(byte[] source, String desc) {
		InputStream is =null;
		FileOutputStream os=null;
		logger.debug(source+" to "+desc);
		try {
			mkdirs(desc);
			is =new ByteArrayInputStream(source);
			os = new FileOutputStream(desc);
			byte[] b = new byte[1024];
			int i =-1;
			while((i=is.read(b))!=-1){
				os.write(b,0,i);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}finally{
			if(is!=null)
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	/**
	 * copy file
	 * @param source
	 * @param desc
	 * @return
	 */
	public static boolean copyFile(InputStream ins, String desc) {
		FileOutputStream os=null;
		logger.debug("start inputstream to "+desc);
		try {
			mkdirs(desc);
			os = new FileOutputStream(desc);
			byte[] b = new byte[1024];
			int i =-1;
			while((i=ins.read(b))!=-1){
				os.write(b,0,i);
			}
			logger.debug(" true end inputstream to "+desc);
			return true;
		} catch (Exception e) {
			logger.debug(" false end inputstream to "+desc);
			logger.error(e.getMessage(), e);
			return false;
		}finally{
				if(os!=null)
					try {
						os.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(os!=null)
						try {
							os.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	}
	

}
