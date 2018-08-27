package paula.mobdev.shoppingmania.activities;


import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;
import paula.mobdev.shoppingmania.R;
import paula.mobdev.shoppingmania.controllers.ItemListSorter;
import paula.mobdev.shoppingmania.controllers.ItemsFactory;
import paula.mobdev.shoppingmania.controllers.ListItemAdapter;
import paula.mobdev.shoppingmania.controllers.ProdcutsHandler;
import paula.mobdev.shoppingmania.controllers.RecyclerItemTouchHelper;
import paula.mobdev.shoppingmania.dbhandlers.DBActionsInvoker;
import paula.mobdev.shoppingmania.model.CSVReader;
import paula.mobdev.shoppingmania.model.Item;

public class ListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private RecyclerView recyclerView;
    private List<Item> itemsList;
    private ListItemAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    private DBActionsInvoker dbActionsInvoker ;
    private boolean deleteItem;
    private MaterialSearchView searchView ;
    private ProdcutsHandler productsHandler;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // setup activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        emptyTextView = (TextView)findViewById(R.id.empty_text);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Nunito-ExtraBold.ttf");
        emptyTextView.setTypeface(custom_font);

        // init db invoker
        dbActionsInvoker = DBActionsInvoker.getInstance(this);

        //load items
        itemsList = dbActionsInvoker.loadItems();


        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ListItemAdapter(this, itemsList);
        refreshList();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        //init products handler
        productsHandler = new ProdcutsHandler(this);
        productsHandler.readProducts();

        // init search tool
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.addSuggestions(productsHandler.getSuggestions());

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                Log.d("search","suggestion : "+suggestion);
                Item item = productsHandler.isItemFound(suggestion);
                if(item!=null) {
                    searchView.setQuery(suggestion, true);
                    itemsList.add(0,item);
                    refreshList();

                }else{
                    Toast.makeText(getApplicationContext(), "Item not found!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

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
                searchView.openSearch();
                emptyTextView.setVisibility(View.INVISIBLE);
            }
        });

    }

    /**
     * method make volley network call and parses json
     */
    private void refreshList() {
        if (itemsList.size()==0)
            emptyTextView.setVisibility(View.VISIBLE);
        else
            emptyTextView.setVisibility(View.INVISIBLE);
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

    @Override
    public void onBackPressed() {
        if (searchView.isOpen()) {
            // Close the search on the back button press.
            searchView.closeSearch();
            refreshList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cost:
                if(itemsList.size()!=0)
                    showCost();
                return true;
            case R.id.action_sort:
                sortList();
                return true;
            case R.id.action_reset:
                if(itemsList.size()!=0)
                    resetView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showCost() {
        FragmentManager fm = getFragmentManager();
        MyDialogFragment dialogFragment = new MyDialogFragment ();
        Bundle args = new Bundle();
        args.putDouble("prices",getTotalPrices());
        dialogFragment.setArguments(args);
        dialogFragment.show(fm, "Sample Fragment");
    }

    private double getTotalPrices() {
        double prices = 0;
        for(Item item :itemsList){
            prices+=item.getPrice();
        }
        return prices;
    }

    private void resetView() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbActionsInvoker.deleteAll();
                        itemsList.clear();
                        refreshList();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.alert_reset_message))
                .setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener)
                .show();
    }

    private void sortList() {
        itemsList = ItemListSorter.categorySort(itemsList);
        refreshList();
    }
}
