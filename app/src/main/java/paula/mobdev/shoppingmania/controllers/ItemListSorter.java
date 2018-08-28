package paula.mobdev.shoppingmania.controllers;



import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import paula.mobdev.shoppingmania.model.Item;

public class ItemListSorter {
    /**
     * sorts the list according to checked or not checked only
     * @param itemList
     * @return
     */
    public static List<Item> normalSort(List<Item> itemList){
        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getStatus()==o2.getStatus())
                  return   0;
                return o1.getStatus()?1:-1;
            }
        });
        return itemList;
    }

    /**
     * sorts the list according to status then category then name
     * @param itemList
     * @return
     */
    public static List<Item> categorySort(List<Item> itemList) {
        Collections.sort(itemList, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getStatus()==o2.getStatus()) {
                    return o1.getCategory().equals(o2.getCategory()) ?
                            o1.getName().compareTo(o2.getName()) :
                            o1.getCategory().compareTo(o2.getCategory());
                }
                return o1.getStatus()?1:-1;
            }
        });
        return itemList;
    }
}
