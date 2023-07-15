package com.ekumfi.juice.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rygelouv.audiosensei.player.AudioSenseiPlayerView;
import com.ekumfi.juice.R;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by 2CLearning on 2/8/2018.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout bubble_parent, layout_parent;
    LinearLayout layout;

    TextView name, instructor;

    LinearLayout txtMsgFrame;
    EmojiconTextView txtMsg;
    FrameLayout linkPrevFrame;
    LinearLayout linkPrevFrame2;
    ImageView linkImg;
    LinearLayout linkTextArea;
    TextView linkTitle;
    TextView linkDesc;
    ImageView close;


    FrameLayout replyPrevFrame;
    LinearLayout replyPrevFrame2;
    ImageView replyImg;
    LinearLayout replyTextArea;
    TextView replyName;
    TextView replyBody;
    ImageView replyClose;


    RelativeLayout picFrame;
    ImageView image;
    RelativeLayout downloadStatusWrapper_pic;
    ProgressBar pbar_pic;
    ImageView uploadImg_pic;
    CardView retry;
    TextView retry_text;

    RelativeLayout vidFrame;
    ImageView videoImageView;
    RelativeLayout downloadStatusWrapper_vid;
    ProgressBar pbar_vid;
    ImageView uploadImg_vid;
    CardView retry_vid;
    TextView retry_text_vid;
    CardView play;

    RelativeLayout mapFrame;
    ImageView map;
    RelativeLayout downloadStatusWrapper_map;
    ProgressBar pbar_map;
    ImageView uploadImg_map;
    CardView retry_map;
    TextView retry_text_map;

    RelativeLayout liveLocFrame;
    ImageView liveLoc;
    RelativeLayout downloadStatusWrapper_live_loc;
    ProgressBar pbar_live_loc;
    ImageView uploadImg_live_loc;
    CardView retry_live_loc;
    TextView live_location_ended, stop_sharing;

    LinearLayout docFrame;
    RelativeLayout doc;
    ImageView docIcon;
    TextView docTitle;
    ImageView pdfImg;
    RelativeLayout downloadStatusWrapper_doc;
    ImageView uploadImg_doc;
    ProgressBar pbar_doc;

    RelativeLayout audioFrame;
    AudioSenseiPlayerView audioSenseiPlayerView;
    RelativeLayout downloadStatusWrapper_audio;
    ProgressBar pbar_audio;
    ImageView uploadImg_audio;

    LinearLayout identifierLayout;


    TextView time, audiotext;
    ImageView statusImg;
    TextView metaData;

    public MessageViewHolder(View v, Activity activity) {
        super(v);

        bubble_parent = itemView.findViewById(R.id.bubble_layout_parent);
        layout_parent = itemView.findViewById(R.id.layout_parent);
        layout = itemView.findViewById(R.id.bubble_layout);

        name = itemView.findViewById(R.id.nameTextView);
        instructor = itemView.findViewById(R.id.instructorTextView);

        txtMsgFrame = itemView.findViewById(R.id.txt_msg_frame);
        txtMsg = itemView.findViewById(R.id.txt_msg);
        audiotext = itemView.findViewById(R.id.audiotext);

        linkPrevFrame = itemView.findViewById(R.id.link_prev_frame);
        linkPrevFrame2 = itemView.findViewById(R.id.link_prev_frame2);
        linkImg = itemView.findViewById(R.id.link_img);
        linkTextArea = itemView.findViewById(R.id.link_text_area);
        linkTitle = itemView.findViewById(R.id.link_title);
        linkDesc = itemView.findViewById(R.id.link_desc);
        close = itemView.findViewById(R.id.close);

        replyPrevFrame = itemView.findViewById(R.id.reply_prev_frame);
        replyPrevFrame2 = itemView.findViewById(R.id.reply_prev_frame2);
        replyTextArea = itemView.findViewById(R.id.reply_text_area);
        replyName = itemView.findViewById(R.id.reply_name);
        replyBody = itemView.findViewById(R.id.reply_body);
        replyClose = itemView.findViewById(R.id.replyClose);

        picFrame = itemView.findViewById(R.id.pic_frame);
        image = itemView.findViewById(R.id.photoImageView);
        downloadStatusWrapper_pic = itemView.findViewById(R.id.downloadStatusWrapper_pic);
        pbar_pic = itemView.findViewById(R.id.pbar_pic);
        uploadImg_pic = itemView.findViewById(R.id.uploadImg_pic);
        retry = itemView.findViewById(R.id.retry);
        retry_text = itemView.findViewById(R.id.retry_text);

        vidFrame = itemView.findViewById(R.id.vid_frame);
        videoImageView = itemView.findViewById(R.id.videoImageView);
        downloadStatusWrapper_vid = itemView.findViewById(R.id.downloadStatusWrapper_vid);
        pbar_vid = itemView.findViewById(R.id.pbar_vid);
        uploadImg_vid = itemView.findViewById(R.id.uploadImg_vid);
        retry_vid = itemView.findViewById(R.id.retry_vid);
        retry_text_vid = itemView.findViewById(R.id.retry_text_vid);
        play = itemView.findViewById(R.id.play);

        mapFrame = itemView.findViewById(R.id.map_frame);
        map = itemView.findViewById(R.id.mapImageView);
        downloadStatusWrapper_map = itemView.findViewById(R.id.downloadStatusWrapper_map);
        pbar_map = itemView.findViewById(R.id.pbar_map);
        uploadImg_map = itemView.findViewById(R.id.uploadImg_map);
        retry_map = itemView.findViewById(R.id.retry_map);
        retry_text_map = itemView.findViewById(R.id.retry_text_map);

        liveLocFrame = itemView.findViewById(R.id.live_loc_frame);
        liveLoc = itemView.findViewById(R.id.liveLocImageView);
        downloadStatusWrapper_live_loc = itemView.findViewById(R.id.downloadStatusWrapper_live_loc);
        pbar_live_loc = itemView.findViewById(R.id.pbar_live_loc);
        uploadImg_live_loc = itemView.findViewById(R.id.uploadImg_live_loc);
        live_location_ended = itemView.findViewById(R.id.live_location_ended);
        stop_sharing = itemView.findViewById(R.id.stop_sharing);

        docFrame = itemView.findViewById(R.id.doc_frame);
        doc = itemView.findViewById(R.id.upcomingdoc);
        docIcon = itemView.findViewById(R.id.docIcon);
        docTitle = itemView.findViewById(R.id.docTitle);
        downloadStatusWrapper_doc = itemView.findViewById(R.id.downloadStatusWrapper_doc);
        uploadImg_doc = itemView.findViewById(R.id.uploadImg_doc);
        pbar_doc = itemView.findViewById(R.id.pbar);
        downloadStatusWrapper_live_loc = itemView.findViewById(R.id.downloadStatusWrapper_live_loc);
        uploadImg_live_loc = itemView.findViewById(R.id.uploadImg_live_loc);
        pbar_live_loc = itemView.findViewById(R.id.pbar_live_loc);


        pdfImg = itemView.findViewById(R.id.pdf_img);

        audioFrame = itemView.findViewById(R.id.audio_frame);
        audioSenseiPlayerView = itemView.findViewById(R.id.audio_player);
        downloadStatusWrapper_audio = itemView.findViewById(R.id.downloadStatusWrapper_audio);
        pbar_audio = itemView.findViewById(R.id.pbar_vid);
        uploadImg_audio = itemView.findViewById(R.id.uploadImg_audio);

        time = itemView.findViewById(R.id.time);
        statusImg = itemView.findViewById(R.id.statusImg);
        metaData = itemView.findViewById(R.id.metaData);

        identifierLayout = itemView.findViewById(R.id.identifierLayout);
    }
}
