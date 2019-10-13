package nl.whitedove.verbruiksmanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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
        Spinner spJaarMaandDag = findViewById(R.id.spJaarMaandDag);
        String[] saJaarMaandDag = getResources().getStringArray(R.array.JaarMaandDag);
        ArrayAdapter<String> jaarMaandDagAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, saJaarMaandDag);
        spJaarMaandDag.setAdapter(jaarMaandDagAdapter);
        if (!Helper.JaarMaandDagSelectie.isEmpty()) {
            @SuppressWarnings("unchecked")
            ArrayAdapter<String> aa = (ArrayAdapter<String>) spJaarMaandDag.getAdapter();
            spJaarMaandDag.setSelection(aa.getPosition(Helper.JaarMaandDagSelectie));
        }

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

        Spinner spCat = findViewById(R.id.spCat);
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        List<String> cats = dh.getCategorien();
        cats.add(0, getResources().getString(R.string.AlleApps));
        String[] array = new String[cats.size()];
        cats.toArray(array);
        ArrayAdapter<String> categorienAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cats);
        spCat.setAdapter(categorienAdapter);

        if (!Helper.CategorieSelectieMain.isEmpty()) {
            @SuppressWarnings("unchecked")
            ArrayAdapter<String> aa = (ArrayAdapter<String>) spCat.getAdapter();
            spCat.setSelection(aa.getPosition(Helper.CategorieSelectieMain));
        }

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
        TipVanDeDagBijStart();
        InitSpinners();

        FloatingActionButton fabGrafiek = findViewById(R.id.fabGrafiek);
        fabGrafiek.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabGrafiek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Grafiek();
            }
        });

        FloatingActionButton fabAppVoegtoe = findViewById(R.id.fabAppVoegtoe);
        fabAppVoegtoe.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabAppVoegtoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApparaatInvoeren();
            }
        });

        FloatingActionButton fabPdf = findViewById(R.id.fabPdf);
        fabPdf.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorBackgroundFab)));
        fabPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaakPdf();
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

        final ListView lvApparaten = findViewById(R.id.lvApparaten);
        Spinner spJaarMaandDag = findViewById(R.id.spJaarMaandDag);
        Spinner spCat = findViewById(R.id.spCat);
        String sCat = spCat.getSelectedItem().toString();
        Helper.CategorieSelectieMain = sCat;
        int catId = -1;
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        if (!sCat.equals(getResources().getString(R.string.AlleApps))) {
            Categorie cat = dh.getCategoriebyName(sCat);
            catId = cat.getID();
        }

        List<Apparaat> apparaten = dh.getApparaten(catId);
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

        TextView tvr1k2 = findViewById(R.id.tvr1k2);
        TextView tvr1k3 = findViewById(R.id.tvr1k3);
        TextView tvr2k2 = findViewById(R.id.tvr2k2);
        TextView tvr2k3 = findViewById(R.id.tvr2k3);
        TextView tvr3k2 = findViewById(R.id.tvr3k2);
        TextView tvr3k3 = findViewById(R.id.tvr3k3);

        tvr1k2.setText(Helper.getVerbruikString(this, VerbruikDag));
        tvr2k2.setText(Helper.getVerbruikString(this, VerbruikMaand));
        tvr3k2.setText(Helper.getVerbruikString(this, VerbruikJaar));
        tvr1k3.setText(Helper.getEuroString(this, KostenDag));
        tvr2k3.setText(Helper.getEuroString(this, KostenMaand));
        tvr3k3.setText(Helper.getEuroString(this, KostenJaar));
    }

    private void MaakPdf() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            PdfDocument document = new PdfDocument();

            // A4 page info
            // For testing
            // int pageHeight = 200;
            int pageHeight = 842;
            int pageWidth = 595;

            int pageNum = 1;
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            canvas.drawColor(ContextCompat.getColor(this, R.color.colorGroenAchtergrond));

            int margeTop = 50;
            int h = margeTop;
            int offsetLijn = 18;
            int marge = 10;
            int kolomVerbruikPer = 220;
            int kolomKeer = 300;
            int kolomVerbruik = 440;
            int kolomKosten = 515;
            int kolomPagenum = 560;

            Paint paint = new Paint();
            paint.setTextSize(24);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setColor(ContextCompat.getColor(this, R.color.colorTekst));
            canvas.drawText(String.format("%s %s", getString(R.string.app_name), getString(R.string.VerbruikEnKostenPerJaar)), marge, h, paint);
            paint.setTextSize(14);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(String.format(Locale.getDefault(), "%d", pageNum), kolomPagenum, h, paint);
            DatabaseHelper dh = DatabaseHelper.getInstance(this);
            List<Apparaat> apparaten = dh.getApparaten(-1);
            Helper.SortApparaten(apparaten);
            double prijs = Helper.GetPrijs(this);
            double VerbruikJaar = 0f;
            double KostenJaar = 0f;

            h = 90;
            pageNum = 2;

            for (Apparaat apparaat : apparaten) {
                ApparaatVerbruik apv = Helper.BerekenVerbruik(apparaat, prijs);
                VerbruikJaar += apv.getVerbruikJaar();
                KostenJaar += apv.getKostenJaar();

                paint.setColor(ContextCompat.getColor(this, R.color.colorLijn));
                canvas.drawLine(marge, h - offsetLijn, canvas.getWidth() - 2 * marge, h - offsetLijn, paint);

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setColor(ContextCompat.getColor(this, R.color.colorTekst));
                canvas.drawText(apparaat.getName(), marge, h, paint);

                Helper.InvoerwijzeType invoerWijze = Helper.InvoerwijzeType.fromInt(apparaat.getInvoerWijze());

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                if (invoerWijze == Helper.InvoerwijzeType.VermogenPerTijd) {
                    String verbruikPer = Helper.getVermogenString(this, apparaat.getVermogen());
                    canvas.drawText(verbruikPer, kolomVerbruikPer, h, paint);
                    String keer = Helper.getKeerVermogenString(this, apparaat);
                    canvas.drawText(keer, kolomKeer, h, paint);
                } else {
                    String verbruik = Helper.getVerbruikString(this, 1000.0f * apparaat.getVerbruik());
                    canvas.drawText(verbruik, kolomVerbruikPer, h, paint);
                    String keer = Helper.getKeerString(this, apparaat);
                    canvas.drawText(keer, kolomKeer, h, paint);
                }

                paint.setColor(ContextCompat.getColor(this, R.color.colorOranje));
                canvas.drawText(Helper.getVerbruikString(this, apv.getVerbruikJaar()), kolomVerbruik, h, paint);
                canvas.drawText(Helper.getEuroString(this, apv.getKostenJaar()), kolomKosten, h, paint);

                if (h + margeTop > pageHeight) {
                    document.finishPage(page);
                    pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    canvas.drawColor(ContextCompat.getColor(this, R.color.colorGroenAchtergrond));
                    h = margeTop;

                    paint.setTextSize(24);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setColor(ContextCompat.getColor(this, R.color.colorTekst));
                    canvas.drawText(String.format("%s %s", getString(R.string.app_name), getString(R.string.VerbruikEnKostenPerJaar)), marge, h, paint);
                    paint.setTextSize(14);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    canvas.drawText(String.format(Locale.getDefault(), "%d", pageNum), kolomPagenum, h, paint);
                    pageNum++;
                    h = 90;
                }
                h += 25;
            }
            paint.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            canvas.drawLine(marge, h - offsetLijn, canvas.getWidth() - 2 * marge, h - offsetLijn, paint);

            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setColor(ContextCompat.getColor(this, R.color.colorTekst));
            canvas.drawText(getString(R.string.Totaal), marge, h, paint);

            paint.setColor(ContextCompat.getColor(this, R.color.colorOranje));
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            canvas.drawText(Helper.getVerbruikString(this, VerbruikJaar), kolomVerbruik, h, paint);
            canvas.drawText(Helper.getEuroString(this, KostenJaar), kolomKosten, h, paint);

            document.finishPage(page);

            // Schrijf PDF naar file en open hem vervolgens
            String pdfName = getString(R.string.app_name) + ".pdf";
            String pad = getDirectory(this);
            File outputFile = new File(pad, pdfName);

            try {
                //noinspection ResultOfMethodCallIgnored
                outputFile.createNewFile();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
            } catch (IOException e) {
                Helper.ShowMessage(this, getString(R.string.ErrorPdf));
                return;
            }

            File file = new File(pad, pdfName);
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Helper.ShowMessage(this, getString(R.string.NoPdfReader));
            }
        } else
            Helper.ShowMessage(this, getString(R.string.PdfKitKat));
    }

    private static String getDirectory(Context cxt) {
        String state = Environment.getExternalStorageState();
        // Return external storage folder
        if (Environment.MEDIA_MOUNTED.equals(state))
            //noinspection ConstantConditions
            return cxt.getExternalFilesDir(null).getAbsolutePath();
        // Return internal storage folder
        return cxt.getFilesDir().getAbsolutePath();
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
        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        dh.deleteApparaat(AppId);
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
        assert inflater != null;
        child = inflater.inflate(R.layout.listview_context_menu, null);
        listView = child.findViewById(R.id.listView_context_menu);

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
