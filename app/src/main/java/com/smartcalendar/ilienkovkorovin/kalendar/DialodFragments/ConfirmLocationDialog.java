package com.smartcalendar.ilienkovkorovin.kalendar.DialodFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smartcalendar.ilienkovkorovin.kalendar.R;
import com.smartcalendar.ilienkovkorovin.kalendar.StartActivity;

public class ConfirmLocationDialog extends DialogFragment {
    TextView cityNameTextView;
    String cityName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        cityName = getArguments().getString(StartActivity.CITY_NAME_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.confirm_city_dialog, null);
        builder.setView(v);
        cityNameTextView = v.findViewById(R.id.dialog_city_name);
        cityNameTextView.setText(cityName);
        builder.setMessage(R.string.confirm_your_city)
                .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// сэттим город в поле
                    }
                }).setNegativeButton(R.string.no_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
