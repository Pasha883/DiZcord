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

public class Contacts extends Fragment implements ContactsAdapter.OnContactClickListener {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Contact> contacts = new ArrayList<>();
    private DatabaseReference contactsRef, nameRef;
    String currentUserId = "TestUserID"; // Test user ID
    String usserName = "None";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Firebase reference setup (user's contacts)

        contactsRef = FirebaseDatabase.getInstance("https://dizzzcord-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users").child(currentUserId).child("contacts");
        nameRef = FirebaseDatabase.getInstance("https://dizzzcord-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users").child(currentUserId).child("name");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usserName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "Ошибка при получении данных: " + error.getMessage());
            }
        });


        loadContacts();

        return view;
    }

    private void loadContacts() {
        contactsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contacts.clear();
                for (DataSnapshot contactSnapshot : snapshot.getChildren()) {
                    String contactId = contactSnapshot.getKey();
                    String nickname = contactSnapshot.getValue(String.class);
                    if (contactId != null && nickname != null) {
                        contacts.add(new Contact(contactId, nickname));
                    }
                }
                adapter = new ContactsAdapter(contacts, Contacts.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "Ошибка при получении данных: " + error.getMessage());
            }
        });
    }

    @Override
    public void onCallButtonClick(Contact contact) {
        // Показываем Toast с ID контакта
        String message = "Инициализация звонка абоненту с ID: " + contact.id;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

        // Передаем имя пользователя и собеседника в CallFragment
        String userName = usserName; // Здесь используйте реальное имя пользователя
        String callerName = contact.nickname; // Имя собеседника

        // Создаем новый фрагмент и передаем аргументы
        Call callFragment = Call.newInstance(userName, callerName);

        // Переходим в CallFragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, callFragment)
                .addToBackStack(null)  // Это добавит фрагмент в стек для возможности возврата
                .commit();
    }
}

