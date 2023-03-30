package com.example.stackify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SolutionRecyclerViewAdapter extends RecyclerView.Adapter<SolutionRecyclerViewAdapter.MyViewHolder> {
    private RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private List<Solution> solutionList;
    public SolutionRecyclerViewAdapter(Context context, List<Solution> solutionList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.solutionList = solutionList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SolutionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new SolutionRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.solNameTextView.setText(solutionList.get(position).getSolutionName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        holder.solDateTextView.setText(solutionList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return solutionList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView solNameTextView;
        TextView solDateTextView;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            solNameTextView = itemView.findViewById(R.id.solNameTextView);
            solDateTextView = itemView.findViewById(R.id.solDateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
