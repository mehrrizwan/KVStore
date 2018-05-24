package com.mehrrizwan.keyvaluestore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mehrrizwan.lib.keyvaluestore.greendao.KVStore;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KVStore.initialize(this);
        KVStore.put("testkey", "testvalue");
        Log.d("KeyValueStore", KVStore.getString("testkey"));

    }
}
