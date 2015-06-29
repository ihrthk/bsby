package com.bstry.bsby.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.bstry.bsby.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangls on 15/3/7.
 */
public class DetailActivity extends BaseActivity {

    public static final String CLOTHING_ID = "CLOTHING_ID";
    private TextView textView;
    private LinearLayout llUrls;
    private LinearLayout llImages;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ViewPager viewPager;
    private MyPageAdapter adapter;
    private CirclePageIndicator indicator;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        adapter = new MyPageAdapter(this, new ArrayList<String>());
        viewPager.setAdapter(adapter);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true).build();

        textView = (TextView) findViewById(R.id.tv_title);


        final String objectId = getIntent().getStringExtra(CLOTHING_ID);
        AVQuery<AVObject> query = AVQuery.getQuery("Clothing");
        query.getInBackground(objectId, new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                showUi(avObject, e);
            }
        });


    }

    private void showUi(AVObject avObject, AVException e) {
        if (e == null) {
            textView.setText(avObject.getString("title"));

            avObject.getRelation("url").getQuery().findInBackground(
                    new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> avObjects, AVException e) {
                            showUrls(avObjects, e);
                        }
                    });
            avObject.getRelation("image").getQuery().findInBackground(
                    new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> avObjects, AVException e) {
                            showImages(avObjects, e);
                        }
                    });
        }
    }

    private void showImages(List<AVObject> avObjects, AVException e) {
        if (e == null) {
            for (int i = 0; i < avObjects.size(); i++) {
                final AVObject object = avObjects.get(i);
                String url = object.getAVFile("image").getUrl();
                adapter.add(url);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showUrls(List<AVObject> avObjects, AVException e) {
        if (e == null) {
            for (int i = 0; i < avObjects.size(); i++) {
                final AVObject object = avObjects.get(i);
                String urlType = object.getString("urlType");
                String name;
                switch (urlType) {
                    case "jd":
                        name = "京东";
                        break;
                    case "taobao":
                        name = "淘宝";
                        break;
                    case "tmall":
                        name = "天猫";
                        break;
                    case "wd":
                        name = "微店";
                        break;
                    default:
                        name = "其他";
                        break;
                }
                url = object.getString("url");
                break;

            }
        }
    }

    public void onBuy(View view) {
        Intent intent = new Intent(DetailActivity.this, WebviewActivity.class);
        intent.putExtra(WebviewActivity.URL, url);
        startActivity(intent);
    }

    class MyPageAdapter extends PagerAdapter {

        private Context context;
        private List<String> list;

        private List<View> views = new ArrayList<View>();

        MyPageAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            imageLoader.loadImage(list.get(position), options, new MyImageLoadingListener(imageView));
            container.addView(imageView);
            views.add(imageView);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        public void add(String url) {
            list.add(url);
        }
    }

    private class MyImageLoadingListener implements ImageLoadingListener {
        private final ImageView imageView;

        public MyImageLoadingListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    DetailActivity.this.getResources(), R.drawable.load_image));
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            imageView.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }
}
