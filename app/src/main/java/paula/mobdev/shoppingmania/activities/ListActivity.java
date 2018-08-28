package paula.mobdev.shoppingmania.activities;


import android.app.FragmentManager;
import android.content.DialogInterface;
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

import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;
import paula.mobdev.shoppingmania.R;
import paula.mobdev.shoppingmania.controllers.ItemListSorter;
import paula.mobdev.shoppingmania.controllers.ListItemAdapter;
import paula.mobdev.shoppingmania.controllers.ProdcutsHandler;
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

        setEmptyListTextView();


        initDB();


        loadItems();


        initRecyclerView();



        initSearchTool();


        initFloatingActionButton();

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


    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.openSearch();
                emptyTextView.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void initSearchTool() {
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
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.alert_item_not_found),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new ListItemAdapter(this, itemsList);
        refreshList();
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
    }

    private void loadItems() {
        //load items
        itemsList = dbActionsInvoker.loadItems();
    }

    private void initDB() {
        // init db invoker
        dbActionsInvoker = DBActionsInvoker.getInstance(this);
    }

    private void setEmptyListTextView() {
        // init the "empty list" alert
        emptyTextView = (TextView)findViewById(R.id.empty_text);
        // set its font to Nunito
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Nunito-ExtraBold.ttf");
        emptyTextView.setTypeface(custom_font);
    }

    /**
     * refresh for the recycler view after date change
     */
    private void refreshList() {

        // show/hide "empty list" alert
        if (itemsList.size()==0)
            emptyTextView.setVisibility(View.VISIBLE);
        else
            emptyTextView.setVisibility(View.INVISIBLE);

        // notify listeners
        mAdapter.notifyDataSetChanged();

    }

    private void showCost() {
        FragmentManager fm = getFragmentManager();
        ExpectedCostFragment dialogFragment = new ExpectedCostFragment ();
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
