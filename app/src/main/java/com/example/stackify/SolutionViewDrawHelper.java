package com.example.stackify;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Handles functions relating to drawing on the canvas.
 */
public class SolutionViewDrawHelper {
    private Canvas canvas;
    private Paint boxPaint;
    private Paint pathPaint;
    private Paint framePaint;
    private Paint textPaint;
    private int cardboardLight;
    private int cardboardDark;
    private int appGreen;
    private int appBlack;
    private int textOffsetTop;
    private int textOffsetRight;


    public SolutionViewDrawHelper(Canvas canvas) {
        this.canvas = canvas;
        this.cardboardLight = Color.rgb(154, 113, 65);
        this.cardboardDark = Color.rgb(126, 92, 53);
        this.appGreen = Color.rgb(29, 185, 84);
        this.appBlack = Color.rgb(25, 20, 20);
        this.textOffsetTop = 60;
        this.textOffsetRight = 100;

        boxPaint = new Paint();
        boxPaint.setTextSize(boxPaint.getTextSize() * 4);
        boxPaint.setAntiAlias(true);
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(cardboardLight);

        pathPaint = new Paint();
        pathPaint.setDither(true);
        pathPaint.setColor(cardboardDark);
        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(1);

        framePaint = new Paint();
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(10);
        framePaint.setColor(appBlack);
        framePaint.setStrokeWidth(5);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textPaint.getTextSize() * 4);
        textPaint.setStrokeWidth(5);
        textPaint.setColor(Color.BLACK);
    }

    /**
     * Draws a rectangle with the specified dimensions
     */
    public void drawBox(Box box, int containerHeight, int offset) {
        // Since (0,0) is the top-left corner of the device, y-axis calculations need to take that into account
        int top = containerHeight - box.getBottomLeft().getY() + offset;
        int bottom = top - box.getHeight();
        int left = box.getBottomLeft().getX();
        int right = left + box.getWidth();
        canvas.drawRect(left, top, right, bottom, boxPaint);
        // Draw a black frame around the rectangle
        canvas.drawRect(left, top, right, bottom, framePaint);

        // Draw label for the box
        String formattedString = String.format("%d", box.getUnpackOrder());
        canvas.drawText(formattedString, right - textOffsetRight , bottom + textOffsetTop, textPaint);
    }

    /**
     * Draws two parallelograms that give a rectangle a 3D effect
     * @param box
     * @param offset
     */
    public void drawBox3D(Box box, int containerHeight, int offset) {
        // Since (0,0) is the top-left corner of the device, y-axis calculations need to take that into account
        int top = containerHeight - box.getBottomLeft().getY() + offset;
        int bottom = top - box.getHeight();
        int left = box.getBottomLeft().getX();
        int right = left + box.getWidth();

        // Draw the top parallelogram
        Path topRect = new Path();
        topRect.setFillType(Path.FillType.EVEN_ODD);
        topRect.moveTo(left + offset, bottom - offset);
        topRect.lineTo(left, bottom);
        topRect.lineTo(right + offset, bottom - offset);
        topRect.moveTo(right, bottom);
        topRect.lineTo(right + offset ,bottom - offset);
        topRect.lineTo(left, bottom);
        topRect.close();

        canvas.drawPath(topRect, pathPaint);

        // Draw the edges around the top parallelogram
        Path topEdges = new Path();
        topEdges.moveTo(left, bottom);
        topEdges.lineTo(left + offset, bottom - offset);
        topEdges.moveTo(left + offset, bottom - offset);
        topEdges.lineTo(right + offset, bottom - offset);
        topEdges.moveTo(right + offset, bottom - offset);
        topEdges.lineTo(right, bottom);
        topEdges.moveTo(right, bottom);

        canvas.drawPath(topEdges, framePaint);

        // Draw the right parallelogram
        Path rightRect = new Path();
        rightRect.setFillType(Path.FillType.EVEN_ODD);
        rightRect.moveTo(right + offset, top - offset);
        rightRect.lineTo(right, top);
        rightRect.lineTo(right + offset, bottom - offset);
        rightRect.moveTo(right, bottom);
        rightRect.lineTo(right + offset ,bottom - offset);
        rightRect.lineTo(right, top);
        rightRect.close();

        canvas.drawPath(rightRect, pathPaint);

        // Draw the edges around the right parallelogram
        Path rightEdges = new Path();
        rightEdges.moveTo(right, top);
        rightEdges.lineTo(right + offset, top - offset);
        rightEdges.moveTo(right + offset, top - offset);
        rightEdges.lineTo(right + offset, bottom - offset);
        rightEdges.moveTo(right + offset, bottom - offset);
        rightEdges.lineTo(right, bottom);
        rightEdges.moveTo(right, bottom);

        canvas.drawPath(rightEdges, framePaint);

    }
}
