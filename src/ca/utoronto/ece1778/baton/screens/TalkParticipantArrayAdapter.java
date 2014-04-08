package ca.utoronto.ece1778.baton.screens;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.classmanage.ClassParticipate;

public class TalkParticipantArrayAdapter extends ArrayAdapter<ClassParticipate> {

	Context context;
	int layoutResourceId;
	List<ClassParticipate> data = null;

	public TalkParticipantArrayAdapter(Context context, int resource, List<ClassParticipate> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutResourceId = resource;
		this.data = objects;
	}
	
	public void resetDataList(List<ClassParticipate> newDataList)
	{
		data=newDataList;
		this.notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return data.size();
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TalkBuddyRowHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new TalkBuddyRowHolder();
            holder.txtName = (TextView)row.findViewById(R.id.talk_txtBuddyName);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TalkBuddyRowHolder)row.getTag();
        }
        
        ClassParticipate cp = data.get(position);
        holder.txtName.setText(cp.getStudent().getF_name()+" "+cp.getStudent().getL_name()) ;
        Typeface tf = Typeface.createFromAsset(((Activity)context).getAssets(), Constants.TYPEFACE_COMIC_RELIEF);
        holder.txtName.setTypeface(tf);
        holder.uid = cp.getStudent().getUid();
        
        if(null!=cp.getCurTicketList()&&!cp.getCurTicketList().isEmpty())
        {
        	holder.txtName.setBackgroundColor(Color.GREEN);
        	holder.txtName.setTextColor(Color.BLACK);
        }
        
        return row;
	}
	
	public static class TalkBuddyRowHolder {
		TextView txtName; // student name
		int uid;
	}

}
