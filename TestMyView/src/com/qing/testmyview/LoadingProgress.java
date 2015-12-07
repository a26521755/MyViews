package com.qing.testmyview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class LoadingProgress extends View {

	private float mRadius;//半径
	private float mRadiusWidth;//圆的宽度
	private int mColor = Color.RED;//颜色
	private int mDirect;//方向:0代表顺时针，1代表逆时针
	private int speed;
	private Paint mPaint;//画笔
	private Paint mPaint2;//画小圆的画笔

	public LoadingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingProgress);
		try {
			mRadius = a.getDimension(R.styleable.LoadingProgress_progressRadius, 55);
			mRadiusWidth = a.getDimension(R.styleable.LoadingProgress_progressWidth, 25);
			mColor = a.getColor(R.styleable.LoadingProgress_progressColor, Color.RED);
			mDirect = a.getInt(R.styleable.LoadingProgress_progressDirect, 0);
			speed = a.getInt(R.styleable.LoadingProgress_speed, 2000);
		} finally {
			a.recycle();
		}
		init();
		
	}

	public LoadingProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingProgress(Context context) {
		this(context, null);
	}

	
	private float centerX ;
	private float centerY ;

	private void init() {
		centerX = mRadius + mRadiusWidth;
		centerY = mRadius + mRadiusWidth;
		mPaint = new Paint();
		mPaint2= new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint2.setAntiAlias(true);
		mPaint2.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(mRadiusWidth);
		mPaint2.setStrokeWidth(mRadiusWidth/2);
		mPaint.setColor(mColor);
		mPaint2.setColor(mColor);
		SweepGradient gradient;
		if(mDirect == 0){
			gradient = new SweepGradient(centerX, centerY,  Color.argb(0, 255, 255, 255),mColor);
		}else {
			gradient = new SweepGradient(centerX, centerY, mColor, Color.argb(0, 255, 255, 255));
		}
		mPaint.setShader(gradient);
		mPaint.setStyle(Paint.Style.STROKE);
	}

	public float getmRadiusWidth() {
		return mRadiusWidth;
	}

	public void setmRadiusWidth(float mRadiusWidth) {
		this.mRadiusWidth = mRadiusWidth;
	}

	public float getmRadius() {
		return mRadius;
	}

	public void setmRadius(float mRadius) {
		this.mRadius = mRadius;
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
			width = (int)(2*(mRadius+mRadiusWidth));
		}
		if(heightMode == MeasureSpec.EXACTLY){
			height = heightSize;
		}else {
			height = (int)(2*(mRadius+mRadiusWidth));
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		Log.i("tata", centerX + " -------- " + centerY);
		canvas.drawCircle(centerX + mRadius, centerY, mRadiusWidth / 2, mPaint2);
		canvas.drawCircle(centerX, centerY, mRadius, mPaint);
		startAnimation();
	}

	public void startAnimation(){
		ObjectAnimator animator;
		if(mDirect == 0){
			animator =ObjectAnimator.ofFloat(this,"rotation", 0,360);
		}else {
			animator =ObjectAnimator.ofFloat(this,"rotation", 0,-360);
		}
		animator.setInterpolator(new LinearInterpolator());
		animator.setDuration(speed);
		animator.setRepeatCount(Animation.INFINITE);
		animator.start();
		
	}

	public int getmColor() {
		return mColor;
	}

	public void setmColor(int mColor) {
		this.mColor = mColor;
	}

	public int getmDirect() {
		return mDirect;
	}

	public void setmDirect(int mDirect) {
		this.mDirect = mDirect;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
