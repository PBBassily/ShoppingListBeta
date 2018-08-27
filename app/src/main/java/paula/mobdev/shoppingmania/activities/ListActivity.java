package paula.mobdev.shoppingmania.activities;

import android.os.Bundle;
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
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import paula.mobdev.shoppingmania.R;
import paula.mobdev.shoppingmania.controllers.ItemsFactory;
import paula.mobdev.shoppingmania.controllers.ListItemAdapter;
import paula.mobdev.shoppingmania.model.Item;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Item> itemsList;
    private ListItemAdapter mAdapter;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        itemsList = new ArrayList<Item>();
        recyclerView = findViewById(R.id.recycler_view);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        mAdapter = new ListItemAdapter(this, itemsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);





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


}
