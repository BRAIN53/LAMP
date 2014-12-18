package com.example.slidinglayout3d;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import sz.m.utils.GoogleWeather;
import sz.m.utils.Weather;
import sz.m.utils.Tools;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SketchPad extends Activity {
	private static final int SELECTPHOTOFROMALBUM = 0;
	private static final int SECONDSOFDAY = 24 * 60 * 60;
	private Button button;
	private ImageView imageView;
	private Bitmap baseBitmap;
	private Bitmap baseImage;
	private Canvas canvas;
	private Paint paint;
	private int screenWidth;
	private int screenHeight;
	Rect dst;
	private ArrayList<Integer> pointX = new ArrayList<Integer>();
	private ArrayList<Integer> pointY = new ArrayList<Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sketch_pad);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels / 2;

		imageView = (ImageView) findViewById(R.id.iv);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(6);
		// 创建一个可修改的Bitmap
		baseBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(baseBitmap);
		canvas.drawColor(Color.WHITE);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.e48fd91739989a6ea09492158dfb7f22_m);

		bitmap = Tools.getColorBitmap(screenWidth, screenHeight);

		dst = new Rect(0, 0, screenWidth, screenHeight);// 屏幕
		canvas.drawBitmap(bitmap, null, dst, null);
		
		LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(
				screenWidth, screenHeight);
		imageView.setLayoutParams(rl);
		
		imageView.setImageBitmap(baseBitmap);
		baseImage = Tools.getColorBitmap(screenWidth, screenHeight);
		imageView.setOnTouchListener(new OnTouchListener() {
			// 定义手指坐标
			int startx;
			int starty;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startx = (int) event.getX();
					starty = (int) event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					int newx = (int) event.getX();
					int newy = (int) event.getY();
					canvas.drawLine(startx, starty, newx, newy, paint);
					// 跟新画笔的开始位置
					startx = (int) event.getX();
					starty = (int) event.getY();
					imageView.setImageBitmap(baseBitmap);
					pointX.add(newx);
					pointY.add(newy);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return true;
			}
		});

	}

	public void save(View v) {
		try {
			File file = new File(Environment.getExternalStorageDirectory(),
					System.currentTimeMillis() + ".jpg");
			FileOutputStream fos = new FileOutputStream(file);
			baseBitmap.compress(CompressFormat.JPEG, 100, fos);// 第一个参数为图片格式
			fos.close();
			Toast.makeText(this, "保存图片成功", 2000).show();
			// 模拟消息：SD卡被重新挂载了
			Intent intent = new Intent();
			intent.setAction(intent.ACTION_MEDIA_MOUNTED);
			intent.setData(Uri.fromFile(Environment
					.getExternalStorageDirectory()));
			sendBroadcast(intent);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "保存图片失败", 2000).show();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void selectPic(View v) {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		getImage.setType("image/jpeg");
		startActivityForResult(getImage, SELECTPHOTOFROMALBUM);
	}

	public void reset(View v) {
		// Bitmap bitmap = Tools.getColorBitmap(screenWidth, screenHeight);
		// Rect dst = new Rect(0, 0, screenWidth, screenHeight);// 屏幕
		canvas.drawBitmap(baseImage, null, dst, null);
		pointX.removeAll(pointX);
		pointY.removeAll(pointY);
	}

	public void cancel(View v) {
		finish();
	}

	public void confirm(View v) {
		finish();
	}

	public void setThisMode(View v) {
		ArrayList<Integer> pointTemp = new ArrayList<Integer>();
		ArrayList<Integer> pointXh = new ArrayList<Integer>();
		ArrayList<Integer> pointYh = new ArrayList<Integer>();

		int l = pointX.size();
		for (Integer i = 0; i < l - 1; i++) {
			if (pointX.get(i) >= pointX.get(i + 1)) {
				int j = i + 1;
				while (pointX.get(j) <= pointX.get(i)) {
					j++;
					if (j >= l - 1)
						break;
				}
				i = j - 1;
			} else {
				pointXh.add(pointX.get(i));
				pointYh.add(pointY.get(i));

			}

		}
		pointX = pointXh;
		pointY = pointYh;
		l = pointX.size();

		for (Integer i = 0; i < l; i++) {
			int t = (int) (SECONDSOFDAY * 1.0 / screenWidth * pointX.get(i));
			pointX.set(i, t);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ContentResolver resolver = getContentResolver();
		Bitmap myBitmap = null;
		/**
		 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断
		 * ，这里的requestCode跟startActivityForResult里面第二个参数对应
		 */
		if (requestCode == SELECTPHOTOFROMALBUM) {
			try {
				// 获得图片的uri
				Uri originalUri = data.getData();
				// 将图片内容解析成字节数组
				byte[] mContent = readStream(resolver.openInputStream(Uri
						.parse(originalUri.toString())));
				// 将字节数组转换为ImageView可调用的Bitmap对象
				myBitmap = getPicFromBytes(mContent, null);
				// imageView.setImageBitmap(myBitmap);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else if (requestCode == 1) {

			try {
				super.onActivityResult(requestCode, resultCode, data);
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] mContent = baos.toByteArray();
				// imageView.setImageBitmap(myBitmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 把得到的图片绑定在控件上显示
			// imageView.setImageBitmap(myBitmap);
		}
		if (myBitmap != null) {
			baseImage = myBitmap;
			canvas.drawBitmap(myBitmap, null, dst, null);
			imageView.setImageBitmap(baseBitmap);
			pointX.removeAll(pointX);
			pointY.removeAll(pointY);
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

}