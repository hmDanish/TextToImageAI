package com.appssuite.ai.image.generator.text.to.texttoimageai;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.common.collect.ImmutableList;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectStyleAdopter.ImageClicked {
    RewardedAd mRewardedAd;
    SelectStyleAdopter myAdopter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<SelectStyle> styles;
    AppCompatButton pro_option;
    AdView mAdView;
    String promptStyle = "";
    EditText searchResult;
    TextView selectedStyle;
    Button createButton ;
    /////////////////////////////////////////
    private BillingClient billingClient;
    private ProductDetails productDetails;
    private Purchase purchase;

    static final String TAG = "InAppPurchaseTag";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ////add load........
        LoadRewardAds();
        //Banner Add......
        BannerAdd();
        recyclerView = findViewById(R.id.Horizontal_recycler);
        styles = new ArrayList<>();
        createButton = (Button) findViewById(R.id.createBtn);
        selectedStyle = findViewById(R.id.selectedStyle);
        searchResult = findViewById(R.id.searchResult);
        pro_option = findViewById(R.id.pro_option);




        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManager);

        styles.add(new SelectStyle("Cartoon",(R.drawable.cartoon_btn)));
        styles.add(new SelectStyle("Ai Avatar",(R.drawable.aiavatar_btn)));
        styles.add(new SelectStyle("Anime Girl",(R.drawable.animegirl_btn)));
        styles.add(new SelectStyle("Anime Boy",(R.drawable.animeboy_btn)));
        styles.add(new SelectStyle("Pencil",(R.drawable.pencil_btn)));
        styles.add(new SelectStyle("Neon",(R.drawable.neon_btn)));
        styles.add(new SelectStyle("Oil Painting",(R.drawable.oilpainting_btn)));
        styles.add(new SelectStyle("Ink",(R.drawable.ink_btn)));
        styles.add(new SelectStyle("Water Paint",(R.drawable.waterpaint_btn)));
        styles.add(new SelectStyle("Illustration",(R.drawable.illustration_btn)));
        styles.add(new SelectStyle("",(R.drawable.no_filter)));

        myAdopter = new SelectStyleAdopter(styles,MainActivity.this);
        recyclerView.setAdapter(myAdopter);

        billingSetup();
        //Buy premium Package

        pro_option.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this,Premium.class);
//            startActivity(intent);

            makePurchase(view);

        });

        //Search Image Result
        createButton.setOnClickListener(view -> {
            if (checkConnection(MainActivity.this)) {
                if (searchResult.getText().toString().isEmpty()) {
                    searchResult.setError("Please Write Something...");
                    Toast.makeText(getApplicationContext(), "Command Prompt Is Empty...", Toast.LENGTH_SHORT).show();
                }
                else {
                    String commandPrompt = searchResult.getText().toString().concat(promptStyle);
                    Intent intent = new Intent(MainActivity.this,DisplayImage.class);
                    intent.putExtra("commandPrompt",commandPrompt);
                    showReward();
                    startActivity(intent);
                    finishAffinity();

                }
            }
            else{
                Toast.makeText(MainActivity.this, "Please Check Your Internet Connection !!!", Toast.LENGTH_SHORT).show();

            }



        });
        ///////banner add
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
    }


    private void billingSetup() {

        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(
                    @NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() ==
                        BillingClient.BillingResponseCode.OK) {
                    Log.i(TAG, "OnBillingSetupFinish connected");
                    queryProduct();
                } else {
                    Log.i(TAG, "OnBillingSetupFinish failed");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Log.i(TAG, "OnBillingSetupFinish connection lost");
            }
        });
    }
    private void queryProduct() {

        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("credit_50")
                                                .setProductType(
                                                        BillingClient.ProductType.INAPP)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                new ProductDetailsResponseListener() {
                    public void onProductDetailsResponse(
                            @NonNull BillingResult billingResult,
                            @NonNull List<ProductDetails> productDetailsList) {

                        if (!productDetailsList.isEmpty()) {
                            productDetails = productDetailsList.get(0);
                            runOnUiThread(() -> {
//                                binding.buyButton.setEnabled(true);
//                                binding.statusText.setText(productDetails.getName());
                            });
                        } else {
                            Log.i(TAG, "onProductDetailsResponse: No products");
                        }
                    }
                }
        );
    }
    public void makePurchase(View view) {

        BillingFlowParams billingFlowParams =
                BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                                ImmutableList.of(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(productDetails)
                                                .build()
                                )
                        )
                        .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(BillingResult billingResult,
                                       List<Purchase> purchases) {

            if (billingResult.getResponseCode() ==
                    BillingClient.BillingResponseCode.OK
                    && purchases != null) {
                for (Purchase purchase : purchases) {
                    completePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() ==
                    BillingClient.BillingResponseCode.USER_CANCELED) {
                Log.i(TAG, "onPurchasesUpdated: Purchase Canceled");
            } else {
                Log.i(TAG, "onPurchasesUpdated: Error");
            }
        }
    };
    private void completePurchase(Purchase item) {

        purchase = item;

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
            runOnUiThread(() -> {
//                binding.consumeButton.setEnabled(true);
//                binding.statusText.setText("Purchase Complete");
            });
    }

    public void consumePurchase(View view) {
        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult,
                                          @NonNull String purchaseToken) {
                if (billingResult.getResponseCode() ==
                        BillingClient.BillingResponseCode.OK) {
                    runOnUiThread(() -> {
//                        binding.consumeButton.setEnabled(false);
//                        binding.statusText.setText("Purchase consumed");
                    });
                }
            }
        };
        billingClient.consumeAsync(consumeParams, listener);
    }

    @Override
    public void OnImageClicked(int position) {
        promptStyle = ","+styles.get(position).name;
        selectedStyle.setText("#"+styles.get(position).name);
        Toast.makeText(this, styles.get(position).name, Toast.LENGTH_SHORT).show();

    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    public  void BannerAdd(){
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });



    }

    public void showReward() {

        if (mRewardedAd != null) {

            mRewardedAd.show(MainActivity.this, rewardItem -> {
                // Handle the reward.

            });
        } else {
            Toast.makeText(this, "The rewarded ad wasn't ready yet.Kindly wait...`  1", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }

    }

    public  void LoadRewardAds(){
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                        Log.d(TAG, loadAdError.toString());
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                            }
                        });

                    }
                });

    }

    private String getAdvertisingId() {
        Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
        } catch (IOException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }

        return  adInfo.getId();
    }

    private void checkAppOpenCount() {
        String adId = getAdvertisingId();
        SharedPreferences sharedPreferences = getSharedPreferences("appOpenCount", MODE_PRIVATE);
        int count = sharedPreferences.getInt(adId, 0);
        count++;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(adId, count);
        editor.apply();

        // use count variable to show the number of times the user has opened the app
    }


}