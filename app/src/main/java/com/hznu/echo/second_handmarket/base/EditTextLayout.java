package com.hznu.echo.second_handmarket.base;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hznu.echo.second_handmarket.R;

/**
 * Created by wengqian on 2018/4/7.
 */

public class EditTextLayout extends LinearLayout {
    private EditText editText;
    private ImageView imageView;
    public EditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edittext,this);
        editText = (EditText)findViewById(R.id.edittext);
        imageView = (ImageView) findViewById(R.id.delete);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 获得文本框中的文字
                String text = editText.getText().toString().trim();
                if ("".equals(text)) {
                    imageView.setVisibility(View.INVISIBLE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    public void setHint(String s){
        SpannableString hint = new SpannableString(s);//这里输入自己想要的提示文字
        editText.setHint(hint);
    }

    public String getTextView(){
        return editText.getText().toString();
    }

    public void setInputType(int inputType) {
        editText.setInputType(inputType);
    }
}

