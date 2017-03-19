package nl.whitedove.verbruiksmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class CustomListAdapterApparaten extends BaseAdapter {

    private List<Apparaat> listData;
    private LayoutInflater layoutInflater;
    private Helper.JaarMaandDagType jmd;
    private Context context;

    CustomListAdapterApparaten(Context context, List<Apparaat> listData, Helper.JaarMaandDagType jmd) {
        this.listData = listData;
        Helper.SortApparaten(listData);
        this.jmd = jmd;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.apparaten_list_layout, parent, false);
            holder = new ViewHolder();
            holder.tvApparaatId = (TextView) convertView.findViewById(R.id.tvApparaatId);
            holder.tvApparaatNaam = (TextView) convertView.findViewById(R.id.tvApparaatNaam);
            holder.tvApparaatVerbruik = (TextView) convertView.findViewById(R.id.tvApparaatVerbruik);
            holder.tvApparaatKosten = (TextView) convertView.findViewById(R.id.tvApparaatKosten);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Apparaat apparaat = listData.get(position);
        ApparaatVerbruik av = Helper.BerekenVerbruik(apparaat, Helper.GetPrijs(context));
        holder.tvApparaatId.setText(Integer.toString(apparaat.getId()));
        holder.tvApparaatNaam.setText(apparaat.getName());
        if (jmd == Helper.JaarMaandDagType.Jaar) {
            holder.tvApparaatVerbruik.setText(Helper.getVerbruikString(av.getVerbruikJaar()));
            holder.tvApparaatKosten.setText(Helper.getEuroString(av.getKostenJaar()));
        } else if (jmd == Helper.JaarMaandDagType.Maand) {
            holder.tvApparaatVerbruik.setText(Helper.getVerbruikString(av.getVerbruikMaand()));
            holder.tvApparaatKosten.setText(Helper.getEuroString(av.getKostenMaand()));
        } else if (jmd == Helper.JaarMaandDagType.Dag) {
            holder.tvApparaatVerbruik.setText(Helper.getVerbruikString(av.getVerbruikDag()));
            holder.tvApparaatKosten.setText(Helper.getEuroString(av.getKostenDag()));
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvApparaatNaam;
        TextView tvApparaatVerbruik;
        TextView tvApparaatKosten;
        TextView tvApparaatId;
    }
}
