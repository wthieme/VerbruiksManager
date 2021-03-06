package nl.whitedove.verbruiksmanager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ApparaatInvoerenActivity extends AppCompatActivity {

    Helper.SchermType terug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apparaat_invoeren);
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

    private void Init() {
        FloatingActionButton fabSave = findViewById(R.id.fabSave);
        fabSave.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatOplsaan();
            }
        });

        FloatingActionButton fabCancel = findViewById(R.id.fabCancel);
        fabCancel.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Terug();
            }
        });

        FloatingActionButton fabVerwijder = findViewById(R.id.fabVerwijder);
        fabVerwijder.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabVerwijder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatVerwijderen();
            }
        });

        EditText etVermogen = findViewById(R.id.etVermogen);
        etVermogen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToonVerbruik();
            }
        });

        EditText etAantalUur = findViewById(R.id.etAantalUur);
        etAantalUur.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToonVerbruik();
            }
        });

        EditText etVerbruik = findViewById(R.id.etVerbruik);
        etVerbruik.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToonVerbruik();
            }
        });

        EditText etAantalKeer = findViewById(R.id.etAantalKeer);
        etAantalKeer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ToonVerbruik();
            }
        });

        LinearLayout llVerbruik = findViewById(R.id.llVerbruik);
        llVerbruik.setVisibility(View.GONE);

        TextView tvUitleg = findViewById(R.id.tvUitleg);
        tvUitleg.setText(getString(R.string.Uitleg1));

        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        terug = Helper.SchermType.fromInt(intent.getIntExtra("terug", 0));
        Helper.ActieType actie = Helper.ActieType.fromInt(intent.getIntExtra("actie", 0));
        int ApparaatId = intent.getIntExtra("apparaatId", -1);
        TextView tvActie = findViewById(R.id.tvActie);

        InitSpinners();

        if (actie == Helper.ActieType.toevoegen) {
            tvActie.setText(String.format(getString(R.string.AppActie), "toevoegen"));
        } else {
            tvActie.setText(String.format(getString(R.string.AppActie), "wijzigen"));
            Apparaat apparaat = dh.getApparaat(ApparaatId);
            ToonApparaat(apparaat);
        }

        ToonVerbruik();
    }

    private void ApparaatVerwijderen() {
        TextView tvAppId = findViewById(R.id.tvAppId);
        String sAppId = tvAppId.getText().toString();
        DatabaseHelper dh = DatabaseHelper.getInstance(this);

        if (!sAppId.isEmpty()) {
            int appId = Integer.parseInt(sAppId);
            dh.deleteApparaat(appId);
        }
        Terug();
    }

    private void Terug() {
        if (terug == Helper.SchermType.LijstApparaten) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (terug == Helper.SchermType.Grafiek) {
            Intent intent = new Intent(this, VerbruikGrafiekActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void ApparaatOplsaan() {
        TextView tvAppId = findViewById(R.id.tvAppId);
        String sAppId = tvAppId.getText().toString();

        if (sAppId.isEmpty() || sAppId.equals("-1")) {
            ApparaatToevoegen();
        } else {
            ApparaatWijzigen();
        }
        Terug();
    }

    private void InitSpinners() {
        Spinner spInvoerWijze = findViewById(R.id.spInvoerWijze);
        String[] saInvoerWijze = getResources().getStringArray(R.array.InvoerWijze);
        ArrayAdapter<String> adInvoerWijze = new ArrayAdapter<>(this, R.layout.spinner_item, saInvoerWijze);
        spInvoerWijze.setAdapter(adInvoerWijze);

        spInvoerWijze.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                WisselInvoerWijze();
                ToonVerbruik();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                WisselInvoerWijze();
                ToonVerbruik();
            }
        });

        Spinner spDagWeekMaandJaarW = findViewById(R.id.spDagWeekMaandJaarW);
        String[] saDagWeekMaandJaarW = getResources().getStringArray(R.array.DagWeekMaandJaar);
        ArrayAdapter<String> adDagWeekMaandJaarW = new ArrayAdapter<>(this, R.layout.spinner_item, saDagWeekMaandJaarW);
        spDagWeekMaandJaarW.setAdapter(adDagWeekMaandJaarW);

        spDagWeekMaandJaarW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ToonVerbruik();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ToonVerbruik();
            }
        });

        Spinner spDagWeekMaandJaarKwh = findViewById(R.id.spDagWeekMaandJaarKwh);
        String[] saDagWeekMaandJaarKwh = getResources().getStringArray(R.array.DagWeekMaandJaar);
        ArrayAdapter<String> adDagWeekMaandJaarKwh = new ArrayAdapter<>(this, R.layout.spinner_item, saDagWeekMaandJaarKwh);
        spDagWeekMaandJaarKwh.setAdapter(adDagWeekMaandJaarKwh);

        spDagWeekMaandJaarKwh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ToonVerbruik();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ToonVerbruik();
            }
        });

        Spinner spCategorie = findViewById(R.id.spCategorie);
        DatabaseHelper dh = DatabaseHelper.getInstance(this);

        List<String> cats = dh.getCategorien();
        String[] array = new String[cats.size()];
        cats.toArray(array);

        ArrayAdapter<String> categorienAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cats);
        spCategorie.setAdapter(categorienAdapter);
        spCategorie.setSelection(categorienAdapter.getPosition("Overig"));

    }

    private void ApparaatToevoegen() {
        Apparaat apparaat = GetApparaatFromControls();
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        dh.addApparaat(apparaat);
    }

    private void ApparaatWijzigen() {
        Apparaat apparaat = GetApparaatFromControls();
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        dh.updateApparaat(apparaat);
    }

    private void WisselInvoerWijze() {
        Spinner spInvoerWijze = findViewById(R.id.spInvoerWijze);
        TextView tvUitleg = findViewById(R.id.tvUitleg);

        String siw = spInvoerWijze.getSelectedItem().toString();
        Helper.InvoerwijzeType invoerWijze = siw.startsWith("Vermogen") ? Helper.InvoerwijzeType.VermogenPerTijd : Helper.InvoerwijzeType.VerbruikPerKeer;

        LinearLayout llVermogen = findViewById(R.id.llVermogen);
        LinearLayout llVerbruik = findViewById(R.id.llVerbruik);
        EditText etApparaat = findViewById(R.id.etApparaat);

        if (invoerWijze == Helper.InvoerwijzeType.VermogenPerTijd) {
            llVermogen.setVisibility(View.VISIBLE);
            llVerbruik.setVisibility(View.GONE);
            etApparaat.setNextFocusDownId(R.id.etVermogen);
            tvUitleg.setText(getString(R.string.Uitleg1));
        } else {
            llVerbruik.setVisibility(View.VISIBLE);
            llVermogen.setVisibility(View.GONE);
            etApparaat.setNextFocusDownId(R.id.etVerbruik);
            tvUitleg.setText(getString(R.string.Uitleg2));
        }
    }

    private void ToonApparaat(Apparaat apparaat) {
        TextView tvAppId = findViewById(R.id.tvAppId);
        EditText etName = findViewById(R.id.etApparaat);
        Spinner spCategorie = findViewById(R.id.spCategorie);
        Spinner spInvoerWijze = findViewById(R.id.spInvoerWijze);
        EditText etVermogen = findViewById(R.id.etVermogen);
        EditText etAantalUur = findViewById(R.id.etAantalUur);
        Spinner spDagWeekMaandJaarW = findViewById(R.id.spDagWeekMaandJaarW);
        EditText etVerbruik = findViewById(R.id.etVerbruik);
        EditText etAantalKeer = findViewById(R.id.etAantalKeer);
        Spinner spDagWeekMaandJaarKwh = findViewById(R.id.spDagWeekMaandJaarKwh);

        int pos;
        tvAppId.setText(Integer.toString(apparaat.getId()));
        etName.setText(apparaat.getName());
        DatabaseHelper dh = DatabaseHelper.getInstance(this);

        Categorie cat = dh.getCategoriebyId(apparaat.getCategorieId());
        @SuppressWarnings("unchecked")
        ArrayAdapter<String> aa = (ArrayAdapter<String>) spCategorie.getAdapter();
        spCategorie.setSelection(aa.getPosition(cat.getName()));

        pos = apparaat.getInvoerWijze() - 1;
        spInvoerWijze.setSelection(pos);

        etVermogen.setText(Integer.toString(apparaat.getVermogen()));

        etAantalUur.setText(Integer.toString(apparaat.getAantalUur()));

        pos = apparaat.getGedurendePer() - 1;
        spDagWeekMaandJaarW.setSelection(pos);

        etVerbruik.setText(Double.toString(apparaat.getVerbruik()));

        etAantalKeer.setText(Integer.toString(apparaat.getAantalKeer()));

        pos = apparaat.getVerbruikPer() - 1;
        spDagWeekMaandJaarKwh.setSelection(pos);
    }

    private Apparaat GetApparaatFromControls() {
        TextView tvAppId = findViewById(R.id.tvAppId);
        EditText etName = findViewById(R.id.etApparaat);
        Spinner spCategorie = findViewById(R.id.spCategorie);
        Spinner spInvoerWijze = findViewById(R.id.spInvoerWijze);
        EditText etVermogen = findViewById(R.id.etVermogen);
        EditText etAantalUur = findViewById(R.id.etAantalUur);
        Spinner spDagWeekMaandJaarW = findViewById(R.id.spDagWeekMaandJaarW);
        EditText etVerbruik = findViewById(R.id.etVerbruik);
        EditText etAantalKeer = findViewById(R.id.etAantalKeer);
        Spinner spDagWeekMaandJaarKwh = findViewById(R.id.spDagWeekMaandJaarKwh);

        String siw = spInvoerWijze.getSelectedItem().toString();
        Helper.InvoerwijzeType iw = siw.startsWith("Vermogen") ? Helper.InvoerwijzeType.VermogenPerTijd : Helper.InvoerwijzeType.VerbruikPerKeer;

        String sVermogen = etVermogen.getText().toString();
        int vermogen = 0;
        if (Helper.tryParseInt(sVermogen)) vermogen = Integer.parseInt(sVermogen);

        String sAantalUur = etAantalUur.getText().toString();
        int aantalUur = 0;
        if (Helper.tryParseInt(sAantalUur)) aantalUur = Integer.parseInt(sAantalUur);

        String sDWMJW = spDagWeekMaandJaarW.getSelectedItem().toString();
        Helper.DagWeekMaandJaarType dwmjW = Helper.DagWeekMaandJaarType.valueOf(sDWMJW);

        String sVerbruik = etVerbruik.getText().toString();
        double verbruik = 0;
        if (Helper.tryParseDouble(sVerbruik)) verbruik = Double.parseDouble(sVerbruik);

        String sAantalKeer = etAantalKeer.getText().toString();
        int aantalKeer = 0;
        if (Helper.tryParseInt(sAantalKeer)) aantalKeer = Integer.parseInt(sAantalKeer);

        String sDWMJKwh = spDagWeekMaandJaarKwh.getSelectedItem().toString();
        Helper.DagWeekMaandJaarType dwmjKwh = Helper.DagWeekMaandJaarType.valueOf(sDWMJKwh);

        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        Categorie cat = dh.getCategoriebyName(spCategorie.getSelectedItem().toString());

        Apparaat apparaat = new Apparaat();
        String sAppId = tvAppId.getText().toString();
        if (!sAppId.isEmpty()) {
            apparaat.setId(Integer.parseInt(sAppId));
        }

        apparaat.setName(etName.getText().toString());
        apparaat.setCategorieId(cat.getID());
        apparaat.setInvoerWijze(iw.getValue());
        apparaat.setVermogen(vermogen);
        apparaat.setAantalUur(aantalUur);
        apparaat.setGedurendePer(dwmjW.getValue());
        apparaat.setVerbruik(verbruik);
        apparaat.setAantalKeer(aantalKeer);
        apparaat.setVerbruikPer(dwmjKwh.getValue());
        return apparaat;
    }

    private void ToonVerbruik() {
        Apparaat apparaat = GetApparaatFromControls();
        double prijs = Helper.GetPrijs(this);

        ApparaatVerbruik apv = Helper.BerekenVerbruik(apparaat, prijs);

        TextView tvr1k2 = findViewById(R.id.tvr1k2);
        TextView tvr1k3 = findViewById(R.id.tvr1k3);
        TextView tvr2k2 = findViewById(R.id.tvr2k2);
        TextView tvr2k3 = findViewById(R.id.tvr2k3);
        TextView tvr3k2 = findViewById(R.id.tvr3k2);
        TextView tvr3k3 = findViewById(R.id.tvr3k3);

        tvr1k2.setText(Helper.getVerbruikString(this, apv.getVerbruikDag()));
        tvr2k2.setText(Helper.getVerbruikString(this, apv.getVerbruikMaand()));
        tvr3k2.setText(Helper.getVerbruikString(this, apv.getVerbruikJaar()));
        tvr1k3.setText(Helper.getEuroString(this, apv.getKostenDag()));
        tvr2k3.setText(Helper.getEuroString(this, apv.getKostenMaand()));
        tvr3k3.setText(Helper.getEuroString(this, apv.getKostenJaar()));
    }

}
