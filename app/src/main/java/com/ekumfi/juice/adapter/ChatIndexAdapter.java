package com.ekumfi.juice.adapter;

/**
 * Created by Nana on 11/10/2017.
 */

import android.app.Activity;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmEkumfiInfo;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.R;

import org.apache.commons.text.StringEscapeUtils;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.realm.Realm;

/**
 * Created by Belal on 6/6/2017.
 */

public class ChatIndexAdapter extends RecyclerView.Adapter<ChatIndexAdapter.ViewHolder> {

    private static final String YOUR_DIALOG_TAG = "";
    ChatIndexAdapterInterface chatIndexAdapterInterface;
    Activity mActivity;
    private ArrayList<RealmChat> realmChats;
    public static final SimpleDateFormat sfd_time = new SimpleDateFormat("h:mm a");

    public ChatIndexAdapter(ChatIndexAdapterInterface chatIndexAdapterInterface, Activity mActivity, ArrayList<RealmChat> realmChats) {
        this.chatIndexAdapterInterface = chatIndexAdapterInterface;
        this.mActivity = mActivity;
        this.realmChats = realmChats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_chat_index, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RealmChat realmChat = realmChats.get(position);

        Realm.init(mActivity);
        Realm.getInstance(RealmUtility.getDefaultConfig(mActivity)).executeTransaction(realm -> {
            String role = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("com.ekumfi.juice" + "ROLE", "");
            if (role.equals("SELLER")) {;
                if (realmChat.getConsumer_id().equals("")) {
                    RealmEkumfiInfo realmEkumfiInfo = Realm.getInstance(RealmUtility.getDefaultConfig(mActivity)).where(RealmEkumfiInfo.class).findFirst();


                    holder.name.setText(realmEkumfiInfo.getName());

                    if (realmEkumfiInfo.getProfile_image_url() != null && !realmEkumfiInfo.getProfile_image_url().equals("")) {
                        Glide.with(mActivity)
                                .load(realmEkumfiInfo.getProfile_image_url()) // image url
                                .apply(new RequestOptions().centerCrop())
                                .into(holder.profilepic);
                    } else {
                        holder.profilepic.setImageDrawable(null);
                    }
                } else {
                    RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(mActivity))
                            .where(RealmCart.class)
                            .equalTo("consumer_id", realmChat.getConsumer_id())
                            .equalTo("seller_id", realmChat.getSeller_id())
                            .findFirst();

                    holder.name.setText(realmCart.getConsumer_name());

                    if (realmCart.getConsumer_profile_image_url() != null && !realmCart.getConsumer_profile_image_url().equals("")) {
                        Glide.with(mActivity)
                                .load(realmCart.getConsumer_profile_image_url()) // image url
                                .apply(new RequestOptions().centerCrop())
                                .into(holder.profilepic);
                    } else {
                        holder.profilepic.setImageDrawable(null);
                    }
                }
            }
            else if (role.equals("CONSUMER")) {
                RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(mActivity))
                        .where(RealmCart.class)
                        .equalTo("consumer_id", realmChat.getConsumer_id())
                        .equalTo("seller_id", realmChat.getSeller_id())
                        .findFirst();

                holder.name.setText(realmCart.getShop_name());

                if (realmCart.getShop_image_url() != null && !realmCart.getShop_image_url().equals("")) {
                    Glide.with(mActivity)
                            .load(realmCart.getShop_image_url()) // image url
                            .apply(new RequestOptions().centerCrop())
                            .into(holder.profilepic);
                } else {
                    holder.profilepic.setImageDrawable(null);
                }
            }
        });

        if (realmChat.getAttachment_type() != null && realmChat.getAttachment_type().equals("map")) {
            holder.message.setText(StringEscapeUtils.unescapeJava("\uD83D\uDCCC") + " Location");
        }
        else if (realmChat.getAttachment_type() != null && realmChat.getAttachment_type().equals("live_location")) {
            holder.message.setText(StringEscapeUtils.unescapeJava("\uD83D\uDCCD") + "Live location");
        }
        else if (realmChat.getAttachment_title() != null && !realmChat.getAttachment_title().equals("")) {
            holder.message.setText(realmChat.getAttachment_title());
        } else {
            if (realmChat.getTag() != null && realmChat.getTag().contains("order_id")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.message.setText(Html.fromHtml("<font color='#228C22'><u>" + realmChat.getText() + "</u></font>", Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.message.setText(Html.fromHtml("<font color='#228C22'><u>" + realmChat.getText() + "</u></font>"));
                }
            } else {
                holder.message.setText(StringEscapeUtils.unescapeJava(realmChat.getText()));
            }
        }

        Date date = null;
        if (!realmChat.getCreated_at().toLowerCase().startsWith("z")) {
            try {
                date = Const.dateTimeFormat.parse(realmChat.getCreated_at());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                date = Const.dateTimeFormat.parse(realmChat.getCreated_at().substring(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        String ago = prettyTime.format(new Date(date.getTime()));
        if (date != null) {
            holder.time.setText(ago);
        } else {
            holder.time.setText("");
        }

        holder.profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatIndexAdapterInterface.onImageClick(realmChats, position, holder);
            }
        });

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatIndexAdapterInterface.onItemClick(realmChats, position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return realmChats.size();
    }

    public interface ChatIndexAdapterInterface {
        void onItemClick(ArrayList<RealmChat> realmChats, int position, ViewHolder holder);
        void onImageClick(ArrayList<RealmChat> realmChats, int position, ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, time, unreadchatscount;
        public EmojiconTextView message;
        public ImageView profilepic;
        public LinearLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            profilepic = itemView.findViewById(R.id.profilepic);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            unreadchatscount = itemView.findViewById(R.id.unreadchatscount);
            item = itemView.findViewById(R.id.item);
        }
    }
}
