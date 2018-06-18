package com.github.codetanzania.feature.company.logincustomer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.codetanzania.feature.settings.EditProfileFragment;
import com.github.codetanzania.open311.android.library.models.customer.CustomerAccount;

import tz.co.codetanzania.R;

/**
 * This is for registering for app account.
 */

public class CompanyRegisterFragment extends Fragment {

    private static final String TAG = "CompanyRegisterFragment";

    private static final String USER_INPUT_FRAGMENT_TAG = "userInputFragment";
    private static RegistrationCallbacks mCallbacks;

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        // cast context
        try {
            mCallbacks = ((RegistrationCallbacks) getActivity());
        } catch (ClassCastException cce) {
            throw new IllegalStateException(String.format("%s must implement %s",
                    getActivity().getClass().getName(), ctx.getClass().getName()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setOptionFlags(true, true, true);

        View view = inflater.inflate(R.layout.frag_company_registration, container, false);

        getChildFragmentManager().beginTransaction().add(
                R.id.id_frag_container, fragment, USER_INPUT_FRAGMENT_TAG).commit();

        view.findViewById(R.id.btn_sendMePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerDone();
            }
        });

        return view;
    }

    private boolean triggerDone() {
        return ((EditProfileFragment) getChildFragmentManager()
                .findFragmentByTag(USER_INPUT_FRAGMENT_TAG)).verifyAndComplete();
    }

    public interface RegistrationCallbacks extends EditProfileFragment.OnUserProfileChangeListener {
        void onRegistrationPosted(CustomerAccount account);
        void onRegistrationFailed(Throwable t);
    }
}
