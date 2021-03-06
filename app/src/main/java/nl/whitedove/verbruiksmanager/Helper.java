package nl.whitedove.verbruiksmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

class Helper {
    private static final boolean DEBUG = false;
    static boolean TipGehad = false;
    static String CategorieSelectieMain = "";
    static String SelectieGrafiek = "";
    static String JaarMaandDagSelectie = "";

    enum CatAppType {
        AlleCategorieen,
        AlleApparaten,
        EenCategorie
    }

    enum ActieType {
        toevoegen(1),
        wijzigen(2);

        private int _value;

        ActieType(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        public static ActieType fromInt(int i) {
            for (ActieType b : ActieType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    enum SchermType {
        LijstApparaten(1),
        Grafiek(2);

        private int _value;

        SchermType(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        public static SchermType fromInt(int i) {
            for (SchermType b : SchermType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    enum InvoerwijzeType {
        VermogenPerTijd(1),
        VerbruikPerKeer(2);

        private int _value;

        InvoerwijzeType(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        public static InvoerwijzeType fromInt(int i) {
            for (InvoerwijzeType b : InvoerwijzeType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    enum JaarMaandDagType {
        Jaar(1),
        Maand(2),
        Dag(3);

        private int _value;

        JaarMaandDagType(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        private static JaarMaandDagType fromInt(int i) {
            for (JaarMaandDagType b : JaarMaandDagType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    enum DagWeekMaandJaarType {
        dag(1),
        week(2),
        maand(3),
        jaar(4);

        private int _value;

        DagWeekMaandJaarType(int Value) {
            this._value = Value;
        }

        public int getValue() {
            return _value;
        }

        public static DagWeekMaandJaarType fromInt(int i) {
            for (DagWeekMaandJaarType b : DagWeekMaandJaarType.values()) {
                if (b.getValue() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    static String getEuroString(Context ctx, double kosten) {
        return String.format(Locale.getDefault(), "%s %.2f", ctx.getString(R.string.euro), kosten);
    }

    static String getVerbruikString(Context ctx, double verbruik) {
        if (verbruik > 10000f)
            return String.format(Locale.getDefault(), "%.0f %s", verbruik / 1000f, ctx.getString(R.string.kWh));
        if (verbruik > 1000f)
            return String.format(Locale.getDefault(), "%.1f %s", verbruik / 1000f, ctx.getString(R.string.kWh));
        return String.format(Locale.getDefault(), "%.2f %s", verbruik / 1000f, ctx.getString(R.string.kWh));
    }

    static String getKeerString(Context ctx, Apparaat apparaat) {
        String[] saDagWeekMaandJaarW = ctx.getResources().getStringArray(R.array.DagWeekMaandJaar);
        return String.format(Locale.getDefault(), "%d %s %s",
                apparaat.getAantalKeer(),
                ctx.getString(R.string.keerper),
                saDagWeekMaandJaarW[apparaat.getVerbruikPer() - 1]);
    }

    static String getVermogenString(Context ctx, int vermogen) {
        return String.format(Locale.getDefault(), "%d %s",
                vermogen,
                ctx.getString(R.string.W));
    }

    static String getKeerVermogenString(Context ctx, Apparaat apparaat) {
        String[] saDagWeekMaandJaarW = ctx.getResources().getStringArray(R.array.DagWeekMaandJaar);
        return String.format(Locale.getDefault(), "%d %s %s",
                apparaat.getAantalUur(),
                ctx.getString(R.string.uurper),
                saDagWeekMaandJaarW[apparaat.getGedurendePer() - 1]);
    }


    static double GetPrijs(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String sPrijs = preferences.getString(ctx.getString(R.string.kWhPrijs), "");
        double prijs = 0f;
        if (tryParseDouble(sPrijs))
            prijs = Double.parseDouble(sPrijs);

        return prijs;
    }

    static boolean TipsStaanUit(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean tipsAan = preferences.getBoolean("Tips", false);
        return !tipsAan;
    }

    static boolean tryParseInt(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean tryParseDouble(String value) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static ApparaatVerbruik BerekenVerbruik(Apparaat apparaat, double prijs) {

        Helper.InvoerwijzeType invoerWijze = Helper.InvoerwijzeType.fromInt(apparaat.getInvoerWijze());
        DateTime nu = DateTime.now();
        int dpj = nu.year().isLeap() ? 366 : 365;
        double vpd = 0.0f;
        double av = 0.0f;
        DagWeekMaandJaarType dwmj = DagWeekMaandJaarType.dag;

        if (invoerWijze == InvoerwijzeType.VermogenPerTijd) {
            int vermogen = apparaat.getVermogen();
            int aantalUur = apparaat.getAantalUur();
            dwmj = Helper.DagWeekMaandJaarType.fromInt(apparaat.getGedurendePer());
            av = vermogen * aantalUur;
        } else if (invoerWijze == InvoerwijzeType.VerbruikPerKeer) {
            int aantalKeer = apparaat.getAantalKeer();
            av = 1000.0f * aantalKeer * apparaat.getVerbruik();
            dwmj = Helper.DagWeekMaandJaarType.fromInt(apparaat.getVerbruikPer());
        }

        if (dwmj == Helper.DagWeekMaandJaarType.dag) {
            vpd = av;
        } else if (dwmj == Helper.DagWeekMaandJaarType.week) {
            vpd = av / 7.0f;
        } else if (dwmj == Helper.DagWeekMaandJaarType.maand) {
            vpd = (av * 12.0 / dpj);
        } else if (dwmj == Helper.DagWeekMaandJaarType.jaar) {
            vpd = (av * 1.0f / dpj);
        }

        ApparaatVerbruik apv = new ApparaatVerbruik();
        apv.setId(apparaat.getId());
        apv.setVerbruikDag(vpd);
        apv.setVerbruikMaand(dpj * vpd / 12.0f);
        apv.setVerbruikJaar(dpj * vpd * 1.0f);
        apv.setKostenDag(prijs * vpd / 1000.0f);
        apv.setKostenMaand(prijs * dpj * vpd / 12.0f / 1000.0f);
        apv.setKostenJaar(prijs * dpj * vpd / 1000.0f);
        return apv;
    }

    private static void Log(String log) {
        if (Helper.DEBUG) {
            System.out.println(log);
        }
    }

    static void ShowMessage(Context cxt, String melding) {
        Helper.Log(melding);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(cxt, melding, duration);
        toast.show();
    }

    static void ToonTip(Context ctx) {
        String tip = TipsHelper.GeefTip();
        TipDialoog td = new TipDialoog(ctx, tip);
        td.show();
    }

    static void SortApparaten(List<Apparaat> apparaten) {
        Collections.sort(apparaten, new Helper.ApparaatComparator());
    }

    private static class ApparaatComparator implements Comparator<Apparaat> {
        public int compare(Apparaat left, Apparaat right) {
            ApparaatVerbruik avLeft = Helper.BerekenVerbruik(left, 1f);
            ApparaatVerbruik avRight = Helper.BerekenVerbruik(right, 1f);
            return Double.compare(avRight.getVerbruikJaar(), avLeft.getVerbruikJaar());
        }
    }

}