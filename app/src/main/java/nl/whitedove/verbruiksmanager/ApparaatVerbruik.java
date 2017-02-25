package nl.whitedove.verbruiksmanager;

class ApparaatVerbruik {

    //private variables
    private int _Id;
    private double _VerbruikDag;
    private double _VerbruikMaand;
    private double _VerbruikJaar;
    private double _KostenDag;
    private double _KostenMaand;
    private double _KostenJaar;

    ApparaatVerbruik() {
    }

    int getId() {
        return this._Id;
    }

    void setId(int id) {
        this._Id = id;
    }

    double getVerbruikDag() {
        return this._VerbruikDag;
    }

    void setVerbruikDag(double verbruikDag) {
        this._VerbruikDag = verbruikDag;
    }

    double getVerbruikMaand() {
        return this._VerbruikMaand;
    }

    void setVerbruikMaand(double verbruikMaand) {
        this._VerbruikMaand = verbruikMaand;
    }

    double getVerbruikJaar() {
        return this._VerbruikJaar;
    }

    void setVerbruikJaar(double verbruikJaar) {
        this._VerbruikJaar = verbruikJaar;
    }

    double getKostenDag() {
        return this._KostenDag;
    }

    void setKostenDag(double kostenDag) {
        this._KostenDag = kostenDag;
    }

    double getKostenMaand() {
        return this._KostenMaand;
    }

    void setKostenMaand(double kostenMaand) {
        this._KostenMaand = kostenMaand;
    }

    double getKostenJaar() {
        return this._KostenJaar;
    }

    void setKostenJaar(double kostenJaar) {
        this._KostenJaar = kostenJaar;
    }
}
