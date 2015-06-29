package com.bstry.bsby.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bstry.bsby.GridItem;
import com.bstry.bsby.R;
import com.bstry.bsby.activity.DetailActivity;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cn.trinea.android.common.view.DropDownListView;

/**
 * Created by zhangls on 2/7/15.
 */
public class SbFragment extends BaseFragment {

    private String TAG = SbFragment.class.getSimpleName();

    private QuickAdapter<GridItem> adapter;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private DropDownListView dropDownListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sb, null);
        dropDownListView = (DropDownListView) view.findViewById(R.id.listView);


        //set drop down listener
        dropDownListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {
            @Override
            public void onDropDown() {
                dropdownLoad(true);
            }
        });

//        // set on bottom listener
//        listView.setOnBottomListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                dropdownLoad(false);
//            }
//        });

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true).build();

        adapter = new QuickAdapter<GridItem>(getActivity(), R.layout.item_grid) {
            @Override
            protected void convert(BaseAdapterHelper baseAdapterHelper, GridItem gridItem) {
                baseAdapterHelper.setText(R.id.tv_public_board, gridItem.title);
                final ImageView imageView = baseAdapterHelper.getView(R.id.imageView);
                imageLoader.displayImage(gridItem.url, imageView, options, new MyImageLoadingListener());
            }
        };
        dropDownListView.setAdapter(adapter);
        dropDownListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.CLOTHING_ID, item.objectId);
                startActivity(intent);
            }
        });


        dropdownLoad(true);
        return view;
    }

    private void dropdownLoad(boolean b) {
        AVQuery<AVObject> query = AVQuery.getQuery("Clothing");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> list, AVException e) {
                showItems(list, e);
                dropDownListView.onBottomComplete();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showItems(List<AVObject> list, AVException e) {
        // comments now contains the comments for posts with images.
        if (e == null) {
            for (AVObject avObject : list) {
                final GridItem item = new GridItem();
                item.objectId = avObject.getObjectId();
                item.title = avObject.getString("title");
                avObject.getRelation("image").getQuery().findInBackground(
                        new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> avObjects, AVException e) {
                                if (e == null) {
                                    for (int i = 0; i < avObjects.size(); i++) {
                                        AVObject object = avObjects.get(i);
                                        if (object.getBoolean("isSource")) {
                                            item.url = object.getAVFile("image").getUrl();
                                            adapter.add(item);
                                            break;
                                        }
                                    }
                                }
                            }
                        });
            }
        }
    }

    private class MyImageLoadingListener implements ImageLoadingListener {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            ImageView imageView1 = (ImageView) view;
            imageView1.setImageBitmap(BitmapFactory.decodeResource(
                    getActivity().getResources(), R.drawable.load_image));
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            ImageView imageView1 = (ImageView) view;
            imageView1.setImageBitmap(loadedImage);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }
}
