package com.sp.my_iot_application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

public class CircularColorView extends View {
    private Paint paint;
    private int temperature;

    public CircularColorView(Context context) {
        super(context);
        init();
    }

    public CircularColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        temperature = 0;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        int color = getBackgroundColorForTemperature(temperature);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private int getBackgroundColorForTemperature(int temperature) {
        // Define temperature ranges
        int coolTemp = 20;
        int moderateTemp = 30;
        int hotTemp = 40;

        if (temperature <= coolTemp) {
            return ContextCompat.getColor(getContext(), R.color.coolColor); // Cool Blue
        } else if (temperature <= moderateTemp) {
            return ContextCompat.getColor(getContext(), R.color.moderateColor); // Light Blue
        } else if (temperature <= hotTemp) {
            return ContextCompat.getColor(getContext(), R.color.hotColor); // Orange
        } else {
            return ContextCompat.getColor(getContext(), R.color.extremeHotColor); // Red
        }
    }
}
