package com.melita.thinkchat.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.melita.thinkchat.ChatsModal;
import com.melita.thinkchat.MsgModal;
import com.melita.thinkchat.RetrofitAPI;
import com.melita.thinkchat.adapters.ChatRVAdapter;
import com.melita.thinkchat.databinding.ActivityChatBinding;
import com.melita.thinkchat.utilities.PreferenceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private PreferenceManager preferenceManager;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;
    private RecyclerView chatsRV;
    private FrameLayout sendMsgIB;
    private EditText userMsgEdt;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatsRV = binding.chatRecyclerView;
        sendMsgIB = binding.FABSend;
        userMsgEdt = binding.inputMessage;
        chatsModalArrayList = new ArrayList<>();

        sendMsgIB.setOnClickListener(v -> {
            if (userMsgEdt.getText().toString().isEmpty()) {
                Toast.makeText(ChatActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                return;
            }
            getResponse(userMsgEdt.getText().toString());
            userMsgEdt.setText("");
        });

        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        chatsRV.setLayoutManager(linearLayoutManager);
        chatsRV.setAdapter(chatRVAdapter);
    }
    private void getResponse(String message) {
        chatsModalArrayList.add(new ChatsModal(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=177974&key=8SOeTNCN9eprDP3J&uid=uid&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if (response.isSuccessful()) {
                    MsgModal modal = response.body();
                    chatsModalArrayList.add(new ChatsModal(modal.getCnt(), BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatsModalArrayList.add(new ChatsModal("An error occurred. Could you please send your message again?", BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });
    }
}