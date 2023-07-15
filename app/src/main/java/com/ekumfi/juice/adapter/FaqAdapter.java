package com.ekumfi.juice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ekumfi.juice.realm.RealmFaq;
import com.ekumfi.juice.R;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;


/**
 * Created by Andy-Obeng on 4/3/2018.
 */

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> implements Filterable {
    ArrayList<RealmFaq> realmFaqArrayList;
    private Context mContext;
    //ArrayList<Category> categoryArrayList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_faq, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    public FaqAdapter(ArrayList<RealmFaq> realmFaqArrayList)
    {
        this.realmFaqArrayList = realmFaqArrayList;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final RealmFaq realmFaq = realmFaqArrayList.get(position);
        holder.titletext.setText(StringEscapeUtils.unescapeJava(realmFaq.getTitle()));
        holder.descriptionView.setText(StringEscapeUtils.unescapeJava(realmFaq.getDescription()));

    }

    @Override
    public int getItemCount() {
        return realmFaqArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }
    public void reload(ArrayList<RealmFaq> realmFaqArrayList)
    {
this.realmFaqArrayList=realmFaqArrayList;
notifyDataSetChanged();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView titletext,descriptionView;
        RelativeLayout section_content,section_footer;
        ImageView foldButton;

        public ViewHolder(View view) {
            super(view);
            titletext = view.findViewById(R.id.titletext);
            descriptionView = view.findViewById(R.id.descriptionView);
            section_content = view.findViewById(R.id.section_content);
            section_footer = view.findViewById(R.id.section_footer);
            foldButton = view.findViewById(R.id.foldButton);
            section_content.setVisibility(View.GONE);




            itemView.setOnClickListener(this);


        }
        @Override
        public void onClick(View view) {
          //  Animation animation1 = AnimationUtils.loadAnimation(view.getContext(), R.anim.click);
          //  view.startAnimation(animation1);
            if(section_content.getVisibility()== View.VISIBLE)
            {
                section_content.setVisibility(View.GONE);
                foldButton.setImageResource(R.drawable.sortdown);
                section_footer.setVisibility(View.VISIBLE);
            }
            else
            {
                section_content.setVisibility(View.VISIBLE);
                foldButton.setImageResource(R.drawable.sortup);
                section_footer.setVisibility(View.GONE);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }


    }
    //get bitmap image from byte array

    private Bitmap convertToBitmap(byte[] b){

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }

}

