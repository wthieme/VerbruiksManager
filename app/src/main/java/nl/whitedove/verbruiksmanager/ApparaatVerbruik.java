package nl.whitedove.verbruiksmanager;

public class ApparaatVerbruik {

    //private variables
    private int _Id;
    private double _VerbruikDag;
    private double _VerbruikMaand;
    private double _VerbruikJaar;
    private double _KostenDag;
    private double _KostenMaand;
    private double _KostenJaar;

    public ApparaatVerbruik() {
    }

    public int getId() {
        return this._Id;
    }

    public void setId(int id) {
        this._Id = id;
    }

    public double getVerbruikDag() {
        return this._VerbruikDag;
    }

    public void setVerbruikDag(double verbruikDag) {
        this._VerbruikDag = verbruikDag;
    }

    public double getVerbruikMaand() {
        return this._VerbruikMaand;
    }

    public void setVerbruikMaand(double verbruikMaand) {
        this._VerbruikMaand = verbruikMaand;
    }

    public double getVerbruikJaar() {
        return this._VerbruikJaar;
    }

    public void setVerbruikJaar(double verbruikJaar) {
        this._VerbruikJaar = verbruikJaar;
    }

    public double getKostenDag() {
        return this._KostenDag;
    }

    public void setKostenDag(double kostenDag) {
        this._KostenDag = kostenDag;
    }

    public double getKostenMaand() {
        return this._KostenMaand;
    }

    public void setKostenMaand(double kostenMaand) {
        this._KostenMaand = kostenMaand;
    }

    public double getKostenJaar() {
        return this._KostenJaar;
    }

    public void setKostenJaar(double kostenJaar) {
        this._KostenJaar = kostenJaar;
    }
}
