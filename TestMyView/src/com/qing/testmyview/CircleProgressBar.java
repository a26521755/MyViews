package com.qing.testmyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



/**
 * @author 郭清青
 * 
 * 圆形可显示进度的progressbar
 *
 */
public class CircleProgressBar extends View {
	private int max;//最大进度
	private int progress;//当前进度
	private Paint paint;//画笔
	private RectF oval;//画进度条的RectF
	private boolean showText;//是否显示进度文字
	private int color;//进度条颜色
	private float r;//半径
	private float roundWidth;//进度宽度
	private int textColor;//文字颜色
	private String mText;
	private float textWidth;
	private float textSize;
	private float centerX;
	private float centerY;
	

	
	public CircleProgressBar(Context context) {
		super(context);
		paint = new Paint();
		oval = new RectF();
	}

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray t = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
		try {
			setShowText(t.getBoolean(R.styleable.CircleProgressBar_showText, true));
			max = t.getInt(R.styleable.CircleProgressBar_max, 100);
			color = t.getColor(R.styleable.CircleProgressBar_color, Color.YELLOW);
			textColor = t.getColor(R.styleable.CircleProgressBar_textColor, Color.BLACK);
			textSize = t.getDimension(R.styleable.CircleProgressBar_textSize, 15);
			r = t.getDimension(R.styleable.CircleProgressBar_round, 55);
			roundWidth = t.getDimension(R.styleable.CircleProgressBar_roundWidth, 10);
		} finally {
			t.recycle();
		}
		paint = new Paint();
		oval = new RectF();
		
		
	}

	public synchronized  int getMax() {
		return max;
	}

	public synchronized  void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max必须大于0!");
		}
		this.max = max;
	}

	public synchronized  int getProgress() {
		return progress;
	}

	public synchronized  void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress必须大于0!");
		}
		this.progress = progress;
		if(progress > max ) progress = max;
	
		postInvalidate();
		requestLayout();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width;
		int height;
		if(widthMode == MeasureSpec.EXACTLY){
			width = widthSize;
		}else {
			width = (int)(2*(r+roundWidth));
		}
		if(heightMode == MeasureSpec.EXACTLY){
			height = heightSize;
		}else {
			height = (int)(2*(r+roundWidth));
		}
		centerX = width/2;
		centerY = height/2;
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setAntiAlias(true);// 设置抗锯齿
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
		paint.setColor(Color.GRAY);// 设置画笔颜色
		paint.setStrokeWidth(roundWidth);// 设置画笔宽度
		paint.setStyle(Paint.Style.STROKE);// 设置中空样式
		canvas.drawCircle(centerX, centerY, r, paint);// 在中心画个半径为r的圆，宽度为roundWidth，也就是灰色的底边
		paint.setColor(color);// 设置画笔颜色（进度颜色）
		oval.set(centerX-r, centerY-r, centerX+r, centerY+r);// 设置
		canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, paint);// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
		paint.reset();// 将画笔重置
		paint.setAntiAlias(true);// 设置抗锯齿
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);// 帮助消除锯齿
		if (showText) {
			if(progress == max){
				mText = "完成";
			}else if(progress < 10){
				mText ="0" + progress+"%";
			}else {
				mText = progress+"%";
			}
			textWidth = paint.measureText(mText);
			FontMetrics fm = paint.getFontMetrics(); 
			float textHeight = fm.descent - fm.ascent +2;
			//Log.i("tata",textWidth+" ====");

			paint.setColor(textColor);
			paint.setTextSize(textSize);
			canvas.drawText(mText, centerX-textWidth*2, centerY+textHeight/2, paint);

		}

	}

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
		invalidate();
		requestLayout();
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

}
