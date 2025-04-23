package com.example.dizzzcord;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Call extends Fragment {

    private TextView userNameTextView, callerNameTextView;
    private FrameLayout localVideoFrame, remoteVideoFrame;

    // Название пользователя и собеседника
    private String userName;
    private String callerName;

    // Метод для установки имени пользователя и собеседника
    public static Call newInstance(String userName, String callerName) {
        Call fragment = new Call();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        args.putString("callerName", callerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        // Получаем имена из аргументов
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
            callerName = getArguments().getString("callerName");
        }

        // Инициализация элементов UI
        userNameTextView = view.findViewById(R.id.userName);
        callerNameTextView = view.findViewById(R.id.callerName);
        localVideoFrame = view.findViewById(R.id.localVideoFrame);
        remoteVideoFrame = view.findViewById(R.id.remoteVideoFrame);

        // Устанавливаем имена
        userNameTextView.setText("Вы (" + userName + ")");
        callerNameTextView.setText(callerName);

        return view;
    }
}
