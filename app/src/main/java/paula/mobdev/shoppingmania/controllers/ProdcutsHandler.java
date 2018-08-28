package paula.mobdev.shoppingmania.controllers;

import android.content.Context;

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
        CSVReader csvReader = new CSVReader(context, "products/products.csv");
        try {
            rows = csvReader.readCSV();
            map = csvReader.getMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getSuggestions (){
        ArrayList<String> suggestions = new ArrayList<String>();
        for (String [] row : rows) {
            suggestions.add(row[0]);
        }
        return  suggestions;
    }
    public Item isItemFound(String query){
        query = query.toLowerCase().trim();
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
