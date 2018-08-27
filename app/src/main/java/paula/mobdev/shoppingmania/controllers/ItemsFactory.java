package paula.mobdev.shoppingmania.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import paula.mobdev.shoppingmania.model.Item;

public class ItemsFactory {
    public static List<Item> getPlaceHolders() {
        Random random= new Random();
        List<Item> list = new ArrayList<Item>();
        for(int i=0;i<12;i++){
            Item item= new Item(null,"Item "+i,"cheese",34.2*i,i>6?true:false);
            list.add(item);

        }
        return list;
    }

    public static Item createItem() {
        Random r = new Random();
        Item item= new Item(null,"Item "+r.nextInt(),"cheese",r.nextDouble(),false);
        return item ;
    }
}
