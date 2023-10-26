package com.example.oosd2023project_android;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizedJsonRequest extends JsonObjectRequest {

    private String token;

    public AuthorizedJsonRequest(
            int method,
            String token,
            String url,
            @Nullable JSONObject jsonRequest,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
