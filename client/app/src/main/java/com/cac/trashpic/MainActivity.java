package com.cac.trashpic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.cac.trashpic.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Variables.snackView = view;

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Variables.CAMERA_REQUEST);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cardboard, R.id.nav_glass, R.id.nav_metal, R.id.nav_paper, R.id.nav_plastic, R.id.nav_trash)
                .setOpenableLayout(drawer)
                .build();
        Variables.navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, Variables.navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, Variables.navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Variables.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.i("get img", "saving bmp");
            Variables.bmp = (Bitmap) data.getExtras().get("data");
            Log.i("get img", "saved img in bmp");
            //Variables.imageView.setImageBitmap(Variables.bmp);

            //Toast.makeText(MainActivity.this, (CharSequence)"Checking", Toast.LENGTH_SHORT).show();
            //Snackbar.make(Variables.snackView, (CharSequence)"Checking", Snackbar.LENGTH_SHORT).show();

            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            Variables.bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
            byte[] byteArray = byteArrayBitmapStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("img", "img.jpg",
                            RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                    .build();

            Request request = new Request.Builder()
                    .url(Variables.endpoint_url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("error", "request failure");
                    //Toast.makeText(MainActivity.this, (CharSequence)"Error - try again in 20 seconds", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(Variables.snackView, (CharSequence)"Error - try again in 20 seconds", Snackbar.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException{

                    Variables.responseBody = response.body().string();

                    Log.i("response", String.valueOf(response.code()) + " : " + Variables.responseBody);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Variables.responseVar = response;
                            Log.i("setting text", "in try-catch");
                            //tv.setText(Variables.responseBody);
                            Log.i("setting text", "set text");


                            Variables.resultJSON = new JsonParser().parse(Variables.responseBody).getAsJsonObject();

                            Variables.resultID = Variables.resultJSON.get("prediction").getAsInt();

                            Log.i("result", String.valueOf(Variables.resultID));

                            switch (Variables.resultID) {
                                case 0:
                                    Variables.navController.navigate(R.id.nav_cardboard);
                                    break;
                                case 1:
                                    Variables.navController.navigate(R.id.nav_glass);
                                    break;
                                case 2:
                                    Variables.navController.navigate(R.id.nav_metal);
                                    break;
                                case 3:
                                    Variables.navController.navigate(R.id.nav_paper);
                                    break;
                                case 4:
                                    Variables.navController.navigate(R.id.nav_plastic);
                                    break;
                                case 5:
                                    Variables.navController.navigate(R.id.nav_trash);
                                    break;
                                default:
                                    Log.i("error", "result id is not valid");
                                    break;
                            }




                        }
                    });
                }
            });
        }
    }
}