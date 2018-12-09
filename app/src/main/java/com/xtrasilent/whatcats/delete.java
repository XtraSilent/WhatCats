package com.xtrasilent.whatcats;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class delete extends AppCompatActivity {
    TextView Imagetitle;
    TextView Imageid;

    ProgressDialog pd;
    ImageView Image;


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


        Imagetitle.setText(imagetitle);
        Imageid.setText(imageid);

        ImageView imageView = findViewById(R.id.catimage);

        Glide.with(this)
                .load(imageurl)
                .into(Image);


    }


}
