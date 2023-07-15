package com.ekumfi.juice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.pojo.Participant;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ekumfi.juice.R;

import java.util.ArrayList;


public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ViewHolder> implements Filterable {
    ArrayList<Participant> Participants;
    private Context mContext;

    public ParticipantsAdapter(ArrayList<Participant> Participants) {
        this.Participants = Participants;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_participant, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Participant participant = Participants.get(position);

        holder.name.setText(participant.getName());
        if (participant.getConnected() == 1) {
            holder.status.setText("Online");
            holder.statuscolor.setBackgroundResource(R.drawable.circle_background_green);
        } else {
            holder.status.setText("Offline");
            holder.statuscolor.setBackgroundResource(R.drawable.circle_background_red);
        }

        if (participant.isTeacher()) {
            holder.teacher.setVisibility(View.VISIBLE);
        } else {
            holder.teacher.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(participant.getProfileimgurl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user_icon)).into(holder.profileimg);
    }

    @Override
    public int getItemCount() {
        return Participants.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public void reload(ArrayList<Participant> Participants) {
        this.Participants = Participants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        RelativeLayout parent;
        TextView status, statuscolor, name, teacher, participantno;
        RoundedImageView profileimg;

        public ViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.provider_name);
            status = view.findViewById(R.id.status);
            teacher = view.findViewById(R.id.teacher);
            profileimg = view.findViewById(R.id.profileimg);
            statuscolor = view.findViewById(R.id.statuscolor);
            participantno = view.findViewById(R.id.participantno);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {


        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }


    }


}

