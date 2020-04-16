package me.rubl.loftmoney.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import me.rubl.loftmoney.R;

public class DiagramView extends View {

    private float expenses = 1f;
    private float incomes = 2f;
    private int expensesColor = 0;
    private int incomesColor = 0;
    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagramView(Context context) {
        super(context);
        init();
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DiagramView, 0, 0);

        try {
            expensesColor = a.getColor(R.styleable.DiagramView_expensesColor, ContextCompat.getColor(context, R.color.dark_sky_blue));
            incomesColor = a.getColor(R.styleable.DiagramView_incomesColor, ContextCompat.getColor(context, R.color.apple_green));
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        if (expensesColor != 0) expensePaint.setColor(expensesColor);
        else expensePaint.setColor(ContextCompat.getColor(getContext(), R.color.dark_sky_blue));

        if (incomesColor != 0) incomePaint.setColor(incomesColor);
        else incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.apple_green));
    }

    public void update(float expenses, float incomes) {
        this.expenses = expenses;
        this.incomes = incomes;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float total = expenses + incomes;
        float expenseAngle = 360f * expenses / total;
        float incomesAngle = 360f * incomes / total;
        int space = 15;

        int size = Math.min(getWidth(), getHeight()) -  space * 2;

        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(
                xMargin - space, yMargin,
                getWidth() - xMargin - space,
                getHeight() - yMargin,
                180 - expenseAngle / 2, expenseAngle,
                true,
                expensePaint
        );

        canvas.drawArc(
                xMargin + space, yMargin,
                getWidth() - xMargin + space,
                getHeight() - yMargin,
                360 - incomesAngle / 2, incomesAngle,
                true, incomePaint
        );

    }
}
