package com.example.mylibrary;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void showMsg(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
