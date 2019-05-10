package com.example.contactslistdemo.presenter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contactslistdemo.R;
import com.google.android.material.snackbar.Snackbar;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImage;
    private TextView mLabel;
    private Contact mBoundContact;

    public ContactViewHolder(final View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.rounded_iv_profile);
        mLabel = itemView.findViewById(R.id.tv_label);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBoundContact != null) {
                    Snackbar.make(v, "Hi, I'm " + mBoundContact.name, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    public void bind(Contact contact) {
        mBoundContact = contact;
        mLabel.setText(contact.name);
    }
}
