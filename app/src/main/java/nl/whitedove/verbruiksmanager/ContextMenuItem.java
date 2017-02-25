package nl.whitedove.verbruiksmanager;

import android.graphics.drawable.Drawable;

class ContextMenuItem {

    private Drawable drawable;
    private String text;

    ContextMenuItem(Drawable drawable, String text) {
        super();
        this.drawable = drawable;
        this.text = text;
    }

    Drawable getDrawable() {
        return drawable;
    }

    void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

}