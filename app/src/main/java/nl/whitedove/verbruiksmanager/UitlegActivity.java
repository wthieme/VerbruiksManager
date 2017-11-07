package nl.whitedove.verbruiksmanager;

import android.app.Activity;
import android.os.Build;
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
        TextView tvTekstUitleg = findViewById(R.id.tvTekstUitleg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            tvTekstUitleg.setText(Html.fromHtml(getString(R.string.TekstUitleg), Html.FROM_HTML_MODE_LEGACY));
        else
            //noinspection deprecation
            tvTekstUitleg.setText(Html.fromHtml(getString(R.string.TekstUitleg)));
    }
}
