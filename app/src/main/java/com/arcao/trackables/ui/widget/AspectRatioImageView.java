package com.arcao.trackables.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.arcao.trackables.R;
import com.arcao.trackables.ui.widget.util.ViewAspectRatioMeasurer;

public class AspectRatioImageView extends ImageView {
	private static final float DEFAULT_ASPECT_RATIO = 1F;
	private final ViewAspectRatioMeasurer aspectRatioMeasurer;

	public AspectRatioImageView(Context context) {
		this(context, null);
	}

	public AspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
		float aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO);
		a.recycle();

		aspectRatioMeasurer = new ViewAspectRatioMeasurer(aspectRatio);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		aspectRatioMeasurer.measure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(aspectRatioMeasurer.getMeasuredWidth(), aspectRatioMeasurer.getMeasuredHeight());
	}
	
}
