package com.android.pps.util;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeService {

	//前景色
	private static int FOREGROUND_COLOR=0xff000000;
	//背景色
	private static int BACKGROUND_COLOR=0xffffffff;
	
	// 图片宽度的,注意图片不要设置太大,否则会影响二维码的识别
	private static final int IMAGE_HALFWIDTH = 20;
    /** 
     * 将指定的内容生成成二维码 
     * 
     * @param content 将要生成二维码的内容 
     * @return 返回生成好的二维码事件 
     * @throws WriterException WriterException异常 
     */  
    public static Bitmap CreateQRCode(String content, Bitmap icon) {  
    	// 缩放一个40*40的图片
		//如果icon部位null,则生成带图片的二维码
		if(icon != null){
			icon = Untilly.zoomBitmap(icon, IMAGE_HALFWIDTH);
		}
		
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, 300, 300, hints);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		
		//如果icon部位null,则生成带图片的二维码
		if(icon != null){
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
							&& y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH) {
						pixels[y * width + x] = icon.getPixel(x - halfW + IMAGE_HALFWIDTH,
								y - halfH + IMAGE_HALFWIDTH);
					} else {
						if (matrix.get(x, y)) {
							pixels[y * width + x] = FOREGROUND_COLOR;
						} else { // 无信息设置像素点为白色
							pixels[y * width + x] = BACKGROUND_COLOR;
						}
					}

				}
			}
		}else{
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = FOREGROUND_COLOR;
					} else { // 无信息设置像素点为白色
						pixels[y * width + x] = BACKGROUND_COLOR;
					}
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
    }
}
