package com.zy.deskpic;

import android.app.Activity;
import android.os.Bundle;

public class NoLayoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        //TODO ChangePic
        finish();
    }

}
