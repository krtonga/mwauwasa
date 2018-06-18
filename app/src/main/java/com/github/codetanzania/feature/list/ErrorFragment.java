package com.github.codetanzania.feature.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import tz.co.codetanzania.R;

public class ErrorFragment extends Fragment {

    public static final String ERROR_MSG = "ERROR_MSG";
    public static final String ERROR_ICN = "ERROR_ICN";

    private TextView tvErrorIcn;
    private Button   btnReload;

    private OnReloadClickListener mClickListener;

    public static ErrorFragment getInstance(@NonNull String msg, int icn) {
        Bundle args = new Bundle();
        args.putString(ErrorFragment.ERROR_MSG, msg);
        args.putInt(ErrorFragment.ERROR_ICN, icn);

        ErrorFragment fragment = new ErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(
            LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_err, viewGroup, false);
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        // bind data to the components
        Bundle bundle = getArguments();
        String errMsg;
        if (bundle != null) {
            errMsg = bundle.getString(ERROR_MSG, getString(R.string.msg_server_error));
        } else {
            errMsg = getString(R.string.msg_server_error);
        }
        // Integer errIcn = bundle.getInt(ERROR_ICN);

        tvErrorIcn = (TextView) view.findViewById(R.id.tv_ErrorMsg);
        btnReload  = (Button) view.findViewById(R.id.btn_Reload);

        tvErrorIcn.setText(errMsg);
        tvErrorIcn.setAllCaps(true);

        // when the refresh button is hit
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onReloadClicked();
            }
        });
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        // context must implement the interface
        this.mClickListener = (OnReloadClickListener) context;
    }

    /* Bridge communication between this fragment and the attached activity */
    public interface OnReloadClickListener {

        /* callback to execute when the click action is performed */
        void onReloadClicked();
    }
}
