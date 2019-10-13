package nl.whitedove.verbruiksmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerbruikGrafiekActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grafiek_verbruik);
        Init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Settings) {
            Intent i = new Intent(this, InstellingenActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_TipVdDag) {
            Helper.ToonTip(this);
            return true;
        }
        if (id == R.id.action_Uitleg) {
            Intent i = new Intent(this, UitlegActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Init() {
        FloatingActionButton fabAppVoegtoe = findViewById(R.id.fabAppVoegtoe);
        fabAppVoegtoe.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabAppVoegtoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatInvoeren();
            }
        });

        FloatingActionButton fabApparaten = findViewById(R.id.fabApparaten);
        fabApparaten.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabApparaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apparaten();
            }
        });

        InitSpinners();
        ToondataBackground();
    }

    private void InitSpinners() {
        Spinner spGrCat = findViewById(R.id.spGrCat);
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        List<String> cats = dh.getCategorien();
        cats.add(0, getResources().getString(R.string.AlleApps));
        cats.add(0, getResources().getString(R.string.AlleCats));
        String[] array = new String[cats.size()];
        cats.toArray(array);
        ArrayAdapter<String> categorienAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cats);
        spGrCat.setAdapter(categorienAdapter);

        if (!Helper.SelectieGrafiek.isEmpty())
            spGrCat.setSelection(categorienAdapter.getPosition(Helper.SelectieGrafiek));

        spGrCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ToondataBackground();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ToondataBackground();
            }

        });
    }

    private void Apparaten() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void ApparaatInvoeren() {
        Intent intent = new Intent(this, ApparaatInvoerenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("actie", Helper.ActieType.toevoegen.getValue());
        intent.putExtra("apparaatId", -1);
        intent.putExtra("terug", Helper.SchermType.Grafiek.getValue());
        startActivity(intent);
    }

    private void ToondataBackground() {
        Spinner spGrCat = findViewById(R.id.spGrCat);
        String sCat = spGrCat.getSelectedItem().toString();
        Helper.SelectieGrafiek = sCat;
        Context context = getApplicationContext();
        //noinspection unchecked
        new AsyncGetStatsTask(this).execute(Pair.create(context, sCat));
    }

    private void ToonGrafiek(List<VerbruikStat> stats) {
        PieChart chart = findViewById(R.id.pcVerbruik);
        TextView tvGeenGegevens = findViewById(R.id.tvGeenGegevens);
        if (stats == null || stats.size() == 0) {
            chart.setVisibility(View.GONE);
            tvGeenGegevens.setVisibility(View.VISIBLE);
            return;
        } else {
            tvGeenGegevens.setVisibility(View.GONE);
            chart.setVisibility(View.VISIBLE);
        }
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.setTouchEnabled(false);
        chart.setNoDataText(getString(R.string.nodata));
        chart.setEntryLabelColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        chart.setDrawEntryLabels(true);

        double totaal = 0f;
        int[] colors = new int[]{ContextCompat.getColor(this, R.color.colorGrafiek1),
                ContextCompat.getColor(this, R.color.colorGrafiek2),
                ContextCompat.getColor(this, R.color.colorGrafiek3),
                ContextCompat.getColor(this, R.color.colorGrafiek4),
                ContextCompat.getColor(this, R.color.colorGrafiek5),
                ContextCompat.getColor(this, R.color.colorGrafiek6),
                ContextCompat.getColor(this, R.color.colorGrafiek7),
                ContextCompat.getColor(this, R.color.colorGrafiek8),
                ContextCompat.getColor(this, R.color.colorGrafiek9),
                ContextCompat.getColor(this, R.color.colorGrafiek10),
                ContextCompat.getColor(this, R.color.colorGrafiek11),
                ContextCompat.getColor(this, R.color.colorGrafiek12),
                ContextCompat.getColor(this, R.color.colorGrafiek13),
                ContextCompat.getColor(this, R.color.colorGrafiek14),
                ContextCompat.getColor(this, R.color.colorGrafiek15),
                ContextCompat.getColor(this, R.color.colorGrafiek16),
                ContextCompat.getColor(this, R.color.colorGrafiek17),
                ContextCompat.getColor(this, R.color.colorGrafiek18),
                ContextCompat.getColor(this, R.color.colorGrafiek19),
                ContextCompat.getColor(this, R.color.colorGrafiek20),
                ContextCompat.getColor(this, R.color.colorGrafiek21),
                ContextCompat.getColor(this, R.color.colorGrafiek22),
                ContextCompat.getColor(this, R.color.colorGrafiek23),
                ContextCompat.getColor(this, R.color.colorGrafiek24),
                ContextCompat.getColor(this, R.color.colorGrafiek25)};

        for (VerbruikStat stat : stats) {
            totaal += stat.getVerbruik();
        }

        ArrayList<PieEntry> dataT = new ArrayList<>();
        int i = 0;

        ArrayList<LegendEntry> les = new ArrayList<>();

        for (VerbruikStat stat : stats) {
            long procent = Math.round(100.0 * stat.getVerbruik() / totaal);

            @SuppressLint("DefaultLocale") String txt = procent > 1 ? String.format("%d%%", procent) : "";
            dataT.add(new PieEntry(procent, txt));
            LegendEntry le = new LegendEntry();
            le.formColor = colors[i++];
            le.label = stat.getName();
            le.form = Legend.LegendForm.SQUARE;
            le.formSize = 10;
            les.add(le);
        }

        Legend legend = chart.getLegend();
        legend.setCustom(les);
        legend.setEnabled(true);
        legend.setXEntrySpace(20f);
        legend.setTextSize(12f);
        legend.setWordWrapEnabled(true);

        PieDataSet dsT = new PieDataSet(dataT, "");
        dsT.setColors(colors);
        dsT.setDrawValues(false);

        PieData data = new PieData(dsT);
        data.setValueTextSize(14f);
        data.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        chart.setData(data);
        chart.animateXY(500, 500);
        chart.invalidate();
    }

    private static class AsyncGetStatsTask extends AsyncTask<Pair<Context, String>, Void, List<VerbruikStat>> {

        private WeakReference<VerbruikGrafiekActivity> activityWeakReference;

        AsyncGetStatsTask(VerbruikGrafiekActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @SafeVarargs
        @Override
        protected final List<VerbruikStat> doInBackground(Pair<Context, String>... params) {

            Context context = params[0].first;
            String sCat = params[0].second;
            List<VerbruikStat> stats;
            int catId = -1;
            Boolean alleCats = sCat.equals(context.getString(R.string.AlleCats));
            Boolean alleApps = sCat.equals(context.getString(R.string.AlleApps));
            DatabaseHelper dh = DatabaseHelper.getInstance(context);

            if (!alleCats && !alleApps) {
                Categorie cat = dh.getCategoriebyName(sCat);
                catId = cat.getID();
            }

            Helper.CatAppType catApp;
            if (alleApps) catApp = Helper.CatAppType.AlleApparaten;
            else if (alleCats) catApp = Helper.CatAppType.AlleCategorieen;
            else catApp = Helper.CatAppType.EenCategorie;

            stats = dh.getJaarStats(catApp, catId);
            Collections.sort(stats, StatsComparator.instance);

            return stats;
        }

        @Override
        protected void onPostExecute(List<VerbruikStat> stats) {
            VerbruikGrafiekActivity activity = activityWeakReference.get();
            if (activity != null) activity.ToonGrafiek(stats);
        }
    }
}