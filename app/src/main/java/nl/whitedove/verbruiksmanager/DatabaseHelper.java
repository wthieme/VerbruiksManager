package nl.whitedove.verbruiksmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VerbruiksManager";

    private static final String TAB_APPARAAT = "Apparaat";
    private static final String APT_ID = "Id";
    private static final String APT_CAT_ID = "CategorieId";
    private static final String APT_NAME = "Name";
    private static final String APT_INVOERWIJZE = "InvoerWijze";
    private static final String APT_VERMOGEN = "Vermogen";
    private static final String APT_AANTALUUR = "AantalUur";
    private static final String APT_GEDURENDEPER = "GedurendePer";
    private static final String APT_VERBRUIK = "Verbruik";
    private static final String APT_AANTALKEER = "AantalKeer";
    private static final String APT_VERBRUIKPER = "VerbruikPer";

    private static final String TAB_CATEGORIE = "Categorie";
    private static final String CTE_ID = "Id";
    private static final String CTE_NAME = "Name";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TAB_CATEGORIE + "("
                + CTE_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + CTE_NAME + " TEXT NOT NULL"
                + ")";

        db.execSQL(sql);

        List<String> cats = Arrays.asList("Verlichting ", "Televisie en HiFi", "Computers, Tablets, Mobiel en Wifi",
                "Verwarming, Warm water en Airco", "Tuin en Gereedschap", "Keuken (inclusief Koelkast en Diepvries)", "Wassen, Drogen en Strijken", "Overig");

        for (String cat : cats) {
            ContentValues values = new ContentValues();
            values.put(CTE_NAME, cat);
            db.insert(TAB_CATEGORIE, null, values);
        }

        sql = "CREATE TABLE " + TAB_APPARAAT + "("
                + APT_ID + " INTEGER PRIMARY KEY NOT NULL,"
                + APT_NAME + " TEXT NOT NULL,"
                + APT_CAT_ID + " INTEGER NOT NULL,"
                + APT_INVOERWIJZE + " INTEGER,"
                + APT_VERMOGEN + " INTEGER,"
                + APT_AANTALUUR + " INTEGER,"
                + APT_GEDURENDEPER + " INTEGER,"
                + APT_VERBRUIK + " REAL,"
                + APT_AANTALKEER + " INTEGER,"
                + APT_VERBRUIKPER + " INTEGER,"
                + " FOREIGN KEY(" + APT_CAT_ID + ") REFERENCES " + TAB_CATEGORIE + "(" + CTE_ID + ")"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void addApparaat(Apparaat apparaat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APT_NAME, apparaat.getName());
        values.put(APT_CAT_ID, apparaat.getCategorieId());
        values.put(APT_INVOERWIJZE, apparaat.getInvoerWijze());
        values.put(APT_VERMOGEN, apparaat.getVermogen());
        values.put(APT_AANTALUUR, apparaat.getAantalUur());
        values.put(APT_GEDURENDEPER, apparaat.getGedurendePer());
        values.put(APT_VERBRUIK, apparaat.getVerbruik());
        values.put(APT_AANTALKEER, apparaat.getAantalKeer());
        values.put(APT_VERBRUIKPER, apparaat.getVerbruikPer());
        db.insert(TAB_APPARAAT, null, values);
        db.close();
    }

    Apparaat getApparaat(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TAB_APPARAAT, new String[]{APT_ID, APT_NAME, APT_CAT_ID, APT_INVOERWIJZE, APT_VERMOGEN, APT_AANTALUUR, APT_GEDURENDEPER, APT_VERBRUIK, APT_AANTALKEER, APT_VERBRUIKPER},
                APT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Apparaat apparaat = new Apparaat();
        if (cursor != null) {
            cursor.moveToFirst();
            apparaat.setId(cursor.getInt(0));
            apparaat.setName(cursor.getString(1));
            apparaat.setCategorieId(cursor.getInt(2));
            apparaat.setInvoerWijze(cursor.getInt(3));
            apparaat.setVermogen(cursor.getInt(4));
            apparaat.setAantalUur(cursor.getInt(5));
            apparaat.setGedurendePer(cursor.getInt(6));
            apparaat.setVerbruik(cursor.getDouble(7));
            apparaat.setAantalKeer(cursor.getInt(8));
            apparaat.setVerbruikPer(cursor.getInt(9));
            cursor.close();
        }
        return apparaat;
    }

    List<Apparaat> getApparaten(int catId) {
        List<Apparaat> list = new ArrayList<>();
        String selectQuery = "SELECT "
                + APT_ID + ","
                + APT_NAME + ","
                + APT_CAT_ID + ","
                + APT_INVOERWIJZE + ","
                + APT_VERMOGEN + ","
                + APT_AANTALUUR + ","
                + APT_GEDURENDEPER + ","
                + APT_VERBRUIK + ","
                + APT_AANTALKEER + ","
                + APT_VERBRUIKPER
                + " FROM " + TAB_APPARAAT
                + (catId == -1 ? "" : " WHERE " + APT_CAT_ID + " = ?")
                + " ORDER BY " + APT_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if (catId == -1) {
            cursor = db.rawQuery(selectQuery, null);

        } else {
            cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(catId)});
        }

        if (cursor.moveToFirst()) {
            do {
                Apparaat apparaat = new Apparaat();
                apparaat.setId(cursor.getInt(0));
                apparaat.setName(cursor.getString(1));
                apparaat.setCategorieId(cursor.getInt(2));
                apparaat.setInvoerWijze(cursor.getInt(3));
                apparaat.setVermogen(cursor.getInt(4));
                apparaat.setAantalUur(cursor.getInt(5));
                apparaat.setGedurendePer(cursor.getInt(6));
                apparaat.setVerbruik(cursor.getDouble(7));
                apparaat.setAantalKeer(cursor.getInt(8));
                apparaat.setVerbruikPer(cursor.getInt(9));
                list.add(apparaat);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    List<VerbruikStat> getJaarStats(Helper.CatAppType CatApp, int catId) {
        String selectQuery = "SELECT "
                + TAB_APPARAAT + "." + APT_ID + ","
                + TAB_APPARAAT + "." + APT_NAME + ","
                + TAB_APPARAAT + "." + APT_CAT_ID + ","
                + TAB_APPARAAT + "." + APT_INVOERWIJZE + ","
                + TAB_APPARAAT + "." + APT_VERMOGEN + ","
                + TAB_APPARAAT + "." + APT_AANTALUUR + ","
                + TAB_APPARAAT + "." + APT_GEDURENDEPER + ","
                + TAB_APPARAAT + "." + APT_VERBRUIK + ","
                + TAB_APPARAAT + "." + APT_AANTALKEER + ","
                + TAB_APPARAAT + "." + APT_VERBRUIKPER + ","
                + TAB_CATEGORIE + "." + CTE_NAME
                + " FROM " + TAB_APPARAAT
                + " INNER JOIN " + TAB_CATEGORIE + " ON " + TAB_CATEGORIE + "." + CTE_ID + "=" + TAB_APPARAAT + "." + APT_CAT_ID
                + (CatApp == Helper.CatAppType.EenCategorie ? " WHERE " + TAB_CATEGORIE + "." + CTE_ID + " = ?" : "")
                + " ORDER BY " + (CatApp == Helper.CatAppType.AlleCategorieen ? TAB_CATEGORIE + "." + CTE_ID : TAB_APPARAAT + "." + APT_NAME);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        if (CatApp == Helper.CatAppType.EenCategorie) {
            cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(catId)});
        } else {
            cursor = db.rawQuery(selectQuery, null);
        }

        List<VerbruikStat> list = new ArrayList<>();

        String oldcat = "";
        VerbruikStat stat = null;

        if (cursor.moveToFirst()) {
            do {
                Apparaat apparaat = new Apparaat();
                apparaat.setId(cursor.getInt(0));
                apparaat.setName(cursor.getString(1));
                apparaat.setCategorieId(cursor.getInt(2));
                apparaat.setInvoerWijze(cursor.getInt(3));
                apparaat.setVermogen(cursor.getInt(4));
                apparaat.setAantalUur(cursor.getInt(5));
                apparaat.setGedurendePer(cursor.getInt(6));
                apparaat.setVerbruik(cursor.getDouble(7));
                apparaat.setAantalKeer(cursor.getInt(8));
                apparaat.setVerbruikPer(cursor.getInt(9));
                String catOrName = CatApp == Helper.CatAppType.AlleCategorieen ? cursor.getString(10) : cursor.getString(1);
                ApparaatVerbruik av = Helper.BerekenVerbruik(apparaat, 0f);

                if (!oldcat.equals(catOrName)) {
                    if (stat != null) list.add(stat);
                    stat = new VerbruikStat();
                    stat.setName(catOrName);
                    oldcat = catOrName;
                }
                if (stat != null) {
                    stat.setVerbruik(stat.getVerbruik() + av.getVerbruikJaar());
                }

            } while (cursor.moveToNext());

            list.add(stat);
        }
        cursor.close();
        return list;
    }

    void updateApparaat(Apparaat apparaat) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APT_NAME, apparaat.getName());
        values.put(APT_CAT_ID, apparaat.getCategorieId());
        values.put(APT_INVOERWIJZE, apparaat.getInvoerWijze());
        values.put(APT_VERMOGEN, apparaat.getVermogen());
        values.put(APT_AANTALUUR, apparaat.getAantalUur());
        values.put(APT_GEDURENDEPER, apparaat.getGedurendePer());
        values.put(APT_VERBRUIK, apparaat.getVerbruik());
        values.put(APT_AANTALKEER, apparaat.getAantalKeer());
        values.put(APT_VERBRUIKPER, apparaat.getVerbruikPer());

        db.update(TAB_APPARAAT, values, APT_ID + " = ?",
                new String[]{String.valueOf(apparaat.getId())});
        db.close();
    }

    void deleteApparaat(int apparaatId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TAB_APPARAAT, APT_ID + " = ?",
                new String[]{String.valueOf(apparaatId)});
        db.close();
    }

    List<String> getCategorien() {
        List<String> list = new ArrayList<>();
        String selectQuery = "SELECT " + CTE_NAME + " FROM " + TAB_CATEGORIE + " order by " + CTE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    Categorie getCategoriebyName(String cat) {
        String selectQuery = "SELECT " + CTE_ID + "," + CTE_NAME + " FROM " + TAB_CATEGORIE + " WHERE " + CTE_NAME + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{cat});

        Categorie result = new Categorie();
        if (cursor.moveToFirst()) {
            result.setID(cursor.getInt(0));
            result.setName(cursor.getString(1));
        }
        cursor.close();
        return result;
    }

    Categorie getCategoriebyId(int catId) {
        String selectQuery = "SELECT " + CTE_ID + "," + CTE_NAME + " FROM " + TAB_CATEGORIE + " WHERE " + CTE_ID + "=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(catId)});

        Categorie result = new Categorie();
        if (cursor.moveToFirst()) {
            result.setID(cursor.getInt(0));
            result.setName(cursor.getString(1));
        }
        cursor.close();
        return result;
    }

}