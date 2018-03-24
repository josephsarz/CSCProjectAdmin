package com.codegene.femicodes.cscprojectadmin.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codegene.femicodes.cscprojectadmin.R;
import com.codegene.femicodes.cscprojectadmin.models.Report;
import com.codegene.femicodes.cscprojectadmin.utils.Constants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportFragment extends Fragment {


    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        //initialize recyclerview and FIrebase objects
        recyclerView = view.findViewById(R.id.report_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.REFERENCE_CHILD_REPORTS);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reports");
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Report, ReportViewHolder> FBRA = new FirebaseRecyclerAdapter<Report, ReportViewHolder>(
                Report.class,
                R.layout.item_report,
                ReportViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(ReportViewHolder viewHolder, Report model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setComplainDetails(model.getComplainDetails());
                viewHolder.setComplaintName(model.getComplaintName());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(getContext(), post_key, Toast.LENGTH_LONG).show();

                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ReportViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComplainDetails(String complainDetails) {
            TextView reportDetails = mView.findViewById(R.id.report_details);
            reportDetails.setText(complainDetails);
        }

        public void setComplaintName(String complaintName){
            TextView name = mView.findViewById(R.id.reporter_name);
            name.setText(complaintName);
        }

    }

}
