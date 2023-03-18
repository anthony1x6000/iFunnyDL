package com.anth1x.ifunnydl;

import android.content.Context;
import android.graphics.Typeface;

public class fonts {
    public static Typeface fontButton;
    public static Typeface fontSubtitle;
    public static Typeface fontBodyText;
    public static Typeface fontTitle;
    public static Typeface fontInput;

    public static void init(Context context) {
        fontButton = Typeface.createFromAsset(context.getAssets(), "fonts/Porter.ttf");
        fontSubtitle = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Light.ttf");
        fontBodyText = Typeface.createFromAsset(context.getAssets(), "fonts/CooperHewitt-Light.otf");
        fontTitle = Typeface.createFromAsset(context.getAssets(), "fonts/Kollektif-Bold.ttf");
        fontInput = Typeface.createFromAsset(context.getAssets(), "fonts/CooperHewitt-LightItalic.otf");
    }
}