package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

public class ToastUtilFromFirstModule {
    public static void showMsg(Context context, String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
