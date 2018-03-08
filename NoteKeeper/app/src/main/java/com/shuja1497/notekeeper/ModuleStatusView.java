package com.shuja1497.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    public static final int EDIT_MODE_MODULE_CONSTANT = 7;
    public static final int INVALID_INDEX = -1;
    public static final int SHAPE_CIRCLE = 0;
    public static final float DEFAULT_OUTLINE_WIDTH_DP = 2f;
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
    private int mShape;
    private ModuleStatusAccessibilityHelper mAccessibilityHelper;

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

        setFocusable(true);

        mAccessibilityHelper = new ModuleStatusAccessibilityHelper(this);
//    we need to provide info to the system that this helper class provides accessiblity to our custom views
        ViewCompat.setAccessibilityDelegate(this, mAccessibilityHelper);// now helper class is connected to our custom view

        // changing physical pixels to independent pixels
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float displayDensity = dm.density ;
        float defaultOutlineWidthPixels = displayDensity * DEFAULT_OUTLINE_WIDTH_DP;

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        mOutlineColor = a.getColor(R.styleable.ModuleStatusView_outlineColor, Color.BLACK);// black is default
        mShape = a.getInt(R.styleable.ModuleStatusView_shape, SHAPE_CIRCLE);
        mOutlineWidth = a.getDimension(R.styleable.ModuleStatusView_outlineWidth, defaultOutlineWidthPixels);

        a.recycle(); // now we can't interact with the typed array anymore

//        mOutlineWidth = 6f;
        mShapeSize = 144f;
        mSpacing = 30f;

        mRadius = (mShapeSize-mOutlineWidth)/2;

        // creating rectangles
//        setupModuleRectangles();

//        mOutlineColor = Color.BLACK;
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

    // to forward the callbacks to the helper class we need to override 3 methods .

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        mAccessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mAccessibilityHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return mAccessibilityHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
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

    private void setupModuleRectangles(int width) {

        int availableWidth = width - getPaddingEnd() - getPaddingStart() ;
        int horizontalModuleThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        int maxHorizontalModules = Math.min(horizontalModuleThatCanFit, mModuleStatus.length);

        mModuleRectangles = new Rect[mModuleStatus.length];

        for (int moduleIndex = 0; moduleIndex < mModuleRectangles.length ; moduleIndex++){

            int row  = moduleIndex / maxHorizontalModules ;
            int column = moduleIndex % maxHorizontalModules ;

            // we need top and left edge first
            int x  = getPaddingStart() + (int) (column * (mShapeSize + mSpacing));// left edge
            int y =  getPaddingTop() + (int) (row * (mShapeSize+mSpacing));
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

//        w , h > current width and height
//        oldw , oldh > previous width and height
        // creating rectangles
        setupModuleRectangles(w);

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

            if (mShape == SHAPE_CIRCLE) {
                // x and y co-ordinate of the centre of circle
                float x = mModuleRectangles[moduleIndex].centerX(); //  x co-ordinate of the center of curent rectangle
                float y = mModuleRectangles[moduleIndex].centerY(); //  y co-ordinate of the center of curent rectangle

                // to draw the filled circle
                if (mModuleStatus[moduleIndex])
                    canvas.drawCircle(x, y, mRadius, mPaintFill);// fill the circle only if the module is completed

                canvas.drawCircle(x, y, mRadius, mOutlinePaint);
            }else {
                drawSquare(canvas, moduleIndex);
            }
        }
    }

    private void drawSquare(Canvas canvas, int moduleIndex) {
        Rect moduleRectangle = mModuleRectangles[moduleIndex];

        if(mModuleStatus[moduleIndex])
            canvas.drawRect(moduleRectangle, mPaintFill);

        canvas.drawRect(moduleRectangle.left + (mOutlineWidth/2),
                moduleRectangle.top + (mOutlineWidth/2),
                moduleRectangle.right - (mOutlineWidth/2),
                moduleRectangle.bottom - (mOutlineWidth/2),
                mOutlinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //event provides info about the touch event .. also provides the touch action
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                // finding module which is touched
                int moduleIndex = findItemAtPoint(event.getX(), event.getY());
                onModuleSelected(moduleIndex);
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void onModuleSelected(int moduleIndex) {

        if (moduleIndex==INVALID_INDEX)
            return;

        mModuleStatus[moduleIndex] = ! mModuleStatus[moduleIndex];
        // if a change like above occurs then we need to inform the system that the view needs to be redrawn .
        invalidate();

        // we need to update the accessibility state
        mAccessibilityHelper.invalidateVirtualView(moduleIndex);
        mAccessibilityHelper.sendEventForVirtualView(moduleIndex, AccessibilityEvent.TYPE_VIEW_CLICKED);
    }

    private int findItemAtPoint(float x, float y) {
        int moduleIndex = INVALID_INDEX;

        for (int i=0; i<mModuleRectangles.length; i++){
            if (mModuleRectangles[i].contains((int) x, (int) y)){
                moduleIndex = i;
                break;
            }
        }
        return moduleIndex;
    }

    // ExploreByTouchHelper handles many accessibility details .
    private class ModuleStatusAccessibilityHelper extends ExploreByTouchHelper{

        /**
         * Constructs a new helper that can expose a virtual view hierarchy for the
         * specified host view.
         *
         * @param host view whose virtual view hierarchy is exposed by this helper
         */
        public ModuleStatusAccessibilityHelper(View host) {
            // the view will be a reference to our custom view class
            super(host);
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {

            // when view is selected when user taps on them
            int moduleIndex = findItemAtPoint(x, y);
            return moduleIndex==INVALID_INDEX ? ExploreByTouchHelper.INVALID_ID : moduleIndex;
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            if (mModuleRectangles == null)
                return;

            // first we need to provide id values to each of module shape within the custom view
            for (int moduleIndex =0; moduleIndex < mModuleRectangles.length ; moduleIndex++)
                virtualViewIds.add(moduleIndex);
            // after setting the virtual view IDs we will now setup the viryual views in onPopul...
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {

            node.setFocusable(true);
            node.setBoundsInParent(mModuleRectangles[virtualViewId]);
            node.setContentDescription("Module " + virtualViewId);// for screen reader .

            node.setCheckable(true);
            node.setChecked(mModuleStatus[virtualViewId]);

            // virtual view supporting the click action from the D-Pad
            node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            // handling action from the center d-Pad button
            switch (action){
                case AccessibilityNodeInfoCompat.ACTION_CLICK:
                    onModuleSelected(virtualViewId);
                    return true;
            }
            return false;
        }
    }
}

// to provide the size and measure of the custom view
