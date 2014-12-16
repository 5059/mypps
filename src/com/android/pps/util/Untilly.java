package com.android.pps.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * 工具类
 * 
 * @author 
 * 
 */
public class Untilly {
	
    /**
     * 创建指定文件文件夹
     */
    private void createDir(Context context) {
    	if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
    		// 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
    		//得到一个路径，内容是sdcard的文件夹路径和名字
            String ptImage = sdcardDir.getPath() + "/testAndroid/Image/";
            String ptinfo = sdcardDir.getPath() + "/testAndroid/info/";
            File pathImg = new File(ptImage);
            File pathInfo = new File(ptinfo);
            
            if(!pathImg.exists()) {
            	//若不存在，创建目录，可以在应用启动的时候创建
            	pathImg.mkdirs();
            	Toast.makeText(context, "文件夹Image创建成功", Toast.LENGTH_SHORT).show();
            }else{
            	Toast.makeText(context, "文件夹Image已存在", Toast.LENGTH_SHORT).show();
            }
            
            if(!pathInfo.exists()) {
            	//若不存在，创建目录，可以在应用启动的时候创建
            	pathInfo.mkdirs();
            	Toast.makeText(context, "文件夹Info创建成功", Toast.LENGTH_SHORT).show();
            }else{
            	Toast.makeText(context, "文件夹Info已存在", Toast.LENGTH_SHORT).show();
            }
    	}else{
    		Toast.makeText(context, "请插入sdcard", Toast.LENGTH_SHORT).show();
    	}
	}
	
	/**
	 * 缩放图片
	 * 
	 * @param icon
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap icon, int h) {
		// 缩放图片
		Matrix m = new Matrix();
		float sx = (float) 2 * h / icon.getWidth();
		float sy = (float) 2 * h / icon.getHeight();
		m.setScale(sx, sy);
		// 重新构造一个2h*2h的图片
		return Bitmap.createBitmap(icon, 0, 0, icon.getWidth(),
				icon.getHeight(), m, false);
	}

	/**
	 * 读取文件
	 * @param fileName
	 * @return byte[]
	 */
	public static byte[] readFile(String fileName) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			fis = new FileInputStream(fileName);
			byte[] buffer = new byte[8 * 1024];
			int readSize = -1;
			baos = new ByteArrayOutputStream();
			while ((readSize = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, readSize);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * 获取sdCard路径
	 * @return
	 */
	public static String getSdcardDir() {
		Log.i("DemoMain", "a getSdcardDir");
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	

	/** 
     * 分享功能 
     *  
     * @param context 
     *            上下文 
     * @param activityTitle 
     *            Activity的名字 
     * @param msgTitle 
     *            消息标题 
     * @param msgText 
     *            消息内容 
     * @param imgPath 
     *            图片路径，不分享图片则传null 
     */  
    public static void shareImg(Context context,String activityTitle, String imgPath) {  
        Intent intent = new Intent(Intent.ACTION_SEND);  
        File f = new File(imgPath);  
        if (f != null && f.exists() && f.isFile()) {  
            intent.setType("image/jpg");  
            Uri u = Uri.fromFile(f);  
            intent.putExtra(Intent.EXTRA_STREAM, u);  
            context.startActivity(Intent.createChooser(intent, activityTitle));  
        }else{
        	Toast.makeText(context, "图片不存在", Toast.LENGTH_SHORT);
        }
    } 
	
    public static void shareText(Context context, String activityTitle, String text){
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/*"); // 纯文本    
		intent.putExtra(intent.EXTRA_TEXT, text);
		((FragmentActivity)context).startActivityForResult(Intent.createChooser(intent, activityTitle), 0);
    }
    
	
	/**
	 * 
	 * @param data
	 *            数据
	 * @param path
	 *            路径
	 * @param fileName
	 *            文件名
	 * @return true成功 false失败
	 */
	public static boolean writeToSdcard(byte[] data, String path, String fileName) {
		FileOutputStream fos = null;
		try {
			// 判断有没有文件夹
			File filePath = new File(path);
			if (!filePath.exists()) {
				// 创建文件夹
				filePath.mkdirs();
			}

			// 判断有没有同名的文件
			File file = new File(path + fileName);
			// 有的话，删除
			if (file.exists()) {
				file.delete();
			}
			// 写文件
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			return true;

		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void deleteFile(String filePath){
		File file = new File(filePath);
		// 有的话，删除
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static String getFormateDateStamp(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		return sdf.format(date);
	}

}
