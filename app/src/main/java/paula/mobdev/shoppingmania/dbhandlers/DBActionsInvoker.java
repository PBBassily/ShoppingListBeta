package paula.mobdev.shoppingmania.dbhandlers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import paula.mobdev.shoppingmania.model.Item;

public class DBActionsInvoker implements Runnable{

    private static DBActionsInvoker instance = null ;
    private static SQLiteDBHelper db = null;
    private List<Item> items;

    /**
     * Singleton constructor
     * @param context
     * @return
     */
    public static DBActionsInvoker getInstance (Context context){

        if(instance == null){
            db = new SQLiteDBHelper(context);
            return new DBActionsInvoker();
        }
        return instance;
    }


    public List<Item> loadItems(){

        List<Item> res= db.getAllItems();
        if(res==null)
            return new ArrayList<Item>();
        for (Item item : res)
            Log.d("db",item.getStatus()+"");
        return res;
    }

    public void saveItem(Item item){

        if(item.getId() != null){
            Log.d("db" , "item "+item.getName()+" will be updated");
            db.updateItem(item);
        }else{
            Log.d("db" , "item "+item.getName()+" will be added to DB");
            db.addItem(item);
        }

    }

    public void  deleteItem(Integer id){
        if(id == null)
            return ;
        Log.d("item info" , "item "+ id +" will be deleted from db");
        db.deleteItem(id);
    }
    public void setItems(List<Item> items){
        this.items=items;
    }

    @Override
    public void run() {
        for (Item item:items)
            saveItem(item);
    }
}
