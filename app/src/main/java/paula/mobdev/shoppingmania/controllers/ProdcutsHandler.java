package paula.mobdev.shoppingmania.controllers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import paula.mobdev.shoppingmania.model.CSVReader;
import paula.mobdev.shoppingmania.model.Item;

public class ProdcutsHandler {

    private Context context;
    private List<String[]> rows = new ArrayList<>();
    private HashMap<String,Integer> map = new HashMap<String,Integer>();

    public ProdcutsHandler(Context context) {
        this.context = context;
    }

    public void readProducts() {
        CSVReader csvReader = new CSVReader(context, "products_sheet.csv");
        try {
            rows = csvReader.readCSV();
            map = csvReader.getMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("search","rows size"+rows.size());
        Log.d("search","map size"+map.size());
    }
    public ArrayList<String> getSuggestions (){
        ArrayList<String> suggestions = new ArrayList<String>();
        for (String [] row : rows) {
            suggestions.add(row[0]);
        }
        Log.d("search","suggestions size"+suggestions.size());
        return  suggestions;
    }
    public Item isItemFound(String query){
        Log.d("search","query : "+query);
        Item item = null ;
        if(map.containsKey(query)){
            String[] itemData = rows.get(map.get(query));
            String itemName = itemData[0];
            String itemCategory = itemData[1];
            Double itemPrice = Double.parseDouble(itemData[2]);
            item = new Item(null,itemName,itemCategory,itemPrice,false);
        }
        return item;
    }
}
