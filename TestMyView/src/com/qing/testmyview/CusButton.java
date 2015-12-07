package com.qing.testmyview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

@SuppressLint("ClickableViewAccessibility")
public class CusButton extends Button {
	// 记录坐标
	private float downX;
	private float downY;

	// 自定义属性
	private float r;
	private int mColor;
	private float alphaFactor;// 透明系数

	// 绘画相关
	private Paint mPaint;
	private Rect mRect;
	private float maxR;// 最大半径
	private RadialGradient mGradient;// 颜色
	private ObjectAnimator animator;// 属性动画
	private boolean mAnimationIsCancel;
	private boolean mIsAnimating = false;// 是否有动画
	private Path mPath = new Path();

	private boolean flag = true;
	private float tempR;

	public CusButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CusButton);
		try {
			r = a.getDimension(R.styleable.CusButton_radiusRoud, 50);
			tempR = r;
			r = 0;
			mColor = a.getColor(R.styleable.CusButton_radiusColor, mColor);
			alphaFactor = a.getFloat(R.styleable.CusButton_alphaFloat, alphaFactor);
		} finally {
			a.recycle();
		}

	}

	public CusButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CusButton(Context context) {
		super(context);
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAlpha(100);
		setmColor(Color.BLACK, 0.2f);

	}

	public int getmColor() {
		return mColor;
	}

	public void setmColor(int mColor, float alphaFactor) {
		this.mColor = mColor;
		this.alphaFactor = alphaFactor;
	}

	public float getR() {
		return r;
	}

	public void setR(float r) {
		this.r = r;
		if (this.r > 0) {
			mGradient = new RadialGradient(downX, downY, this.r, adjustAlphaColor(mColor), mColor, TileMode.MIRROR);
			mPaint.setShader(mGradient);
		}
		invalidate();
	}

	public int adjustAlphaColor(int color) {
		int alpha = Math.round(Color.alpha(color) * alphaFactor);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(alpha, red, green, blue);
	}

	public float getAlphaFactor() {
		return alphaFactor;
	}

	public void setAlphaFactor(float alphaFactor) {
		this.alphaFactor = alphaFactor;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean res = super.onTouchEvent(event);
		if (isEnabled() && event.getActionMasked() == MotionEvent.ACTION_DOWN) {
			mRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
			downX = event.getX();
			downY = event.getY();
			mAnimationIsCancel = false;
			animator = ObjectAnimator.ofFloat(this, "r", 0, tempR).setDuration(400);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
					mIsAnimating = true;
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// setR(0);
					setAlpha(1);
					mIsAnimating = false;
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});
			animator.start();
			if (!res) {
				return true;
			}
		} else if (event.getActionMasked() == MotionEvent.ACTION_MOVE && isEnabled()) {
			downX = event.getX();
			downY = event.getY();
			mAnimationIsCancel = !mRect.contains(getLeft() + (int) downX, getTop() + (int) downY);
			if (mAnimationIsCancel = !mRect.contains(getLeft() + (int) event.getX(), getTop() + (int) event.getY()))
				setR(0);
			else
				setR(tempR);
			if (!res) {
				return true;
			}

		} else if (isEnabled() && event.getActionMasked() == MotionEvent.ACTION_UP && !mAnimationIsCancel) {
			downX = event.getX();
			downY = event.getY();
			float tempR2 = (float) Math.sqrt(downX * downX + downY * downY);
			float targetR = Math.max(tempR2, maxR);
			if (mIsAnimating) {
				animator.cancel();
			}
			animator = ObjectAnimator.ofFloat(this, "r", r, targetR);
			animator.setDuration(500);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
					mIsAnimating = true;
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					setR(0);
					setAlpha(1);
					mIsAnimating = false;
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}
			});
			animator.start();
			if (!res) {
				return true;
			}

		}

		return res;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		if (isInEditMode()) {
			return;
		}

		canvas.save(Canvas.CLIP_SAVE_FLAG);
		mPath.reset();
		mPath.addCircle(downX, downY, r, Path.Direction.CW);
		canvas.clipPath(mPath);
		canvas.restore();
		canvas.drawCircle(downX, downY, r, mPaint);
//		if (flag) {
//            r = tempR;
//			flag = false;
//			return;
//		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		maxR = (float) Math.sqrt(w * w + h * h);
	}

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// int width;
	// int height;
	// int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	// int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	// String text = getText().toString();
	// float textwidth = mPaint.measureText(text);
	// if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
	// width = widthSize;
	// } else {
	//
	// width = (int) (textwidth + 50);
	// }
	// if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
	// height = heightSize;
	// } else {
	// FontMetrics fm = mPaint.getFontMetrics();
	// height = (int) (fm.descent - fm.ascent + 2);
	//
	// }
	// setMeasuredDimension(width, height);
	// }

}
