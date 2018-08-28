package paula.mobdev.shoppingmania.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import paula.mobdev.shoppingmania.R;

public class ExpectedCostFragment extends DialogFragment {

    private static final double MAXIMUM_TOTAL_PRICE = 10000.0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // setup the main view
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        View rootView = inflater.inflate(R.layout.custom_cost_dialogue, container, false);
        double prices = getArguments().getDouble("prices");

        // access to the 3 textViews
        TextView subTextView = (TextView)rootView.findViewById(R.id.cost_subtotal);
        TextView vatTextView = (TextView)rootView.findViewById(R.id.cost_vat);
        TextView totalTextView = (TextView)rootView.findViewById(R.id.cost_total);

        // upper limit on the prices
        // to avoid view deformation
        String suffix = "";
        if(prices > MAXIMUM_TOTAL_PRICE){
            prices = MAXIMUM_TOTAL_PRICE;
            suffix = "+";
        }
        subTextView.setText(getResources().getString(R.string.currency_symbol) + prices+suffix);

        vatTextView.setText(getResources().getString(R.string.currency_symbol) +
                decimalFormat.format(prices*.14)+suffix);

        totalTextView.setText(getResources().getString(R.string.currency_symbol) +
                decimalFormat.format(prices*.14+prices)+suffix);

        return rootView;
    }
}
