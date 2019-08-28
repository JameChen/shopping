/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nahuo.quicksale.customview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.nahuo.quicksale.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

@SuppressLint("ClickableViewAccessibility")
public class ViewFlowCircleIndicator extends View implements ViewFlowIndicator {
	private static final int INVALID_POINTER = -1;
	private static final int STYLE_STROKE = 0;
	private static final int STYLE_FILL = 1;

	private float mRadius;
	private float mActiveRadius = 0.5f;
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintInactive = new Paint(ANTI_ALIAS_FLAG);
	private final Paint mPaintActive = new Paint(ANTI_ALIAS_FLAG);
	private int mCurrentPage;
	private int mSnapPage;
	private int mViewCounts;
	private float mPageOffset;
	private int mOrientation;
	private boolean mCentered;
	private boolean mSnap;
	private boolean mStroke;

	private ViewFlow mViewFlow;

	private int mTouchSlop;
	private float mLastMotionX = -1;
	private int mActivePointerId = INVALID_POINTER;
	private boolean mIsDragging;

	public ViewFlowCircleIndicator(Context context) {
		this(context, null);
	}

	public ViewFlowCircleIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewFlowCircleIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		// Retrieve styles attributs
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);

		// Gets the inactive circle type, defaulting to "fill"
		int activeType = a.getInt(R.styleable.CircleIndicator_activeType, STYLE_FILL);

		int activeDefaultColor = 0xFFFFFFFF;

		// Get a custom inactive color if there is one
		int activeColor = a.getColor(R.styleable.CircleIndicator_activeColor, activeDefaultColor);

		// Gets the inactive circle type, defaulting to "stroke"
		int inactiveType = a.getInt(R.styleable.CircleIndicator_inactiveType, STYLE_STROKE);

		int inactiveDefaultColor = 0x44FFFFFF;
		// Get a custom inactive color if there is one
		int inactiveColor = a.getColor(R.styleable.CircleIndicator_inactiveColor, inactiveDefaultColor);

		// Retrieve the radius
		mRadius = a.getDimension(R.styleable.CircleIndicator_radius1, 4.0f);
		mActiveRadius = a.getDimension(R.styleable.CircleIndicator_activeRadius, 0.5f);
		mCentered = a.getBoolean(R.styleable.CircleIndicator_cir_centered, true);

		mOrientation = a.getInt(R.styleable.CircleIndicator_android_orientation, HORIZONTAL);
		mSnap = a.getBoolean(R.styleable.CircleIndicator_snap, false);

		mStroke = a.getBoolean(R.styleable.CircleIndicator_stroke, false);

		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setColor(a.getColor(R.styleable.CircleIndicator_strokeColor, Color.parseColor("#FFDDDDDD")));
		mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.CircleIndicator_cir_strokeWidth, 2.5f));

		a.recycle();

		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		initColors(activeColor, inactiveColor, activeType, inactiveType);
	}

	private void initColors(int activeColor, int inactiveColor, int activeType, int inactiveType) {
		// Select the paint type given the type attr
		switch (inactiveType) {
			case STYLE_FILL:
				mPaintInactive.setStyle(Style.FILL);
				break;
			default:
				mPaintInactive.setStyle(Style.STROKE);
		}
		mPaintInactive.setColor(inactiveColor);

		// Select the paint type given the type attr
		switch (activeType) {
			case STYLE_STROKE:
				mPaintActive.setStyle(Style.STROKE);
				break;
			default:
				mPaintActive.setStyle(Style.FILL);
		}
		mPaintActive.setColor(activeColor);
	}

	public void setCentered(boolean centered) {
		mCentered = centered;
		invalidate();
	}

	public boolean isCentered() {
		return mCentered;
	}

	public void setStrokeColor(int color) {
		mPaintInactive.setColor(color);
		invalidate();
	}

	public int getStrokeColor() {
		return mPaintInactive.getColor();
	}

	public void setFillColor(int fillColor) {
		mPaintActive.setColor(fillColor);
		invalidate();
	}

	public int getFillColor() {
		return mPaintActive.getColor();
	}

	public void setOrientation(int orientation) {
		switch (orientation) {
			case HORIZONTAL:
			case VERTICAL:
				mOrientation = orientation;
				requestLayout();
				break;

			default:
				throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
		}
	}

	public int getOrientation() {
		return mOrientation;
	}

	public void setRadius(float radius) {
		mRadius = radius;
		invalidate();
	}

	public float getRadius() {
		return mRadius;
	}

	public void setSnap(boolean snap) {
		mSnap = snap;
		invalidate();
	}

	public boolean isSnap() {
		return mSnap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mViewCounts = getViewCount();
		if (mViewCounts == 0) {
			return;
		}

		if (mCurrentPage >= mViewCounts) {
			setCurrentItem(mViewCounts - 1);
			return;
		}

		int longSize;
		int longPaddingBefore;
		int longPaddingAfter;
		int shortPaddingBefore;
		if (mOrientation == HORIZONTAL) {
			longSize = getWidth();
			longPaddingBefore = getPaddingLeft();
			longPaddingAfter = getPaddingRight();
			shortPaddingBefore = getPaddingTop();
		} else {
			longSize = getHeight();
			longPaddingBefore = getPaddingTop();
			longPaddingAfter = getPaddingBottom();
			shortPaddingBefore = getPaddingLeft();
		}

		final float threeRadius = mRadius * 3;
		final float shortOffset = shortPaddingBefore + mRadius;
		float longOffset = longPaddingBefore + mRadius;
		if (mCentered) {
			longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((mViewCounts * threeRadius) / 2.0f);
		}

		float dX;
		float dY;

		float pageFillRadius = mRadius;
		if (mStroke) {
			if (mPaintStroke.getStrokeWidth() > 0) {
				pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
			}
		}

		// Draw stroked circles
		for (int iLoop = 0; iLoop < mViewCounts; iLoop++) {
			float drawLong = longOffset + (iLoop * threeRadius);
			if (mOrientation == HORIZONTAL) {
				dX = drawLong;
				dY = shortOffset;
			} else {
				dX = shortOffset;
				dY = drawLong;
			}
			// Only paint fill if not completely transparent
			if (mPaintInactive.getAlpha() > 0) {
				canvas.drawCircle(dX, dY, pageFillRadius, mPaintInactive);
			}
			if (mStroke) {
				// Only paint stroke if a stroke width was non-zero
				if (pageFillRadius != mRadius) {
					canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
				}
			}
		}

		// Draw the filled circle according to the current scroll
		float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
		if (!mSnap) {
			cx += mPageOffset * threeRadius;
		}
		if (mOrientation == HORIZONTAL) {
			dX = longOffset + cx;
			dY = shortOffset;
		} else {
			dX = shortOffset;
			dY = longOffset + cx;
		}
		canvas.drawCircle(dX, dY, pageFillRadius + mActiveRadius, mPaintActive);
	}

	public boolean onTouchEvent(MotionEvent ev) {
		if (super.onTouchEvent(ev)) {
			return true;
		}
		mViewCounts = getViewCount();
		if (mViewCounts == 0) {
			return false;
		}

		final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
				mLastMotionX = ev.getX();
				break;

			case MotionEvent.ACTION_MOVE: {
				final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
				final float x = MotionEventCompat.getX(ev, activePointerIndex);
				final float deltaX = x - mLastMotionX;

				if (!mIsDragging) {
					if (Math.abs(deltaX) > mTouchSlop) {
						mIsDragging = true;
					}
				}

				if (mIsDragging) {
					mLastMotionX = x;
				}

				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (!mIsDragging) {
					final int width = getWidth();
					final float halfWidth = width / 2f;
					final float sixthWidth = width / 6f;

					if ((mCurrentPage > 0) && (ev.getX() < halfWidth - sixthWidth)) {
						if (action != MotionEvent.ACTION_CANCEL) {
						}
						return true;
					} else if ((mCurrentPage < mViewCounts - 1) && (ev.getX() > halfWidth + sixthWidth)) {
						if (action != MotionEvent.ACTION_CANCEL) {
						}
						return true;
					}
				}

				mIsDragging = false;
				mActivePointerId = INVALID_POINTER;
				break;

			case MotionEventCompat.ACTION_POINTER_DOWN: {
				final int index = MotionEventCompat.getActionIndex(ev);
				mLastMotionX = MotionEventCompat.getX(ev, index);
				mActivePointerId = MotionEventCompat.getPointerId(ev, index);
				break;
			}

			case MotionEventCompat.ACTION_POINTER_UP:
				final int pointerIndex = MotionEventCompat.getActionIndex(ev);
				final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
				if (pointerId == mActivePointerId) {
					final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
					mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
				}
				mLastMotionX = MotionEventCompat.getX(ev, MotionEventCompat.findPointerIndex(ev, mActivePointerId));
				break;
		}

		return true;
	}

	public void setCurrentItem(int item) {
		mCurrentPage = item;
		invalidate();
	}

	public void notifyDataSetChanged() {
		invalidate();
	}

	/*
     * (non-Javadoc)
     *
     * @see android.view.View#onMeasure(int, int)
     */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mOrientation == HORIZONTAL) {
			setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
		} else {
			setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
		}
	}

	/**
	 * Determines the width of this mViewFlow
	 *
	 * @param measureSpec A measureSpec packed into an int
	 * @return The width of the mViewFlow, honoring constraints from measureSpec
	 */
	private int measureLong(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		mViewCounts = getViewCount();
		if ((specMode == MeasureSpec.EXACTLY) || mViewCounts == 0) {
			// We were told how big to be
			result = specSize;
		} else {
			// Calculate the width according the views count
			result = (int) (getPaddingLeft() + getPaddingRight() + (mViewCounts * 2 * mRadius) + (mViewCounts - 1) * mRadius + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this mViewFlow
	 *
	 * @param measureSpec A measureSpec packed into an int
	 * @return The height of the mViewFlow, honoring constraints from
	 * measureSpec
	 */
	private int measureShort(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the height
			result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mCurrentPage = savedState.currentPage;
		mSnapPage = savedState.currentPage;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPage = mCurrentPage;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPage;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public void setViewCount(int mViewCount) {
		this.mViewCounts = mViewCount;
	}

	public int getViewCount() {
		return mViewFlow != null ? mViewFlow.getViewsCount() : this.mViewCounts;
	}

	@Override
	public void onSwitched(View view, int position) {
		mViewCounts = getViewCount();
		if (mViewCounts != 0)
			setCurrentItem(position % mViewCounts);
	}

	@Override
	public void setViewFlow(ViewFlow view) {
		this.mViewFlow = view;
	}

	@Override
	public void onScrolled(int h, int v, int oldh, int oldv) {

	}}