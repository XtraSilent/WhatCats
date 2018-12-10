package com.xtrasilent.whatcats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;


public class delete extends AppCompatActivity {
    TextView Imagetitle;
    TextView Imageid;
    ProgressDialog progressDialog;
    ProgressDialog pd;
    ImageView Image;

    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delete);


        Intent intent = getIntent();

        String imageid = intent.getStringExtra("image_id");
        String imageurl = intent.getStringExtra("image_url");
        String imagetitle = intent.getStringExtra("image_title");


        DisplayMetrics dm = new DisplayMetrics();
        WindowManager vm = (WindowManager) getApplicationContext().getSystemService(getApplicationContext().WINDOW_SERVICE);
        vm.getDefaultDisplay().getMetrics(dm);


        pd = new ProgressDialog(delete.this);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Imagetitle = findViewById(R.id.imagetitle);
        Imageid = findViewById(R.id.imageid);
        Image = findViewById(R.id.catimage);
        delete = findViewById(R.id.btndelete);


        Imagetitle.setText(imagetitle);
        Imageid.setText(imageid);

        Glide.with(this)
                .load(imageurl)
                .into(Image);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDel(imageid);

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void loadDel(String imageid) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://54.254.229.24/deletecat.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {         // Adding parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("imageID", imageid);

                return params;
            }
        };
        queue.add(postRequest);

        Toast toast = Toast.makeText(getApplicationContext(), "Succesfully deleting data", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(delete.this, SecondActivity.class);
        finish();
        startActivity(intent);


    }


}
