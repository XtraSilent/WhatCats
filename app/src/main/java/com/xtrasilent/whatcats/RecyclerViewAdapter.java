package com.xtrasilent.whatcats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    Context context;
    ImageButton deletebutton;
    List<DataAdapter> dataAdapters;
    List<DataAdapter> mArrayList;

    ImageLoader imageLoader;


    public RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.mArrayList = getDataAdapter;
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


        Viewholder.deletebtn.setOnClickListener((View) -> {
            Intent i = new Intent(context.getApplicationContext(), delete.class);
            i.putExtra("image_id", dataAdapterOBJ.getImageid());
            i.putExtra("image_url", dataAdapterOBJ.getImageURL());
            i.putExtra("image_title", dataAdapterOBJ.getImageTitle());
            context.startActivity(i);
        });

        Viewholder.relative.setOnClickListener((View) -> {
            String test = dataAdapterOBJ.getImageTitle();
            test = test.replaceAll("[\\d.%]", "");
            // Showing RecyclerView Clicked Item value using Toast.
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://google.com/search?q=" + test + "cat"));
            context.startActivity(intent);
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
        public LinearLayout relative;
        public ImageButton deletebtn;
        public ViewHolder(View itemView) {

            super(itemView);

            ImageTitleTextView = itemView.findViewById(R.id.ImageNameTextView);
            VollyImageView = itemView.findViewById(R.id.VolleyImageView);
            Username1 = itemView.findViewById(R.id.username);
            relative = itemView.findViewById(R.id.deleteHis);
            deletebtn = itemView.findViewById(R.id.deletebutton);


        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    dataAdapters = mArrayList;
                } else {

                    ArrayList<DataAdapter> filteredList = new ArrayList<>();

                    for (DataAdapter catname : mArrayList) {

                        if (catname.getImageTitle().toLowerCase().contains(charString)) {

                            filteredList.add(catname);
                        }
                    }

                    dataAdapters = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataAdapters;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataAdapters = (ArrayList<DataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

