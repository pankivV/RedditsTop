package com.vrgsoft.redditstop.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vrgsoft.redditstop.MainActivity;
import com.vrgsoft.redditstop.R;
import com.vrgsoft.redditstop.Utils;
import com.vrgsoft.redditstop.data.OnImageLoadCallback;

import com.vrgsoft.redditstop.ui.viewmodel.TopPostsListViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ImageFragment extends Fragment implements OnImageLoadCallback {

    public static final String TAG = "ImageFragment";
    private static final String SAVED_IMAGE_URL_KEY = "saved_image";
    private static final String IMAGE_DESCRIPTION = "REDDIT_";

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 0;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ImageView mImageView;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;
    private TopPostsListViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_IMAGE_URL_KEY, mViewModel.getSelectedImageUrl());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment,
                container, false);
        mImageView = view.findViewById(R.id.full_res_image_view);
        mProgressBar = view.findViewById(R.id.image_fragment_progress_bar);
        mFab = view.findViewById(R.id.fab);
        mFab.hide();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermissions()){
                    saveToGallery(((BitmapDrawable)mImageView.getDrawable()).getBitmap());
                }else{
                    requestPermissions(PERMISSIONS, WRITE_EXTERNAL_STORAGE_PERMISSION);
                }
            }
        });
        setProgressView(true);
        return view;
    }

    private void saveToGallery(final Bitmap bitmap) {
        if (hasPermissions()) {
            new Runnable() {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = IMAGE_DESCRIPTION + timeStamp;

                @Override
                public void run() {
                    Utils.saveImageToGallery(getContext(),
                            bitmap,
                            imageFileName,
                            IMAGE_DESCRIPTION);
                }
            }.run();
        }else {
            requestPermissions(PERMISSIONS, WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TopPostsListViewModel.class);
        //restore url from bundle in case when system kills activity
        if (savedInstanceState != null){
            mViewModel.selectPostsImageUrl(savedInstanceState.getString(SAVED_IMAGE_URL_KEY));
        }
        mViewModel.initImageDownload(this, this);
    }

    private void setProgressView(boolean show){
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mImageView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFab.getVisibility() == View.VISIBLE){
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }
        });
        setProgressView(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "onRequestPermissionsResult: permissions granted");
                    saveToGallery(((BitmapDrawable)mImageView.getDrawable()).getBitmap());
                } else {
                    Toast.makeText(getContext(), getResources().getText(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                ((MainActivity)getActivity()).onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean hasPermissions(){
        int code = ContextCompat.checkSelfPermission(getContext(), PERMISSIONS[0]);
        return code == PackageManager.PERMISSION_GRANTED;
    }
}
