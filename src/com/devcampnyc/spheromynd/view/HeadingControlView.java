package com.devcampnyc.spheromynd.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.devcampnyc.spheromynd.R;

public class HeadingControlView extends View  {

  private int mHeading = 0;
  private Paint mTickerPaint, mNeedlePaint;

  public HeadingControlView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initPaint();
  }

  public HeadingControlView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initPaint();
  }
  
  public HeadingControlView(Context context) {
    super(context);
    initPaint();
  }

  private void initPaint() {
    mTickerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTickerPaint.setColor(getResources().getColor(R.color.red));
    mTickerPaint.setStrokeWidth(40);
    mTickerPaint.setStyle(Paint.Style.STROKE);
    
    mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mNeedlePaint.setColor(getResources().getColor(R.color.darkred));
    mNeedlePaint.setStrokeWidth(60);
    mNeedlePaint.setStyle(Paint.Style.STROKE);
  }
  
  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    
    int height = getMeasuredHeight();
    int width = getMeasuredWidth();

    int px = (width / 2);
    int py = (height / 2);

    Point center = new Point(px, py);

    int radius = Math.min(px, py) - 25;

    RectF boundingBox = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
    
    Path circlePath = new Path();
    
    int circleStart = -88 + mHeading;
    
    circlePath.arcTo(boundingBox, circleStart, 358);

    Path needlePath = new Path();
    
    int needleStart = (-94 + mHeading);
    
    needlePath.arcTo(boundingBox, needleStart, 8);
    
    canvas.drawPath(circlePath, mTickerPaint);
    canvas.drawPath(needlePath, mNeedlePaint);
  }
  
  public void setHeading(int degrees) {
    mHeading = degrees;
    this.invalidate();
  }
  

}
