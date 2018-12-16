package com.xtrasilent.whatcats;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionHandler session;
    Button GetImageFromGalleryButton, UploadImageOnServerButton;


    ImageView ShowSelectedImage;

    EditText imageName;

    Bitmap FixBitmap;

    String ImageTag = "image_tag";

    String ImageName = "image_data";

    ProgressDialog progressDialog;

    ByteArrayOutputStream byteArrayOutputStream;

    byte[] byteArray;

    String ConvertImage;

    String GetImageNameFromEditText;

    HttpURLConnection httpURLConnection;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter;

    int RC;

    private static final String KEY_STATUS = "result";
    String URLserver = "http://54.254.229.24/upload-image-to-server.php"; //http://54.254.229.24/upload-image-to-server.php
    LinearLayout ln2;

    BufferedReader bufferedReader;

    StringBuilder stringBuilder;
    Button tryagain;
    boolean check = true;

    private int GALLERY = 1, CAMERA = 2;
    TextView welcomeText1;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        long millis = System.currentTimeMillis() % 1000;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        result = findViewById(R.id.result);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ln2 = findViewById(R.id.linear2);
        tryagain = findViewById(R.id.tryagain);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        welcomeText1 = findViewById(R.id.welcomeText);
        welcomeText1.setText("Welcome " + user.getFullName());


        Random rand = new Random();

        int n = rand.nextInt(99999) + 1;

        GetImageFromGalleryButton = findViewById(R.id.imgbtn);

        UploadImageOnServerButton = findViewById(R.id.uploadbtn);


        ShowSelectedImage = findViewById(R.id.imageView);


        imageName = findViewById(R.id.imageName);

        imageName.setText("cat" + n + millis);


        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPictureDialog();


            }
        });
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);


            }
        });

        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                GetImageNameFromEditText = imageName.getText().toString();

                if (FixBitmap == null) {
                    Toast.makeText(MainActivity.this, "Please Select/capture Image first!!", Toast.LENGTH_SHORT).show();
                    //UploadImageOnServerButton.setEnabled(false);
                } else
                    UploadImageToServer();

            }
        });


        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            session.logoutUser();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_gallery) {
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            session.logoutUser();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showPictureDialog() {


        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    UploadImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
            UploadImageOnServerButton.setVisibility(View.VISIBLE);
        }
    }


    public void UploadImageToServer() {

        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);


        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(MainActivity.this, "Image is Uploading", "Please Wait", false, false);

            }

            @Override
            protected void onPostExecute(String string1) {

                UploadImageOnServerButton.setVisibility(View.GONE);
                GetImageFromGalleryButton.setVisibility(View.GONE);
                /* RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLserver, null,

                        response -> {
                            VolleyLog.v("response:",response.toString());
                        },
                        error -> {

                        });
                //add request to queue
                requestQueue.add(jsonObjectRequest);*/


               /* RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest stringRequest =  new StringRequest(Request.Method.POST, URLserver,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                result.setText(response);
                                System.out.println("****************"+response);
                                requestQueue.stop();
                                /*finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.setText("Something happen...");
                        error.printStackTrace();
                        requestQueue.stop();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                }
                );

                MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);*/

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, URLserver, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONObject obj = new JSONObject(response.toString());

                                    String results = obj.getString("result");
                                    if (results != null) {


                                        ln2.setVisibility(View.VISIBLE);
                                        tryagain.setVisibility(View.VISIBLE);
                                        result.setText(results);


                                    } else {

                                        ln2.setVisibility(View.VISIBLE);
                                        tryagain.setVisibility(View.VISIBLE);
                                        //UploadImageOnServerButton.setVisibility(View.GONE);
                                        // GetImageFromGalleryButton.setVisibility(View.GONE);
                                        result.setText("This Not A cat maybe...");

                                    }


                                } catch (JSONException e) {
                                    result.setText(e.toString());
                                    ln2.setVisibility(View.VISIBLE);
                                    tryagain.setVisibility(View.VISIBLE);
                                    //UploadImageOnServerButton.setVisibility(View.GONE);
                                    //GetImageFromGalleryButton.setVisibility(View.GONE);
                                    result.setText("Something Wrong with the server!");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                result.setText(error.toString());
                                ln2.setVisibility(View.VISIBLE);
                                tryagain.setVisibility(View.VISIBLE);
                                UploadImageOnServerButton.setVisibility(View.GONE);
                                GetImageFromGalleryButton.setVisibility(View.GONE);
                                result.setText("TRY AGAIN");
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsObjRequest);
                super.onPostExecute(string1);


                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, string1, Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(Void... params) {


                MainActivity.ImageProcessClass imageProcessClass = new MainActivity.ImageProcessClass();
                User user = session.getUserDetails();
                String id = user.getUserID();

                HashMap<String, String> HashMapParams = new HashMap<String, String>();

                HashMapParams.put(ImageTag, GetImageNameFromEditText);

                HashMapParams.put(ImageName, ConvertImage);

                HashMapParams.put("user_id", id);

                String FinalData = imageProcessClass.ImageHttpRequest(URLserver, HashMapParams);  //192.168.43.246

                return FinalData;

            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();


    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                outputStream.flush();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null) {

                        stringBuilder.append(RC2);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            } else {

                Toast.makeText(MainActivity.this, "Error to gain permission to access the camera. Please allow to use this Application", Toast.LENGTH_LONG).show();

            }
        }
    }
}
