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
public class ComplaintAdap extends ArrayAdapter<String> {
    ArrayList<String> ComplaintID;
    ArrayList<String> ComplaintNo;

    public ComplaintAdap(Context context, ArrayList<String> complaintID, ArrayList<String> complaintNo) {
        super(context, R.layout.complaintlist, complaintNo);
        ComplaintID = complaintID;
        ComplaintNo = complaintNo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder viewHolder;
        if (convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.complaintlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.complaintNo = (TextView) row.findViewById(R.id.complaintno);
            viewHolder.complaintID = (TextView) row.findViewById(R.id.complaintID);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }
        String compid = ComplaintID.get(position);
        String compno = ComplaintNo.get(position);

        viewHolder.complaintID.setText(compid);
        viewHolder.complaintNo.setText(compno);
        return row;
    }

    public class ViewHolder {
        TextView complaintNo, complaintID;
    }
}
