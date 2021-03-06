package com.vrgsoft.redditstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.vrgsoft.redditstop.ui.ImageFragment;
import com.vrgsoft.redditstop.ui.TopPostsListFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new TopPostsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof ImageFragment){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    private void startFragment(Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.container,
                        fragment)
                .addToBackStack(tag)
                .commit();
    }

    public void startImageFragment(String url){
        ImageFragment fragment = new ImageFragment();
        //ImageFragment fragment = ImageFragment.getInstance(url);
        startFragment(fragment, ImageFragment.TAG);
    }
}
