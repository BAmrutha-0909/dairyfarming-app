// app/src/main/java/com/example/dairyfarming/sync/SyncRepository.java
package com.example.dairyfarming.sync;

import android.content.Context;
import android.util.Log;

import com.example.dairyfarming.data.*;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncRepository {
    private final AppDatabase db;
    private final FirebaseFirestore fs = FirebaseFirestore.getInstance();
    private final CollectionReference buffaloCol;

    public SyncRepository(Context ctx, String email) {
        this.db = DbProvider.get(ctx);
        // Scope by email so multiple local users don’t collide
        this.buffaloCol = fs.collection("users").document(email).collection("buffalo");
    }

    public void pushAll() {
        new Thread(() -> {
            try {
                List<Buffalo> list = db.buffaloDao().rawAll();
                for (Buffalo b : list) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", b.name);
                    m.put("litersPerDay", b.litersPerDay);
                    m.put("predictedLitersNextDay", b.predictedLitersNextDay);
                    m.put("updatedAt", b.updatedAt);
                    m.put("deleted", b.deleted);
                    buffaloCol.document(String.valueOf(b.id)).set(m);
                }
            } catch (Exception e) {
                Log.e("Sync", "pushAll failed", e);
            }
        }).start();
    }

    public void pullAll() {
        buffaloCol.get().addOnSuccessListener(snap -> new Thread(() -> {
            for (DocumentSnapshot d : snap) {
                Buffalo b = new Buffalo();
                b.id = Long.parseLong(d.getId());
                b.name = d.getString("name");
                Double liters = d.getDouble("litersPerDay");
                b.litersPerDay = liters == null ? 0 : liters;
                b.predictedLitersNextDay = d.getDouble("predictedLitersNextDay");
                Number upd = (Number) d.get("updatedAt");
                b.updatedAt = upd == null ? System.currentTimeMillis() : upd.longValue();
                Boolean del = d.getBoolean("deleted");
                b.deleted = del != null && del;
                db.buffaloDao().insert(b);
            }
        }).start());
    }
}
