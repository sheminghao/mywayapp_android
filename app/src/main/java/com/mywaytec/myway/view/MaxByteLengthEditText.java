package com.mywaytec.myway.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

/**
 * Created by shemh on 2017/5/16.
 */

//android上文本框输入限制最大字节数
public class MaxByteLengthEditText extends EditText {

    private int maxByteLength = 255;

    private String encoding = "utf-8";

    public MaxByteLengthEditText(Context context) {
        super(context);
        init();
    }

    public MaxByteLengthEditText(Context context,AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    private void init() {
        setFilters(new InputFilter[] {inputFilter});
    }

    public int getMaxByteLength() {
        return maxByteLength;
    }

    public void setMaxByteLength(int maxByteLength) {
        this.maxByteLength = maxByteLength;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public int getCurrentByteLength() {
        return len;
    }

    int len;

    /**
     * input输入过滤
     */
    private InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            try {
                len = 0;
                boolean more = false;
                do {
                    SpannableStringBuilder builder =
                            new SpannableStringBuilder(dest).replace(dstart, dend, source.subSequence(start, end));
//                    len = builder.toString().getBytes(encoding).length;
                    len = source.toString().length();
                    more = len > maxByteLength;
                    if (more) {
                        end--;
                        source = source.subSequence(start, end);
                    }
                } while (more);
                return source;
            } catch (Exception e) {
                return "Exception";
            }
        }
    };
}
