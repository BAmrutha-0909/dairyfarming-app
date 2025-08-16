package com.example.dairyfarming.ui.fragments;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.dairyfarming.data.*;
import com.example.dairyfarming.databinding.FragmentSummaryBinding;

import java.util.List;

public class SummaryFragment extends Fragment {
    private FragmentSummaryBinding bind;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentSummaryBinding.inflate(inflater, container, false);
        db = DbProvider.get(requireContext());

        // Observe live buffalo list
        LiveData<List<Buffalo>> live = db.buffaloDao().liveAll();
        live.observe(getViewLifecycleOwner(), list -> {
            int count = list.size();
            double total = 0, avg = 0;
            for (Buffalo bf : list) total += bf.litersPerDay;
            if(count > 0) avg = total / count;

            bind.tvCount.setText(String.valueOf(count));
            bind.tvTotal.setText(String.format("%.1f L/day", total));
            bind.tvAverage.setText(String.format("%.1f L/buffalo", avg));
        });

        return bind.getRoot();
    }
}
