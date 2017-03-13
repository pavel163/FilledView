package com.ebr163.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ergashev on 12.03.17.
 */

public class FillableView extends View {

    private static final String SCHEMA = "http://schemas.android.com/apk/res/android";

    public static final int START_LEFT = 0;
    public static final int START_TOP = 1;
    public static final int START_RIGHT = 2;
    public static final int START_BOTTOM = 3;

    private int startPosition = START_LEFT;
    private int fillColor = Color.BLACK;
    private String text;
    private int radius = 0;
    private int textSize;

    private final Path textPath = new Path();
    private final Path croppedProgressPath = new Path();
    private final Path croppedTextPath = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width;
    private int height;
    private float percent = 0.1F;
    private Path progressStrokePath = new Path();
    private Rect textBounds = new Rect();

    private String widthType;
    private String heightType;
    //helpers
    private final Region region = new Region();
    private final Region textRegion = new Region();

    public FillableView(Context context) {
        this(context, null);
    }

    public FillableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initText();
    }

    private void initAttrs(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            heightType = attrs.getAttributeValue(SCHEMA, "layout_height");
            widthType = attrs.getAttributeValue(SCHEMA, "layout_width");

            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.FillableView,
                    0, 0);
            try {
                fillColor = a.getColor(R.styleable.FillableView_fill_color, Color.BLACK);
                startPosition = a.getInteger(R.styleable.FillableView_start_position, 0);
                text = a.getString(R.styleable.FillableView_text);
                textSize = a.getDimensionPixelSize(R.styleable.FillableView_textSize,
                        getContext().getResources().getDimensionPixelSize(R.dimen.defaultTextSize));
                radius = a.getDimensionPixelSize(R.styleable.FillableView_radius, 0);
            } finally {
                a.recycle();
            }
        }
    }

    private void initText() {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int textHeight = 0;
        int textWidth = 0;

        if (text != null) {
            paint.getTextBounds(text, 0, text.length(), textBounds);
            textHeight = textBounds.height();
            textWidth = textBounds.width();
        }

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            if (widthType.equals("-1")) {
                width = widthSize;
            } else {
                width = textWidth;
            }
        } else {
            width = textWidth;
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            if (heightType.equals("-1")) {
                height = heightSize;
            } else {
                height = textHeight;
            }
        } else {
            height = textHeight;
        }

        int cx = width / 2;
        int cy = (height + textHeight) / 2;

        if (text != null) {
            paint.getTextPath(text, 0, text.length(), cx, cy, textPath);
        }
        progressStrokePath = getRoundRectPath(0, 0, width, height, radius);

        computePaths();
        setMeasuredDimension(width, height);
    }

    private Path getRoundRectPath(float left, float top, float right, float bottom, float radius) {
        Path roundRectPath = new Path();
        RectF rectF = new RectF();
        rectF.set(left, top, right, bottom);
        roundRectPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
        return roundRectPath;
    }

    public void setProgress(float percent) {
        if (percent >= 0f && percent <= 1F) {
            this.percent = percent;
            computePaths();
            invalidate();
        }
    }

    private void computePaths() {
        computeCroppedProgressPath();
        computeCroppedTextPath();
    }

    public void computeCroppedProgressPath() {
        if (startPosition == START_RIGHT) {
            region.set((int) (width * (1F - percent)), 0, width, height);
        } else if (startPosition == START_LEFT) {
            region.set(0, 0, (int) (width * percent), height);
        } else if (startPosition == START_TOP) {
            region.set(0, 0, width, (int) (height * percent));
        } else if (startPosition == START_BOTTOM) {
            region.set(0, (int) (height * (1F - percent)), width, height);
        }
        region.setPath(progressStrokePath, region); // INTER
        textRegion.setPath(textPath, region);
        region.op(textRegion, Region.Op.DIFFERENCE); // DIFFERENCE
        croppedProgressPath.rewind();
        region.getBoundaryPath(croppedProgressPath);
    }

    public void computeCroppedTextPath() {
        if (startPosition == START_RIGHT) {
            region.set(0, 0, (int) (width * (1F - percent)), height);
        } else if (startPosition == START_LEFT) {
            region.set((int) (width * percent), 0, width, height);
        } else if (startPosition == START_TOP) {
            region.set(0, (int) (height * percent), width, height);
        } else if (startPosition == START_BOTTOM) {
            region.set(0, 0, width, (int) (height * (1F - percent)));
        }
        textRegion.setPath(textPath, region); // INTERSECT
        croppedTextPath.rewind();
        textRegion.getBoundaryPath(croppedTextPath);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(progressStrokePath, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(croppedProgressPath, paint);
        canvas.drawPath(croppedTextPath, paint);
    }
}
