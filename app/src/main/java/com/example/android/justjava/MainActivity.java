package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submitOrder(View view) {
        EditText customerName = (EditText) findViewById(R.id.name_edit_text);
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);

        String customer = customerName.getText().toString();
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, customer);

        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
        sendEmail.setData(Uri.parse("mailto:"));
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject, customer));
        sendEmail.putExtra(Intent.EXTRA_TEXT, priceMessage);
//        sendEmail.putExtra(Intent.EXTRA_EMAIL, "musti2304@gmail.com");
        if(sendEmail.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmail);
        }
    }


    public String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String customer) {
        String priceMessage = getString(R.string.order_summary_name, customer);
        priceMessage += getString(R.string.order_summary_cream, hasWhippedCream);
        priceMessage += getString(R.string.order_summary_chocolate, hasChocolate);
        priceMessage += getString(R.string.order_summary_quantity, quantity);
        priceMessage += getString(R.string.order_summary_total, NumberFormat.getCurrencyInstance().format(price));
        priceMessage += getString(R.string.thank_you);
        return priceMessage;
    }


    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;
        if(hasWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (hasChocolate) {
            basePrice = basePrice + 2;
        }
        return quantity * basePrice;
    }

    public void increment(View view) {
        if(quantity < 100) {
            quantity = quantity + 1;
            displayQuantity(quantity);
        } else {
            Toast.makeText(this, "You can't have more!", Toast.LENGTH_SHORT).show();
        }
    }

    public void decrement(View view) {
        if(quantity > 1) {
            quantity = quantity - 1;
            displayQuantity(quantity);
        } else {
            Toast.makeText(this, "You can't have less!", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.order_summary_text_view);
        quantityTextView.setText("" + number);
    }

}
