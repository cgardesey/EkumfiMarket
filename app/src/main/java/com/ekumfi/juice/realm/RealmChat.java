package com.ekumfi.juice.realm;

import com.ekumfi.juice.constants.Const;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 2CLearning on 12/16/2017.
 */

public class RealmChat extends RealmObject implements Comparable<RealmChat> {

    private int id;
    @PrimaryKey
    private String chat_id;
    private String chat_ref_id;
    private String text;
    private String link;
    private String link_title;
    private String link_description;
    private String link_image;
    private String attachment_url;
    private String attachment_type;
    private String attachment_title;
    private int read_by_recipient;
    private int sent_by_consumer;
    private String sender_role;
    private String tag;
    private String consumer_id;
    private String seller_id;
    private String agent_id;
    private String wholesaler_id;
    private String ekumfi_info_id;
    private String created_at;
    private String updated_at;


    private String consumer_name;
    private String consumer_profile_image_url;


    private String name;
    private String sender_name;
    private String recipient_name;
    private String picture;
    private String sender_picture;
    private String recipient_picture;
    private String reply_name;
    private String reply_body;

    private boolean instructor;


    public RealmChat() {

    }


    public RealmChat(int id, String chat_id, String chat_ref_id, String text, String link, String link_title, String link_description, String link_image, String attachment_url, String attachment_type, String attachment_title, int read_by_recipient, int sent_by_consumer, String sender_role, String tag, String consumer_id, String seller_id, String agent_id, String wholesaler_id, String ekumfi_info_id, String created_at, String updated_at) {
        this.id = id;
        this.chat_id = chat_id;
        this.chat_ref_id = chat_ref_id;
        this.text = text;
        this.link = link;
        this.link_title = link_title;
        this.link_description = link_description;
        this.link_image = link_image;
        this.attachment_url = attachment_url;
        this.attachment_type = attachment_type;
        this.attachment_title = attachment_title;
        this.read_by_recipient = read_by_recipient;
        this.sent_by_consumer = sent_by_consumer;
        this.sender_role = sender_role;
        this.tag = tag;
        this.consumer_id = consumer_id;
        this.seller_id = seller_id;
        this.agent_id = agent_id;
        this.wholesaler_id = wholesaler_id;
        this.ekumfi_info_id = ekumfi_info_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public RealmChat(int id, String chat_id, String chat_ref_id, String text, String link, String link_title, String link_description, String link_image, String attachment_url, String attachment_type, String attachment_title, int read_by_recipient, int sent_by_consumer, String sender_role, String tag, String consumer_id, String seller_id, String agent_id, String wholesaler_id, String ekumfi_info_id, String created_at, String updated_at, String consumer_name, String consumer_profile_image_url) {
        this.id = id;
        this.chat_id = chat_id;
        this.chat_ref_id = chat_ref_id;
        this.text = text;
        this.link = link;
        this.link_title = link_title;
        this.link_description = link_description;
        this.link_image = link_image;
        this.attachment_url = attachment_url;
        this.attachment_type = attachment_type;
        this.attachment_title = attachment_title;
        this.read_by_recipient = read_by_recipient;
        this.sent_by_consumer = sent_by_consumer;
        this.sender_role = sender_role;
        this.tag = tag;
        this.consumer_id = consumer_id;
        this.seller_id = seller_id;
        this.agent_id = agent_id;
        this.wholesaler_id = wholesaler_id;
        this.ekumfi_info_id = ekumfi_info_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.consumer_name = consumer_name;
        this.consumer_profile_image_url = consumer_profile_image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_ref_id() {
        return chat_ref_id;
    }

    public void setChat_ref_id(String chat_ref_id) {
        this.chat_ref_id = chat_ref_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getLink_description() {
        return link_description;
    }

    public void setLink_description(String link_description) {
        this.link_description = link_description;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public String getAttachment_title() {
        return attachment_title;
    }

    public void setAttachment_title(String attachment_title) {
        this.attachment_title = attachment_title;
    }

    public int getRead_by_recipient() {
        return read_by_recipient;
    }

    public void setRead_by_recipient(int read_by_recipient) {
        this.read_by_recipient = read_by_recipient;
    }

    public int getSent_by_consumer() {
        return sent_by_consumer;
    }

    public void setSent_by_consumer(int sent_by_consumer) {
        this.sent_by_consumer = sent_by_consumer;
    }

    public String getSender_role() {
        return sender_role;
    }

    public void setSender_role(String sender_role) {
        this.sender_role = sender_role;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getWholesaler_id() {
        return wholesaler_id;
    }

    public void setWholesaler_id(String wholesaler_id) {
        this.wholesaler_id = wholesaler_id;
    }

    public String getEkumfi_info_id() {
        return ekumfi_info_id;
    }

    public void setEkumfi_info_id(String ekumfi_info_id) {
        this.ekumfi_info_id = ekumfi_info_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getConsumer_name() {
        return consumer_name;
    }

    public void setConsumer_name(String consumer_name) {
        this.consumer_name = consumer_name;
    }

    public String getConsumer_profile_image_url() {
        return consumer_profile_image_url;
    }

    public void setConsumer_profile_image_url(String consumer_profile_image_url) {
        this.consumer_profile_image_url = consumer_profile_image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSender_picture() {
        return sender_picture;
    }

    public void setSender_picture(String sender_picture) {
        this.sender_picture = sender_picture;
    }

    public String getRecipient_picture() {
        return recipient_picture;
    }

    public void setRecipient_picture(String recipient_picture) {
        this.recipient_picture = recipient_picture;
    }

    public String getReply_name() {
        return reply_name;
    }

    public void setReply_name(String reply_name) {
        this.reply_name = reply_name;
    }

    public String getReply_body() {
        return reply_body;
    }

    public void setReply_body(String reply_body) {
        this.reply_body = reply_body;
    }


    public boolean isInstructor() {
        return instructor;
    }

    public void setInstructor(boolean instructor) {
        this.instructor = instructor;
    }

    @Override
    public int compareTo(RealmChat realmChat) {
        try {
            if (Const.dateTimeFormat.parse(created_at).getTime() > Const.dateTimeFormat.parse(realmChat.getCreated_at()).getTime()) {
                return 1;
            } else if (Const.dateTimeFormat.parse(created_at).getTime() < Const.dateTimeFormat.parse(realmChat.getCreated_at()).getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
