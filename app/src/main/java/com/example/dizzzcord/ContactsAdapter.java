package com.example.dizzzcord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> contactList;
    private OnContactClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nicknameTextView;
        public Button callButton;

        public ViewHolder(View view) {
            super(view);
            nicknameTextView = view.findViewById(R.id.contactNickname);
            callButton = view.findViewById(R.id.callButton);
        }
    }

    public ContactsAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.contactList = contacts;
        this.listener = listener;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = contactList.get(position);
        holder.nicknameTextView.setText(contact.nickname);
        holder.callButton.setOnClickListener(v -> listener.onCallButtonClick(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public interface OnContactClickListener {
        void onCallButtonClick(Contact contact);
    }
}
