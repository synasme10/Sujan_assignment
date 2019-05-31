package com.example.onlineshoppingapp_week4.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineshoppingapp_week4.API.UserAPI;
import com.example.onlineshoppingapp_week4.MainActivity;
import com.example.onlineshoppingapp_week4.Model.UserModel;
import com.example.onlineshoppingapp_week4.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupFrag extends Fragment implements View.OnClickListener{

EditText et_lname,et_fname,et_uname,et_pass;
Button btn_reg;
UserAPI uapi;

    public SignupFrag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_signup, container, false);

        et_fname= view.findViewById(R.id.etUserFname);
        et_lname=view.findViewById(R.id.etUserLname);
        et_uname=view.findViewById(R.id.etUsername);
        et_pass=view.findViewById(R.id.etPassword);
        btn_reg=view.findViewById(R.id.btnRegister);

        btn_reg.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnRegister)
        {if(Signupvalidation()==true){
            //validation_Registration();
            Gson gson=new GsonBuilder()
                    .setLenient().create();
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:6060/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            uapi=retrofit.create(UserAPI.class);
            String uFname=et_fname.getText().toString();
            String uLname=et_lname.getText().toString();
            String uname=et_uname.getText().toString();
            String pass=et_pass.getText().toString();

            UserModel userModel= new UserModel(uFname,uLname,uname,pass);


            Call<Void> Usercall=uapi.useradd(userModel);
            Usercall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }}

    }

    private boolean Signupvalidation()
    {
        if(et_fname.getText().toString().isEmpty())
        {
            et_fname.setError("Please enter your firstname");
            et_fname.requestFocus();
            return false;

        }
        if(et_lname.getText().toString().isEmpty())
        {
            et_lname.setError("Please enter your lastname");
            et_lname.requestFocus();
            return false;

        }
        if(et_uname.getText().toString().isEmpty())
        {
            et_uname.setError("Please enter your username");
            et_uname.requestFocus();
            return false;

        }
        if(et_pass.getText().toString().isEmpty())
        {
            et_pass.setError("Please enter your Password");
            et_pass.requestFocus();
            return false;

        }
        return true;
    }
}
