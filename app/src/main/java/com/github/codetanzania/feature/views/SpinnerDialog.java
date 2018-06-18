package com.github.codetanzania.feature.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tz.co.codetanzania.R;

public class SpinnerDialog {
    private ArrayList<String> items;
    private Activity context;
    private String dialogTitle;
    private OnSpinnerItemClick onSpinnerItemClick;
    private AlertDialog alertDialog;
    private int selectedPosition;
    private int style;

    public SpinnerDialog(Activity activity,ArrayList<String> items,String dialogTitle) {
        this.items = items;
        this.context = activity;
        this.dialogTitle = dialogTitle;
    }

    public SpinnerDialog(Activity activity,ArrayList<String> items,String dialogTitle, int style) {
        this(activity, items, dialogTitle);
        this.style = style;
    }

    private ArrayAdapter<String> getArrayAdapter(final List<String> values) {
        return new ArrayAdapter<String>(context, R.layout.view_items, values) {

            int selectedPositionInner = 0;

            @Override public View getView(int position, View convertView, ViewGroup parent) {

                // HINT: Uncommenting the next check optimizes the app, since inflating layout
                // is an expensive operation. But also the check introduces NullPointerException.
                //
                // TODO: Uncomment when the fix is found
                // View view = convertView;
                // if (view == null) {
                //    view = context.getLayoutInflater().inflate(R.layout.view_items, null);
                // }

                // TODO: Get rid of this line when the above fix is done
                View view = context.getLayoutInflater().inflate(R.layout.view_items, null);

                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setChecked(position == selectedPositionInner);
                checkBox.setTag(position);
                checkBox.setText(values.get(position));
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedPositionInner = (int)v.getTag();
                        selectedPosition = selectedPositionInner;
                        notifyDataSetChanged();
                    }
                });

                return view;
            }
        };
    }

    public void bindOnSpinnerListener(OnSpinnerItemClick onSpinnerItemClick1) {
        this.onSpinnerItemClick = onSpinnerItemClick1;
    }

    public void showSpinnerDialog() {

        AlertDialog.Builder adb = new AlertDialog.Builder(context);

        final View view = context.getLayoutInflater().inflate(R.layout.layout_dialog, null);

        final ListView listView = (ListView) view.findViewById(R.id.list);
        /*final EditText searchBox = (EditText) view.findViewById(R.id.searchBox);*/
        final ArrayAdapter<String> adapter = getArrayAdapter(items);
        final Button btnSelect = (Button) view.findViewById(R.id.select);
        final Button btnCancel = (Button) view.findViewById(R.id.close);

        listView.setAdapter(adapter);
        adb.setView(view);

        alertDialog = adb.create();
        alertDialog.getWindow().getAttributes().windowAnimations = style;

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpinnerItemClick.onClick(items.get(selectedPosition), selectedPosition);
                alertDialog.dismiss();
            }
        });

        // TODO: This comment is here because ListAdapter#Filter does not work correctly when ListAdapter#getView() method is overridden.
        // Uncomment when the solution is found.
        /*searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(searchBox.getText().toString());
            }
        });*/

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

}