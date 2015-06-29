package com.bstry.bsby.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bstry.bsby.Catalog;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangls on 2/7/15.
 */
public class SearchFragment extends BaseFragment {

    ImageLoader imageLoader;
    DisplayImageOptions options;
    QuickAdapter adapter;
    private QuickAdapter<Catalog> catalogAdapter;
    private ListView lvCatalog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, null);


        lvCatalog = (ListView) view.findViewById(R.id.listView);
        catalogAdapter = new QuickAdapter<Catalog>(getActivity(), R.layout.item_search_catalog) {

            @Override
            protected void convert(BaseAdapterHelper baseAdapterHelper, Catalog catalog) {
                baseAdapterHelper.setText(R.id.tv_public_board, catalog.name);
            }
        };

        lvCatalog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Catalog catalog = (Catalog) parent.getItemAtPosition(position);
                AVQuery<AVObject> query = AVQuery.getQuery("Clothing");
                query.whereEqualTo("clothType", catalog.typeId);
                query.findInBackground(new FindCallback<AVObject>() {
                    public void done(List<AVObject> list, AVException e) {
                        showItems(list, e);

                    }
                });


            }
        });

        loadCattLogs();


        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true).build();

        adapter = new QuickAdapter<GridItem>(getActivity(), R.layout.item_search) {
            @Override
            protected void convert(BaseAdapterHelper baseAdapterHelper, GridItem gridItem) {
                baseAdapterHelper.setText(R.id.tv_public_board, gridItem.title);
                final ImageView imageView = baseAdapterHelper.getView(R.id.imageView);
                imageLoader.displayImage(gridItem.url, imageView, options, new MyImageLoadingListener());
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.CLOTHING_ID, item.objectId);
                startActivity(intent);
            }
        });

        return view;
    }

    public void loadCattLogs() {
        AVQuery<AVObject> query = AVQuery.getQuery("ClothType");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    List<Catalog> list = new ArrayList<Catalog>();
                    for (AVObject avObject : avObjects) {
                        Catalog catalog = new Catalog();
                        catalog.name = avObject.getString("name");
                        catalog.typeId = avObject.getString("typeId");
                        list.add(catalog);
                    }


                    catalogAdapter.addAll(list);
                    lvCatalog.setAdapter(catalogAdapter);
                }
            }
        });
    }

    private void showItems(List<AVObject> list, AVException e) {
        // comments now contains the comments for posts with images.
        if (e == null) {
            adapter.clear();
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
                                            adapter.notifyDataSetChanged();
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
