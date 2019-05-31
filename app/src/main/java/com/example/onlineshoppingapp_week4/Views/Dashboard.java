package com.example.onlineshoppingapp_week4.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineshoppingapp_week4.API.ItemAPI;
import com.example.onlineshoppingapp_week4.ItemAdapter;
import com.example.onlineshoppingapp_week4.MainActivity;
import com.example.onlineshoppingapp_week4.Model.ItemModel;
import com.example.onlineshoppingapp_week4.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    Button btnadd,btnlogout;
    List<ItemModel> itemModelList=new ArrayList<>();
    Retrofit retrofit;
    ItemAPI itemAPI;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Boolean isloggedin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView=findViewById(R.id.recyclerView);
        btnadd=findViewById(R.id.btn_additemcall);
        btnlogout=findViewById(R.id.btn_logout);
        btnadd.setOnClickListener(this);
        btnlogout.setOnClickListener(this);

        sharedPreferences=this.getSharedPreferences("APP", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        getItems();
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

    }

    public void getItems()
    {
        retrofit=new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:6060")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemAPI=retrofit.create(ItemAPI.class);

        Call<List<ItemModel>> listCall=itemAPI.getItems();
        listCall.enqueue(new Callback<List<ItemModel>>() {
            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                List<ItemModel> itemModelList=response.body();
                recyclerView.setAdapter(new ItemAdapter(itemModelList,getApplicationContext()));

            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_additemcall)
        {
            Intent intent=new Intent(Dashboard.this,Additem.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.btn_logout)
        {
            isloggedin=true;
            editor.putBoolean("loginchecker",isloggedin);
            editor.commit();


            Intent intent=new Intent(Dashboard.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }
}
