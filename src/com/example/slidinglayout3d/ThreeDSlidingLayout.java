package com.example.slidinglayout3d;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class ThreeDSlidingLayout extends RelativeLayout implements OnTouchListener {

	
	public static final int SNAP_VELOCITY = 200;


	public static final int DO_NOTHING = 0;


	public static final int SHOW_MENU = 1;


	public static final int HIDE_MENU = 2;


	private int slideState;


	private int screenWidth;


	private int leftEdge = 0;


	private int rightEdge = 0;


	private int touchSlop;


	private float xDown;


	private float yDown;


	private float xMove;


	private float yMove;


	private float xUp;

	private boolean isLeftLayoutVisible;

	private boolean isSliding;

	private boolean loadOnce;

	private View leftLayout;

	private View rightLayout;

	private Image3dView image3dView;

	private View mBindView;

	private MarginLayoutParams leftLayoutParams;

	private MarginLayoutParams rightLayoutParams;

	private ViewGroup.LayoutParams image3dViewParams;

	private VelocityTracker mVelocityTracker;

	public ThreeDSlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public void setScrollEvent(View bindView) {
		mBindView = bindView;
		mBindView.setOnTouchListener(this);
	}

	public void scrollToLeftLayout() {
		image3dView.clearSourceBitmap();
		new ScrollTask().execute(-10);
	}

	public void scrollToRightLayout() {
		image3dView.clearSourceBitmap();
		new ScrollTask().execute(10);
	}

	public boolean isLeftLayoutVisible() {
		return isLeftLayoutVisible;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			leftLayout = findViewById(R.id.menu);
			leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
			rightEdge = -leftLayoutParams.width;
			rightLayout = findViewById(R.id.content);
			rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
			rightLayoutParams.width = screenWidth;
			rightLayout.setLayoutParams(rightLayoutParams);
			image3dView = (Image3dView) findViewById(R.id.image_3d_view);
			image3dView.setSourceView(leftLayout);
			loadOnce = true;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			xDown = event.getRawX();
			yDown = event.getRawY();
			slideState = DO_NOTHING;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = event.getRawX();
			yMove = event.getRawY();
			int moveDistanceX = (int) (xMove - xDown);
			int moveDistanceY = (int) (yMove - yDown);
			checkSlideState(moveDistanceX, moveDistanceY);
			switch (slideState) {
				case SHOW_MENU:
					rightLayoutParams.rightMargin = -moveDistanceX;
					onSlide();
					break;
				case HIDE_MENU:
					rightLayoutParams.rightMargin = rightEdge - moveDistanceX;
					onSlide();
					break;
				default:
					break;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			xUp = event.getRawX();
			int upDistanceX = (int) (xUp - xDown);
			if (isSliding) {
				switch (slideState) {
				case SHOW_MENU:
					if (shouldScrollToLeftLayout()) {
						scrollToLeftLayout();
					} else {
						scrollToRightLayout();
					}
					break;
				case HIDE_MENU:
					if (shouldScrollToRightLayout()) {
						scrollToRightLayout();
					} else {
						scrollToLeftLayout();
					}
					break;
				default:
					break;
				}
			} else if (upDistanceX < touchSlop && isLeftLayoutVisible) {
				scrollToRightLayout();
			}
			recycleVelocityTracker();
			break;
		}
		if (v.isEnabled()) {
			if (isSliding) {
				unFocusBindView();
				return true;
			}
			if (isLeftLayoutVisible) {
				return true;
			}
			return false;
		}
		return true;
	}
	private void onSlide() {
		checkSlideBorder();
		rightLayout.setLayoutParams(rightLayoutParams);
		image3dView.clearSourceBitmap();
		image3dViewParams = image3dView.getLayoutParams();
		image3dViewParams.width = -rightLayoutParams.rightMargin;
		image3dView.setLayoutParams(image3dViewParams);
		showImage3dView();
	}

	private void checkSlideState(int moveDistanceX, int moveDistanceY) {
		if (isLeftLayoutVisible) {
			if (!isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX < 0) {
				isSliding = true;
				slideState = HIDE_MENU;
			}
		} else if (!isSliding && Math.abs(moveDistanceX) >= touchSlop && moveDistanceX > 0
				&& Math.abs(moveDistanceY) < touchSlop) {
			isSliding = true;
			slideState = SHOW_MENU;
		}
	}

	private void checkSlideBorder() {
		if (rightLayoutParams.rightMargin > leftEdge) {
			rightLayoutParams.rightMargin = leftEdge;
		} else if (rightLayoutParams.rightMargin < rightEdge) {
			rightLayoutParams.rightMargin = rightEdge;
		}
	}

	private boolean shouldScrollToLeftLayout() {
		return xUp - xDown > leftLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	private boolean shouldScrollToRightLayout() {
		return xDown - xUp > leftLayoutParams.width / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	private void unFocusBindView() {
		if (mBindView != null) {
			mBindView.setPressed(false);
			mBindView.setFocusable(false);
			mBindView.setFocusableInTouchMode(false);
		}
	}

	private void showImage3dView() {
		if (image3dView.getVisibility() != View.VISIBLE) {
			image3dView.setVisibility(View.VISIBLE);
		}
		if (leftLayout.getVisibility() != View.INVISIBLE) {
			leftLayout.setVisibility(View.INVISIBLE);
		}
	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int rightMargin = rightLayoutParams.rightMargin;
			while (true) {
				rightMargin = rightMargin + speed[0];
				if (rightMargin < rightEdge) {
					rightMargin = rightEdge;
					break;
				}
				if (rightMargin > leftEdge) {
					rightMargin = leftEdge;
					break;
				}
				publishProgress(rightMargin);
			}
			if (speed[0] > 0) {
				isLeftLayoutVisible = false;
			} else {
				isLeftLayoutVisible = true;
			}
			isSliding = false;
			return rightMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... rightMargin) {
			rightLayoutParams.rightMargin = rightMargin[0];
			rightLayout.setLayoutParams(rightLayoutParams);
			image3dViewParams = image3dView.getLayoutParams();
			image3dViewParams.width = -rightLayoutParams.rightMargin;
			image3dView.setLayoutParams(image3dViewParams);
			showImage3dView();
			unFocusBindView();
		}

		@Override
		protected void onPostExecute(Integer rightMargin) {
			rightLayoutParams.rightMargin = rightMargin;
			rightLayout.setLayoutParams(rightLayoutParams);
			image3dViewParams = image3dView.getLayoutParams();
			image3dViewParams.width = -rightLayoutParams.rightMargin;
			image3dView.setLayoutParams(image3dViewParams);
			if (isLeftLayoutVisible) {
				image3dView.setVisibility(View.INVISIBLE);
				leftLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
