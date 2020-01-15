package com.vrgsoft.redditstop.data;

import android.content.Context;

public class Repository {

    private static Repository sRepository;

    public Repository(Context context) {

    }

    public static Repository getInstance(final Context context) {
        if (sRepository == null) {
            synchronized (Repository.class) {
                if (sRepository == null) {
                    sRepository = new Repository(context);
                }
            }
        }
        return sRepository;
    }

}
