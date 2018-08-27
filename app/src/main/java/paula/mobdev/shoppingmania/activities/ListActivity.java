package paula.mobdev.shoppingmania.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import paula.mobdev.shoppingmania.R;
import paula.mobdev.shoppingmania.controllers.ItemsFactory;
import paula.mobdev.shoppingmania.controllers.ListItemAdapter;
import paula.mobdev.shoppingmania.controllers.RecyclerItemTouchHelper;
import paula.mobdev.shoppingmania.dbhandlers.DBActionsInvoker;
import paula.mobdev.shoppingmania.model.Item;

public class ListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private RecyclerView recyclerView;
    private List<Item> itemsList;
    private ListItemAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private DBActionsInvoker dbActionsInvoker ;
    private boolean deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setup activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.coordinator_layout);


        // init db invoker
        dbActionsInvoker = DBActionsInvoker.getInstance(this);

        //load items
        itemsList = dbActionsInvoker.loadItems();


        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ListItemAdapter(this, itemsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = ItemsFactory.createItem();
                itemsList.add(0,item);
                refreshList();
            }
        });
    }

    /**
     * method make volley network call and parses json
     */
    private void refreshList() {
        mAdapter.notifyDataSetChanged();

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof ListItemAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar

            String name = itemsList.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Item deletedItem = itemsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            deleteItem = true ;

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            refreshList();

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // restore item if user pressed UNDO
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    refreshList();
                    deleteItem = false;

                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
            snackbar.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(deleteItem){
                        Log.d("db","will delete");
                        dbActionsInvoker.deleteItem(deletedItem.getId());
                    }else{
                        Log.d("db","will not delete");
                    }
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cartList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    protected void onPause() {
        Log.d("db","pause");
        super.onPause();
        dbActionsInvoker.setItems(itemsList);
        dbActionsInvoker.run();
    }
}
