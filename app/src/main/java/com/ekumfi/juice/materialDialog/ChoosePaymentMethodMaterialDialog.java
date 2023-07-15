package com.ekumfi.juice.materialDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.ekumfi.juice.R;

import java.util.Timer;
import java.util.TimerTask;

public class ChoosePaymentMethodMaterialDialog extends DialogFragment {
    public static ProgressDialog dialog1;
    private static final String MY_LOGIN_ID = "MY_LOGIN_ID";
    CardView mtn;

    String amount, cart_id, stock_cart_id;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getStock_cart_id() {
        return stock_cart_id;
    }

    public void setStock_cart_id(String stock_cart_id) {
        this.stock_cart_id = stock_cart_id;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_choose_payment_method,null);

        mtn = view.findViewById(R.id.mtn);

        mtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Payment Instructions")
                        .setMessage(getString(R.string.dial_170))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Pay", (dialog, which) -> {
                            MomonumberMaterialDialog momonumberMaterialDialog = new MomonumberMaterialDialog();
                            if (momonumberMaterialDialog != null && momonumberMaterialDialog.isAdded()) {

                            } else {
                                momonumberMaterialDialog.setType("");
                                momonumberMaterialDialog.setAmount(amount);
                                if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER")) {
                                    momonumberMaterialDialog.setCart_id(cart_id);
                                } else {
                                    momonumberMaterialDialog.setStock_cart_id(stock_cart_id);
                                }
                                momonumberMaterialDialog.show(getFragmentManager(), "");
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
//                                        .setNegativeButton(android.R.string.no, null)
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        // doneBtn.setOnClickListener(doneAction);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                });
            }
        }, 5);
        return builder.create();

    }


}