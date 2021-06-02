package com.genedev.mathsolver;

import android.content.Intent;
import androidx.core.app.ShareCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;

import com.genedev.mathsolver.MainActivity;
import com.genedev.mathsolver.R;
import com.genedev.mathsolver.data.APIKey;
import com.genedev.mathsolver.data.Converter;
import com.genedev.mathsolver.data.Subpod;
import com.genedev.mathsolver.data.WolframAdapter;
import com.genedev.mathsolver.data.WolframAlphaAsyncTask;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.Objects;

public class SolutionActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.wolframalpha.com/v2/query?input=";
    private static final String FORMAT = "&format=image,plaintext";
    private static final String OUTPUT = "&output=JSON";
    private static final String APP_ID = "&appid=" + APIKey.ID;
    private static final String PODE_STATE = "&podstate=Step-by-step%20solution";

    RecyclerView solutionList;
    ArrayList<Subpod> subpods;
    WolframAdapter adapter;
    String result;

    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        Intent startedSolutionActivity = getIntent();
        if(startedSolutionActivity.hasExtra(MainActivity.TO_SOLUTIONS_LABEL) && startedSolutionActivity.hasExtra(MainActivity.INPUT_LABEL))
        {
            Bundle bundle = startedSolutionActivity.getExtras();
            result = bundle.getString(MainActivity.INPUT_LABEL);
            Log.e("RESULT", result);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        solutionList = (RecyclerView) findViewById(R.id.solution_list);

        subpods = new ArrayList<>();

        adapter = new WolframAdapter(this, subpods);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        solutionList.setAdapter(adapter);
        solutionList.setLayoutManager(layoutManager);

        WolframAlphaAsyncTask wolframTask = new WolframAlphaAsyncTask(adapter, getApplicationContext());
        String input = Converter.convertMathToWolfram(result);
        input = input.trim();
        String urlString = createFullURL(input);
        Log.e("URL:", urlString);
        wolframTask.execute(urlString);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate share_menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        if(id == R.id.menu_item_email){
            StringBuilder linksBuilder = new StringBuilder();
            for(Subpod subpod : adapter.getResults()){
                if(subpod.getImage() != null) {
                    linksBuilder.append(subpod.getImage().getSourceLink());
                    linksBuilder.append("\n");
                    linksBuilder.append("\n");
                }
            }
            String links = linksBuilder.toString();
            Log.e("Links", links);
            shareText(links);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void shareText(String textToShare) {
        Log.e("Links", textToShare);
        String mimeType = "text/plain";
        String title = "Share Solutions Image Links";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }


    public String createFullURL(String input) {
        String fullURL = BASE_URL + input + FORMAT + OUTPUT + APP_ID + PODE_STATE;
        return fullURL;
    }

}
