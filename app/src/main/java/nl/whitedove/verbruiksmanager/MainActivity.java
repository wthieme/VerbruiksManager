package nl.whitedove.verbruiksmanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper mDH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        } else if (id == R.id.action_Uitleg) {
            Intent i = new Intent(this, UitlegActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void InitSpinners() {
        Spinner spJaarMaandDag = (Spinner) findViewById(R.id.spJaarMaandDag);
        String[] saJaarMaandDag = getResources().getStringArray(R.array.JaarMaandDag);
        ArrayAdapter<String> jaarMaandDagAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, saJaarMaandDag);
        spJaarMaandDag.setAdapter(jaarMaandDagAdapter);
        if (!Helper.JaarMaandDagSelectie.isEmpty())
            spJaarMaandDag.setSelection(((ArrayAdapter) spJaarMaandDag.getAdapter()).getPosition(Helper.JaarMaandDagSelectie));

        spJaarMaandDag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ToonApparatenEnVerbruik();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ToonApparatenEnVerbruik();
            }

        });

        Spinner spCat = (Spinner) findViewById(R.id.spCat);
        List<String> cats = mDH.getCategorien();
        cats.add(0, getResources().getString(R.string.AlleApps));
        String[] array = new String[cats.size()];
        cats.toArray(array);
        ArrayAdapter<String> categorienAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cats);
        spCat.setAdapter(categorienAdapter);

        if (!Helper.CategorieSelectieMain.isEmpty())
            spCat.setSelection(((ArrayAdapter) spCat.getAdapter()).getPosition(Helper.CategorieSelectieMain));

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ToonApparatenEnVerbruik();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ToonApparatenEnVerbruik();
            }

        });

    }

    private void Init() {
        mDH = new DatabaseHelper(getApplicationContext());

        TipVanDeDagBijStart();
        InitSpinners();

        FloatingActionButton fabAppVoegtoe = (FloatingActionButton) findViewById(R.id.fabAppVoegtoe);
        fabAppVoegtoe.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabAppVoegtoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatInvoeren();
            }
        });

        FloatingActionButton fabGrafiek = (FloatingActionButton) findViewById(R.id.fabGrafiek);
        fabGrafiek.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabGrafiek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grafiek();
            }
        });

        ToonApparatenEnVerbruik();
    }

    private void TipVanDeDagBijStart() {
        if (Helper.TipGehad) return;
        if (Helper.TipsStaanUit(this)) return;
        Helper.ToonTip(this);
        Helper.TipGehad = true;
    }

    private void ToonApparatenEnVerbruik() {

        final ListView lvApparaten = (ListView) findViewById(R.id.lvApparaten);
        Spinner spJaarMaandDag = (Spinner) findViewById(R.id.spJaarMaandDag);
        Spinner spCat = (Spinner) findViewById(R.id.spCat);
        String sCat = spCat.getSelectedItem().toString();
        Helper.CategorieSelectieMain = sCat;
        int catId = -1;
        if (!sCat.equals(getResources().getString(R.string.AlleApps))) {
            Categorie cat = mDH.getCategoriebyName(sCat);
            catId = cat.getID();
        }

        List<Apparaat> apparaten = mDH.getApparaten(catId);
        String sjmd = spJaarMaandDag.getSelectedItem().toString();
        Helper.JaarMaandDagSelectie = sjmd;
        Helper.JaarMaandDagType jmd = Helper.JaarMaandDagType.valueOf(sjmd);
        lvApparaten.setAdapter(new CustomListAdapterApparaten(this, apparaten, jmd));
        lvApparaten.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                Apparaat app = (Apparaat) parent.getItemAtPosition(pos);
                NewMenu(app.getId());
                return true;
            }
        });
        lvApparaten.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Apparaat app = (Apparaat) parent.getItemAtPosition(pos);
                ApparaatWijzigen(app.getId());
            }
        });
        ToonVerbruik(apparaten);
    }

    private void ToonVerbruik(List<Apparaat> apparaten) {
        double prijs = Helper.GetPrijs(this);
        double VerbruikDag = 0f;
        double VerbruikMaand = 0f;
        double VerbruikJaar = 0f;
        double KostenDag = 0f;
        double KostenMaand = 0f;
        double KostenJaar = 0f;
        for (Apparaat apparaat : apparaten) {
            ApparaatVerbruik apv = Helper.BerekenVerbruik(apparaat, prijs);
            VerbruikDag += apv.getVerbruikDag();
            VerbruikMaand += apv.getVerbruikMaand();
            VerbruikJaar += apv.getVerbruikJaar();
            KostenDag += apv.getKostenDag();
            KostenMaand += apv.getKostenMaand();
            KostenJaar += apv.getKostenJaar();
        }

        TextView tvr1k2 = (TextView) findViewById(R.id.tvr1k2);
        TextView tvr1k3 = (TextView) findViewById(R.id.tvr1k3);
        TextView tvr2k2 = (TextView) findViewById(R.id.tvr2k2);
        TextView tvr2k3 = (TextView) findViewById(R.id.tvr2k3);
        TextView tvr3k2 = (TextView) findViewById(R.id.tvr3k2);
        TextView tvr3k3 = (TextView) findViewById(R.id.tvr3k3);

        tvr1k2.setText(Helper.getVerbruikString(VerbruikDag));
        tvr2k2.setText(Helper.getVerbruikString(VerbruikMaand));
        tvr3k2.setText(Helper.getVerbruikString(VerbruikJaar));
        tvr1k3.setText(Helper.getEuroString(KostenDag));
        tvr2k3.setText(Helper.getEuroString(KostenMaand));
        tvr3k3.setText(Helper.getEuroString(KostenJaar));
    }

    private void ApparaatInvoeren() {
        Intent intent = new Intent(this, ApparaatInvoerenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("actie", Helper.ActieType.toevoegen.getValue());
        intent.putExtra("apparaatId", -1);
        intent.putExtra("terug", Helper.SchermType.LijstApparaten.getValue());
        startActivity(intent);
    }

    private void Grafiek() {
        Intent intent = new Intent(this, VerbruikGrafiekActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void ApparaatWijzigen(int AppId) {
        Intent intent = new Intent(this, ApparaatInvoerenActivity.class);
        intent.putExtra("actie", Helper.ActieType.wijzigen.getValue());
        intent.putExtra("apparaatId", AppId);
        intent.putExtra("terug", Helper.SchermType.LijstApparaten.getValue());
        startActivity(intent);
    }

    private void ApparaatVerwijderen(int AppId) {
        mDH.deleteApparaat(AppId);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    public void NewMenu(final int AppId) {
        List<ContextMenuItem> contextMenuItems;
        final Dialog customDialog = new Dialog(this);

        LayoutInflater inflater;
        View child;
        ListView listView;
        ContextMenuAdapter adapter;

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        child = inflater.inflate(R.layout.listview_context_menu, null);
        listView = (ListView) child.findViewById(R.id.listView_context_menu);

        contextMenuItems = new ArrayList<>();

        contextMenuItems.add(new ContextMenuItem(ContextCompat.getDrawable(this, R.drawable.wijzig), getString(R.string.Wijzigen)));
        contextMenuItems.add(new ContextMenuItem(ContextCompat.getDrawable(this, R.drawable.delete), getString(R.string.Verwijderen)));

        adapter = new ContextMenuAdapter(this, contextMenuItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                customDialog.dismiss();
                switch (position) {

                    case 0:
                        ApparaatWijzigen(AppId);
                        return;

                    case 1:
                        ApparaatVerwijderen(AppId);
                }
            }
        });

        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(child);
        customDialog.show();
    }
}
