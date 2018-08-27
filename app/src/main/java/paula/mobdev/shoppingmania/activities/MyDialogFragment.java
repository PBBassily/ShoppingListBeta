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

public class MyDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DecimalFormat decimalFormat = new DecimalFormat(".##");
        View rootView = inflater.inflate(R.layout.custom_cost_dialogue, container, false);
        double prices = getArguments().getDouble("prices");

        TextView subTextView = (TextView)rootView.findViewById(R.id.cost_subtotal);
        TextView vatTextView = (TextView)rootView.findViewById(R.id.cost_vat);
        TextView totalTextView = (TextView)rootView.findViewById(R.id.cost_total);

        String plus = "";
        if(prices>10000.0){
            prices = 10000.0;
            plus="+";
        }
        subTextView.setText(getResources().getString(R.string.currency_symbol) + prices+plus);

        vatTextView.setText(getResources().getString(R.string.currency_symbol) +
                decimalFormat.format(prices*.14)+plus);

        totalTextView.setText(getResources().getString(R.string.currency_symbol) +
                decimalFormat.format(prices*.14+prices)+plus);

        return rootView;
    }
}
