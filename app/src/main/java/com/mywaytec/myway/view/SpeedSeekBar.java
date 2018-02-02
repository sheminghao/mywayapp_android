package com.mywaytec.myway.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mywaytec.myway.R;

/**
 */
public class SpeedSeekBar extends LinearLayout {

    WindowManager mWindowManager;
    TextView tvSpeed;
    SeekBar seekBar;

    public SpeedSeekBar(Context context) {
        this(context, null);
    }

    public SpeedSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr){
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        View view = View.inflate(context, R.layout.view_speed_seekbar, null);
        tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        tvSpeed.setText(seekBar.getProgress()+lowSpeed+" km/h");
        Log.i("TAG", "------初始化速度，" + seekBar.getProgress()+lowSpeed+" km/h");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeed.setText((progress+lowSpeed)+" km/h");
//                Log.i("TAG", "------seekBar改变监听速度，" + (progress+lowSpeed)+" km/h");

//                int width = mWindowManager.getDefaultDisplay().getWidth();
//                width = width * 730 / 750;
//                int mW = seekBar.getWidth() * progress / seekBar.getMax() - tvSpeed.getWidth()/2+(width-seekBar.getWidth())/2;
//                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                if (mW>tvSpeed.getWidth()/10 && mW < seekBar.getWidth()-tvSpeed.getWidth()-width+seekBar.getWidth()){
//                    params.setMarginStart(mW);
//                }else if(mW >= seekBar.getWidth()-tvSpeed.getWidth()-width+seekBar.getWidth()){
//                    mW = seekBar.getWidth() - tvSpeed.getWidth()-width+seekBar.getWidth();
//                    params.setMarginStart(mW);
//                }else {
//                    params.setMarginStart(0);
//                }
//                tvSpeed.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.i("TAG", "------onStopTrackingTouch，" + (seekBar.getProgress()+lowSpeed)+" km/h");
                if (null != onProgressChange)
                    onProgressChange.progressChange(seekBar.getProgress()+lowSpeed);
            }
        });
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        addView(view);
        higeSpeed = 25;
        lowSpeed = 15;
        setHighSpeed(higeSpeed, lowSpeed);
        setLowSpeed(higeSpeed, lowSpeed);

    }

    private OnProgressChange onProgressChange;
    public void setOnProgressChange(OnProgressChange onProgressChange){
        this.onProgressChange = onProgressChange;
    }

    public interface OnProgressChange{
        void progressChange(int progress);
    }


    private int higeSpeed;
    public void setHighSpeed(int higeSpeed, int lowSpeed){
        if (higeSpeed == 0){
            higeSpeed = 25;
        }
        this.higeSpeed = higeSpeed;
        seekBar.setMax(higeSpeed-lowSpeed);
        Log.i("TAG", "------设置最高速度，" + seekBar.getProgress()+" km/h");
    }

    private int lowSpeed;
    public void setLowSpeed(int higeSpeed, int lowSpeed){
        if (lowSpeed == 0){
            lowSpeed = 15;
        }
        this.lowSpeed = lowSpeed;
        tvSpeed.setText(seekBar.getProgress()+lowSpeed+" km/h");
        seekBar.setMax(higeSpeed-lowSpeed);
        Log.i("TAG", "------设置最低速度，" + seekBar.getProgress()+" km/h");
    }

    public void setPracticalSpeed(int Speed){
        seekBar.setProgress(Speed - lowSpeed);
        Log.i("TAG", "------设置最低速度，" + seekBar.getProgress()+" km/h");
    }

    public int getProgress(){
        return seekBar.getProgress();
    }
}