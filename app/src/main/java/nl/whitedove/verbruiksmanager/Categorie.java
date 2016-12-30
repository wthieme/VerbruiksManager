package nl.whitedove.verbruiksmanager;

public class Categorie {

    int _Id;
    String _Name;

    public Categorie() {
    }

    public Categorie(int id, String name) {
        this._Id = id;
        this._Name = name;
    }

    public int getID() {
        return this._Id;
    }
    public void setID(int id) {
        this._Id = id;
    }

    public String getName() {
        return this._Name;
    }
    public void setName(String name) {
        this._Name = name;
    }
}
