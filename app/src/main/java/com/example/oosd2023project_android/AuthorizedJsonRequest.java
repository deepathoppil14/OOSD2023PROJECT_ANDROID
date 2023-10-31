package com.example.oosd2023project_android;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * OOSD Workshop 8 - Team 2 - 2023
 *
 * This is an extension of the Volley JsonObjectRequest to enable
 * easily adding a token value to the HTTP Authorization header.
 * Author: Grayson
 */
public class AuthorizedJsonRequest extends JsonObjectRequest {

    // the token value
    private String token;

    // The same as the JsonObjectRequest constructor, however
    // it accepts token as a parameter
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

    // Returns a map containing the appropriate authorization header
    // for calls to the workshop 7 secured endpoints
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
