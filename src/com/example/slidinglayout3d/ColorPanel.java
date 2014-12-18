package com.example.slidinglayout3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


/*
 * 通过拖动灯或组来控制颜色
 * 
 * 
 * */

public class ColorPanel extends Activity {
	/** Called when the activity is first created. */
	private int picWidth, picHeight, screenWidth, screenHeight;
	Bitmap pic = null;
	Button btnColor;
	Map<String, Integer> lamps = new HashMap<String, Integer>();
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	Map<String, String> lampmap;
	Map<String, String> groupmap;
	private Context c;
	//显示button ，灯  的容器
	RelativeLayout relativeLayout;
	LinearLayout LL;
	// 灯的对象 格式 为 lampID ---Map
	Map<String, Map<String, String>> LAMPS = new HashMap<String, Map<String, String>>();
	// 分组对象 格式为 groupID ---Map
	Map<String, Map<String, String>> GROUPS = new HashMap<String, Map<String, String>>();
	// 所有Button 对象
	List<Button> allButtons = new ArrayList<Button>();
	private static final int D = 40;
	Handler handler;
	private String lastOperateButton = "";// 当前操作的按钮的tag L*, G* 为对应的LAMPS GROUPS
											// 的 String字段

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_panel);
		c = this;
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels / 2 - 100;
		picWidth = screenWidth;
		picHeight = screenHeight;

		for (int l = 0; l < 4; l++) {
			lampmap = new HashMap<String, String>();
			lampmap.put("lampID", "L" + l);
			lampmap.put("name", "La " + l);
			lampmap.put("isOn", "true");
			lampmap.put("color", l + 0xffff + "");
			lampmap.put("x", 300 + l + "");
			lampmap.put("y", 300 + l + "");
			LAMPS.put(lampmap.get("lampID"), lampmap);
		}

		for (int g = 0; g < 1; g++) {
			groupmap = new HashMap<String, String>();
			groupmap.put("groupID", "G" + g);
			groupmap.put("name", "Gr " + g);
			groupmap.put("isOn", "true");
			groupmap.put("color", g + 0xffff + "");
			groupmap.put("x", 300 + g + "");
			groupmap.put("y", 300 + g + "");
			groupmap.put("lamp1", "LID1");
			groupmap.put("lamp2", "LID2");
			GROUPS.put(groupmap.get("groupID"), groupmap);
		}

		btnColor = (Button) findViewById(R.id.btnColor);
		relativeLayout = (RelativeLayout) findViewById(R.id.colorXY);
		LL= (LinearLayout) findViewById(R.id.colorXYLL);
		
		Bitmap bm = getGradual();
		LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(
				screenWidth, screenHeight);
		relativeLayout.setLayoutParams(rl);
		relativeLayout.setBackgroundDrawable(new BitmapDrawable(bm));

		Iterator iterLamp = LAMPS.entrySet().iterator();
		while (iterLamp.hasNext()) {
			Map.Entry entry = (Map.Entry) iterLamp.next();
			String lampID = (String) entry.getKey();
			Map<String, String> lampMap = (Map<String, String>) entry
					.getValue();
			Button lamp = new Button(c);
			RelativeLayout.LayoutParams lampRL = new RelativeLayout.LayoutParams(
					100, 100);
			lamp.setGravity(Gravity.LEFT);
			lamp.setLayoutParams(lampRL);
			lamp.setText(lampMap.get("name"));
			//lamp.setBackgroundColor(0xff00ff);
			lamp.setBackgroundResource(R.drawable.lamp);
			touchListener oTL = new touchListener();
			relativeLayout.addView(lamp, -1);
			lamp.setOnTouchListener(oTL);
			lamp.setTag(lampMap.get("lampID"));
			allButtons.add(lamp);

		}

		Iterator iterGROUP = GROUPS.entrySet().iterator();
		while (iterGROUP.hasNext()) {
			Map.Entry entry = (Map.Entry) iterGROUP.next();
			String groupID = (String) entry.getKey();
			Map<String, String> groupMap = (Map<String, String>) entry
					.getValue();
			Button group = new Button(c);
			RelativeLayout.LayoutParams lampRL = new RelativeLayout.LayoutParams(
					100, 100);
			group.setLayoutParams(lampRL);
			group.setText(groupMap.get("name"));
			group.setBackgroundResource(R.drawable.lamp);
			touchListener oTL = new touchListener();
			relativeLayout.addView(group, -1);
			group.setOnTouchListener(oTL);
			group.setTag(groupMap.get("groupID"));
			allButtons.add(group);

		}
		handler = new Handler();
		handler.postDelayed(new Runnable(){  
			@Override
			public void run() {
				updateView();
			}
		}, 500);
		
	}

	private int getYFromColor(Integer color) {
		int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		int Y = (int) (a * 1.0 / 255 * picHeight);
		return Y;
	}

	private int getXFromColor(Integer color) {
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		int X = (int) (color * 1.0 / 0xffffffff * picWidth);
		return X;
	}

	private Bitmap getGradual() {
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
		pic = mGradualChangeBitmap;
		return mGradualChangeBitmap;
	}

	private int getColor(int x, int y) {
		if (pic == null)
			return -1;
		if (x < 0 || x >= pic.getWidth())
			return -1;
		if (y < 0 || y >= pic.getHeight())
			return -1;
		// 获得Bitmap 图片中每一个点的color颜色值
		int color = pic.getPixel(x, y);
		// 如果你想做的更细致的话 可以把颜色值的R G B 拿到做响应的处理

		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
		return color;
	}

	private void setColor(String t, int c) {
		if (btnColor != null ) {
			btnColor.setBackgroundColor(c);
			btnColor.setText(t);
		}
		if(LL!=null){
			LL.setBackgroundColor(c);
		}
	}

	class touchListener implements OnTouchListener {
		private int lastX, lastY; // 记录移动的最后的位置

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// 获取Action
			int ea = event.getAction();
			Log.i("TAG", "Touch:" + ea);
			switch (ea) {
			case MotionEvent.ACTION_DOWN: // 按下
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				lastOperateButton = (String) v.getTag();
				break;
			/**
			 * layout(l,t,r,b) l Left position, relative to parent t Top
			 * position, relative to parent r Right position, relative to parent
			 * b Bottom position, relative to parent
			 * */
			case MotionEvent.ACTION_MOVE: // 移动
				// 移动中动态设置位置
				int dx = (int) event.getRawX() - lastX;
				int dy = (int) event.getRawY() - lastY;
				int left = v.getLeft() + dx;
				int top = v.getTop() + dy;
				int right = v.getRight() + dx;
				int bottom = v.getBottom() + dy;
				if (left < 0) {
					left = 0;
					right = left + v.getWidth();
				}
				if (right > screenWidth) {
					right = screenWidth;
					left = right - v.getWidth();
				}
				if (top < 0) {
					top = 0;
					bottom = top + v.getHeight();
				}
				if (bottom > screenHeight) {
					bottom = screenHeight;
					top = bottom - v.getHeight();
				}
				v.layout(left, top, right, bottom);
				Log.i("", "position：" + left + ", " + top + ", " + right + ", "
						+ bottom);
				// 将当前的位置再次设置
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				
				int color = getColor((left + right) / 2, bottom - 2);
				// update DB, send to lamp & server
				//v.setBackgroundColor(color);
				//v.setBackgroundResource(R.drawable.lamp);
				setColor((String) v.getTag(), color);
				lastOperateButton = (String) v.getTag();
				
				break;
			case MotionEvent.ACTION_UP: // 脱离
				lastX = (int) event.getRawX();
				lastY = (int) event.getRawY();
				if (v.getTag().toString().startsWith("L")) {
					LAMPS.get(v.getTag()).put("x", lastX + "");
					LAMPS.get(v.getTag()).put("y", lastY + "");
				} else {
					GROUPS.get(v.getTag()).put("x", lastX + "");
					GROUPS.get(v.getTag()).put("y", lastY + "");
				}
				
				String t = computeDistance(v.getTag().toString(), lastX, lastY);
				if (t != null) {
					String vTag = (String) v.getTag();
					if (vTag.startsWith("G") && t.startsWith("L"))
						break;
					allButtons.remove(relativeLayout.findViewWithTag(vTag));
					relativeLayout.removeViewInLayout(relativeLayout.findViewWithTag(vTag));
					if (vTag.startsWith("L")) {
						// 灯到灯
						if (t.startsWith("L")) {
							LAMPS.remove(vTag);
							LAMPS.remove(t);
							String gkey = generateGroupKey();
							groupmap = new HashMap<String, String>();
							groupmap.put("groupID", gkey);
							groupmap.put("name", "G " + gkey);
							groupmap.put("isOn", "true");
							groupmap.put("color", 0xffff + "");
							groupmap.put("x", 80 + "");  
							groupmap.put("y", 50 + "");
							groupmap.put("lamp1", vTag);
							groupmap.put("lamp2", t);
							GROUPS.put(gkey, groupmap);

							Button nB = (Button) relativeLayout
									.findViewWithTag(t);
							nB.setText(groupmap.get("name"));
							//nB.setBackgroundColor(0xff00ff);
							//nB.setBackgroundResource(R.drawable.lamp);
							nB.setTag(groupmap.get("groupID"));
							

						} else {
							//灯到组
							LAMPS.remove(vTag);
							Map<String, String> groupmap = GROUPS.get(t);
							String lampID = generateLampOfGroupKey(groupmap);
							groupmap.put(lampID, vTag);
						}
					} else {
						// v is a group

						if (t.startsWith("L")) {

						} else {
							//组到组
							Map<String, String> groupmapSrc = GROUPS.get(vTag);
							Map<String, String> groupmapDst = GROUPS.get(t);

							Iterator iterLamp = groupmapSrc.entrySet()
									.iterator();
							while (iterLamp.hasNext()) {
								Map.Entry entry = (Map.Entry) iterLamp.next();
								String key = (String) entry.getKey();
								String value = (String) entry.getValue();
								if (key.startsWith("lamp")) {
									String lampID = generateLampOfGroupKey(groupmapDst);
									groupmapDst.put(lampID, value);
								}
							}
							GROUPS.remove(vTag);

						}

					}

				}
				break;
			}
			return true;
		}

	}

	public void openCloseLamp(View v) {

		if (lastOperateButton.equals(""))
			return;
		if (lastOperateButton.startsWith("L")) {
			String isOn = LAMPS.get(lastOperateButton).get("isOn");
			if (isOn.equals("true")) {
				Button b = (Button) relativeLayout
						.findViewWithTag(lastOperateButton);
				//b.setBackgroundColor(Color.DKGRAY);
				LAMPS.get(lastOperateButton).put("isOn", "false");
				setColor(b.getText().toString(),Color.DKGRAY);
				((Button)v).setText("开灯");
			} else {
				Button b = (Button) relativeLayout
						.findViewWithTag(lastOperateButton);
				//b.setBackgroundColor(Color.WHITE);
				LAMPS.get(lastOperateButton).put("isOn", "true");
				int x = Integer.parseInt(LAMPS.get(lastOperateButton).get("x"));
				int y = Integer.parseInt(LAMPS.get(lastOperateButton).get("y"))-relativeLayout.getTop();
				int color = getColor(x, y);
				setColor(b.getText().toString(), color );
				((Button)v).setText("关灯");
			}
		} else {
			String isOn = GROUPS.get(lastOperateButton).get("isOn");
			if (isOn.equals("true")) {
				Button b = (Button) relativeLayout
						.findViewWithTag(lastOperateButton);
				//b.setBackgroundColor(Color.DKGRAY);
				GROUPS.get(lastOperateButton).put("isOn", "false");
				setColor(b.getText().toString(),Color.DKGRAY);
				((Button)v).setText("开灯");
			} else {
				Button b = (Button) relativeLayout
						.findViewWithTag(lastOperateButton);
				//b.setBackgroundColor(Color.WHITE);
				GROUPS.get(lastOperateButton).put("isOn", "true");
				//setColor(b.getText().toString(),Color.WHITE);
				int x = Integer.parseInt(GROUPS.get(lastOperateButton).get("x"));
				int y = Integer.parseInt(GROUPS.get(lastOperateButton).get("y"))-relativeLayout.getTop();
				int color = getColor(x, y);
				setColor(b.getText().toString(), color );
				((Button)v).setText("关灯");

			}
		}

	}

	public void bindScene(View v) {

		Intent SelectScene = new Intent(c, SelectScene.class);
		startActivityForResult(SelectScene, 2);
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);

	}

	public void addLamp(View v) {

	}

	public void deleteLamp(View v) {

	}

	public void dissolveGroup(View v) {
		if (lastOperateButton.startsWith("G")) {
			// 删除当前组 button ，新增Lamp灯 button
			Map<String, String> map = GROUPS.get(lastOperateButton);
			Button btnGroup =(Button) relativeLayout.findViewWithTag(lastOperateButton);
			int left = btnGroup.getLeft();
			int top = btnGroup.getTop();
			int right = btnGroup.getRight();
			int bottom = btnGroup.getBottom() ;
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if(key.startsWith("lamp")){
					//生产新的  Button
					Button lamp = new Button(c);
					RelativeLayout.LayoutParams lampRL = new RelativeLayout.LayoutParams(
							100, 100);
					lamp.setGravity(Gravity.LEFT);
					lamp.setLayoutParams(lampRL);
					lamp.setText(value);
					lamp.setBackgroundResource(R.drawable.lamp);
					touchListener oTL = new touchListener();
					relativeLayout.addView(lamp, -1);
					lamp.setOnTouchListener(oTL);
					lamp.setTag(value);
					allButtons.add(lamp);
					//添加到LAMPS 中
					lampmap = new HashMap<String, String>();
					lampmap.put("lampID", value);
					lampmap.put("name", value);
					lampmap.put("isOn", "true");
					lampmap.put("color",  0xffff00 + "");
					lampmap.put("x", map.get("x"));
					lampmap.put("y", map.get("y"));
					LAMPS.put(lampmap.get("lampID"), lampmap);
				}
			}
			//从GROUPS中删除组
			GROUPS.remove(lastOperateButton);
			//从allButtons 删除组对应的按钮
			allButtons.remove((Button)relativeLayout.findViewWithTag(lastOperateButton));
			//删除组按钮
			relativeLayout.removeViewInLayout(relativeLayout.findViewWithTag(lastOperateButton));
			
			handler.postDelayed(new Runnable(){
				@Override
				public void run() {
					updateView();
				}
			}, 5);
			
		} else {
			String text = "不是一个分组";
			Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
		}
	}

	public void changeName(View v) {

		new AlertDialog.Builder(c).setTitle("请输入新名字")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(new EditText(c)).setPositiveButton("确定", null)
				.setNegativeButton("取消", null).show();

	}

	public String generateGroupKey() {
		while (true) {
			int g = (int) (Math.random() * 100);

			Map<String, String> groupMap = GROUPS.get("G" + g);
			if (groupMap == null)
				return "G" + g;
		}
	}

	public String generateLampOfGroupKey(Map<String, String> groupmap) {
		while (true) {
			int g = (int) (Math.random() * 100);
			String groupMap = groupmap.get("lamp" + g);
			if (groupMap == null)
				return "lamp" + g;
		}
	}

	public void updateView() {
		int d= 80;
		int parentTop  =  relativeLayout.getTop();
		int parentLeft = relativeLayout.getLeft();
		for (Button b : allButtons) {
			String bTag = (String) b.getTag();
			Map<String,String> map;
			if(bTag.startsWith("G")){
				 map = GROUPS.get(bTag);
			}else{
				 map = LAMPS.get(bTag);
			}
			int x= Integer.parseInt( map.get("x") );
			int y= Integer.parseInt( map.get("y") );
			b.layout(x-d-parentLeft, y-d-parentTop, x-parentLeft, y-parentTop);
		}
	}

	public String computeDistance(String tag, int lastX, int lastY) {
		String result = null;
		Iterator iterLamp = LAMPS.entrySet().iterator();
		while (iterLamp.hasNext()) {
			Map.Entry entry = (Map.Entry) iterLamp.next();
			String lampID = (String) entry.getKey();
			if (lampID.equals(tag))
				continue;
			Map<String, String> lampMap = (Map<String, String>) entry
					.getValue();
			int d = distance(lastX, lastY, Integer.parseInt(lampMap.get("x")),
					Integer.parseInt(lampMap.get("y")));
			if (d < D) {
				return lampID;
			}
		}

		Iterator iterGROUP = GROUPS.entrySet().iterator();
		while (iterGROUP.hasNext()) {
			Map.Entry entry = (Map.Entry) iterGROUP.next();
			String groupID = (String) entry.getKey();
			if (groupID.equals(tag))
				continue;
			Map<String, String> groupMap = (Map<String, String>) entry
					.getValue();
			int d = distance(lastX, lastY, Integer.parseInt(groupMap.get("x")),
					Integer.parseInt(groupMap.get("y")));
			if (d < D) {
				return groupID;
			}
		}
		return result;

	}

	private int distance(int x1, int y1, int x2, int y2) {
		double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return (int) d;
	}

}