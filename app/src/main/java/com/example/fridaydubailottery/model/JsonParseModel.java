package com.example.fridaydubailottery.model;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParseModel {


    public static UserModel getUser(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);

            JSONObject userObject = jsonObject.getJSONObject("user");
            String token = jsonObject.getString("access_token");
            String id = userObject.getString("id");
            String email = userObject.getString("email");
            String userName = userObject.getString("name");


            return new UserModel(id, userName, token, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
