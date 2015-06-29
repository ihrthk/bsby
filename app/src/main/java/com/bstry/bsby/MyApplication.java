package com.bstry.bsby;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by zhangls on 2/13/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        String applicationId = "54bn58lg5qdvg8vv2sytxx6es2zpq09ygh70q9573i35i7ah";
        String clientKey = "4z3x92tq318ify22bvhj31lds5r8qj6rulxktvr5aqhxp8zy";
        AVOSCloud.initialize(this, applicationId, clientKey);
    }
}
