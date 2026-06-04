package com.example.livraisonapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.AuthApi;
import com.example.livraisonapp.model.*;
import com.example.livraisonapp.utils.RetrofitClient;

import retrofit2.*;

public class LoginActivity extends AppCompatActivity {

    EditText etLogin, etPassword;
    Button btnLogin;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String token = null;
        String login = etLogin.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        AuthApi api = RetrofitClient.getInstance().create(AuthApi.class);

        LoginRequest req = new LoginRequest();
        req.login = login;
        req.password = password;

        api.login(req).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {

                    AuthResponse res = response.body();

                    String token = res.token;
                    getSharedPreferences("app", MODE_PRIVATE)
                            .edit()
                            .putString("TOKEN", token)
                            .apply();

                    Toast.makeText(LoginActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

                    Intent intent;

                    if (res.role.equals("LIVREUR")) {
                        intent = new Intent(LoginActivity.this, LivreurActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, MainControllerActivity.class);
                    }

                    intent.putExtra("TOKEN", token);

                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
