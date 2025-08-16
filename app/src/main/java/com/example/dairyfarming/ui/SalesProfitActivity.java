package com.example.dairyfarming.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dairyfarming.R;
import com.example.dairyfarming.ui.fragments.SalesProfitFragment;

public class SalesProfitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SalesProfitFragment())
                    .commit();
        }
    }
}
