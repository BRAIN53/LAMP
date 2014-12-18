package sz.lamp.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

public class VoiceAxis extends View {

	public VoiceAxis(Context context, AttributeSet attrs) {
		super(context, attrs);
		// get infomation from server to draw the axis
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(),
				canvas.getHeight() / 2, paint);
		// paint.setColor(Color.RED);
		// canvas.drawLine(canvas.getWidth()/7*2, canvas.getHeight()/2,
		// canvas.getWidth()/7*4, canvas.getHeight()/2 , paint);
		// canvas.drawLine(canvas.getWidth()/7*6, canvas.getHeight()/2,
		// canvas.getWidth(), canvas.getHeight()/2 , paint);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		int step = (int) (canvas.getWidth() * 1.0 / 60);
		Path path1 = new Path();
		for (int x = 0; x < canvas.getWidth(); x += step) {
			if (x % 15 == 0) {
				path1.moveTo(x, canvas.getHeight() * 1 / 3);
				path1.lineTo(x, canvas.getHeight() * 2 / 3);
			} else {
				path1.moveTo(x, canvas.getHeight() * 2 / 5);
				path1.lineTo(x, canvas.getHeight() * 3 / 5);
			}
		}
		canvas.drawPath(path1, paint);
		float textY = (float) (canvas.getHeight() * 1.0 * 2 / 3 + 19);
		float stepX = (float) (canvas.getWidth() * 1.0 / 5);
		paint.setTextSize(22);
		canvas.drawText("µÍÒô", 5, textY, paint);
		canvas.drawText("¸ßÒô", canvas.getWidth() - 55, textY, paint);
	}

}
