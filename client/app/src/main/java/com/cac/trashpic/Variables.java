package com.cac.trashpic;

import android.graphics.Bitmap;
import android.view.View;

import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Dictionary;

public class Variables {

    public static final String endpoint_url = "https://predict-bk3qauef2q-uw.a.run.app";

    public static final int CAMERA_REQUEST = 1888;

    public static Bitmap bmp;

    public static String responseBody;

    public static NavController navController;

    public static Gson gson = new Gson();

    public static JsonObject resultJSON;

    public static int resultID = -1;

    public static View snackView;

}
