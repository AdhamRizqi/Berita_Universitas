package com.azhar.reportapps.ui.history;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.reportapps.R;
import com.azhar.reportapps.model.ModelDatabase;
import com.azhar.reportapps.utils.BitmapManager;
import com.azhar.reportapps.viewmodel.HistoryViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.HistoryAdapterCallback{

    List<ModelDatabase> modelDatabaseList = new ArrayList<>();
    HistoryAdapter historyAdapter;
    HistoryViewModel historyViewModel;
    Toolbar toolbar;
    RecyclerView rvHistory;
    TextView tvNotFound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setStatusBar();
        setToolbar();
        setInitLayout();
        setViewModel();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }





    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setInitLayout() {
        rvHistory = findViewById(R.id.rvHistory);
        tvNotFound = findViewById(R.id.tvNotFound);

        tvNotFound.setVisibility(View.GONE);

        historyAdapter = new HistoryAdapter(this, modelDatabaseList, this);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);
    }

    private void setViewModel() {
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getDataLaporan().observe(this, modelDatabases -> {
            if (modelDatabases.isEmpty()) {
                tvNotFound.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
            } else {
                tvNotFound.setVisibility(View.GONE);
                rvHistory.setVisibility(View.VISIBLE);
            }
            historyAdapter.setDataAdapter(modelDatabases);
        });
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }

    @Override
    public void onDelete(ModelDatabase modelDatabase) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus riwayat ini?");
        alertDialogBuilder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {
            int uid = modelDatabase.uid;
            historyViewModel.deleteDataById(uid);
            Toast.makeText(HistoryActivity.this,
                    "Yeay! Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show();
        });

        alertDialogBuilder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
  



}