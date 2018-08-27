package paula.mobdev.shoppingmania.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import paula.mobdev.shoppingmania.R;
import paula.mobdev.shoppingmania.model.Item;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {
    private Context context;
    private List<Item> cartList;

    /**
     * inner class to manipulate list_item layout
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, category, price;
        public CheckBox checkBox;
        public RelativeLayout viewBackground, viewForeground;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_name);
            category = view.findViewById(R.id.item_category);
            price = view.findViewById(R.id.item_price);
            checkBox = view.findViewById(R.id.item_checkbox);
            viewBackground = view.findViewById(R.id.item_view_background);
            viewForeground = view.findViewById(R.id.item_view_foreground);
        }
    }


    public ListItemAdapter(Context context, List<Item> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // retrive java object
        final Item item = cartList.get(position);

        // set attributes to the view
        holder.name.setText(item.getName());
        holder.category.setText(item.getCategory());
        holder.price.setText(context.getResources().getString(R.string.currency_symbol) + item.getPrice());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(item.getStatus());

        // set the item view according to item status
        refreshHolder(holder,item.getStatus());

        // add action listener to the items's checkbox
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                item.setStatus(isChecked);

                // update view UI of the item
                refreshHolder(holder,isChecked);

                // update the list order
                refreshList(isChecked,position,item);

                // update the overall list view
                notifyDataSetChanged();
                }
            }
        );
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Item item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void addLastItem(Item item) {

        cartList.add(item);

    }

    public void addFirstItem(Item item) {

        cartList.add(0,item);

    }

    private void refreshList(boolean isChecked, int position, Item item) {

        if(isChecked){

            removeItem(position);
            addLastItem(item);

        } else{

            removeItem(position);
            addFirstItem(item);

        }
    }


    private void refreshHolder(MyViewHolder holder, boolean isChecked){

        if(isChecked) {

            // change text color
            holder.name.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));

            // draw strike on the item name
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            // make the item looks transparent
            holder.name.setAlpha((float) .5);
            holder.price.setAlpha((float) .5);
            holder.category.setAlpha((float) .5);
            holder.checkBox.setAlpha((float) .5);


        }else{

            // restore item name text color
            holder.name.setTextColor(context.getResources().getColor(R.color.colorTextPrimary));

            //remove the strike fro the text
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            // restore item opacity
            holder.name.setAlpha((float) 1);
            holder.price.setAlpha((float) 1);
            holder.category.setAlpha((float) 1);
            holder.checkBox.setAlpha((float) 1);

        }
    }
}
