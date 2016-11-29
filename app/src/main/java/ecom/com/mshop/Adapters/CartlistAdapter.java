package ecom.com.mshop.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ecom.com.mshop.Database.Items;
import ecom.com.mshop.R;
import ecom.com.mshop.UI.ItemDetails;

/**
 * Created by Pandey on 21-11-2016.
 */
public class CartlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Items.ProductsData> cartItems;
    Context context;
    public CartlistAdapter(Context context, List<Items.ProductsData> cart) {
        this.context=context;
        this.cartItems=cart;
    }




    @Override
    public int getItemViewType(int position) {
        if(position == cartItems.size() - 1 && position !=0){
            return 2;
        }else if(position>=0 && position< cartItems.size()-1) {
            return 1;
        }else{
            return 3;
        }

    }

    private void setAnimation(FrameLayout container, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        container.startAnimation(animation);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:
                View viewONE = inflater.from(parent.getContext()).inflate(R.layout.cart_item_list, parent, false);
                viewHolder= new CartListViewHolder(viewONE,context);
                break;

            case 2:
                View viewTWO = inflater.from(parent.getContext()).inflate(R.layout.activity_price, parent, false);
                viewHolder= new PriceListViewHolder(viewTWO,context);
                break;
            case 3:
                View price = inflater.from(parent.getContext()).inflate(R.layout.activity_price, parent, false);
                viewHolder= new PriceListViewHolder(price,context);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 1:
                CartListViewHolder vh1 = (CartListViewHolder)holder;
                configureViewHoldercart(vh1,position);
                break;
            case 2:
                PriceListViewHolder vh2 = (PriceListViewHolder)holder;
                configureViewHolderPrice(vh2,position);
                break;
            case 3:
                PriceListViewHolder vh3 = (PriceListViewHolder)holder;
                configureViewHolderPrice(vh3,position);
                break;
        }
    }

    private void configureViewHolderPrice(PriceListViewHolder vh2, int position) {
        int size =cartItems.size();
        int cart_count=size;
        float sum =0;
        while(size>0){
            Items.ProductsData items = cartItems.get(position);
            float cost = items.getItemQuantity() * items.getItemPrice();
            sum = sum + cost;
            size--;
        }
        vh2.getQuantity().setText(String.valueOf(cart_count));
        vh2.getPrice().setText(String.valueOf(sum));

    }

    private void configureViewHoldercart(CartListViewHolder vh1, int position) {
        Items.ProductsData productsData= cartItems.get(position);
        String url = productsData.getItemIamgeURL();
        float price = productsData.getItemPrice();
        String prodprice= "$"+String.valueOf(price);
        Glide.with(context)
                .load(url)
                .into(vh1.getImageView());
        vh1.getQuantity().setText(String.valueOf(cartItems.get(position).getItemQuantity()));
        vh1.getProductprice().setText(prodprice);
        vh1.getProdName().setText(productsData.getItemName());
        vh1.getProdCompsnyname().setText(productsData.getItemDescription());

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public interface OnSharingOptionSelected {
        void onOptionSelected(int id, int position);
    }
}
