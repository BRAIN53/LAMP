package sz.lamp.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class ColorXY extends RelativeLayout {
	
	private static int LEFT_WIDTH = 0;
	private Bitmap mGradualChangeBitmap;
	private Paint mBitmapPaint;
	

	public ColorXY(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBitmapPaint=new Paint();
	}
	
	 @Override
	 protected void onDraw(Canvas canvas) {
	        // ×ó±ß
	        canvas.drawBitmap(getGradual() , null , new Rect(24, 24, 607 , 350), mBitmapPaint);
	        
	 }
	 
	 
	 private Bitmap getGradual() {
	        if (mGradualChangeBitmap == null) {
	            Paint leftPaint = new Paint();
	            leftPaint.setStrokeWidth(1);
	            mGradualChangeBitmap = Bitmap.createBitmap(607, 400, Config.RGB_565);
	            Canvas canvas = new Canvas(mGradualChangeBitmap);
	            int bitmapWidth = mGradualChangeBitmap.getWidth();
	            //LEFT_WIDTH = bitmapWidth;
	            int bitmapHeight = mGradualChangeBitmap.getHeight();
	            int[] leftColors = new int[] {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA};
	            Shader leftShader = new LinearGradient(0, bitmapHeight / 2, bitmapWidth, bitmapHeight / 2, leftColors, null, TileMode.REPEAT);
	            LinearGradient shadowShader = new LinearGradient(bitmapWidth / 2, 0, bitmapWidth / 2, bitmapHeight,
	                    Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
	            ComposeShader shader = new ComposeShader(leftShader, shadowShader, PorterDuff.Mode.SCREEN);
	            leftPaint.setShader(shader);
	            canvas.drawRect(0, 0, bitmapWidth, bitmapHeight, leftPaint);
	        }
	        return mGradualChangeBitmap;
	   }


}
