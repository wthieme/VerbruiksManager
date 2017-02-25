package nl.whitedove.verbruiksmanager;

class VerbruikStat {

    private String _Name;
    private double _Verbruik;

    VerbruikStat() {
    }

    VerbruikStat(String name, int verbruik) {
        this._Name = name;
        this._Verbruik = verbruik;
    }

    double getVerbruik() {
        return this._Verbruik;
    }

    void setVerbruik(double verbruik) {
        this._Verbruik = verbruik;
    }

    String getName() {
        return this._Name;
    }

    void setName(String name) {
        this._Name = name;
    }
}
