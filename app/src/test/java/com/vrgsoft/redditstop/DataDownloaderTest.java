package com.vrgsoft.redditstop;

import android.util.Log;

import com.vrgsoft.redditstop.data.DataDownloader;
import com.vrgsoft.redditstop.data.OnDataUpdateCallback;
import com.vrgsoft.redditstop.data.model.Post;

import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

public class DataDownloaderTest {
    private static final String TAG = "DataDownloaderTest";
    private DataDownloader mDataDownloader;

    @Before
    public void setUp() throws Exception {
        mDataDownloader = new DataDownloader(sOnDataUpdateCallback);
    }

    static OnDataUpdateCallback sOnDataUpdateCallback = new OnDataUpdateCallback() {
        @Override
        public void onDataUpdate(List<Post> posts) {
            if (posts != null){
                Log.i(TAG, "onDataUpdate: OK - posts list size = " + posts.size());
            }
        }
    };

    static final String JSON_DATA = "{\"kind\": \"Listing\", \"data\": {\"modhash\": \"5pm3fgd98xf03432ca3b52ea960e4b1ee9a0065c681481afe0\", \"dist\": 25, \"children\": [";
    static final String WRONG_JSON_DATA = "";

}