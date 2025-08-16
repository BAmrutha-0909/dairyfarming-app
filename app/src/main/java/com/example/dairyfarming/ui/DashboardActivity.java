// ui/DashboardActivity.java
package com.example.dairyfarming.ui;
import androidx.fragment.app.Fragment;
import com.example.dairyfarming.ui.fragments.*;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import com.example.dairyfarming.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dairyfarming.data.*;
import com.example.dairyfarming.databinding.ActivityDashboardBinding;
import com.example.dairyfarming.ml.MilkPredictor;
import com.example.dairyfarming.sync.SyncRepository;

import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding bind;
    AppDatabase db;
    BuffaloAdapter adapter;
    LiveData<List<Buffalo>> live;
    String email;
    SyncRepository sync;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        bind = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        db = DbProvider.get(this);
        email = getIntent().getStringExtra("email");
        sync = new SyncRepository(this, email);

        adapter = new BuffaloAdapter(new BuffaloAdapter.Listener() {
            @Override public void onClick(Buffalo bf) { showEditDialog(bf); }
            @Override public void onLongClick(Buffalo bf) { confirmDelete(bf); }
        });

        bind.recycler.setLayoutManager(new LinearLayoutManager(this));
        bind.recycler.setAdapter(adapter);

        ImageView profile = bind.ivProfile;
        profile.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class).putExtra("email", email))
        );

        bind.fabAdd.setOnClickListener(v -> showAddDialog());

        // observe data
        live = db.buffaloDao().liveAll();
        live.observe(this, adapter::submit);

        // initial pull from cloud; then push on changes
        sync.pullAll();
    }

    private void showAddDialog(){
        var v = LayoutInflater.from(this).inflate(com.example.dairyfarming.R.layout.dialog_add_edit, null);
        EditText etName = v.findViewById(com.example.dairyfarming.R.id.etName);
        EditText etLiters = v.findViewById(com.example.dairyfarming.R.id.etLiters);
        etLiters.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(this)
                .setTitle("Add Buffalo")
                .setView(v)
                .setPositiveButton("Save", (d, w) -> new Thread(() -> {
                    Buffalo b = new Buffalo();
                    b.name = etName.getText().toString().trim();
                    b.litersPerDay = parse(etLiters.getText().toString());
                    b.predictedLitersNextDay = new MilkPredictor()
                            .predictNextDay(Collections.singletonList(b.litersPerDay), b.litersPerDay);
                    b.updatedAt = System.currentTimeMillis();
                    db.buffaloDao().insert(b);
                    sync.pushAll();
                }).start())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(Buffalo old){
        var v = LayoutInflater.from(this).inflate(com.example.dairyfarming.R.layout.dialog_add_edit, null);
        EditText etName = v.findViewById(com.example.dairyfarming.R.id.etName);
        EditText etLiters = v.findViewById(com.example.dairyfarming.R.id.etLiters);
        etName.setText(old.name);
        etLiters.setText(String.valueOf(old.litersPerDay));

        new AlertDialog.Builder(this)
                .setTitle("Edit Buffalo")
                .setView(v)
                .setPositiveButton("Save Changes", (d, w) -> new Thread(() -> {
                    old.name = etName.getText().toString().trim();
                    old.litersPerDay = parse(etLiters.getText().toString());
                    old.predictedLitersNextDay = new MilkPredictor()
                            .predictNextDay(Collections.singletonList(old.litersPerDay), old.litersPerDay);
                    old.updatedAt = System.currentTimeMillis();
                    db.buffaloDao().update(old);
                    sync.pushAll();
                }).start())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(Buffalo bff){
        new AlertDialog.Builder(this)
                .setMessage("Delete this record?")
                .setPositiveButton("Delete", (d, w) -> new Thread(() -> {
                    db.buffaloDao().softDelete(bff.id, System.currentTimeMillis());
                    sync.pushAll();
                }).start())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private double parse(String s){
        try { return Double.parseDouble(s); } catch (Exception e){ return 0; }
    }
    private void openFragment(Fragment fragment) {
        findViewById(R.id.recycler).setVisibility(View.GONE);
        findViewById(R.id.fabAdd).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // restore dashboard UI
            findViewById(R.id.recycler).setVisibility(View.VISIBLE);
            findViewById(R.id.fabAdd).setVisibility(View.VISIBLE);
            findViewById(R.id.fragment_container).setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
