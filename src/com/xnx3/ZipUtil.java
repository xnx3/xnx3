package com.xnx3;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩或解压zip：
 * 由于直接使用java.util.zip工具包下的类，会出现中文乱码问题，所以使用ant.jar中的org.apache.tools.zip下的工具类
 * @author Administrator
 */

public class ZipUtil {

    /**
     * @param args 主方法
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
    	try {
			ZipUtil.zip("/Users/apple/git/wangmarket/target/classes/plugin/siteTransfer/export/219/", "/Users/apple/Desktop", "sitedata.zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
//    	try {
//			unzip("/Users/apple/Desktop/s.zip","/Users/apple/Desktop/12/haha/");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        
    }
    
    
    

    
	/**
	 * 将可变长参数中的file全部压缩到名为zipFileName的压缩包中，压缩包存放在zipPath路径下
	 * 
	 * 
	 * @param zipPath
	 *            * 压缩包文件路径*
	 * @param zipFileName
	 *            * 压缩包文件名 *
	 * @param files
	 *            * 需要压缩的文件*
	 * @throws Exception
	 * @throws IOException
	 */
	public static void zip(String zipPath, String zipFileName, File... files) throws Exception, IOException {
		List<File> fileList = new ArrayList<File>();
		Collections.addAll(fileList, files);
		zip(fileList, zipPath, zipFileName);
	}
 
	/**
	 * * 对文件或文件目录进行压缩 * *
	 * 
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径。 如 /Users/apple/Desktop/wangmarket/
	 * @param zipPath 压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹。传入如： /Users/apple/Desktop/
	 * @param zipFileName 保存出来的压缩文件的名字，如 : wangmarket.zip 
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
		if(srcPath == null || srcPath.length() == 0){
			System.out.println("要压缩的源文件路径不能为空");
			return;
		}
		if(zipPath == null || zipPath.length() == 0){
			System.out.println("压缩文件保存的路径不能为空");
			return;
		}
//		if (isEmpty(srcPath) || isEmpty(zipPath) || isEmpty(zipFileName)) {
//			throw new Exception("路径或文件名不能为空");
//		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try {
			File srcFile = new File(srcPath);
			// 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory() && zipPath.indexOf(srcPath) != -1) {
				throw new Exception("zipPath must not be the child directory of srcPath.");
			}
			// 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory()) {
				zipDir.mkdirs();
			}
			// 创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);
			if (zipFile.exists()) {
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				// 删除已存在的目标文件
				zipFile.delete();
			}
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			// 如果只是压缩一个文件，则需要截取该文件的父目录;如果是一个文件夹，则直接从文件中的内容开始，不保留文件夹名，若想要保留文件夹名，则也需要截取父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile()) {
				int index = srcPath.lastIndexOf(File.separator);
				if (index != -1) {
					srcRootDir = srcPath.substring(0, index);
				}
//				System.out.println("isfile");
			}
			// 调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, zos);
			zos.flush();
		} finally {
			closeQuietly(zos);
		}
	}
 
	public static void zip(List<File> fileList, String zipPath, String zipFileName) throws Exception, IOException {
//		if (isEmpty(zipPath) || isEmpty(zipFileName)) {
//			throw new Exception("路径或文件名不能为空");
//		}
		if (null == fileList || fileList.isEmpty()) {
			throw new Exception("文件列表不能为空");
		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try {
			// 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			for (File srcFile : fileList) {
				if (srcFile.isDirectory() && zipPath.indexOf(srcFile.getPath()) != -1) {
					throw new Exception("zipPath must not be the child directory of srcPath.");
				}
			}
			// 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory()) {
				zipDir.mkdirs();
			}
			// 创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);
			if (zipFile.exists()) {
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				// 删除已存在的目标文件
				zipFile.delete();
			}
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			String srcPath;
			for (File srcFile : fileList) {
				srcPath = srcFile.getPath();
				// 如果只是压缩一个文件，则需要截取该文件的父目录
				String srcRootDir = srcPath;
				/* * if (srcFile.isFile()) { */
				int index = srcFile.getPath().lastIndexOf(File.separator);
				if (index != -1) {
					srcRootDir = srcPath.substring(0, index);
				}
				// 调用递归压缩方法进行目录或文件压缩
				zip(srcRootDir, srcFile, zos);
			}
			zos.flush();
		} finally {
			closeQuietly(zos);
		}
	}
	

	 
	/**
	 * 递归压缩文件夹
	 * 
	 * @param srcRootDir
	 *            * 压缩文件夹根目录 *
	 * @param file
	 *            * 当前递归压缩的文件或目录对象
	 * 
	 * @param zos
	 *            * 压缩文件存储对象 *
	 * @throws IOException
	 */
	private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws IOException {
		if (file == null) {
			return;
		}
		// 如果是文件，则直接压缩该文件
		if (file.isFile()) {
			int count, bufferLen = 1024;
			byte data[] = new byte[bufferLen];
			// 获取文件相对于压缩文件夹根目录的子路径
			String subPath = file.getAbsolutePath();
//			System.out.println(subPath);
			int index = subPath.indexOf(srcRootDir);
			if (index != -1) {
				int srdLength = srcRootDir.length();
				subPath = subPath.substring( (srdLength > 1 ? srdLength-1 : srdLength) + File.separator.length());
			}
			ZipEntry entry = new ZipEntry(subPath);
//			System.out.println(subPath);
			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(file));
				zos.putNextEntry(entry);
				while ((count = bis.read(data, 0, bufferLen)) != -1) {
					zos.write(data, 0, count);
				}
			} finally {
				closeQuietly(bis);
				// 这个一直都要用不能关
				// IOUtil.closeQuietly(zos);
			}
		}
		// 如果是目录，则压缩整个目录
		else {
			// 压缩目录中的文件或子目录
			File[] childFileList = file.listFiles();
			if (null == childFileList) {
				return;
			}
			for (File childFile : childFileList) {
				zip(srcRootDir, childFile, zos);
			}
		}
	}


	private static void closeQuietly(Closeable Closeable) {
		if (null != Closeable) {
			try {
				Closeable.close();
				Closeable = null;
			} catch (Exception e) {
			}
		}
	}

    
    

    /**
     * 解压文件
     * @param unzip 要解压的zip文件， 传入如：  /Users/apple/Desktop/bbs.zip   将在解压的文件夹本身所在目录，将文件释放出来。就如同解压到当前文件夹
     * @throws IOException
     */
    public static void unzip(String unzip) throws IOException {
        File file = new File(unzip);
        String basePath = file.getParent();
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        
        unzip(zis, basePath+"/");
    }


    /**
     * 解压文件
     * @param zipPath 要解压的zip文件， 传入如：  /Users/apple/Desktop/bbs.zip
     * @param targetPath 解压到的目标目录 （文件夹），如： /Users/apple/Desktop/ss1/   则会将zip中的文件列表解压到此文件夹中
     * 			<ul>
     * 				<li>如果填写具体解压的文件目录，则将文件解压到这个指定的目录中。如果中，某个文件夹不存在，则会自动创建这个文件夹。</li>
     * 				<li>如果此处传入null或者空字符串，则将文件解压到当前压缩包所在的目录，也就是相当于 解压到当前文件夹</li>
     * 			</ul>
     * @throws IOException
     */
    public static void unzip(String zipPath, String targetPath) throws IOException {
        File file = new File(zipPath);
        if(targetPath == null || targetPath.length() == 0){
        	targetPath = file.getParent();
        }
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        
        unzip(zis, targetPath);
    }

    private static void unzip(ZipInputStream zis, String basePath) throws IOException {
        ZipEntry entry = zis.getNextEntry();
        if (entry != null) {
//            File file = new File(basePath + File.separator + entry.getName());
            File file = new File(basePath + entry.getName());
            if (file.isDirectory()) {
                // 可能存在空文件夹
                if (!file.exists())
                    file.mkdirs();
                unzip(zis, basePath);
            } else {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists())
                    parentFile.mkdirs();
                FileOutputStream fos = new FileOutputStream(file);// 输出流创建文件时必须保证父路径存在
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = zis.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                zis.closeEntry();
                unzip(zis, basePath);
            }
        }
    }
        
}