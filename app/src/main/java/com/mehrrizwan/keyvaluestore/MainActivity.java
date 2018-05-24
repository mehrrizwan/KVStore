package com.mehrrizwan.keyvaluestore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mehrrizwan.lib.keyvaluestore.greendao.KeyValueStore;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyValueStore.createInstance(this);
        KeyValueStore.getInstance().put("testkey", "testvalue");
        Log.d("KeyValueStore", KeyValueStore.getInstance().get("testkey"));

    }
}
