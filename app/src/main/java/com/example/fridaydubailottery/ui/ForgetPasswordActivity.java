package com.example.fridaydubailottery.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.fridaydubailottery.R;
import com.example.fridaydubailottery.networking.VollyResponseCallsBack;
import com.example.fridaydubailottery.networking.VollyServerCom;
import com.example.fridaydubailottery.utils.Constants;
import com.example.fridaydubailottery.utils.Extension;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends AppCompatActivity implements VollyResponseCallsBack {
    private EditText etEmail;
    private Button btnForgetPassword;
    private Context context;
    private VollyServerCom vollyServerCom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                if (Extension.isEmailValid(email, context)) {
                    JSONObject forgetPasswordObject = new JSONObject();
                    try {
                        forgetPasswordObject.put("email", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vollyServerCom.vollyPOSTRequest(Constants.INSTANCE.getURI_FORGET_PASSWORD(), forgetPasswordObject, "request_tag", 0, "Please Wait", false);

                }
            }
        });
    }

    private void initView() {
        context = ForgetPasswordActivity.this;
        etEmail = findViewById(R.id.etEmail);
        btnForgetPassword = findViewById(R.id.btnForgetPassword);
        vollyServerCom=new VollyServerCom(context,this);
    }

    @Override
    public void onVollySuccess(String result, int request_id) {
        Toast.makeText(context, "New password sent to email", Toast.LENGTH_SHORT).show();
        etEmail.setText("");
    }

    @Override
    public void onVollyError(String error, int request_id) {
        Toast.makeText(context, "The email must be a valid email address. ", Toast.LENGTH_SHORT).show();
    }
}