package nl.whitedove.verbruiksmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TipDialoog extends Dialog implements
        android.view.View.OnClickListener {

    Context context;
    String tipTekst;

    public TipDialoog(Context ctx, String tip) {
        super(ctx);
        this.context = ctx;
        this.tipTekst = tip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tip_dialoog);
        Button btOk = (Button) findViewById(R.id.btOk);
        btOk.setOnClickListener(this);
        TextView tvTip = (TextView) findViewById(R.id.tvTip);
        tvTip.setText(tipTekst);
        CheckBox ckTip = (CheckBox) findViewById(R.id.ckTip);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean tipAan = preferences.getBoolean("Tips", false);
        ckTip.setChecked(tipAan);
    }

    @Override
    public void onClick(View v) {
        CheckBox ckTip = (CheckBox) findViewById(R.id.ckTip);
        Boolean tipAan = ckTip.isChecked();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Tips", tipAan);
        editor.commit();
        dismiss();
    }
}

