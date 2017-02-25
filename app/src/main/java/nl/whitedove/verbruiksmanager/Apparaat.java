package nl.whitedove.verbruiksmanager;

public class Apparaat {

    //private variables
    private int _Id;
    private String _Name;
    private int _CategorieId;
    private int _InvoerWijze;
    private int _Vermogen;
    private int _AantalUur;
    private int _GedurendePer;
    private double _Verbruik;
    private int _AantalKeer;
    private int _VerbruikPer;

    Apparaat() {
    }

    int getId() {
        return this._Id;
    }

    void setId(int id) {
        this._Id = id;
    }

    String getName() {
        return this._Name;
    }

    void setName(String name) {
        this._Name = name;
    }

    int getCategorieId() {
        return this._CategorieId;
    }

    void setCategorieId(int categorieId) {
        this._CategorieId = categorieId;
    }

    int getInvoerWijze() {
        return this._InvoerWijze;
    }

    void setInvoerWijze(int invoerWijze) {
        this._InvoerWijze = invoerWijze;
    }

    int getVermogen() {
        return this._Vermogen;
    }

    void setVermogen(int vermogen) {
        this._Vermogen = vermogen;
    }

    int getAantalUur() {
        return this._AantalUur;
    }

    void setAantalUur(int aantalUur) {
        this._AantalUur = aantalUur;
    }

    int getGedurendePer() {
        return this._GedurendePer;
    }

    void setGedurendePer(int gedurendePer) {
        this._GedurendePer = gedurendePer;
    }

    double getVerbruik() {
        return this._Verbruik;
    }

    void setVerbruik(double verbruik) {
        this._Verbruik = verbruik;
    }

    int getAantalKeer() {
        return this._AantalKeer;
    }

    void setAantalKeer(int aantalKeer) {
        this._AantalKeer = aantalKeer;
    }

    int getVerbruikPer() {
        return this._VerbruikPer;
    }

    void setVerbruikPer(int verbruikPer) {
        this._VerbruikPer = verbruikPer;
    }

}
