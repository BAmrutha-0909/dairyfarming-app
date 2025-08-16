// app/src/main/java/com/example/dairyfarming/ml/MilkPredictor.java
package com.example.dairyfarming.ml;

import java.util.List;

public class MilkPredictor {
    // TODO: load a .tflite model if you have one; for now, simple heuristic
    public double predictNextDay(List<Double> last7days, double currentLiters) {
        if (last7days == null || last7days.isEmpty()) return currentLiters;
        double sum = 0;
        for (double v : last7days) sum += v;
        double avg = sum / last7days.size();
        // simple smoothing
        return 0.6 * currentLiters + 0.4 * avg;
    }
}
