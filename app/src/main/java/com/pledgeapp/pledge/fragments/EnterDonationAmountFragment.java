package com.pledgeapp.pledge.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.pledgeapp.pledge.PledgeApplication;
import com.pledgeapp.pledge.R;
import com.pledgeapp.pledge.models.NonProfit;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Fragment for entering a donation amount
 */
public class EnterDonationAmountFragment extends Fragment {

    private static final String KEY_NON_PROFIT = "key_non_profit";
    private static final double MIN_DONATION_AMOUNT = 10.00; // In dollars
    private onDonationSuccessListener mListener;

    public interface onDonationSuccessListener {
        void onDonationSuccess();
    }

    public static EnterDonationAmountFragment newInstance(NonProfit nonProfit) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_NON_PROFIT, nonProfit);

        EnterDonationAmountFragment fragment = new EnterDonationAmountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Create view for this fragment!
        View v =  inflater.inflate(R.layout.fragment_enter_amount, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        final NonProfit nonProfit = getArguments().getParcelable(KEY_NON_PROFIT);
        if (nonProfit == null) {
            throw new RuntimeException("Must pass nonprofit to this fragment");
        }

        final EditText etDonationAmount = (EditText) v.findViewById(R.id.etDonationAmount);
        final Button btnSubmitDonation = (Button) v.findViewById(R.id.btnSubmitDonation);
        btnSubmitDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmitDonation.setEnabled(false);
                final Double donationAmount = getDollarValue(etDonationAmount.getText().toString());

                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Donating $" + String.format("%.2f", donationAmount) + "...");
                pd.setTitle("Donation");
                pd.setCancelable(false);
                pd.show();

                PledgeApplication.getPledgeModel().donate(donationAmount, nonProfit, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "You donated $" + String.format("%.2f", donationAmount) + "!", Toast.LENGTH_SHORT).show();
                        mListener.onDonationSuccess();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("NETWORK ERROR", errorResponse.toString());
                        Toast.makeText(getContext(), "This nonprofit isn't processing donations", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                });
            }
        });
        btnSubmitDonation.setEnabled(false);

        etDonationAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etDonationAmount.removeTextChangedListener(this);

                double dollarValue = getDollarValue(s.toString());
                String formatted = NumberFormat.getCurrencyInstance().format(dollarValue);

                etDonationAmount.setText(formatted);
                etDonationAmount.setSelection(formatted.length());

                etDonationAmount.addTextChangedListener(this);

                btnSubmitDonation.setEnabled(dollarValue >= MIN_DONATION_AMOUNT);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etDonationAmount.setText(NumberFormat.getCurrencyInstance().format(0));

        TextView tvNonProfitName = (TextView) v.findViewById(R.id.tvNonProfitName);
        tvNonProfitName.setText(nonProfit.getName());

        return v;
    }

    public double getDollarValue(String formattedString) {
        String cleanString = formattedString.replaceAll("[$,.]", "");
        double dollarValue;
        if (!cleanString.isEmpty()) {
            dollarValue = Double.parseDouble(cleanString) / 100;
        } else {
            dollarValue = 0;
        }

        return dollarValue;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onDonationSuccessListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                                                 + " must implement OnPaymentMethodSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
