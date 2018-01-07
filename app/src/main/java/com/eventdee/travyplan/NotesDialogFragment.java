package com.eventdee.travyplan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dialog Fragment containing rating form.
 */
public class NotesDialogFragment extends DialogFragment {

    public static final String TAG = NotesDialogFragment.class.getSimpleName();
    private NotesListener mNotesListener;

    @BindView(R.id.notes_form_text)
    EditText mNotesText;

    interface NotesListener {
        void onClick(String notes);
    }

    public NotesDialogFragment(){
    }

    public static NotesDialogFragment newInstance(String notes) {
        NotesDialogFragment frag = new NotesDialogFragment();
        Bundle args = new Bundle();
        args.putString("notes", notes);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_notes, container, false);
        ButterKnife.bind(this, v);

        // to show keyboard when fragment is launched
//        mNotesText.requestFocus();
//        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotesText.setText(getArguments().getString("notes"));

        // to show keyboard at launch and hide with dismiss but not working
        mNotesText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NotesListener) {
            mNotesListener = (NotesListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @OnClick(R.id.notes_form_button)
    public void onSubmitClicked(View view) {
        String notes = mNotesText.getText().toString();

        if (mNotesListener != null) {
            mNotesListener.onClick(notes);
        }
        dismiss();
    }

    @OnClick(R.id.notes_form_cancel)
    public void onCancelClicked(View view) {
        dismiss();
    }
}
