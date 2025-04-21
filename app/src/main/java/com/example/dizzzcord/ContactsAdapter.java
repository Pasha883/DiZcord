package com.example.dizzzcord;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> contactList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nicknameTextView;

        public ViewHolder(View view) {
            super(view);
            nicknameTextView = view.findViewById(R.id.contactNickname);
        }
    }

    public ContactsAdapter(List<Contact> contacts) {
        this.contactList = contacts;
    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nicknameTextView.setText(contactList.get(position).nickname);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}

