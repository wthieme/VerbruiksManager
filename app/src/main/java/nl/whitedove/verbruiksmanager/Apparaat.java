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

    public Apparaat() {
    }

    public int getId() {
        return this._Id;
    }

    public void setId(int id) {
        this._Id = id;
    }

    public String getName() {
        return this._Name;
    }

    public void setName(String name) {
        this._Name = name;
    }

    public int getCategorieId() {
        return this._CategorieId;
    }

    public void setCategorieId(int categorieId) {
        this._CategorieId = categorieId;
    }

    public int getInvoerWijze() {
        return this._InvoerWijze;
    }

    public void setInvoerWijze(int invoerWijze) {
        this._InvoerWijze = invoerWijze;
    }

    public int getVermogen() {
        return this._Vermogen;
    }

    public void setVermogen(int vermogen) {
        this._Vermogen = vermogen;
    }

    public int getAantalUur() {
        return this._AantalUur;
    }

    public void setAantalUur(int aantalUur) {
        this._AantalUur = aantalUur;
    }

    public int getGedurendePer() {
        return this._GedurendePer;
    }

    public void setGedurendePer(int gedurendePer) {
        this._GedurendePer = gedurendePer;
    }

    public double getVerbruik() {
        return this._Verbruik;
    }

    public void setVerbruik(double verbruik) {
        this._Verbruik = verbruik;
    }

    public int getAantalKeer() {
        return this._AantalKeer;
    }

    public void setAantalKeer(int aantalKeer) {
        this._AantalKeer = aantalKeer;
    }

    public int getVerbruikPer() {
        return this._VerbruikPer;
    }

    public void setVerbruikPer(int verbruikPer) {
        this._VerbruikPer = verbruikPer;
    }

}
