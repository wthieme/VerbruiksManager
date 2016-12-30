package nl.whitedove.verbruiksmanager;

public class ApparaatSoort {

    //private variables
    private int _Id;
    private String _Name;
    private int _Verbruik;

    public ApparaatSoort() {
    }

    public ApparaatSoort(int id, String name, int verbruik) {
        this._Id = id;
        this._Name = name;
        this._Verbruik = verbruik;
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

    public int getVerbruik() {
        return this._Verbruik;
    }

    public void setVerbruik(int verbruik) {
        this._Verbruik = verbruik;
    }

}
