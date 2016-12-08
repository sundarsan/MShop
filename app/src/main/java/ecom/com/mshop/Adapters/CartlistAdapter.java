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
import ecom.com.mshop.UI.CartItems;
import ecom.com.mshop.UI.ItemDetails;

/**
 * Created by Pandey on 21-11-2016.
 */
public class CartlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> cartItems;
    Context context;
    public CartlistAdapter(Context context, List<Object> cart) {
        this.context=context;
        this.cartItems=cart;
    }




    @Override
    public int getItemViewType(int position) {
        if(cartItems.get(position) instanceof Items.ProductsData ){
            return 1;
        }else {
            return 2;
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 1:
                View viewONE = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_list, parent, false);
                viewHolder= new CartListViewHolder(viewONE,context);
                break;

            case 2:
                View viewTWO = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_price, parent, false);
                viewHolder= new PriceListViewHolder(viewTWO,context);
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
        }
    }

    private void configureViewHolderPrice(PriceListViewHolder vh2, int position) {
        CartItems.CartCheckOut checkOut = (CartItems.CartCheckOut)cartItems.get(position);
        vh2.getQuantity().setText(String.valueOf(checkOut.getCount()));
        vh2.getPrice().setText(String.valueOf(checkOut.getTotalBill()));
    }

    private void configureViewHoldercart(CartListViewHolder vh1, int position) {
        Items.ProductsData productsData= (Items.ProductsData) cartItems.get(position);
        String url = productsData.getItemIamgeURL();
        float price = productsData.getItemPrice();
        String prodprice= "$"+String.valueOf(price);
        Glide.with(context)
                .load(url)
                .into(vh1.getImageView());
        vh1.getQuantity().setText(String.valueOf(productsData.getItemQuantity()));
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
