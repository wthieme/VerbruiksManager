package nl.whitedove.verbruiksmanager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VerbruikGrafiekActivity extends AppCompatActivity {

    DatabaseHelper mDH;

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
        FloatingActionButton fabAppVoegtoe = (FloatingActionButton) findViewById(R.id.fabAppVoegtoe);
        fabAppVoegtoe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBackgroundFab)));
        fabAppVoegtoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatInvoeren();
            }
        });

        FloatingActionButton fabApparaten = (FloatingActionButton) findViewById(R.id.fabApparaten);
        fabApparaten.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBackgroundFab)));
        fabApparaten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apparaten();
            }
        });

        mDH = new DatabaseHelper(getApplicationContext());
        InitSpinners();
        ToondataBackground();
    }

    private void InitSpinners() {
        Spinner spGrCat = (Spinner) findViewById(R.id.spGrCat);
        List<String> cats = mDH.getCategorien();
        cats.add(0, getResources().getString(R.string.AlleApps));
        cats.add(0, getResources().getString(R.string.AlleCats));
        String[] array = new String[cats.size()];
        cats.toArray(array);
        ArrayAdapter<String> categorienAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cats);
        spGrCat.setAdapter(categorienAdapter);

        if (!Helper.SelectieGrafiek.isEmpty())
            spGrCat.setSelection(((ArrayAdapter) spGrCat.getAdapter()).getPosition(Helper.SelectieGrafiek));

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
        Spinner spGrCat = (Spinner) findViewById(R.id.spGrCat);
        String sCat = spGrCat.getSelectedItem().toString();
        Helper.SelectieGrafiek = sCat;

        new AsyncGetStatsTask().execute(sCat);
    }

    private void ToonGrafiek(List<VerbruikStat> stats) {
        PieChart chart = (PieChart) findViewById(R.id.lcPerdag);
        TextView tvGeenGegevens = (TextView) findViewById(R.id.tvGeenGegevens);
        if (stats == null || stats.size() == 0) {
            chart.setVisibility(View.GONE);
            tvGeenGegevens.setVisibility(View.VISIBLE);
            return;
        } else {
            tvGeenGegevens.setVisibility(View.GONE);
            chart.setVisibility(View.VISIBLE);
        }

        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.setNoDataText(getString(R.string.nodata));
        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(20f);
        legend.setTextSize(12f);
        legend.setMaxSizePercent(20f);

        double totaal = 0f;

        int[] colors = new int[]{getResources().getColor(R.color.colorGrafiek1),
                getResources().getColor(R.color.colorGrafiek2),
                getResources().getColor(R.color.colorGrafiek3),
                getResources().getColor(R.color.colorGrafiek4),
                getResources().getColor(R.color.colorGrafiek5),
                getResources().getColor(R.color.colorGrafiek6),
                getResources().getColor(R.color.colorGrafiek7),
                getResources().getColor(R.color.colorGrafiek8),
                getResources().getColor(R.color.colorGrafiek9),
                getResources().getColor(R.color.colorGrafiek10),
                getResources().getColor(R.color.colorGrafiek11),
                getResources().getColor(R.color.colorGrafiek12),
                getResources().getColor(R.color.colorGrafiek13),
                getResources().getColor(R.color.colorGrafiek14),
                getResources().getColor(R.color.colorGrafiek15),
                getResources().getColor(R.color.colorGrafiek16),
                getResources().getColor(R.color.colorGrafiek17),
                getResources().getColor(R.color.colorGrafiek18),
                getResources().getColor(R.color.colorGrafiek19),
                getResources().getColor(R.color.colorGrafiek20),
                getResources().getColor(R.color.colorGrafiek21),
                getResources().getColor(R.color.colorGrafiek22),
                getResources().getColor(R.color.colorGrafiek23),
                getResources().getColor(R.color.colorGrafiek24),
                getResources().getColor(R.color.colorGrafiek25)};

        for (VerbruikStat stat : stats) {
            totaal += stat.getVerbruik();
        }

        List<String> xVals = new ArrayList<>();
        ArrayList<Entry> dataT = new ArrayList<>();
        int i = 1;

        String[] labels = new String[stats.size()];
        int[] cols = new int[stats.size()];

        for (VerbruikStat stat : stats) {
            long procent = Math.round(100.0 * stat.getVerbruik() / totaal);
            if (procent > 1) xVals.add(String.format("%d%%", procent));
            else xVals.add("");
            labels[i - 1] = stat.getName();
            cols[i - 1] = colors[i - 1];
            dataT.add(new Entry(procent, i++));
        }

        legend.setCustom(cols, labels);

        PieDataSet dsT = new PieDataSet(dataT, "");
        dsT.setColors(colors);

        ValueFormatter myformat = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return "";
            }
        };
        dsT.setValueFormatter(myformat);

        PieData data = new PieData(xVals, dsT);
        data.setValueTextSize(14f);
        data.setValueTextColor(getResources().getColor(R.color.colorPrimary));

        chart.setData(data);
        chart.animateXY(500, 500);
        chart.invalidate();
    }

    private class AsyncGetStatsTask extends AsyncTask<String, Void, List<VerbruikStat>> {

        @Override
        protected List<VerbruikStat> doInBackground(String... params) {

            String sCat = params[0];
            List<VerbruikStat> stats = null;
            int catId = -1;
            Boolean alleCats = (sCat == getResources().getString(R.string.AlleCats));
            Boolean alleApps = (sCat == getResources().getString(R.string.AlleApps));
            if (!alleCats && !alleApps) {
                Categorie cat = mDH.getCategoriebyName(sCat);
                catId = cat.getID();
            }

            Helper.CatAppType catApp;
            if (alleApps) catApp = Helper.CatAppType.AlleApparaten;
            else if (alleCats) catApp = Helper.CatAppType.AlleCategorieen;
            else catApp = Helper.CatAppType.EenCategorie;

            stats = mDH.getJaarStats(catApp, catId);
            Collections.sort(stats, new StatsComparator());

            return stats;
        }

        @Override
        protected void onPostExecute(List<VerbruikStat> stats) {
            ToonGrafiek(stats);
        }
    }

    public class StatsComparator implements Comparator<VerbruikStat> {
        public int compare(VerbruikStat left, VerbruikStat right) {
            return Double.compare(right.getVerbruik(), left.getVerbruik());
        }
    }

}