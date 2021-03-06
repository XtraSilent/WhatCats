package com.xtrasilent.whatcats;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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

    private Context context;
    private List<DataAdapter> dataAdapters;
    private List<DataAdapter> mArrayList;


    RecyclerViewAdapter(List<DataAdapter> getDataAdapter, Context context) {

        super();
        this.dataAdapters = getDataAdapter;
        this.mArrayList = getDataAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder Viewholder, int position) {

        DataAdapter dataAdapterOBJ = dataAdapters.get(position);

        ImageLoader imageLoader = ImageAdapter.getInstance(context).getImageLoader();

        imageLoader.get(dataAdapterOBJ.getImageUrl(),
                ImageLoader.getImageListener(
                        Viewholder.VollyImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )

        );

        Viewholder.VollyImageView.setImageUrl(dataAdapterOBJ.getImageUrl(), imageLoader);
        Viewholder.ImageTitleTextView.setText(dataAdapterOBJ.getImageTitle());
        Viewholder.Username1.setText("Scanned By :" + dataAdapterOBJ.getFullname());


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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ImageTitleTextView;
        private TextView Username1;
        private NetworkImageView VollyImageView;
        private LinearLayout relative;
        private ImageButton deletebtn;

        private ViewHolder(View itemView) {

            super(itemView);

            ImageTitleTextView = itemView.findViewById(R.id.ImageNameTextView);
            VollyImageView = itemView.findViewById(R.id.VolleyImageView);
            Username1 = itemView.findViewById(R.id.username);
            relative = itemView.findViewById(R.id.deleteHis);
            deletebtn = itemView.findViewById(R.id.deletebutton);


        }
    }
}

