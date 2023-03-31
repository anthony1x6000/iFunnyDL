package com.anth1x.ifunnydl;

import android.os.Environment;

import java.io.File;

public class globalDefaults {
    public static final File tmpDest = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunnyTMP");
    static final File GIFoutputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunny/");
    public static final String tmpDestString = String.valueOf(tmpDest);

//    public static void init(Context context) {
//        tmpDest = new File(context.getCacheDir(), "iFunnyTMP");
//        tmpDestString = String.valueOf(tmpDest);
//    } download manager doesnt support cache directories i guess. https://stackoverflow.com/questions/63466516/getting-java-lang-securityexception-unsupported-path-but-i-am-asking-for-permi
//if u end up using this agian, init with this in first run method in activity: globalDefaults.init(this);


}
