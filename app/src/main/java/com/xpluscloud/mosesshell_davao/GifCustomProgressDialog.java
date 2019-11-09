package com.xpluscloud.mosesshell_davao;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class GifCustomProgressDialog extends ProgressDialog {
//  private AnimationDrawable animation;

    public static ProgressDialog ctor(Context context) {
        GifCustomProgressDialog dialog = new GifCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    public GifCustomProgressDialog(Context context) {
        super(context);
    }

    public GifCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_progress_dialog);

//        GifPlayer la = (GifPlayer) findViewById(R.id.viewGif);
//        la.setImageResource(R.drawable.animated);
//    la.setBackgroundResource(R.drawable.custom_progress_dialog_animation);
//    animation = (AnimationDrawable) la.getBackground();
    }

    @Override
    public void show() {
        super.show();
//    animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
//    animation.stop();
    }
}

