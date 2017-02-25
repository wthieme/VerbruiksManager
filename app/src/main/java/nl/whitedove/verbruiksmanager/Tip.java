package nl.whitedove.verbruiksmanager;

class Tip {

    private int _Id;
    private String _Tekst;

    public Tip() {
    }

    Tip(int id, String tekst) {
        this._Id = id;
        this._Tekst = tekst;
    }

    int getID() {
        return this._Id;
    }

    void setID(int id) {
        this._Id = id;
    }

    String getTekst() {
        return this._Tekst;
    }

    void setTekst(String tekst) {
        this._Tekst = tekst;
    }
}
