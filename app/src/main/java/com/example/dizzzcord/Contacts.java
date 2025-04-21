package com.example.dizzzcord;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Contact> contacts = new ArrayList<>();
    private DatabaseReference contactsRef;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactsAdapter(contacts);
        recyclerView.setAdapter(adapter);

        //auth = FirebaseAuth.getInstance();
        String currentUserId = "TestUserID";
        contactsRef = FirebaseDatabase.getInstance("https://dizzzcord-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
                .child(currentUserId).child("contacts");

        loadContacts(currentUserId);

        return view;
    }

    private void loadContacts(String userId) {
        Log.d("Contacts", "Loading contacts...");
        DatabaseReference contactsRef = FirebaseDatabase.getInstance("https://dizzzcord-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users")
                .child(userId)
                .child("contacts");

        Log.d("FirebaseDebug", "Получаем ссылку на: users/" + userId + "/contacts");

        contactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseDebug", "Данные успешно получены. Начинаем обработку...");

                contacts.clear();
                Log.d("FirebaseDebug", "Список контактов очищен");

                for (DataSnapshot mapSnapshot : snapshot.getChildren()) {
                    Log.d("FirebaseDebug", "Обработка следующего Map из массива contacts");

                    for (DataSnapshot entry : mapSnapshot.getChildren()) {
                        String contactId = entry.getKey();
                        String nickname = entry.getValue(String.class);

                        Log.d("FirebaseDebug", "Найдена пара: " + contactId + " -> " + nickname);

                        if (contactId != null && nickname != null) {
                            contacts.add(new Contact(contactId, nickname));
                            Log.d("FirebaseDebug", "Контакт добавлен в список: " + contactId + " -> " + nickname);
                        } else {
                            Log.w("FirebaseDebug", "Некорректные данные: " + contactId + " -> " + nickname);
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                Log.d("FirebaseDebug", "Адаптер уведомлен об изменении данных");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "Ошибка при получении данных: " + error.getMessage());
                Toast.makeText(getContext(), "Ошибка загрузки контактов", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
