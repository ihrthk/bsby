package com.bstry.bsby.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.bstry.bsby.R;

/**
 * Created by zhangls on 15/4/11.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText(getVersion());
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "当前版本：" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "没有发现当前版本";
        }
    }
}
