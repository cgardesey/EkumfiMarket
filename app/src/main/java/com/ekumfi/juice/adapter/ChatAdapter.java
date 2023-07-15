package com.ekumfi.juice.adapter;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.MessageActivity.*;
import static com.ekumfi.juice.activity.MessageActivity.RC_OPEN_DOCUMENT;
import static com.ekumfi.juice.constants.Const.getMimeType;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.activity.LiveLocationActivity;
import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.activity.PictureActivity;
import com.ekumfi.juice.activity.StockCartItemsActivity;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.pojo.DateItem;
import com.ekumfi.juice.realm.RealmCartProduct;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmStockCartProduct;
import com.ekumfi.juice.util.DownloadFileAsync;
import com.ekumfi.juice.util.PixelUtil;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.activity.CartItemsActivity;
import com.shockwave.pdfium.PdfiumCore;
import com.ekumfi.juice.R;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;


/**
 * Created by 2CLearning on 2/8/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ChatAdapter";
    public ArrayList<Object> selected_usersList = new ArrayList<>();
    public ArrayList<Object> consolidatedList;
    String NAME;
    String PROFILE_IMAGE_URL;
    Activity activity;
    String chatid = null;
    public static HashMap<String, AsyncTask<String, Integer, String>> downFileAsyncMap = new HashMap<>();
    public static final SimpleDateFormat sfd_time = new SimpleDateFormat("h:mm a");
    public static boolean isMultiSelect = false;

    public static final int TYPE_DATE = 200;
    public static final int TYPE_REALM_CHAT = 100;

    ChatAdapter.ChatAdapterInterface chatAdapterInterface;
    public ChatAdapter(ChatAdapter.ChatAdapterInterface chatAdapterInterface, ArrayList<Object> consolidatedList, Activity activity, String NAME, String PROFILE_IMAGE_URL) {
        this.chatAdapterInterface = chatAdapterInterface;
        this.consolidatedList = consolidatedList;
        this.activity = activity;
        this.NAME = NAME;
        this.PROFILE_IMAGE_URL = PROFILE_IMAGE_URL;
    }

    public static String getDocType(String mimeType) {

        String type = "";

        List<String> word = new ArrayList<String>();
        List<String> excel = new ArrayList<String>();

        List<String> powerpoint = new ArrayList<String>();
        List<String> pdf = new ArrayList<String>();
        List<String> text = new ArrayList<String>();

        word.add("application/msword");
        word.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        word.add("application/vnd.openxmlformats-officedocument.wordprocessingml.template");

        excel.add("application/vnd.ms-excel");
        excel.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        excel.add("application/vnd.openxmlformats-officedocument.spreadsheetml.template");

        powerpoint.add("application/vnd.ms-powerpoint");
        powerpoint.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        powerpoint.add("application/vnd.openxmlformats-officedocument.presentationml.template");
        powerpoint.add("application/vnd.openxmlformats-officedocument.presentationml.slideshow");

        pdf.add("application/pdf");

        text.add("text/plain");

        if (word.contains(mimeType)) {
            type = "word";
        } else if (excel.contains(mimeType)) {
            type = "excel";
        } else if (powerpoint.contains(mimeType)) {
            type = "powerpoint";
        } else if (pdf.contains(mimeType)) {
            type = "pdf";
        } else if (text.contains(mimeType)) {
            type = "text";
        }
        return type;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (consolidatedList.get(position) instanceof DateItem) {
            viewType = TYPE_DATE;
        } else if (consolidatedList.get(position) instanceof RealmChat) {
            viewType = TYPE_REALM_CHAT;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case TYPE_REALM_CHAT:
                View v1 = inflater.inflate(R.layout.recycle_chat, parent, false);
                viewHolder = new MessageViewHolder(v1, activity);
                break;

            case TYPE_DATE:
                View v2 = inflater.inflate(R.layout.recycle_date, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        switch (viewHolder.getItemViewType()) {

            case TYPE_REALM_CHAT:
                final RealmChat realmChat = (RealmChat) consolidatedList.get(position);
                final MessageViewHolder holder = (MessageViewHolder) viewHolder;
                Date date = null;
                chatid = realmChat.getChat_id();
                if (realmChat.getCreated_at() != null && !realmChat.getCreated_at().toLowerCase().startsWith("z")) {
                    try {
                        date = Const.dateTimeFormat.parse(realmChat.getCreated_at());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                final boolean messageIsMine;
                if (realmChat.getSender_role().equals("CUSTOMER")) {
                    messageIsMine = android.preference.PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? true : false;
                } else {
                    messageIsMine = android.preference.PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "").equals("SELLER") ? true : false;
                }



                holder.name.setText(realmChat.getName());
                if (date != null) {
                    holder.time.setText(sfd_time.format(date));
                } else {
                    holder.time.setText("");
                }

                final String attachmenturl = realmChat.getAttachment_url();
                if (messageIsMine) {
                    holder.identifierLayout.setVisibility(View.GONE);

                    holder.layout.setBackgroundResource(R.drawable.bubble_in);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.layout.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    params.setMarginStart(PixelUtil.dpToPx(activity, 64));
                    params.setMarginEnd(PixelUtil.dpToPx(activity, 0));
//                    params.addRule(RelativeLayout.LayoutParams.WRAP_CONTENT);
                    holder.layout.setLayoutParams(params);
                    holder.identifierLayout.setMinimumWidth(PixelUtil.dpToPx(activity, 0));

                    holder.statusImg.setVisibility(View.VISIBLE);
                    Bitmap bitmap;
                    if (realmChat.getCreated_at() == null || realmChat.getCreated_at().toLowerCase().startsWith("z")) {
                        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_timer_round);

                    } else {
                        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_action_tick);
                    }
                    holder.statusImg.setImageBitmap(bitmap);
                } else {
                    holder.identifierLayout.setVisibility(View.GONE);
                    holder.name.setText(realmChat.getName());

                    holder.layout.setBackgroundResource(R.drawable.bubble_out);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.layout.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                    params.setMarginEnd(PixelUtil.dpToPx(activity, 64));
                    params.setMarginStart(PixelUtil.dpToPx(activity, 0));
//                    params.addRule(RelativeLayout.LayoutParams.WRAP_CONTENT);
                    holder.layout.setLayoutParams(params);
                    /*if (realmChat.isInstructor()) {
                        holder.identifierLayout.setMinimumWidth(dpToPx(conferenceCallActivity, 144));
                    } else {
                        holder.identifierLayout.setMinimumWidth(dpToPx(conferenceCallActivity, 0));
                    }*/

                    holder.statusImg.setVisibility(View.GONE);
                }

                if (realmChat.isInstructor()) {
                    holder.instructor.setVisibility(View.VISIBLE);
                } else {
                    holder.instructor.setVisibility(View.INVISIBLE);
                }

                final String sub_folder = messageIsMine ? "Sent" : "Received";
                String folder = null;
                String lastpathseg = null;

                File file = null;

                String imgLoc = null;
                String vidLoc = null;
                String audioLoc;
                if (realmChat.getText() != null) {
                    holder.metaData.setVisibility(View.GONE);
                    holder.docFrame.setVisibility(View.GONE);
                    holder.picFrame.setVisibility(View.GONE);
                    holder.vidFrame.setVisibility(View.GONE);
                    holder.audioFrame.setVisibility(View.GONE);
                    holder.txtMsgFrame.setVisibility(View.VISIBLE);
                    holder.mapFrame.setVisibility(View.GONE);

                    if (realmChat.getTag() != null && realmChat.getTag().contains("order_id")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.txtMsg.setText(Html.fromHtml("<font color='#228C22'><u>" + realmChat.getText() + "</u></font>", Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            holder.txtMsg.setText(Html.fromHtml("<font color='#228C22'><u>" + realmChat.getText() + "</u></font>"));
                        }
                    } else {
                        holder.txtMsg.setText(StringEscapeUtils.unescapeJava(realmChat.getText()));
                    }
                } else {
                    holder.txtMsgFrame.setVisibility(View.GONE);

                    holder.pdfImg.setVisibility(View.GONE);
                    String ext = attachmenturl.substring(attachmenturl.lastIndexOf('.') + 1);
                    if (realmChat.getAttachment_type() == null) {
                        return;
                    }
                    switch (realmChat.getAttachment_type()) {
                        case "image":
                            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats";
                            holder.metaData.setVisibility(View.GONE);
                            holder.docFrame.setVisibility(View.GONE);
                            holder.picFrame.setVisibility(View.VISIBLE);
                            holder.vidFrame.setVisibility(View.GONE);
                            holder.audioFrame.setVisibility(View.GONE);
                            holder.mapFrame.setVisibility(View.GONE);
                            holder.liveLocFrame.setVisibility(View.GONE);
                            break;
                        case "video":
                            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/EkumfiJuice/Chats";
                            holder.metaData.setVisibility(View.GONE);
                            holder.docFrame.setVisibility(View.GONE);
                            holder.picFrame.setVisibility(View.GONE);
                            holder.vidFrame.setVisibility(View.VISIBLE);
                            holder.audioFrame.setVisibility(View.GONE);
                            holder.mapFrame.setVisibility(View.GONE);
                            holder.liveLocFrame.setVisibility(View.GONE);
                            break;
                        case "audio":
                            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats";
                            holder.metaData.setVisibility(View.GONE);
                            holder.docFrame.setVisibility(View.GONE);
                            holder.picFrame.setVisibility(View.GONE);
                            holder.vidFrame.setVisibility(View.GONE);
                            holder.audioFrame.setVisibility(View.VISIBLE);
                            holder.mapFrame.setVisibility(View.GONE);
                            holder.liveLocFrame.setVisibility(View.GONE);
                            break;
                        case "map":
                            folder = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Static";
                            holder.metaData.setVisibility(View.GONE);
                            holder.docFrame.setVisibility(View.GONE);
                            holder.picFrame.setVisibility(View.GONE);
                            holder.vidFrame.setVisibility(View.GONE);
                            holder.audioFrame.setVisibility(View.GONE);
                            holder.mapFrame.setVisibility(View.VISIBLE);
                            holder.liveLocFrame.setVisibility(View.GONE);
                            break;
                        case "live_location":
                            folder = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Live";
                            holder.metaData.setVisibility(View.GONE);
                            holder.docFrame.setVisibility(View.GONE);
                            holder.picFrame.setVisibility(View.GONE);
                            holder.vidFrame.setVisibility(View.GONE);
                            holder.audioFrame.setVisibility(View.GONE);
                            holder.mapFrame.setVisibility(View.GONE);
                            holder.liveLocFrame.setVisibility(View.VISIBLE);

                            if (realmChat.getTag() != null && realmChat.getTag().contains("live_location_ended")) {
                                holder.stop_sharing.setVisibility(View.GONE);
                                holder.live_location_ended.setVisibility(View.VISIBLE);
                            } else {
                                holder.stop_sharing.setVisibility(View.VISIBLE);
                                holder.live_location_ended.setVisibility(View.GONE);
                            }
                            break;
                        default:
                            folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/EkumfiJuice/Chats";
                            holder.pdfImg.setVisibility(View.GONE);
                            holder.metaData.setVisibility(View.VISIBLE);
                            holder.docFrame.setVisibility(View.VISIBLE);
                            holder.picFrame.setVisibility(View.GONE);
                            holder.vidFrame.setVisibility(View.GONE);
                            holder.audioFrame.setVisibility(View.GONE);
                            holder.txtMsgFrame.setVisibility(View.GONE);
                            holder.mapFrame.setVisibility(View.GONE);
                            holder.liveLocFrame.setVisibility(View.GONE);
                            break;
                    }


                    File file_dir = new File(folder, sub_folder);
                    if (!file_dir.exists()) {
                        file_dir.mkdirs();
                    }

                    if (folder.equals("Maps")) {
                        lastpathseg = chatid + ".jpg";
                    } else {
                        lastpathseg = chatid + "." + ext;
                    }
                    file = new File(folder + "/" + sub_folder, lastpathseg);
                    if (file.exists()) {
                        if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            imgLoc = folder + "/" + sub_folder + "/" + lastpathseg;

                            Drawable drawable = Drawable.createFromPath(imgLoc);
                            holder.image.setImageDrawable(drawable);


                            holder.downloadStatusWrapper_pic.setVisibility(View.INVISIBLE);
                            holder.image.setVisibility(View.VISIBLE);
                        } else if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            vidLoc = folder + "/" + sub_folder + "/" + lastpathseg;

                            holder.videoImageView.setImageBitmap(createVideoThumbNail(vidLoc));
                            holder.downloadStatusWrapper_vid.setVisibility(View.INVISIBLE);
                            holder.videoImageView.setVisibility(View.VISIBLE);
                            holder.play.setVisibility(View.VISIBLE);
                        } else if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            audioLoc = folder + "/" + sub_folder + "/" + lastpathseg;

                            holder.audioSenseiPlayerView.setAudioTarget(Uri.parse(audioLoc));
                            holder.downloadStatusWrapper_audio.setVisibility(View.GONE);
                            holder.audioSenseiPlayerView.setVisibility(View.VISIBLE);
                        } else if (folder.equals(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Static")) {
                            holder.map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.location_pin));
                            holder.downloadStatusWrapper_map.setVisibility(View.INVISIBLE);
                            holder.map.setVisibility(View.VISIBLE);
                        }
                        else if (folder.equals(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Live")) {
                            holder.liveLoc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.live_loc_pin));
                            holder.downloadStatusWrapper_live_loc.setVisibility(View.INVISIBLE);
                            holder.liveLoc.setVisibility(View.VISIBLE);
                        }
                        else {
                            String docType = getDocType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext));
                            Bitmap docIconBitmap = null;
                            switch (docType) {
                                case "word":
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_doc);
                                    break;
                                case "excel":
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_xls);
                                    break;
                                case "powerpoint":
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_ppt);
                                    break;
                                case "text":
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_txt);
                                    break;
                                case "pdf":
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_pdf);
                                    break;
                                default:
                                    docIconBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.unknown_file_type);
                                    break;
                            }

                            holder.downloadStatusWrapper_doc.setVisibility(View.INVISIBLE);
                            holder.docIcon.setImageBitmap(docIconBitmap);
                            holder.docTitle.setText(realmChat.getAttachment_title());
                            String meata_data = Const.fileSize(file.length()) + " ∙ " + ext.toUpperCase();
                            holder.metaData.setText(meata_data);
                            if (docType.equals("pdf")) {
                                File pdfImgFile = new File(activity.getFilesDir() + "/EkumfiJuice/PDF", "PDF-" + chatid + ".jpeg");

                                if (!pdfImgFile.exists()) {
                                    Const.generateImageFromPdf(activity, Uri.fromFile(file), 1, new File(activity.getFilesDir().getAbsolutePath() + "/EkumfiJuice/PDF", "PDF-" + chatid + ".jpeg"));
                                }
                                String pdfPgPrevLoc = activity.getFilesDir() + "/EkumfiJuice/PDF/" + "PDF-" + chatid + ".jpeg";
                                Bitmap toBeCropped = BitmapFactory.decodeFile(pdfPgPrevLoc);
                                if (toBeCropped != null) {
                                    int fromHere = (int) (toBeCropped.getHeight() * 0.5);
                                    Bitmap croppedBitmap = Bitmap.createBitmap(toBeCropped, 0, 0, toBeCropped.getWidth(), fromHere);
                                    int pageCount = getPageCount(Uri.fromFile(file));
                                    String pg = pageCount == 1 ? "1 page ∙ " : pageCount + " pages ∙ ";
                                    holder.metaData.setText(pg + meata_data);
                                    //holder.metaData.setGravity(Gravity.CENTER);
                                    holder.pdfImg.setVisibility(View.VISIBLE);
                                    holder.pdfImg.setImageBitmap(croppedBitmap);
                                }
                            }

                        }
                    } else {
                        if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            holder.downloadStatusWrapper_doc.setVisibility(View.VISIBLE);
                            holder.uploadImg_doc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                        } else if (folder.equals(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Static")) {
                            holder.downloadStatusWrapper_map.setVisibility(View.GONE);
                            holder.map.setVisibility(View.VISIBLE);
                            holder.map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.location_pin));

                            holder.uploadImg_map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                        }
                        else if (folder.equals(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats/Maps/Live")) {
                            holder.downloadStatusWrapper_live_loc.setVisibility(View.GONE);
                            holder.liveLoc.setVisibility(View.VISIBLE);
                            holder.liveLoc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.live_loc_pin));

                            holder.uploadImg_live_loc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                        } else if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            holder.downloadStatusWrapper_vid.setVisibility(View.VISIBLE);
                            holder.uploadImg_vid.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                            holder.videoImageView.setVisibility(View.INVISIBLE);
                            holder.play.setVisibility(View.GONE);
                        } else if (folder.equals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats")) {
                            holder.downloadStatusWrapper_audio.setVisibility(View.VISIBLE);
                            holder.uploadImg_audio.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                            holder.audioSenseiPlayerView.setVisibility(View.INVISIBLE);
                        } else {
                            holder.downloadStatusWrapper_pic.setVisibility(View.VISIBLE);
                            holder.uploadImg_pic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                        }
                    }
                }

                File finalFile = file;
                if (realmChat.getCreated_at() == null) {
                    holder.downloadStatusWrapper_doc.setVisibility(View.INVISIBLE);
                    holder.downloadStatusWrapper_pic.setVisibility(View.INVISIBLE);
                    holder.downloadStatusWrapper_vid.setVisibility(View.INVISIBLE);
                    holder.downloadStatusWrapper_audio.setVisibility(View.INVISIBLE);
                    holder.downloadStatusWrapper_map.setVisibility(View.INVISIBLE);
                }

                holder.downloadStatusWrapper_doc.setOnClickListener(view -> {

                    if (!isMultiSelect) {
                        if (holder.pbar_doc.getVisibility() == View.GONE) {
                            holder.pbar_doc.setVisibility(View.VISIBLE);
                            holder.uploadImg_doc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cancel));

//                            chatAdapterInterface.onDocDownloadClick(objects, position, holder);
                            downFileAsyncMap.put(chatid, new DownloadFileAsync(activity, response -> {
                                holder.downloadStatusWrapper_doc.setVisibility(View.INVISIBLE);
                                notifyItemChanged(position);
                                if (response != null) {
                                    // unsuccessful
                                    if (response.contains("java.io.FileNotFoundException")) {
                                        holder.pbar_doc.setVisibility(View.GONE);
                                        new AlertDialog.Builder(activity)
                                                .setTitle(Const.toTitleCase(activity.getString(R.string.download_failed)))
                                                .setMessage(activity.getString(R.string.file_no_longer_available_for_download))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, progress -> {
                                //                                    holder.pbar_doc.setProgress(progress);
                            }, () -> {
                                if (finalFile.exists()) {
                                    finalFile.delete();
                                }
                                holder.pbar_doc.setVisibility(View.GONE);
                                holder.uploadImg_doc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                                Toast.makeText(activity, activity.getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                            }).execute(attachmenturl, finalFile.getAbsolutePath()));
                        } else {
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            holder.pbar_doc.setVisibility(View.GONE);
                            holder.uploadImg_doc.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));

                            AsyncTask<String, Integer, String> downloadFileAsync = downFileAsyncMap.get(chatid);
                            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                                // This would not cancel downloading from httpClient
                                //  we have do handle that manually in onCancelled event inside AsyncTask
                                downloadFileAsync.cancel(true);
                            }
                        }
                    }
                });
                holder.downloadStatusWrapper_pic.setOnClickListener(view -> {

                    if (!isMultiSelect) {
                        if (holder.pbar_pic.getVisibility() == View.GONE) {
                            holder.pbar_pic.setVisibility(View.VISIBLE);
                            holder.uploadImg_pic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cancel));
                            downFileAsyncMap.put(chatid, new DownloadFileAsync(activity, response -> {
                                holder.downloadStatusWrapper_pic.setVisibility(View.INVISIBLE);
                                notifyItemChanged(position);
                                if (response != null) {
                                    // unsuccessful
                                    if (response.contains("java.io.FileNotFoundException")) {
                                        holder.pbar_pic.setVisibility(View.GONE);
                                        new AlertDialog.Builder(activity)
                                                .setTitle(Const.toTitleCase(activity.getString(R.string.download_failed)))
                                                .setMessage(activity.getString(R.string.file_no_longer_available_for_download))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, progress -> {
                                //                                    holder.pbar_pic.setProgress(progress);
                            }, () -> {
                                if (finalFile.exists()) {
                                    finalFile.delete();
                                }
                                holder.pbar_pic.setVisibility(View.GONE);
                                holder.uploadImg_pic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                                Toast.makeText(activity, activity.getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                            }).execute(attachmenturl, finalFile.getAbsolutePath(), Environment.DIRECTORY_PICTURES + "/EkumfiJuice/Chats/" + sub_folder, "image"));
                        } else {
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            holder.pbar_pic.setVisibility(View.GONE);
                            holder.uploadImg_pic.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));

                            AsyncTask<String, Integer, String> downloadFileAsync = downFileAsyncMap.get(chatid);
                            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                                // This would not cancel downloading from httpClient
                                //  we have do handle that manually in onCancelled event inside AsyncTask
                                downloadFileAsync.cancel(true);
                            }
                        }
                    }
                });
                holder.downloadStatusWrapper_vid.setOnClickListener(view -> {

                    if (!isMultiSelect) {
                        if (holder.pbar_vid.getVisibility() == View.GONE) {
                            holder.pbar_vid.setVisibility(View.VISIBLE);
                            holder.uploadImg_vid.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cancel));
                            downFileAsyncMap.put(chatid, new DownloadFileAsync(activity, response -> {
                                holder.downloadStatusWrapper_vid.setVisibility(View.INVISIBLE);
                                notifyItemChanged(position);
                                if (response != null) {
                                    // unsuccessful
                                    if (response.contains("java.io.FileNotFoundException")) {
                                        holder.pbar_vid.setVisibility(View.GONE);
                                        new AlertDialog.Builder(activity)
                                                .setTitle(Const.toTitleCase(activity.getString(R.string.download_failed)))
                                                .setMessage(activity.getString(R.string.file_no_longer_available_for_download))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, progress -> {
                                //                                    holder.pbar_vid.setProgress(progress);
                            }, () -> {
                                if (finalFile.exists()) {
                                    finalFile.delete();
                                }
                                holder.pbar_vid.setVisibility(View.GONE);
                                holder.uploadImg_vid.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                                Toast.makeText(activity, activity.getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                            }).execute(attachmenturl, finalFile.getAbsolutePath(), Environment.DIRECTORY_MOVIES + "/EkumfiJuice/Chats/" + sub_folder, "video"));
                        } else {
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            holder.pbar_vid.setVisibility(View.GONE);
                            holder.uploadImg_vid.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));

                            AsyncTask<String, Integer, String> downloadFileAsync = downFileAsyncMap.get(chatid);
                            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                                // This would not cancel downloading from httpClient
                                //  we have do handle that manually in onCancelled event inside AsyncTask
                                downloadFileAsync.cancel(true);
                            }
                        }
                    }
                });
                holder.downloadStatusWrapper_audio.setOnClickListener(view -> {

                    if (!isMultiSelect) {
                        if (holder.pbar_audio.getVisibility() == View.GONE) {
                            holder.pbar_audio.setVisibility(View.VISIBLE);
                            holder.uploadImg_audio.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cancel));
                            downFileAsyncMap.put(chatid, new DownloadFileAsync(activity, response -> {
                                holder.downloadStatusWrapper_audio.setVisibility(View.INVISIBLE);
                                notifyItemChanged(position);
                                if (response != null) {
                                    // unsuccessful
                                    if (response.contains("java.io.FileNotFoundException")) {
                                        holder.pbar_audio.setVisibility(View.GONE);
                                        new AlertDialog.Builder(activity)
                                                .setTitle(Const.toTitleCase(activity.getString(R.string.download_failed)))
                                                .setMessage(activity.getString(R.string.file_no_longer_available_for_download))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, progress -> {
                                //                                    holder.pbar_audio.setProgress(progress);
                            }, () -> {
                                if (finalFile.exists()) {
                                    finalFile.delete();
                                }
                                holder.pbar_audio.setVisibility(View.GONE);
                                holder.uploadImg_audio.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                                Toast.makeText(activity, activity.getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                            }).execute(attachmenturl, finalFile.getAbsolutePath(), Environment.DIRECTORY_MUSIC + "/EkumfiJuice/Chats/" + sub_folder, "audio"));
                        } else {
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            holder.pbar_audio.setVisibility(View.GONE);
                            holder.uploadImg_audio.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));

                            AsyncTask<String, Integer, String> downloadFileAsync = downFileAsyncMap.get(chatid);
                            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                                // This would not cancel downloading from httpClient
                                //  we have do handle that manually in onCancelled event inside AsyncTask
                                downloadFileAsync.cancel(true);
                            }
                        }
                    }
                });
                holder.downloadStatusWrapper_map.setOnClickListener(view -> {
                    if (!isMultiSelect) {
                        if (holder.pbar_map.getVisibility() == View.GONE) {
                            holder.pbar_map.setVisibility(View.VISIBLE);
                            holder.uploadImg_map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.cancel));
                            downFileAsyncMap.put(chatid, new DownloadFileAsync(activity, response -> {
                                holder.downloadStatusWrapper_map.setVisibility(View.INVISIBLE);
                                notifyItemChanged(position);
                                if (response != null) {
                                    // unsuccessful
                                    if (response.contains("java.io.FileNotFoundException")) {
                                        holder.pbar_map.setVisibility(View.GONE);
                                        new AlertDialog.Builder(activity)
                                                .setTitle(Const.toTitleCase(activity.getString(R.string.download_failed)))
                                                .setMessage(activity.getString(R.string.file_no_longer_available_for_download))

                                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                                // The dialog is automatically dismissed when a dialog button is clicked.
                                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                                                })
                                                .show();

                                    } else {
                                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, progress -> {
                                //                                    holder.pbar_map.setProgress(progress);
                            }, () -> {
                                if (finalFile.exists()) {
                                    finalFile.delete();
                                }
                                holder.pbar_map.setVisibility(View.GONE);
                                holder.uploadImg_map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));
                                Toast.makeText(activity, activity.getString(R.string.download_cancelled), Toast.LENGTH_SHORT).show();
                            }).execute(attachmenturl, finalFile.getAbsolutePath()));
                        } else {
                            if (finalFile.exists()) {
                                finalFile.delete();
                            }
                            holder.pbar_map.setVisibility(View.GONE);
                            holder.uploadImg_map.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.download));

                            AsyncTask<String, Integer, String> downloadFileAsync = downFileAsyncMap.get(chatid);
                            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                                // This would not cancel downloading from httpClient
                                //  we have do handle that manually in onCancelled event inside AsyncTask
                                downloadFileAsync.cancel(true);
                            }
                        }
                    }
                });
                holder.stop_sharing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = null;
                        ProgressDialog mProgress = new ProgressDialog(activity);
                        mProgress.setCancelable(false);
                        mProgress.setIndeterminate(true);

                        mProgress.setTitle("Please wait...");
                        mProgress.show();

                        stringRequest = new StringRequest(
                                Request.Method.PATCH,
                                keyConst.API_URL + "chats/" + realmChat.getChat_id(),
                                response -> {
                                    mProgress.dismiss();
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(activity);
                                            Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                                notifyItemChanged(position, realm.createOrUpdateObjectFromJson(RealmChat.class, response));
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    mProgress.dismiss();
                                    error.printStackTrace();
                                    Const.myVolleyError(activity, error);
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("tag", "live_location_ended");
                                return params;
                            }
                            @Override
                            public Map getHeaders() throws AuthFailureError {
                                HashMap headers = new HashMap();
                                headers.put("accept", "application/json");
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
                                return headers;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        InitApplication.getInstance().addToRequestQueue(stringRequest);
                    }
                });

                holder.txtMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (realmChat.getTag() != null && realmChat.getTag().contains("order_id")) {
                            String order_id = realmChat.getText().replace("Order id: ", "");

                            String role = PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "");
                            if (role.equals("SELLER") && activity.getIntent().getStringExtra("CONSUMER_ID").equals("")) {
                                ProgressDialog dialog = new ProgressDialog(activity);
                                dialog.setMessage("Please wait...");
                                dialog.setCancelable(false);
                                dialog.setIndeterminate(true);
                                dialog.show();

                                StringRequest stringRequest = new StringRequest(
                                        com.android.volley.Request.Method.POST,
                                        keyConst.API_URL + "scoped-stock-cart-products",
                                        response -> {
                                            if (response != null) {
                                                dialog.dismiss();
                                                try {
                                                    final float[] sub_total = {0.00F};
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    Realm.init(activity);
                                                    Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                                        realm.where(RealmStockCartProduct.class).findAll().deleteAllFromRealm();
                                                        realm.createOrUpdateAllFromJson(RealmStockCartProduct.class, jsonArray);

                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            try {
                                                                sub_total[0] += (float)jsonArray.getJSONObject(i).getDouble("price");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });

                                                    activity.startActivity(
                                                            new Intent(activity, StockCartItemsActivity.class)
                                                                    .putExtra("ORDER_ID", order_id)
                                                                    .putExtra("LAUNCHED_FROM_CHAT", true)
                                                    );
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        error -> {
                                            error.printStackTrace();
                                            Const.myVolleyError(activity, error);
                                            dialog.dismiss();
                                            Log.d("Cyrilll", error.toString());
                                        }
                                ) {
                                    @Override
                                    public Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("order_id", order_id);
                                        return params;
                                    }

                                    /** Passing some request headers* */
                                    @Override
                                    public Map getHeaders() throws AuthFailureError {
                                        HashMap headers = new HashMap();
                                        headers.put("accept", "application/json");
                                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
                                        return headers;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        0,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                InitApplication.getInstance().addToRequestQueue(stringRequest);
                            }
                            else {
                                ProgressDialog dialog = new ProgressDialog(activity);
                                dialog.setMessage("Please wait...");
                                dialog.setCancelable(false);
                                dialog.setIndeterminate(true);
                                dialog.show();


                                StringRequest stringRequest = new StringRequest(
                                        com.android.volley.Request.Method.POST,
                                        keyConst.API_URL + "scoped-cart-products",
                                        response -> {
                                            if (response != null) {
                                                dialog.dismiss();
                                                try {
                                                    final float[] sub_total = {0.00F};
                                                    JSONArray jsonArray = new JSONArray(response);
                                                    Realm.init(activity);
                                                    Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                                        realm.where(RealmCartProduct.class).findAll().deleteAllFromRealm();
                                                        realm.createOrUpdateAllFromJson(RealmCartProduct.class, jsonArray);

                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            try {
                                                                sub_total[0] += (float)jsonArray.getJSONObject(i).getDouble("price");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });

                                                    activity.startActivity(
                                                            new Intent(activity, CartItemsActivity.class)
                                                                    .putExtra("LAUNCHED_FROM_CHAT", true)
                                                                    .putExtra("ORDER_ID", order_id)
                                                    );
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        error -> {
                                            error.printStackTrace();
                                            Const.myVolleyError(activity, error);
                                            dialog.dismiss();
                                            Log.d("Cyrilll", error.toString());
                                        }
                                ) {
                                    @Override
                                    public Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("order_id", order_id);
                                        return params;
                                    }

                                    /** Passing some request headers* */
                                    @Override
                                    public Map getHeaders() throws AuthFailureError {
                                        HashMap headers = new HashMap();
                                        headers.put("accept", "application/json");
                                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
                                        return headers;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        0,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                InitApplication.getInstance().addToRequestQueue(stringRequest);
                            }
                        }
                    }
                });

                boolean linkPrevExists = realmChat.getLink_title() != null;
                if (linkPrevExists) {
                    holder.linkPrevFrame.setVisibility(View.VISIBLE);
                    holder.close.setVisibility(View.GONE);
                    if (realmChat.getLink_image() == null) {
                        holder.linkImg.setVisibility(View.GONE);
                    } else {
                        holder.linkImg.setVisibility(View.VISIBLE);
                        Glide.with(activity).load(realmChat.getLink_image()).apply(new RequestOptions().fitCenter()).into(holder.linkImg);
                    }

                    holder.linkTitle.setText(realmChat.getLink_title());
                    holder.linkDesc.setText(realmChat.getLink_description());
                } else {
                    holder.linkPrevFrame.setVisibility(View.GONE);
                }

                boolean referencedChatExists = realmChat.getChat_ref_id() != null;
                if (referencedChatExists) {
                    holder.replyPrevFrame.setVisibility(View.VISIBLE);
                    holder.replyClose.setVisibility(View.GONE);
                    holder.replyName.setText(realmChat.getReply_name());
                    holder.replyBody.setText(realmChat.getReply_body());
                } else {
                    holder.replyPrevFrame.setVisibility(View.GONE);
                }

                holder.replyPrevFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        for (int i = 0; i < consolidatedList.size(); i++) {
                            Object obj = consolidatedList.get(i);
                            if (obj instanceof RealmChat) {
                                if (((RealmChat) obj).getChat_id().equals(((RealmChat) consolidatedList.get(position)).getChat_ref_id())) {

                                    if (activity instanceof MessageActivity) {
                                        MessageActivity.mLinearLayoutManager.scrollToPositionWithOffset(i, 0);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                });

                holder.linkPrevFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        openLinkIfIsLink(realmChat);
                    }
                });

                holder.docFrame.setOnClickListener(v -> {

                    if (!isMultiSelect) {
                        if (holder.downloadStatusWrapper_doc.getVisibility() == View.INVISIBLE) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);

                            if (finalFile.exists()) {

                                if (finalFile.canRead()) {
                                    Log.d("gard", finalFile.toString());

                                    String mimeType = getMimeType(finalFile.getAbsolutePath());
                                    Uri docURI = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", finalFile);
                                    intent.setDataAndType(docURI, mimeType);
                                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    try {
                                        activity.startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(activity, activity.getString(R.string.no_suitable_app_for_viewing_this_file), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    docFile = finalFile;
                                    openDirectory(activity, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + "/EkumfiJuice", "Chats")));
                                }
                            } else {
                                Toast.makeText(activity, activity.getString(R.string.sorry_document_file_not_exist_on_your_internal_storrage), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                String finalImgLoc = imgLoc;
                holder.picFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        if (holder.downloadStatusWrapper_pic.getVisibility() == View.INVISIBLE) {
                            if (finalFile.exists()) {
                                PictureActivity.idPicBitmap = BitmapFactory.decodeFile(finalImgLoc);
                                activity.startActivity(new Intent(activity, PictureActivity.class));
                            } else {
                                Toast.makeText(activity, activity.getString(R.string.sorry_document_file_not_exist_on_your_internal_storrage), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                String finalVidLoc = vidLoc;
                holder.vidFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        if (holder.downloadStatusWrapper_vid.getVisibility() == View.INVISIBLE) {
                            if (finalFile.exists()) {
                                String mimeType = getMimeType(finalFile.getAbsolutePath());
                                Uri docURI = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", finalFile);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(docURI, mimeType);
                                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                try {
                                    activity.startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(activity, activity.getString(R.string.no_suitable_app_for_viewing_this_file), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(activity, "Sorry, this video doesn't exist on your device", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                holder.layout_parent.setOnClickListener(v -> {
                    if (activity instanceof MessageActivity) {
                        MessageActivity.cardview.setVisibility(View.GONE);
                    }
                });

                holder.mapFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        String[] coordinates = realmChat.getLink_description().split(",");
                        String uri = "geo: "+ String.valueOf(coordinates[0])+","+String.valueOf(coordinates[1])+
                                "?q="+  String.valueOf(coordinates[0])+","+String.valueOf(coordinates[1]);
                        activity.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                    }
                });

                holder.liveLocFrame.setOnClickListener(v -> {
                    if (!isMultiSelect) {
                        if (!isMultiSelect) {
                            String[] coordinates = realmChat.getLink_description().split(",");

                            activity.startActivity(
                                    new Intent(activity, LiveLocationActivity.class)
                                            .putExtra("CHAT_ID", realmChat.getChat_id())
                                            .putExtra("LOCATION_ENDED", realmChat.getTag() != null && realmChat.getTag().equals("live_location_ended"))
                                            .putExtra("NAME", NAME)
                                            .putExtra("PROFILE_IMAGE_URL", PROFILE_IMAGE_URL)
                                            .putExtra("LATITUDE", Double.parseDouble(coordinates[0]))
                                            .putExtra("LONGITUDE", Double.parseDouble(coordinates[1]))
                            );
                        }
                    }
                });

                if (selected_usersList.contains(consolidatedList.get(position)))
                    holder.layout_parent.setBackgroundColor(ContextCompat.getColor(activity, R.color.list_item_selected_state));
                else
                    holder.layout_parent.setBackgroundColor(ContextCompat.getColor(activity, R.color.list_item_normal_state));

                break;

            case TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;

                // Populate date item data here
                dateViewHolder.date.setText(dateItem.getDate());

                break;
        }
    }
    //PdfiumAndroid (https://github.com/barteksc/PdfiumAndroid)
//https://github.com/barteksc/AndroidPdfViewer/issues/49

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface ChatAdapterInterface {
        void onDocDownloadClick(ArrayList<Object> names, int position, MessageViewHolder holder);
    }

    public void setFilter(ArrayList<Object> arrayList) {
        consolidatedList = new ArrayList<>();
        consolidatedList.addAll(arrayList);
        notifyDataSetChanged();
    }

    int getPageCount(Uri pdfUri) {
        int pageCount = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(activity);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = activity.getContentResolver().openFileDescriptor(pdfUri, "r");
            com.shockwave.pdfium.PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pageCount = pdfiumCore.getPageCount(pdfDocument);
        } catch (Exception e) {
            //todo with exception
            Log.d(TAG, e.toString());
        }
        return pageCount;
    }

    public void openLinkIfIsLink(RealmChat realmChat) {
        boolean isLink = realmChat.getLink() != null;
        if (isLink) {
            Uri webpage = Uri.parse(realmChat.getLink());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
        }
    }

    public Bitmap createVideoThumbNail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    public void openDirectory(Activity activity, Uri uriToLoad) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

        activity.startActivityForResult(intent, RC_OPEN_DOCUMENT);
    }
}

