package com.example.fridaydubailottery.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fridaydubailottery.MainActivity;

import com.example.fridaydubailottery.R;
import com.example.fridaydubailottery.model.JsonParseModel;
import com.example.fridaydubailottery.model.UserModel;
import com.example.fridaydubailottery.networking.VollyResponseCallsBack;
import com.example.fridaydubailottery.networking.VollyServerCom;
import com.example.fridaydubailottery.utils.Constants;
import com.example.fridaydubailottery.utils.Extension;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements VollyResponseCallsBack {
    private EditText etUserName;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignUp;
    private VollyServerCom vollyServerCom;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        initObject();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUserName.getText().toString();
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (Extension.isEmpty(name, context) && Extension.isEmpty(userName, context))
                    if (Extension.isEmailValid(email, context) && Extension.isPasswordValid(password, context)) {
                        JSONObject loginUserObject = new JSONObject();
                        try {

                            loginUserObject.put("name", name);
                            loginUserObject.put("email", email);
                            loginUserObject.put("password", password);
                            loginUserObject.put("phone", "123123");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.show();
                        vollyServerCom.vollyPOSTRequest(Constants.INSTANCE.getURI_SIGNUP(), loginUserObject, "request_tag", 0);
                    }
            }
        });
    }

    private void initObject() {
        vollyServerCom = new VollyServerCom(context, this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Signing Up");
    }

    private void initView() {
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        context = SignUpActivity.this;
    }

    @Override
    public void onVollySuccess(String result, int request_id) {
        progressDialog.dismiss();
        try {
            UserModel userModel = JsonParseModel.getUser(result);
            Constants.INSTANCE.saveUser(context, userModel);


            startActivity(new Intent(context, MainActivity.class));
            finish();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVollyError(String error, int request_id) {
        progressDialog.dismiss();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(error);
            String errorMessage = jsonObject.getString("error");
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "The email or user name has already been taken.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}