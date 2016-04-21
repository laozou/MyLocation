package ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import test11.mylocation.R;

/**
 * Created by 123 on 2016/4/1.
 */
public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<FunctionInfo> functionInfoList;

    public ListAdapter(Context context,List<FunctionInfo> list){
        mContext = context;
        functionInfoList = list;
    }

    @Override
    public int getCount() {
        return functionInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return functionInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null){
            LayoutInflater layoutInflater =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.demo_list_info,null);

            holder=new Holder();
            holder.title =(TextView)convertView.findViewById(R.id.title);
            holder.desc = (TextView)convertView.findViewById(R.id.desc);

            convertView.setTag(holder);
        }else{
            holder =(Holder) convertView.getTag();
        }

        holder.title.setText(functionInfoList.get(position).getTitle());
        holder.desc.setText(functionInfoList.get(position).getDescription());

        return convertView;
    }

    public class Holder{
        public TextView title;
        public TextView desc;
    }

}