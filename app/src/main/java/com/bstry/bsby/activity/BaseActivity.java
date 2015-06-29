package com.bstry.bsby.activity;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangls on 15/3/29.
 */
public class BaseActivity extends Activity {

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
