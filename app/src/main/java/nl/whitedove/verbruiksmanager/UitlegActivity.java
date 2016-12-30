package nl.whitedove.verbruiksmanager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class UitlegActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uitleg);
        Init();
    }

    private void Init() {
        TextView tvTekstUitleg = (TextView) findViewById(R.id.tvTekstUitleg);
        tvTekstUitleg.setText(Html.fromHtml(getString(R.string.TekstUitleg)));
    }
}
