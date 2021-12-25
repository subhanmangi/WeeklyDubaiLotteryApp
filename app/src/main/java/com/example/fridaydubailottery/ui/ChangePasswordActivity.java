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
import com.example.fridaydubailottery.utils.SharedPreference;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements VollyResponseCallsBack {
    private VollyServerCom vollyServerCom;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                    if (newPassword.equals(confirmPassword)) {
                        JSONObject newPasswordObject = new JSONObject();
                        try {
                            newPasswordObject.put("password", oldPassword);
                            newPasswordObject.put("new_password", newPassword);
                            newPasswordObject.put("confirm_password", confirmPassword);

                            vollyServerCom.vollyAuthPOSTRequest(Constants.INSTANCE.getURI_CHANGE_PASSWORD(), newPasswordObject,
                                    "Bearer " + SharedPreference.INSTANCE.getSimpleString(context, Constants.INSTANCE.getAUTHORIZATION()),
                                    "changePassword,", 0, "Changing Password", false);

                        } catch (Exception e) {
                            Extension.showToast("There is some issue please try again later", context);
                            e.printStackTrace();
                        }
                    } else {
                        Extension.showToast("New and confirmed password not same", context);
                    }
                } else {
                    Extension.showToast("Please filled the fields", context);
                }
            }
        });
    }

    @Override
    public void onVollySuccess(String result, int request_id) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            String message = jsonObject.getString("message");
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            etConfirmPassword.setText("");
            etNewPassword.setText("");
            etOldPassword.setText("");
        } catch (Exception exception) {
            Toast.makeText(context, "There is some issue try again later", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onVollyError(String error, int request_id) {
        Toast.makeText(context, "There is some issue try again later", Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        context = ChangePasswordActivity.this;
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        vollyServerCom = new VollyServerCom(context, this);
    }

}