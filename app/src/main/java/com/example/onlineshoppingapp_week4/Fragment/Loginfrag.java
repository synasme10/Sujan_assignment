package com.example.onlineshoppingapp_week4.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineshoppingapp_week4.API.UserAPI;
import com.example.onlineshoppingapp_week4.R;
import com.example.onlineshoppingapp_week4.Views.Dashboard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Loginfrag extends Fragment implements View.OnClickListener{
EditText etuname,etpass;
Button btn_login;
UserAPI uapi;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Boolean isloggedin;

    public Loginfrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_loginfrag, container, false);

        etuname=view.findViewById(R.id.etUsername);
        etpass=view.findViewById(R.id.etPassword);
        btn_login=view.findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(this);

        sharedPreferences=this.getActivity().getSharedPreferences("APP", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin)
        {if(loginvalidation()==true){
            Gson gson=new GsonBuilder()
                    .setLenient().create();
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:6060/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            uapi=retrofit.create(UserAPI.class);

            String uname=etuname.getText().toString();
            String pass=etpass.getText().toString();


            Call<String> Logincall=uapi.login(uname,pass);

            Logincall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                    isloggedin=true;
                    editor.putBoolean("loginchecker",isloggedin);
                    editor.commit();


                    Intent intent=new Intent(getActivity(), Dashboard.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }}

    }
    private boolean loginvalidation()
    {
        if(etuname.getText().toString().isEmpty())
        {
            etuname.setError("Please enter your username");
            etuname.requestFocus();
            return false;

        }
        if(etpass.getText().toString().isEmpty())
        {
            etuname.setError("Please enter your password");
            etuname.requestFocus();
            return false;

        }
        return true;
    }

}
