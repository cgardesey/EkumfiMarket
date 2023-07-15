package com.ekumfi.juice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.realm.RealmService;
import com.ekumfi.juice.realm.RealmServiceImage;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.R;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Nana on 9/11/2017.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    ServiceAdapterInterface serviceAdapterInterface;
    ArrayList<RealmService> realmServices;
    private Context mContext;

    public ServiceAdapter(ServiceAdapterInterface serviceAdapterInterface, ArrayList<RealmService> realmServices) {
        this.serviceAdapterInterface = serviceAdapterInterface;
        this.realmServices = realmServices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_service, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RealmService realmService = realmServices.get(position);

        holder.servicecategory.setText(realmService.getService_category());
        holder.name.setText(realmService.getName());
        holder.min_charge_amount.setText(String.format("%.2f", realmService.getMin_charge_amount()));
        holder.max_charge_amount.setText(String.format("%.2f", realmService.getMax_charge_amount()));

        String service_id = realmService.getService_id();
        final RealmServiceImage[] realmServiceImage = new RealmServiceImage[1];
        Realm.init(mContext);
        Realm.getInstance(RealmUtility.getDefaultConfig(mContext)).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmServiceImage[0] = realm.where(RealmServiceImage.class)
                        .equalTo("service_id", service_id)
                        .equalTo("featured_image", 1)
                        .findFirst();
            }
        });

        if (realmServiceImage[0] != null) {
            holder.featured_image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(realmServiceImage[0].getUrl())
                    .into(holder.featured_image);
        }
        else {
            holder.featured_image.setVisibility(View.INVISIBLE);
        }

        mContext = holder.more_details.getContext();

        holder.more_details.setOnClickListener(view -> {
            serviceAdapterInterface.onListItemClick(realmServices, position, holder);
        });
    }

    @Override
    public int getItemCount() {
        return realmServices.size();
    }

    public interface ServiceAdapterInterface {
        void onListItemClick(ArrayList<RealmService> realmServices, int position, ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView servicecategory, name, min_charge_amount, max_charge_amount;
        public ImageView more_details, featured_image;

        public ViewHolder(View view) {
            super(view);
            servicecategory = view.findViewById(R.id.servicecategory);
            name = view.findViewById(R.id.provider_name);
            min_charge_amount = view.findViewById(R.id.min_charge_amount);
            max_charge_amount = view.findViewById(R.id.max_charge_amount);
            more_details = view.findViewById(R.id.more_details);
            featured_image = view.findViewById(R.id.featured_image);
        }
    }
}
