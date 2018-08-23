package com.app.util;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;

/**
 * 生成验证码
 * @author pythoner
 *
 */
public class AuthCode {
	Random random = new Random();

	public BitmapDrawable createAuthCode(int authCodeLength) {
		return createAuthCode(genAuthCode(authCodeLength));
	}

	public BitmapDrawable createAuthCode(String authCode) {
		if (authCode == null || authCode.length() == 0) {
			throw new IllegalArgumentException("authCode must be > 0");
		}
		int width = 128;
		int height = 64;
		Bitmap.Config config = Config.RGB_565;
		Bitmap bmp = Bitmap.createBitmap(width, height, config);
		Canvas canvas = new Canvas(bmp);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);

		paint.setColor(getRandomColor(200, 250));
		// 画底色
		canvas.drawRect(0, 0, width, height, paint);

		// 画干扰线
		paint.setColor(getRandomColor(160, 200));
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(15);
			int yl = random.nextInt(15);
			canvas.drawLine(x, y, x + xl, y + yl, paint);
		}
		// 画验证码
		int len = authCode.length();
		int gap = width / (len + 1);
		Matrix matrix = new Matrix();
		for (int i = 0; i < len; i++) {
			char c = authCode.charAt(i);
			paint.setColor(getRandomColor(20, 130));
			paint.setTextSize(getRandomNumber(30, 40));
			matrix.setRotate(getRandomNumber(-30, 30), width >> 1, height >> 1);
			canvas.setMatrix(matrix);
			canvas.drawText(String.valueOf(c), gap * i + (gap >> 1),
					((height + getFontHeight(paint)) >> 1) - 5, paint);
		}

		return new BitmapDrawable(bmp);
	}

	private int getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	private int getRandomNumber(int start, int end) {
		return start + random.nextInt(end - start);
	}

	private int getRandomColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return Color.argb(0xFF, r, g, b);
	}

	public String genAuthCode(int length) {
		if (length < 1)
			return null;
		String chars[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		StringBuffer sb = new StringBuffer();
		int nRand = 0;
		for (int i = 0; i < length; i++) {
			nRand = (int) Math.round(Math.random() * 100D);
			sb.append(chars[nRand % (chars.length - 1)]);
		}

		return sb.toString();
	}

}

