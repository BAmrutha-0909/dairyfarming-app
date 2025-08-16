package com.example.dairyfarming.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.example.dairyfarming.data.AppDatabase;
import com.example.dairyfarming.data.Buffalo;
import com.example.dairyfarming.data.DbProvider;
import com.example.dairyfarming.databinding.FragmentSalesProfitBinding;
import com.example.dairyfarming.ml.MilkPredictor;

import java.util.ArrayList;
import java.util.List;

public class SalesProfitFragment extends Fragment {

    private FragmentSalesProfitBinding bind;
    private AppDatabase db;
    private final double PRICE_PER_LITER = 0.5; // set your milk price

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bind = FragmentSalesProfitBinding.inflate(inflater, container, false);
        db = DbProvider.get(requireContext());

        LiveData<List<Buffalo>> live = db.buffaloDao().liveAll();
        live.observe(getViewLifecycleOwner(), list -> updateData(list));

        return bind.getRoot();
    }

    private void updateData(List<Buffalo> buffaloList) {
        double totalMilk = 0;
        double predictedMilk = 0;
        List<Double> milkHistory = new ArrayList<>();

        for (Buffalo bf : buffaloList) {
            totalMilk += bf.litersPerDay;
            milkHistory.add(bf.litersPerDay);
            predictedMilk += new MilkPredictor().predictNextDay(milkHistory, bf.litersPerDay);
        }

        double profit = totalMilk * PRICE_PER_LITER;

        bind.tvTotalMilk.setText(String.format("%.1f L", totalMilk));
        bind.tvPredictedMilk.setText(String.format("%.1f L", predictedMilk));
        bind.tvProfit.setText(String.format("$%.2f", profit));
    }
}

