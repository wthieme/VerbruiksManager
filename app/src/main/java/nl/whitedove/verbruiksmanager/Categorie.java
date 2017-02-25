package nl.whitedove.verbruiksmanager;

class Categorie {

    private int _Id;
    private String _Name;

    Categorie() {
    }

    int getID() {
        return this._Id;
    }

    void setID(int id) {
        this._Id = id;
    }

    String getName() {
        return this._Name;
    }

    void setName(String name) {
        this._Name = name;
    }
}
