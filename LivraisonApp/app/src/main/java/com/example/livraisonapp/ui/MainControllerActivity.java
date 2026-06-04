package com.example.livraisonapp.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.livraisonapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainControllerActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);

        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        loadFragment(new DashboardFragment());

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());

            } else if (id == R.id.nav_livraisons) {
                loadFragment(new LivraisonsFragment());

            } else if (id == R.id.nav_messages) {
                loadFragment(new MessagesFragment());

            } else if (id == R.id.nav_search) {
                loadFragment(new SearchFragment());
            }

            return true;
        });
    }

    private void loadFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }
}
