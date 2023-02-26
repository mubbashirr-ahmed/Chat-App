package com.example.socialmediamobileqev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Profile extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    EditText editTextSearchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);




        chipNavigationBar = findViewById(R.id.bottom_nav_bar);

        chipNavigationBar.setItemSelected(R.id.Home_profile,
                true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_Container_FrameLayout,
                        new Home_Fragment()).commit();
        bottomMenu();


        editTextSearchbar = findViewById(R.id.search_barEditTextHome);




    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                    Fragment fragment = null;
                    switch (i) {
                        case R.id.Home_profile:
                            fragment = new Home_Fragment();
                            break;
                        //       case R.id.bottom_nav_book:
                        //          fragment = new BookFragment();
                        //         break;
                        case R.id.nav_account:
                            fragment = new account_fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_Container_FrameLayout,
                                    fragment).commit();
                }

        });



    }

}
