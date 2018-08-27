package paula.mobdev.shoppingmania.model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVReader {

    private Context context;
    private String fileName;
    private List<String[]> rows = new ArrayList<>();
    private HashMap<String,Integer> map = new HashMap<String,Integer>();

    public CSVReader(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public List<String[]> readCSV() throws IOException {
        InputStream is = context.getAssets().open(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String csvSplitBy = ",";

        br.readLine();

        while ((line = br.readLine()) != null) {
            String[] row = line.split(csvSplitBy);
            rows.add(row);
            map.put(row[0],rows.size()-1);
        }
        return rows;
    }

    public HashMap<String,Integer> getMapping() {
        return map ;
    }

}
