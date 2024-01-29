package com.appssuite.ai.image.generator.text.to.texttoimageai;


import static com.android.billingclient.api.BillingClient.ProductType.SUBS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResult;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Premium extends AppCompatActivity implements PremiumList.ImageClicked {

    PremiumList premiumAdopter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PremiumModel> pack;
    AppCompatImageView btn_cancel;
    BillingClient billingClient;
    boolean OnItemClicked = false;
    int  clicked;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        btn_cancel = findViewById(R.id.btn_cancel);
        recyclerView = findViewById(R.id.listView);
        pack = new ArrayList<>();

        btn_cancel.setOnClickListener(view -> onBackPressed());
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        pack.add(new PremiumModel("credit_50",R.drawable.ic__credit50_btn));
        pack.add(new PremiumModel("credit_100", R.drawable.ic_credits100_btn));
        pack.add(new PremiumModel("credit_300",R.drawable.ic__credits300_btn));
        premiumAdopter = new PremiumList(pack,this);
        recyclerView.setAdapter(premiumAdopter);
        premiumAdopter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////////////

    }
    @Override
    public void OnImageClicked(int position) {
        Toast.makeText(this, pack.get(position).getPackName(), Toast.LENGTH_SHORT).show();
    }
}