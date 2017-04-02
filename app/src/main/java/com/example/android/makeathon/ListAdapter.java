package com.example.android.makeathon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hp pc on 4/2/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    Context context;
    List<Student> dataSource;


    public ListAdapter(Context context, List<Student> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        Student stud = dataSource.get(position);
        holder.studName.setText(stud.name);
        holder.studBranch.setText(stud.branch);
        holder.studEnrol.setText(stud.enrolNo);


    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView studName, studBranch, studEnrol;

        public ViewHolder(View itemView) {
            super(itemView);
            studName = (TextView) itemView.findViewById(R.id.stud_name);
            studBranch = (TextView) itemView.findViewById(R.id.stud_branch);
            studEnrol = (TextView) itemView.findViewById(R.id.stud_enrol);


        }
    }

}
