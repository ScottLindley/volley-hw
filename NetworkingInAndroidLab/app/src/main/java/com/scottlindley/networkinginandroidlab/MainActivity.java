package com.scottlindley.networkinginandroidlab;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String
            TEA_QUERY = "http://api.walmartlabs.com/v1/search?query=tea&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq",
            CHOCOLATE_QUERY = "http://api.walmartlabs.com/v1/search?query=chocolate&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq",
            CEREAL_QUERY = "http://api.walmartlabs.com/v1/search?query=cereal&format=json&apiKey=hcqn2bnrqmb8tzxt2ejjyphq";

    private RecyclerView mRecyclerView;
    private Button mChocolateBtn, mTeaBtn, mCerealBtn;
    private List<Item> mItems;
    private RequestQueue mRequestQueue;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chocolate_button:
                performVolleyRequest(CHOCOLATE_QUERY);
                break;
            case R.id.cereal_button:
                performVolleyRequest(CEREAL_QUERY);
                break;
            case R.id.tea_button:
                performVolleyRequest(TEA_QUERY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache,network);
        mRequestQueue.start();

        mChocolateBtn = (Button)findViewById(R.id.chocolate_button);
        mCerealBtn = (Button)findViewById(R.id.cereal_button);
        mTeaBtn = (Button)findViewById(R.id.tea_button);

        mItems = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setAdapter(new RecyclerAdapter(mItems));

        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if(info!=null && info.isConnected()){
            mCerealBtn.setOnClickListener(this);
            mChocolateBtn.setOnClickListener(this);
            mTeaBtn.setOnClickListener(this);
        }else{
            Toast.makeText(this, "No network connection found", Toast.LENGTH_SHORT).show();
        }
    }


    private void performVolleyRequest(String url){
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(response.toString());
                    JSONArray itemsArray = (JSONArray) rootObject.get("items");
                    mItems.removeAll(mItems);
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject jObject = (JSONObject) itemsArray.get(i);
                        mItems.add(new Item(
                                jObject.getString("name"), "Price: $" + jObject.getString("salePrice")));
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MainActivity", "onErrorResponse: UH OH WE GOT AN ERROR");
            }
        });
        mRequestQueue.add(request);
    }
}
