package com.xtrasilent.whatcats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.Uri;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    ImageButton deletebutton;
    List<DataAdapter> dataAdapters;

    ImageLoader imageLoader;


    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Viewholder, int position) {

        DataAdapter dataAdapterOBJ = dataAdapters.get(position);

        imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getImageUrl(),
                ImageLoader.getImageListener(
                        Viewholder.VollyImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )

        );

        Viewholder.VollyImageView.setImageUrl(dataAdapterOBJ.getImageUrl(), imageLoader);
        Viewholder.ImageTitleTextView.setText(dataAdapterOBJ.getImageTitle());
        Viewholder.Username1.setText("Scanned By :" + dataAdapterOBJ.getUsername());

        Viewholder.relative.setOnClickListener((View) -> {
            String test = dataAdapterOBJ.getImageTitle();
            test = test.replaceAll("[\\d.%]", "");
            // Showing RecyclerView Clicked Item value using Toast.
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://google.com/search?q=" + test + "cat"));
            context.startActivity(intent);
        });

        deletebutton.setOnClickListener((View) -> {
            Intent i = new Intent(context.getApplicationContext(), delete.class);
            i.putExtra("image_id", dataAdapterOBJ.getImageid());
            i.putExtra("image_url", dataAdapterOBJ.getImageURL());
            i.putExtra("image_title", dataAdapterOBJ.getImageTitle());
            context.startActivity(i);
        });



    }

    @Override
    public int getItemCount() {

        return dataAdapters.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ImageTitleTextView;
        public TextView Username1;
        public NetworkImageView VollyImageView;
        public RelativeLayout relative;
        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleTextView = itemView.findViewById(R.id.ImageNameTextView);
            VollyImageView = itemView.findViewById(R.id.VolleyImageView);
            Username1 = itemView.findViewById(R.id.username);
            deletebutton = itemView.findViewById(R.id.deletebutton);
            relative = itemView.findViewById(R.id.deleteHis);


        }
    }
}