package com.shuja1497.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    public static final int EDIT_MODE_MODULE_CONSTANT = 7;
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private boolean[] mModuleStatus;
    private float mSpacing;
    private float mShapeSize;
    private float mOutlineWidth;
    private Rect[] mModuleRectangles;
    private int mOutlineColor;
    private Paint mOutlinePaint;
    private int mFillColor;
    private Paint mPaintFill;
    private float mRadius;
    private int mMaxHorizontalModules;

    public boolean[] getModuleStatus() {
        return mModuleStatus;
    }

    public void setModuleStatus(boolean[] moduleStatus) {
        mModuleStatus = moduleStatus;
    }


    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // doing our view's initialization work

        // for checking in design mode
        if (isInEditMode())
            setupEditModeValues();



        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);
        a.recycle();

        mOutlineWidth = 6f;
        mShapeSize = 144f;
        mSpacing = 30f;

        mRadius = (mShapeSize-mOutlineWidth)/2;

        // creating rectangles
        setupModuleRectangles();

        mOutlineColor = Color.BLACK;
        // making oaint instance for drawing the outilnes
        mOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setStrokeWidth(mOutlineWidth);
        mOutlinePaint.setColor(mOutlineColor);

        mFillColor = getContext().getResources().getColor(R.color.icon_orange);

        // to color inside the circle
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);
    }

    private void setupEditModeValues() {

        // in edit mode we have to provide the size of array
        boolean[] exampleModuleValues = new boolean[EDIT_MODE_MODULE_CONSTANT];

        int middle  = EDIT_MODE_MODULE_CONSTANT/2 ;
        for (int i = 0 ; i <middle ; i++){
            exampleModuleValues[i] = true ;
        }
        setModuleStatus(exampleModuleValues);
        
    }

    private void setupModuleRectangles() {
        mModuleRectangles = new Rect[mModuleStatus.length];

        for (int moduleIndex = 0; moduleIndex < mModuleStatus.length ; moduleIndex++){

            int row  = moduleIndex / mMaxHorizontalModules ;
            int column = moduleIndex % mMaxHorizontalModules ;

            // we need top and left edge first
            int x  = getPaddingStart() + (int) (column * (mShapeSize + mSpacing));// left edge
            int y =  getPaddingTop() + (int) (row * mShapeSize+mSpacing);
            // creating a rectangle
            mModuleRectangles[moduleIndex] = new Rect(x, y , x+(int)mShapeSize, y+(int)mShapeSize);

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 0;
        int desiredHeight = 0;

        // we will check that whether the available width is enough to draw all the circles
        // if not then we will draw muliple rows to draw all the circles .

//        widthMeasureSpec is an encoded integer value so we use methods  to access the contained values .
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = specWidth - getPaddingEnd() - getPaddingStart() ;
        int horizontalModuleThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        mMaxHorizontalModules = Math.min(horizontalModuleThatCanFit, mModuleStatus.length);

        desiredWidth = (int) ((mMaxHorizontalModules * (mSpacing + mShapeSize)) - mSpacing);
        desiredWidth += getPaddingStart() + getPaddingEnd() ;

        int rows = (mModuleStatus.length-1) / mMaxHorizontalModules + 1 ;// no of rows to draw all the modules

        desiredHeight = (int) ((rows * (mShapeSize + mSpacing)) - mSpacing);
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // we have got our desired width and height but we may not be able to use these values
        // bcz onMeasure method received the constraints on the width and height


        // we need to resolve our desired values against the constraint values
        int width = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        // we need to tell the system about the resolved measurements

        setMeasuredDimension(width, height);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // the onDraw method gets called very frquently so try reducing as much work as possible ..
        // like the radius will remain constant for all circles .. so we can find the radius in init
//        method only once

        // actual drawing ..
        // key drawing part is positioning the circles in correct place

        for (int moduleIndex = 0; moduleIndex < mModuleRectangles.length ; moduleIndex++){

            // x and y co-ordinate of the centre of circle
            float x = mModuleRectangles[moduleIndex].centerX(); //  x co-ordinate of the center of curent rectangle
            float y = mModuleRectangles[moduleIndex].centerY(); //  y co-ordinate of the center of curent rectangle

            // to draw the filled circle
            if (mModuleStatus[moduleIndex])
                canvas.drawCircle(x, y, mRadius, mPaintFill);// fill the circle only if the module is completed

            canvas.drawCircle(x, y, mRadius, mOutlinePaint);

        }

    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }

}

// to provide the size and measure of the custom view
