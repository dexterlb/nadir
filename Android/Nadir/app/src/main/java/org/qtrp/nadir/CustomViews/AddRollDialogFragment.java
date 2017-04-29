package org.qtrp.nadir.CustomViews;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.qtrp.nadir.Database.FilmRollDbHelper;
import org.qtrp.nadir.Database.Roll;
import org.qtrp.nadir.R;

import java.sql.Time;

public class AddRollDialogFragment  extends DialogFragment {
    private FilmRollDbHelper filmRollDbHelper;
    private EditText teName;
    private EditText et_name;
    private CheckBox cb_colour;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_add_rolls, null);
        builder.setView(view);

        filmRollDbHelper = new FilmRollDbHelper(this.getActivity());
        et_name = (EditText) view.findViewById(R.id.dialog_fragment_et_name);
        cb_colour = (CheckBox) view.findViewById(R.id.dialog_fragment_rb_colour);
        Log.i("name: ", "gs");

        builder.setMessage(R.string.add_roll_message)
                .setPositiveButton(R.string.add_roll, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = et_name.getText().toString();
                        String colour = "n";
                        if (cb_colour.isChecked()) {
                            colour = "y";
                        }

                        Long ctime = System.currentTimeMillis() / 1000;

                        Roll roll = new Roll(null, name, colour, ctime);
                        filmRollDbHelper.insertRoll(roll);

                        mListener.onDialogPositiveClick(AddRollDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddRollDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}