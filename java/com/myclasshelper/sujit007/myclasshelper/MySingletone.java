package com.myclasshelper.sujit007.myclasshelper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sujit007 on 3/30/2017.
 */

class MySingletone {

    private static MySingletone mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;


    public MySingletone(Context context){

        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public  static synchronized MySingletone getmInstance(Context context){

        if(mInstance == null){
            mInstance = new MySingletone(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T>void addTorequestQue (Request<T> request){
        requestQueue.add(request);
    }
}
