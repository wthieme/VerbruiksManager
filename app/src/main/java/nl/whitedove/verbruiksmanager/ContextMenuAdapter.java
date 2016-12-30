package nl.whitedove.verbruiksmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContextMenuAdapter extends BaseAdapter {
    Context context;
    List<ContextMenuItem> listContextMenuItems;
    LayoutInflater inflater;

    public ContextMenuAdapter(Context context,
                              List<ContextMenuItem> listContextMenuItems) {
        super();
        this.context = context;
        this.listContextMenuItems = listContextMenuItems;
    }

    static class ViewHolder {
        protected ImageView imageView;
        protected TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.context_menu_item, parent,
                    false);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView_menu);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.textView_menu);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageDrawable(listContextMenuItems
                .get(position).getDrawable());
        viewHolder.textView.setText(listContextMenuItems.get(position)
                .getText());
        return convertView;

    }

    @Override
    public int getCount() {
        return listContextMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}