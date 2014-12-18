package sz.m.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;

public class Tools {

	public static Bitmap getColorBitmap(int picWidth, int picHeight) {
		Bitmap mGradualChangeBitmap = null;
		if (mGradualChangeBitmap == null) {
			mGradualChangeBitmap = Bitmap.createBitmap(picWidth, picHeight,
					Config.RGB_565);
			Canvas canvas = new Canvas(mGradualChangeBitmap);
			int bitmapWidth = mGradualChangeBitmap.getWidth();
			int bitmapHeight = mGradualChangeBitmap.getHeight();
			int[] colors1 = new int[] { Color.RED, Color.YELLOW, Color.GREEN,
					Color.CYAN, Color.BLUE };
			Paint paint;
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(1);

			Shader shader = new LinearGradient(0, 0, 0, bitmapHeight, colors1,
					null, Shader.TileMode.CLAMP);
			paint.setShader(shader);
			canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, paint);
		}
		return mGradualChangeBitmap;
	}

	public static Bitmap getColorBitmap1(int picWidth, int picHeight) {
		Bitmap mGradualChangeBitmap = null;
		if (mGradualChangeBitmap == null) {
			Paint leftPaint = new Paint();
			leftPaint.setStrokeWidth(1);
			mGradualChangeBitmap = Bitmap.createBitmap(picWidth, picHeight,
					Config.RGB_565);
			Canvas canvas = new Canvas(mGradualChangeBitmap);
			int bitmapWidth = mGradualChangeBitmap.getWidth();
			int bitmapHeight = mGradualChangeBitmap.getHeight();
			int[] leftColors = new int[] { Color.RED, Color.YELLOW,
					Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA };
			Shader leftShader = new LinearGradient(0, bitmapHeight / 2,
					bitmapWidth, bitmapHeight / 2, leftColors, null,
					TileMode.REPEAT);
			LinearGradient shadowShader = new LinearGradient(bitmapWidth / 2,
					0, bitmapWidth / 2, bitmapHeight, Color.WHITE, Color.BLACK,
					Shader.TileMode.CLAMP);
			ComposeShader shader = new ComposeShader(leftShader, shadowShader,
					PorterDuff.Mode.SCREEN);
			leftPaint.setShader(shader);
			canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, leftPaint);
		}
		return mGradualChangeBitmap;
	}

	public static byte[] readResource(InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] array = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(array)) != -1) {
			outputStream.write(array, 0, len);
		}
		inputStream.close();
		outputStream.close();
		return outputStream.toByteArray();
	}

}
