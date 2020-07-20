package com.example.estatehunt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    private Context mContext;
    private List<Estate> mUploads;
    StorageReference storageReference;

    public MyAdapter(Context context, List<Estate> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemType, itemLocation, itemAvailable, itemPrice;
        public ImageView itemImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemType = (TextView) itemView.findViewById(R.id.itemType);
            itemLocation = (TextView) itemView.findViewById(R.id.itemLocation);
            itemAvailable = (TextView) itemView.findViewById(R.id.itemAvailable);
            itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final Estate estateCurrent = mUploads.get(position);
        holder.itemType.setText(estateCurrent.getType());
        holder.itemLocation.setText(estateCurrent.getLocation());
        holder.itemAvailable.setText(estateCurrent.getAvailable());
        holder.itemPrice.setText(Integer.toString(estateCurrent.getPrice()));

        Picasso.get().load(estateCurrent.getImageId()).fit().centerInside().into(holder.itemImg);

        holder.setItemClickListner(new ItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                String gType = mUploads.get(position).getType();
                String gLocation = mUploads.get(position).getLocation();
                String gAvailable = mUploads.get(position).getAvailable();
                String gPrice = Integer.toString(mUploads.get(position).getPrice());
                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.itemImg.getDrawable();

                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
                byte[] bytes = stream.toByteArray();

                Intent intent = new Intent(mContext, Display.class);
                intent.putExtra("iPosition", estateCurrent.getImageId());
                intent.putExtra("iType", gType);
                intent.putExtra("iLocation", gLocation);
                intent.putExtra("iAvailable", gAvailable);
                intent.putExtra("iPrice", gPrice);
                intent.putExtra("iImage", bytes);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
}
