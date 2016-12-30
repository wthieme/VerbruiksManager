package nl.whitedove.verbruiksmanager;

public class VerbruikStat {

    String _Name;
    double _Verbruik;

    public VerbruikStat() {
    }

    public VerbruikStat(String name, int verbruik) {
        this._Name = name;
        this._Verbruik = verbruik;
    }

    public double getVerbruik() {
        return this._Verbruik;
    }

    public void setVerbruik(double verbruik) {
        this._Verbruik = verbruik;
    }

    public String getName() {
        return this._Name;
    }

    public void setName(String name) {
        this._Name = name;
    }
}
