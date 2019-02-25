package com.vladislavmyasnikov.courseproject.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vladislavmyasnikov.courseproject.R;
import com.vladislavmyasnikov.courseproject.ui.callbacks.OnBackButtonListener;

public class ProfileEditingFragment extends Fragment implements OnBackButtonListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_editing, container, false);
        view.findViewById(R.id.save_button).setOnClickListener(mSaveButtonListener);
        view.findViewById(R.id.cancel_button).setOnClickListener(mCancelButtonListener);
        mNameField = view.findViewById(R.id.name_field);
        mSurnameField = view.findViewById(R.id.surname_field);
        mPatronymicField = view.findViewById(R.id.patronymic_field);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            mNameField.setText(args.getString(NAME_ARG));
            mSurnameField.setText(args.getString(SURNAME_ARG));
            mPatronymicField.setText(args.getString(PATRONYMIC_ARG));
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.profile_editing_toolbar_title);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        if (isFinished) {
            return false;
        }
        else if (areDataChanged()) {
            DialogFragment dialog = ActionConfirmationDialogFragment.newInstance();
            dialog.setTargetFragment(this, REQUEST_CODE);
            dialog.show(getFragmentManager(), "action_confirmation_tag");
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                isFinished = true;
                getActivity().onBackPressed();
            }
        }
    }

    private boolean areDataChanged() {
        Bundle args = getArguments();
        String initialName = args.getString(NAME_ARG);
        String initialSurname = args.getString(SURNAME_ARG);
        String initialPatronymic = args.getString(PATRONYMIC_ARG);

        return !initialName.equals(mNameField.getText().toString()) ||
                !initialSurname.equals(mSurnameField.getText().toString()) ||
                !initialPatronymic.equals(mPatronymicField.getText().toString());
    }

    private int areDataCorrect() {
        String name = mNameField.getText().toString();
        String surname = mSurnameField.getText().toString();
        String patronymic = mPatronymicField.getText().toString();

        if (name.equals("") || surname.equals("") || patronymic.equals("")) {
            return EMPTY_FIELD_ERROR;
        }
        else if (!isWord(name) || !isWord(surname) || !isWord(patronymic)) {
            return INCORRECT_FIELD_ERROR;
        }
        return 0;
    }

    private boolean isWord(String s) {
        return s.matches("[A-ZА-ЯЁ][a-zA-Zа-яА-ЯёЁ]*$");
    }

    public static ProfileEditingFragment newInstance(String name, String surname, String patronymic) {
        Bundle args = new Bundle();
        args.putString(NAME_ARG, name);
        args.putString(SURNAME_ARG, surname);
        args.putString(PATRONYMIC_ARG, patronymic);

        ProfileEditingFragment fragment = new ProfileEditingFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private View.OnClickListener mSaveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (areDataCorrect()) {
                case 0:
                    SharedPreferences preferences = getActivity().getSharedPreferences(ProfileFragment.PERSISTENT_STORAGE_NAME, Context.MODE_PRIVATE);
                    preferences.edit()
                            .putString(ProfileFragment.USER_NAME, mNameField.getText().toString())
                            .putString(ProfileFragment.USER_SURNAME, mSurnameField.getText().toString())
                            .putString(ProfileFragment.USER_PATRONYMIC, mPatronymicField.getText().toString())
                            .apply();
                    isFinished = true;
                    getActivity().onBackPressed();
                    break;
                case EMPTY_FIELD_ERROR:
                    Toast.makeText(getActivity(), R.string.empty_input_message, Toast.LENGTH_SHORT).show();
                    break;
                case INCORRECT_FIELD_ERROR:
                    Toast.makeText(getActivity(), R.string.incorrect_input_message, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private View.OnClickListener mCancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    private EditText mNameField;
    private EditText mSurnameField;
    private EditText mPatronymicField;
    private boolean isFinished;

    private static final String NAME_ARG = "name_arg";
    private static final String SURNAME_ARG = "surname_arg";
    private static final String PATRONYMIC_ARG = "patronymic_arg";

    private static final int REQUEST_CODE = 2;
    private static final int EMPTY_FIELD_ERROR = 1;
    private static final int INCORRECT_FIELD_ERROR = 2;
}