package com.example.estatehunt;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView itemType, itemLocation, itemAvailable, itemPrice;
    ImageView itemImg;
    ItemClickListner itemClickListner;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        itemType = (TextView) itemView.findViewById(R.id.itemType);
        itemLocation = (TextView) itemView.findViewById(R.id.itemLocation);
        itemAvailable = (TextView) itemView.findViewById(R.id.itemAvailable);
        itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
        itemImg = (ImageView) itemView.findViewById(R.id.itemImg);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListner.onItemClickListner(v, getLayoutPosition());
    }

    public void setItemClickListner(ItemClickListner ic) {
        this.itemClickListner = ic;
    }
}
