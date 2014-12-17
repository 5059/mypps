package com.android.pps.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

/**
 * 将生成的二维码保存在SD卡
 */
public class QRToSDcardSaveService {

	/**
	 * 将位图对象转换为字节数组
	 * @param bm
	 * @return
	 */
	private byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
		return outputStream.toByteArray();
	}
	
	/**
	 * 保存二维码至SD卡
	 * @param fileName
	 * @param bitmap
	 */
	public String saveToSDCard(String path, String fileName, Bitmap bitmap) {
		Untilly.writeToSdcard(Bitmap2Bytes(bitmap), Environment.getExternalStorageDirectory().getAbsolutePath() + path, fileName);
		return Environment.getExternalStorageDirectory().getAbsolutePath() + path + fileName;	//返回图片的路径
	}
	
	
	public File initSavePath() {
		File dateDir = Environment.getExternalStorageDirectory();
		String path = dateDir.getAbsolutePath() + "/testAndroid/Image/";
		File folder = new File(path);
		return folder;
	}
	
	public String saveJpeg(Bitmap bm) {

		long dataTake = System.currentTimeMillis();
		String jpegName = dataTake + ".jpg";

		 File jpegFile = new File(initSavePath(), jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegFile);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			return jpegName;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
