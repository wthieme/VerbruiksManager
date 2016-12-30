package nl.whitedove.verbruiksmanager;

public class Tip {

    int _Id;
    String _Tekst;

    public Tip() {
    }

    public Tip(int id, String tekst) {
        this._Id = id;
        this._Tekst = tekst;
    }

    public int getID() {
        return this._Id;
    }
    public void setID(int id) {
        this._Id = id;
    }

    public String getTekst() {
        return this._Tekst;
    }
    public void setTekst(String tekst) {
        this._Tekst = tekst;
    }
}
