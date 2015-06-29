package me.zhangls.trydress;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.litesuits.common.utils.BitmapUtil;

import java.io.File;

import cat.lafosca.facecropper.FaceCropper;

/**
 * Created by BSDC-ZLS on 2015/3/26.
 */
public class CropperActivity extends Activity {

    private static final String TAG = CropperActivity.class.getSimpleName();
    FaceCropper mFaceCropper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropper);


        ImageView imageView = (ImageView) findViewById(R.id.iv_cropper);



        mFaceCropper = new FaceCropper(1f);
        mFaceCropper.setEyeDistanceFactorMargin(2);
//        mFaceCropper.setDebug(true);


        File filesDir = getFilesDir();
        final File cacheDir = getCacheDir();
        Log.d(TAG,"DD");

        new AsyncTask<Void, Long, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
                FaceCropper.CropResult cropResult = mFaceCropper.cropFace(source, false);
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test1_);
                Bitmap bitmap = mFaceCropper.getCroppedImage(cropResult, bitmap1);

                File file = new File(getCacheDir(), "test");
                boolean test = BitmapUtil.saveBitmap(bitmap, file);

                return file.getAbsolutePath();
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Intent intent=new Intent(CropperActivity.this, PreviewActivity.class);
                intent.putExtra(PreviewActivity.PIC,string);
                startActivity(intent);

            }
        }.execute();
    }
}
