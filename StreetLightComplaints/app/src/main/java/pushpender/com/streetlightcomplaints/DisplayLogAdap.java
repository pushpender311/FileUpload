package pushpender.com.streetlightcomplaints;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pushpender Bhandari on 2/12/2016.
 */
public class DisplayLogAdap extends ArrayAdapter<String> {
    ArrayList<String> FieldUpdate;
    ArrayList<String> Remark;
    ArrayList<String> EnterBy;
    ArrayList<String> EntryTime;

    public DisplayLogAdap(Context context, ArrayList<String> fieldUpdate, ArrayList<String> remark, ArrayList<String> enterBy, ArrayList<String> entryTime) {
        super(context, R.layout.displayloglist, fieldUpdate);
        FieldUpdate = fieldUpdate;
        Remark = remark;
        EnterBy = enterBy;
        EntryTime = entryTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;
        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.displayloglist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtFieldUpdate = (TextView) row.findViewById(R.id.txtField);
            viewHolder.txtRemark = (TextView) row.findViewById(R.id.txtRemark);
            viewHolder.txtEnterBy = (TextView) row.findViewById(R.id.txtEnterBy);
            viewHolder.txtEntryTime = (TextView) row.findViewById(R.id.txtEnterTime);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }
        String fUp = FieldUpdate.get(position);
        String Rem = Remark.get(position);
        String eBy = EnterBy.get(position);
        String eTime = EntryTime.get(position);

        viewHolder.txtFieldUpdate.setText(fUp);
        viewHolder.txtRemark.setText(Rem);
        viewHolder.txtEnterBy.setText(eBy);
        viewHolder.txtEntryTime.setText(eTime);
        return row;
    }

    public class ViewHolder {
        TextView txtFieldUpdate, txtRemark, txtEnterBy, txtEntryTime;
    }
}
