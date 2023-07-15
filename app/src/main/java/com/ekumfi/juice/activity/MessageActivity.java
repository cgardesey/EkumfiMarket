
package com.ekumfi.juice.activity;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.*;
import static androidx.activity.result.contract.ActivityResultContracts.*;
import static com.android.volley.Request.Method.POST;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;
import static com.ekumfi.juice.activity.GetAuthActivity.ACCESSTOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.MapsActivity.RC_CONFIRM_LOCATION;
import static com.ekumfi.juice.activity.SellerHomeActivity.FILE_PICKER_REQUEST_CODE;
import static com.ekumfi.juice.activity.SellerHomeActivity.MYUSERID;
import static com.ekumfi.juice.adapter.ChatAdapter.downFileAsyncMap;
import static com.ekumfi.juice.adapter.ChatAdapter.isMultiSelect;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.keyConst.GUID_WS_URL;
import static com.ekumfi.juice.constants.Const.dateFormat;
import static com.ekumfi.juice.constants.Const.fileSize;
import static com.ekumfi.juice.constants.Const.getFormattedDate;
import static com.ekumfi.juice.constants.Const.isExternalStorageWritable;
import static com.ekumfi.juice.constants.Const.isNetworkAvailable;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.fragment.ConsumerSettingsFragment.ISNIGHTMODE;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;
import static com.ekumfi.juice.util.Socket.EVENT_CLOSED;
import static com.ekumfi.juice.util.Socket.EVENT_OPEN;
import static com.ekumfi.juice.util.Socket.EVENT_RECONNECT_ATTEMPT;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.adapter.MessageViewHolder;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.materialDialog.LocationTypeMaterialDialog;
import com.ekumfi.juice.realm.RealmAgent;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.leocardz.LinkPreviewCallback;
import com.leocardz.SourceContent;
import com.leocardz.TextCrawler;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ChatAdapter;
import com.ekumfi.juice.adapter.ParticipantsAdapter;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.materialDialog.CallSellerMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.other.MyHttpEntity;
import com.ekumfi.juice.pojo.DateItem;
import com.ekumfi.juice.pojo.Participant;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.AlertDialogHelper;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.util.RecyclerItemClickListener;
import com.ekumfi.juice.util.Socket;
import com.shockwave.pdfium.PdfDocument;
import com.yalantis.ucrop.util.FileUtils;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import vn.tungdx.mediapicker.MediaItem;
import vn.tungdx.mediapicker.MediaOptions;
import vn.tungdx.mediapicker.activities.MediaPickerActivity;

public class MessageActivity extends PermisoActivity implements AlertDialogHelper.AlertDialogListener, OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener , LocationTypeMaterialDialog.LocationTypeMDInterface {

    String CONSUMER_ID = "CONSUMER_ID";
    String SELLER_ID = "SELLER_ID";
    String AGENT_ID = "AGENT_ID";
    public static String CONSUMER_NAME = "";
    public static String SELLER_NAME = "";
    public static String AGENT_NAME = "";
    public static String NAME = "NAME";
    String PROFILE_IMAGE_URL = "";

    public static final int REQUEST_MEDIA = 1002;
    public static final int RC_OPEN_DOCUMENT = 5001;
    public static final int RC_FILE_PICKER_PERM = 321;
    public static final int RC_AUDIO_AND_STORAGE = 1230;
    public static final int RC_AUDIO_PICKER = 0;
    public static final int RC_DOC = 852;
    public static final int RC_STORAGE = 322;
    public static final SimpleDateFormat sfd_time = new SimpleDateFormat("h:mm a");
    public static final String PREF_JSON_KEY = "pref_json_key";
    public static final String PREF_MSG_LENGTH_KEY = "pref_msg_length";
    public static final int DEFAULT_MSG_LENGTH = 140;
    public static final int RC_PLACE_PICKER = 102;
    public static final int TYPE_DATE = 200;
    public static final int TYPE_REALM_CHAT = 100;
    static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    static final String NO_CLASS_CHATS = "There are currently no chats.";
    static final String TAG = "ChatActivity";
    public static Context chatContext;
    public static Activity messageActivity;
    public ProgressDialog mProgress;
    public static ArrayList<Object> newObjects = new ArrayList<>();
    public static ArrayList<Object> objects = new ArrayList<>();
    public static ArrayList<Participant> newParticipants = new ArrayList<>();
    public static ArrayList<Participant> participants = new ArrayList<>();
    static RecyclerView recylerView;
    public static RecyclerView recyrlerView_participant;
    public static LinearLayoutManager mLinearLayoutManager;
    static TextView statusMsg;
    static RelativeLayout controls;
    static ChatAdapter chatAdapter;
    public static ParticipantsAdapter participantsAdapter;
    ProgressBar progressBar;
    static ImageView imageView;
    TextCrawler textCrawler;
    static Socket chatSocket;
    ImageButton attach;
    EmojiconEditText messageEditText;
    FrameLayout sendButton;
    ImageView emojiButton;
    Menu context_menu;
    View rootView;
    EmojIconActions emojIcon;
    public static CardView cardview;
    RelativeLayout doc, gal, loc, audio;
    LinearLayout txtMsgFrame;
    EmojiconTextView txtMsg;

    FrameLayout linkPrevFrame;
    ImageView linkImage;
    LinearLayout linkTextArea;
    TextView linkTitle;
    TextView linkDesc;
    ImageView close;

    FrameLayout replyPrevFrame;
    ImageView replyImg;
    LinearLayout replyTextArea;
    TextView replyName;
    TextView replyBody;
    ImageView replyClose;

    static TextView nametextview, availability;
    static LinearLayout name_layout;
    static public File docFile = null;
    private Uri uri;

    ActionMode mActionMode;
    ArrayList<Object> multiselect_list = new ArrayList<>();
    AlertDialogHelper alertDialogHelper;

    static Menu search_menu;

    static SearchView searchView;

    PDFView pdfView;
    RoundedImageView profileimg;
    ImageView downbtn, upbtn, pickfile, participantsBtn, menu;
    LinearLayout participantslayout;

    Integer pageNumber = 0;

    String pdfFileName;

    File wavFile;
    String replyChatId = "";

    public static TextView curpage, participantno;

    public static String link = "", linkimgurl = "";
    private List<MediaItem> mMediaSelectedList;

    RealmConsumer realmConsumer;
    RealmSeller realmSeller;
    private FusedLocationProviderClient fusedLocationClient;
    LocationTypeMaterialDialog locationTypeMaterialDialog;


    LinkPreviewCallback mLinkPreviewCallback;
    private int MAX_ATTACHMENT_COUNT = 5;
    private ArrayList<Uri> docUris = new ArrayList<>();
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing conferenceCallContext menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);
            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

//            search_menu.close();

            MenuItem replyMenuItem = menu.findItem(R.id.action_reply);
            MenuItem deleteMenuItem = menu.findItem(R.id.action_delete);
            if (multiselect_list.size() == 0) {
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            } else if (multiselect_list.size() == 1) {
                deleteMenuItem.setVisible(isDeleteIconShowable());
                replyMenuItem.setVisible(true);
            } else if (multiselect_list.size() > 1) {
                deleteMenuItem.setVisible(isDeleteIconShowable());
                replyMenuItem.setVisible(false);
            }
            return true; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.action_delete) {
                alertDialogHelper.showAlertDialog("", getString(R.string.delete_), getString(R.string.delete).toUpperCase(), getString(R.string.cancel).toUpperCase(), 1, false);
                return true;
            } else if (itemId == R.id.action_reply) {
                sendButton.setVisibility(View.INVISIBLE);
                RealmChat realmChat = (RealmChat) multiselect_list.get(0);
                replyPrevFrame.setVisibility(View.VISIBLE);
                replyChatId = realmChat.getChat_id();
                if (realmChat.getText() == null) {
                    replyBody.setText(realmChat.getAttachment_title());
                } else {
                    replyBody.setText(realmChat.getText());
                }

                String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("CONSUMER")) {
                    replyName.setText(CONSUMER_NAME);
                } else if (role.equals("SELLER")) {
                    if (CONSUMER_ID.equals("")) {
                        replyName.setText(AGENT_NAME);
                    } else {
                        replyName.setText(SELLER_NAME);
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Object>();
            refreshAdapter();
        }
    };
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        chatContext = getApplicationContext();
        messageActivity = this;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        nametextview = findViewById(R.id.nametextview);
        availability = findViewById(R.id.availability);
        name_layout = findViewById(R.id.name_layout);

        String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
        Realm.init(MessageActivity.this);
        Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
            if (role.equals("CONSUMER")) {
                availability.setVisibility(View.VISIBLE);
                availability.setText(getIntent().getStringExtra("AVAILABILITY"));

                Realm.init(getApplicationContext());
                realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmConsumer.class).equalTo("consumer_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();

                CONSUMER_ID = realmConsumer.getConsumer_id();
                PROFILE_IMAGE_URL = getIntent().getStringExtra("PROFILE_IMAGE_URL");
                SELLER_ID = getIntent().getStringExtra("SELLER_ID");
                CONSUMER_NAME = realmConsumer.getName();
                SELLER_NAME = getIntent().getStringExtra("SELLER_NAME");

                NAME = SELLER_NAME;
                nametextview.setText(SELLER_NAME);
            } else {
                PROFILE_IMAGE_URL = getIntent().getStringExtra("PROFILE_IMAGE_URL");
                CONSUMER_ID = getIntent().getStringExtra("CONSUMER_ID");
                SELLER_ID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "SELLER_ID", "");

                if (getIntent().hasExtra("AGENT_ID")) {
                    AGENT_ID = getIntent().getStringExtra("AGENT_ID");
                    availability.setVisibility(View.VISIBLE);
                    availability.setText(Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmAgent.class).findFirst().getAvailability());

                    AGENT_NAME = getIntent().getStringExtra("AGENT_NAME");

                    NAME = AGENT_NAME;
                    nametextview.setText(AGENT_NAME);
                } else {
                    availability.setVisibility(View.GONE);


                    CONSUMER_NAME = getIntent().getStringExtra("CONSUMER_NAME");
                    NAME = CONSUMER_NAME;
                    nametextview.setText(CONSUMER_NAME);
                }

                Realm.init(getApplicationContext());
                realmSeller = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmSeller.class).equalTo("seller_id", SELLER_ID).findFirst();
                SELLER_NAME = realmSeller.getShop_name();
            }
        });

        networkReceiver = new NetworkReceiver();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.pls_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        alertDialogHelper = new AlertDialogHelper(this);
        textCrawler = new TextCrawler();
        emojiButton = findViewById(R.id.emoji_btn);
        participantno = findViewById(R.id.participantno);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MessageActivity.this, menu);
                popupMenu.getMenuInflater().inflate(R.menu.chat_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.audio_call) {
                            String phone_number = "";
                            String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
                            if (role.equals("CONSUMER")) {
                                Realm.init(getApplicationContext());
                                phone_number = Realm.getInstance(RealmUtility.getDefaultConfig(MessageActivity.this)).where(RealmConsumer.class).equalTo("consumer_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(MessageActivity.this).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst().getPrimary_contact();
                            } else if (role.equals("SELLER")) {
                                phone_number = Realm.getInstance(RealmUtility.getDefaultConfig(MessageActivity.this)).where(RealmSeller.class).equalTo("seller_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(MessageActivity.this).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst().getPrimary_contact();
                            }

                            CallSellerMaterialDialog callSellerMaterialDialog = new CallSellerMaterialDialog();
                            if (callSellerMaterialDialog != null && callSellerMaterialDialog.isAdded()) {

                            } else {
                                callSellerMaterialDialog.setPhone_number(phone_number);
                                callSellerMaterialDialog.setConsumer_id(CONSUMER_ID);
                                callSellerMaterialDialog.setSeller_id(SELLER_ID);
                                callSellerMaterialDialog.show(getSupportFragmentManager(), "");
                            }
                        } else if (itemId == R.id.video_call) {
                        }
                        return true;
                    }
                });
            }
        });


        mLinkPreviewCallback = new LinkPreviewCallback() {
            @Override
            public void onPre() {

            }

            @Override
            public void onPos(SourceContent sourceContent, boolean b) {
                List<String> images = sourceContent.getImages();
                String title = sourceContent.getTitle();
                String description = sourceContent.getDescription();
                if ((images.size() == 0) && (title.length() == 0) && (description.length() == 0)) {
                    linkPrevFrame.setVisibility(View.GONE);
                } else {
                    linkPrevFrame.setVisibility(View.VISIBLE);
                }

                if (sourceContent.getImages() != null && sourceContent.getImages().size() > 0 && sourceContent.getImages().get(0).toLowerCase().startsWith("http")) {
                    linkimgurl = sourceContent.getImages().get(0);
                    Glide.with(chatContext).
                            load(linkimgurl)
                            .into(linkImage);
                    linkImage.setVisibility(View.VISIBLE);

                } else {
                    linkimgurl = "";
                    linkImage.setVisibility(View.GONE);
                }
                if (sourceContent.getTitle() != null && sourceContent.getTitle().length() > 0) {
                    linkTitle.setText(sourceContent.getTitle());
                    linkTitle.setVisibility(View.VISIBLE);
                } else {
                    linkTitle.setVisibility(View.GONE);
                }
                if (sourceContent.getFinalUrl() != null && sourceContent.getFinalUrl().length() > 0) {
                    link = sourceContent.getFinalUrl();
                } else {
                    link = "";
                }
                if (sourceContent.getDescription() != null && sourceContent.getDescription().length() > 0) {
                    linkDesc.setText(sourceContent.getDescription());
                    linkDesc.setVisibility(View.VISIBLE);
                } else {
                    linkDesc.setVisibility(View.GONE);
                }
            }
        };

        pdfView = findViewById(R.id.pdfView);

        profileimg = findViewById(R.id.profileimg);

        if (PROFILE_IMAGE_URL != null && !PROFILE_IMAGE_URL.equals("")) {
            Glide.with(getApplicationContext()).load(PROFILE_IMAGE_URL).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user_icon_white)).into(profileimg);
        }

        pickfile = findViewById(R.id.pickfile);
        downbtn = findViewById(R.id.upbtn);
        upbtn = findViewById(R.id.downbtn);
        participantsBtn = findViewById(R.id.participantsBtn);
        imageView = findViewById(R.id.imageView);
        doc = findViewById(R.id.upcomingdoc);
        gal = findViewById(R.id.gal);
        audio = findViewById(R.id.audio);
        loc = findViewById(R.id.loc);
        progressBar = findViewById(R.id.pbar_pic);
        recylerView = findViewById(R.id.recyrlerView);
        recyrlerView_participant = findViewById(R.id.recyrlerView_participant);
        controls = findViewById(R.id.add);
        statusMsg = findViewById(R.id.statusMsg);
        attach = findViewById(R.id.attach);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendMessageButton);
        cardview = findViewById(R.id.card_view);
        participantslayout = findViewById(R.id.participantslayout);
        rootView = findViewById(R.id.root_view);
        emojIcon = new EmojIconActions(this, rootView, messageEditText, emojiButton);
        emojIcon.ShowEmojIcon();

        txtMsgFrame = findViewById(R.id.txt_msg_frame);
        txtMsg = findViewById(R.id.txt_msg);

        linkPrevFrame = findViewById(R.id.link_prev_frame);
        linkImage = findViewById(R.id.link_img);
        linkTextArea = findViewById(R.id.link_text_area);
        linkTitle = findViewById(R.id.link_title);
        linkDesc = findViewById(R.id.link_desc);
        close = findViewById(R.id.close);

        replyPrevFrame = findViewById(R.id.reply_prev_frame);
        replyTextArea = findViewById(R.id.reply_text_area);
        replyName = findViewById(R.id.reply_name);
        replyBody = findViewById(R.id.reply_body);
        replyClose = findViewById(R.id.replyClose);

        searchView = findViewById(R.id.searchView);

        profileimg.setOnClickListener(view -> {
            if (profileimg.getDrawable() == null) {
                //Toast.makeText(mContext, getString(R.string.image_not_set), Toast.LENGTH_SHORT).show();
            } else {
                PictureActivity.idPicBitmap = ((RoundedDrawable) profileimg.getDrawable()).getSourceBitmap();
                startActivity(new Intent(getApplicationContext(), PictureActivity.class));
            }
        });

        pickfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            try {
                startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                //alert user that file manager not working
                Toast.makeText(getApplicationContext(), R.string.error_occured, Toast.LENGTH_SHORT).show();
            }
        });
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
            }

            @Override
            public void onKeyboardClose() {

            }
        });
        chatAdapter = new ChatAdapter(new ChatAdapter.ChatAdapterInterface() {
            @Override
            public void onDocDownloadClick(ArrayList<Object> names, int position, MessageViewHolder holder) {
                /*registerForActivityResult(new GetContent(),
                        new ActivityResultCallback<Uri>() {
                            @Override
                            public void onActivityResult(Uri uri) {
                                // Handle the returned Uri
                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        });*/
            }
        }, objects, this, NAME, PROFILE_IMAGE_URL);


//        chatAdapter.setHasStableIds(true);
        mLinearLayoutManager = new LinearLayoutManager(chatContext);
        recylerView.setLayoutManager(mLinearLayoutManager);
        recylerView.setItemAnimator(null);
        setRecyclerViewAdapter();
        mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
        recylerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recylerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (objects.get(position) instanceof RealmChat) {
                    if (isMultiSelect)
                        multi_select(position);

                    if (mActionMode != null) {
                        mActionMode.invalidate();
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (objects.get(position) instanceof RealmChat) {
                    if (!isMultiSelect) {
                        multiselect_list = new ArrayList<Object>();
                        isMultiSelect = true;

                        if (mActionMode == null) {
                            mActionMode = startSupportActionMode(mActionModeCallback);
                        }
                    }
                    multi_select(position);
                    mActionMode.invalidate();
                }
            }
        }));

        /*participants.clear();
        populateParticipants(getApplicationContext());
        participants.addAll(newParticipants);
        participantsAdapter = new ParticipantsAdapter(participants);
        participantsAdapter.setHasStableIds(true);
        recyrlerView_participant.setLayoutManager(new LinearLayoutManager(chatContext));
        recyrlerView_participant.setAdapter(participantsAdapter);*/

        close.setOnClickListener(v -> {
            linkPrevFrame.setVisibility(View.GONE);
            if (replyPrevFrame.getVisibility() == View.GONE && messageEditText.getText().toString().trim().length() == 0) {
                sendButton.setVisibility(View.GONE);
            }
        });

        replyClose.setOnClickListener(v -> {
            replyPrevFrame.setVisibility(View.GONE);
            if (linkPrevFrame.getVisibility() == View.GONE && messageEditText.getText().toString().trim().length() == 0) {
                sendButton.setVisibility(View.GONE);
            }
        });
        participantsBtn.setOnClickListener(v -> {
            Drawable currentDrawable = participantsBtn.getDrawable();

            Drawable groupForegroundIconDrawable = getResources().getDrawable(R.drawable.group_foreground);
            Drawable groupBackgroundIconDrawable = getResources().getDrawable(R.drawable.group_background);

            Drawable.ConstantState groupForegroundIconConstantState = groupForegroundIconDrawable.getConstantState();
            Drawable.ConstantState groupBackgroundIconConstantState = groupBackgroundIconDrawable.getConstantState();
            Drawable.ConstantState currentIconConstantState = currentDrawable.getConstantState();
            if (currentIconConstantState.equals(groupBackgroundIconConstantState)) {
                participantslayout.setVisibility(View.GONE);
                searchView.setVisibility(View.VISIBLE);
                participantsBtn.setImageDrawable(groupForegroundIconDrawable);
            } else {
                participantslayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                participantsBtn.setImageDrawable(groupBackgroundIconDrawable);
            }
        });

        downbtn.setOnClickListener(v -> {
            pdfView.setVisibility(View.GONE);
            downbtn.setVisibility(View.GONE);
            upbtn.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
        });

        if (docFile == null) {
            downbtn.setVisibility(View.GONE);
            upbtn.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);

            uri = Uri.fromFile(docFile);
            displayFromUri(uri);
        }

        upbtn.setOnClickListener(v -> {
            pdfView.setVisibility(View.VISIBLE);
            downbtn.setVisibility(View.VISIBLE);
            upbtn.setVisibility(View.GONE);
            participantslayout.setVisibility(View.GONE);
            participantsBtn.setImageDrawable(getResources().getDrawable(R.drawable.group_foreground));
            searchView.setVisibility(View.GONE);
        });

        linkPrevFrame.setOnClickListener(v -> {
            if (!link.equals("")) {
                Uri webpage = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


        doc.setOnClickListener(v -> {
            Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                         @Override
                                                         public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                             if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                                                 openFile();
                                                             }
                                                         }

                                                         @Override
                                                         public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                             Permiso.getInstance().showRationaleInDialog(getString(R.string.permissions), getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                         }
                                                     },
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        });

        gal.setOnClickListener(v -> {
//            multiPickerWrapper.getPermissionAndPickMultipleImage();
            if (mMediaSelectedList != null) {
                mMediaSelectedList.clear();
            }
            MediaOptions.Builder builder = new MediaOptions.Builder();
            MediaOptions options = builder.canSelectMultiPhoto(true)
                    .canSelectMultiVideo(true).canSelectBothPhotoVideo()
                    .setMediaListSelected(mMediaSelectedList).build();

            if (options != null) {
                MediaPickerActivity.Companion.open(this, REQUEST_MEDIA, options);
            }
        });

        audio.setOnClickListener(v -> {
            recAudioClicked();
        });

        loc.setOnClickListener(v -> {
            Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                         @Override
                                                         public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                             if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                                 if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER")) {
                                                                     startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class)
                                                                                     .putExtra("LONGITUDE", realmConsumer.getLongitude())
                                                                                     .putExtra("LATITUDE", realmConsumer.getLatitude())
                                                                                     .putExtra("BUTTON_TEXT", "SEND LOCATION")
                                                                             , MapsActivity.RC_CONFIRM_LOCATION);
                                                                 } else {
                                                                     startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class)
                                                                                     .putExtra("LONGITUDE", realmSeller.getLongitude())
                                                                                     .putExtra("LATITUDE", realmSeller.getLatitude())
                                                                                     .putExtra("BUTTON_TEXT", "SEND LOCATION")
                                                                             , MapsActivity.RC_CONFIRM_LOCATION);
                                                                 }
                                                             }
                                                         }

                                                         @Override
                                                         public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                             Permiso.getInstance().showRationaleInDialog(getApplicationContext().getString(R.string.permissions), getApplicationContext().getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                         }
                                                     },
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        });
        attach.setOnClickListener(view -> {
            if (cardview.getVisibility() == View.GONE) {
                cardview.setVisibility(View.VISIBLE);
                //cardview.requestFocus();
                //cardview.requestFocusFromTouch();
            } else {
                cardview.setVisibility(View.GONE);
            }
        });
        // Enable Send button when there's text to send
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    sendButton.setVisibility(View.GONE);
                }

                textCrawler.cancel();
                textCrawler.makePreview(mLinkPreviewCallback, s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Send button sends a message and clears the EditText
        sendButton.setOnClickListener(view -> {
            String text = StringEscapeUtils.escapeJava(messageEditText.getText().toString().trim());
            if (text.length() > 0) {

                RealmChat realmChat = new RealmChat(
                        getId(),
                        UUID.randomUUID().toString(),
                        replyPrevFrame.getVisibility() == View.VISIBLE ? replyChatId : null,
                        text,
                        linkPrevFrame.getVisibility() == View.VISIBLE && !link.equals("") ? link : null,
                        linkPrevFrame.getVisibility() == View.VISIBLE ? linkTitle.getText().toString() : null,
                        linkPrevFrame.getVisibility() == View.VISIBLE ? linkDesc.getText().toString() : null,
                        linkPrevFrame.getVisibility() == View.VISIBLE && !linkimgurl.equals("") ? linkimgurl : null,
                        null,
                        null,
                        null,
                        0,
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                        null,
                        CONSUMER_ID,
                        SELLER_ID,
                        AGENT_ID,
                        "",
                        "",
                        null,
                        null
                );

                saveTempChatToRealm(realmChat);

                String json_string = new Gson().toJson(realmChat);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json_string);
                    SendChatMsg(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                messageEditText.setText("");
                linkimgurl = "";
                link = "";
                linkPrevFrame.setVisibility(View.GONE);
                replyPrevFrame.setVisibility(View.GONE);
            }
        });

        messageEditText.setOnClickListener(v -> cardview.setVisibility(View.GONE));

        recylerView.setOnClickListener(v -> cardview.setVisibility(View.GONE));

        init();
        initChatSocket();
        if (objects.size() == 0 && isNetworkAvailable(chatContext)) {
            Toast toast = Toast.makeText(chatContext, "Checking for new chats...", Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        initSearchView1(objects, chatAdapter);
        filter(objects, "");
    }

    private int getId() {
        Realm.init(messageActivity);
        return Realm.getInstance(RealmUtility.getDefaultConfig(messageActivity)).where(RealmChat.class).max("id").intValue() + 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatSocket != null) {
            chatSocket.leave("chat:" + CONSUMER_ID + SELLER_ID + AGENT_ID);
            chatSocket.clearListeners();
            chatSocket.close();
            chatSocket.terminate();
            chatSocket = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) PreferenceManager.getDefaultSharedPreferences(this).getLong("com.ekumfi.juice" + PREF_MSG_LENGTH_KEY, DEFAULT_MSG_LENGTH))});
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onStop() {
        for (Map.Entry me : downFileAsyncMap.entrySet()) {
            AsyncTask<String, Integer, String> downloadFileAsync = (AsyncTask<String, Integer, String>) me.getValue();
            if (downloadFileAsync != null && downloadFileAsync.getStatus() != AsyncTask.Status.FINISHED) {
                // This would not cancel downloading from httpClient
                //  we have do handle that manually in onCancelled event inside AsyncTask
                downloadFileAsync.cancel(true);
            }
        }
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cardview.setVisibility(View.GONE);
        String chatid = UUID.randomUUID().toString();
        switch (requestCode) {
            case REQUEST_MEDIA:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mMediaSelectedList = MediaPickerActivity.Companion
                                .getMediaItemSelected(data);
                        if (mMediaSelectedList != null) {
                            cardview.setVisibility(View.GONE);

                            for (MediaItem mediaItem : mMediaSelectedList) {
                                String path = mediaItem.getPathOrigin(chatContext);
                                File file = new File(path);
                                if (file.length() > 10000000L) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.file_size_of) + " " + fileSize(file.length()) + " " + getString(R.string.larger_than_limit), Toast.LENGTH_SHORT).show();
                                } else {
                                    RealmChat realmChat = new RealmChat(
                                            getId(),
                                            chatid,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            "URI" + file.getAbsolutePath(),
                                            mediaItem.getType() == 1 ? "image" : "video",
                                            Uri.fromFile(file).getLastPathSegment(),
                                            0,
                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                                            null,
                                            CONSUMER_ID,
                                            SELLER_ID,
                                            AGENT_ID,
                                            "",
                                            "",
                                            null,
                                            null
                                    );

                                    if (isExternalStorageWritable()) {
                                        String folder = mediaItem.getType() == 1 ? "Images" : "Video";
                                        File file_dir = null;
                                        if (folder.equals("Images")) {
                                            file_dir = new File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats", "Sent");
                                        } else {
                                            file_dir = new File(getExternalStoragePublicDirectory(DIRECTORY_MOVIES).getAbsolutePath() + "/EkumfiJuice/Chats", "Sent");
                                        }
                                        if (!file_dir.exists()) {
                                            file_dir.mkdirs();
                                        }
                                        String ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                                        String destinationPath;
                                        if (folder.equals("Images")) {
                                            destinationPath = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getAbsolutePath() + "/EkumfiJuice/Chats" + "/Sent/" + chatid + ext;
                                        } else {
                                            destinationPath = getExternalStoragePublicDirectory(DIRECTORY_MOVIES).getAbsolutePath() + "/EkumfiJuice/Chats" + "/Sent/" + chatid + ext;
                                        }

                                        try {
                                            FileUtils.copyFile(file.getAbsolutePath(), destinationPath);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        saveTempChatToRealm(realmChat);
                                        new SendChatAttachmentAsyncTask(chatContext, realmChat).execute();
                                    }
                                }
                            }
//                            scrollview.setVisibility(View.VISIBLE);
//                            mMediaSelectedList.clear();
                        } else {
                            Log.e(TAG, "Error to get media, NULL");
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;

            case RC_CONFIRM_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            Double longitude = data.getDoubleExtra("LONGITUDE", 0.0d);
                            Double latitude = data.getDoubleExtra("LATITUDE", 0.0d);

                            RealmChat realmChat = new RealmChat(
                                    getId(),
                                    chatid,
                                    null,
                                    null,
                                    null,
                                    null,
                                    latitude + "," + longitude,
                                    null,
                                    "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=" + getResources().getString(R.string.google_maps_key),
                                    "map",
                                    "",
                                    0,
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                                    null,
                                    CONSUMER_ID,
                                    SELLER_ID,
                                    AGENT_ID,
                                    "",
                                    "",
                                    null,
                                    null
                            );

                            saveTempChatToRealm(realmChat);

                            String json_string = new Gson().toJson(realmChat);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(json_string);
                                SendChatMsg(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // some stuff that will happen if there's no result
                        break;
                }
                break;

            case RC_AUDIO_PICKER:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        if (wavFile.length() > 10000000L) {
                            Toast.makeText(getApplicationContext(), getString(R.string.file_size_of) + " " + fileSize(wavFile.length()) + " " + getString(R.string.larger_than_limit), Toast.LENGTH_SHORT).show();
                        } else {
                            if (isExternalStorageWritable()) {
                                File file_dir = new File(getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats", "Sent");
                                if (!file_dir.exists()) {
                                    file_dir.mkdirs();
                                }
                                String destinationPath = getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats/Sent/" + chatid + ".aac";

                                long executionId = FFmpeg.executeAsync("-i " + wavFile.getAbsolutePath() + " -acodec aac " + destinationPath, new ExecuteCallback() {

                                    @Override
                                    public void apply(final long executionId, final int returnCode) {
                                        if (returnCode == RETURN_CODE_SUCCESS) {
                                            Log.i(Config.TAG, "Async command execution completed successfully.");

                                            if (isExternalStorageWritable()) {
                                                File file_dir = new File(getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getAbsolutePath() + "/EkumfiJuice/Chats", "Sent");
                                                if (!file_dir.exists()) {
                                                    file_dir.mkdirs();
                                                }
                                                RealmChat realmChat = new RealmChat(
                                                        getId(),
                                                        chatid,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        "URI" + destinationPath,
                                                        "audio",
                                                        Uri.fromFile(wavFile).getLastPathSegment(),
                                                        0,
                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                                                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                                                        null,
                                                        CONSUMER_ID,
                                                        SELLER_ID,
                                                        AGENT_ID,
                                                        "",
                                                        "",
                                                        null,
                                                        null
                                                );

                                                saveTempChatToRealm(realmChat);
                                                new SendChatAttachmentAsyncTask(chatContext, realmChat).execute();
                                            }
                                        } else if (returnCode == RETURN_CODE_CANCEL) {
                                            Log.i(Config.TAG, "Async command execution cancelled by user.");
                                        } else {
                                            Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                                        }
                                    }
                                });
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(chatContext, getString(R.string.audio_recording_cancelled), Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

            case RC_DOC:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri content_scheme_uri = data.getData();
                        String filePath = getDriveFilePath(getApplicationContext(), content_scheme_uri);
                        Cursor returnCursor =
                                getContentResolver().query(content_scheme_uri, null, null, null, null);
                        /*
                         * Get the column indexes of the data in the Cursor,
                         * move to the first row in the Cursor, get the data,
                         * and display it.
                         */
                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

                        returnCursor.moveToFirst();
                        String file_name = returnCursor.getString(nameIndex);
                        long myFileSize = returnCursor.getLong(sizeIndex);

                        File file = new File(content_scheme_uri.getPath());
                        if (file.length() > 10000000L) {
                            Toast.makeText(this, getString(R.string.file_size_of) + " " + fileSize(file.length()) + " " + getString(R.string.larger_than_limit), Toast.LENGTH_SHORT).show();
                        } else {

                            RealmChat realmChat = new RealmChat(
                                    getId(),
                                    chatid,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    "URI" + filePath,
                                    "document",
                                    file_name,
                                    0,
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                                    null,
                                    CONSUMER_ID,
                                    SELLER_ID,
                                    AGENT_ID,
                                    "",
                                    "",
                                    null,
                                    null
                            );

                            if (isExternalStorageWritable()) {
                                File file_dir = new File(getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getAbsolutePath() + "/EkumfiJuice/Chats", "Sent");
                                if (!file_dir.exists()) {

                                    file_dir.mkdirs();
                                }
                                String ext = filePath.substring(filePath.lastIndexOf("."));
                                String destinationPath = getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getAbsolutePath() + "/EkumfiJuice/Chats//Sent/" + chatid + ext;

                                try {
                                    FileUtils.copyFile(filePath, destinationPath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                saveTempChatToRealm(realmChat);
                                new SendChatAttachmentAsyncTask(chatContext, realmChat).execute();
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;

            case RC_OPEN_DOCUMENT:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            uri = data.getData();
                            // Perform operations on the document using its URI.

                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // Check for the freshest data.
                            getContentResolver().takePersistableUriPermission(uri, takeFlags);

                            String absolutePath = docFile.getAbsolutePath();
                            String mimeType = Const.getMimeType(absolutePath);


                            MediaScannerConnection.scanFile(getApplicationContext(),
                                    new String[]{docFile.getAbsolutePath()},
                                    null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.d("uriwergtwe", uri.toString());
//                                            Uri docURI = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(uri.getPath()));
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setDataAndType(uri, mimeType);
                                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            try {
                                                startActivity(intent);
                                            } catch (ActivityNotFoundException e) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.no_suitable_app_for_viewing_this_file), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPositiveClick(int from) {
        if (from == 1) {
            if (multiselect_list.size() > 0) {
                Realm.init(getApplicationContext());
                Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                    for (int i = 0; i < multiselect_list.size(); i++) {
                        RealmChat tempRealmChat = Realm.getInstance(RealmUtility.getDefaultConfig(chatContext))
                                .where(RealmChat.class)
                                .equalTo("chat_id", ((RealmChat) multiselect_list.get(i)).getChat_id())
                                .findFirst();
                        objects.remove(multiselect_list.get(i));
                        if (tempRealmChat != null) {
                            tempRealmChat.deleteFromRealm();
                        }
                        chatAdapter.notifyDataSetChanged();
                        if (objects.size() == 0) {
                            statusMsg.setVisibility(View.VISIBLE);
                        }
                    }
                });


                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        } else if (from == 2) {
//            if (mActionMode != null) {
//                mActionMode.finish();
//            }

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }

    @AfterPermissionGranted(RC_AUDIO_AND_STORAGE)
    public void recAudioClicked() {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            wavFile = new File(getFilesDir() + "/EkumfiJuice", "temp.wav");
            File parentFile = wavFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            int color = getResources().getColor(R.color.colorPrimaryDark);
            int requestCode = 0;
            AndroidAudioRecorder.with(MessageActivity.this)
                    // Required
                    .setFilePath(wavFile.getAbsolutePath())
                    .setColor(color)
                    .setRequestCode(requestCode)

                    // Optional
                    .setSource(AudioSource.MIC)
                    .setChannel(AudioChannel.STEREO)
                    .setSampleRate(AudioSampleRate.HZ_8000)
                    .setAutoStart(false)
                    .setKeepDisplayOn(true)

                    // Start recording
                    .record();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_AUDIO_AND_STORAGE, perms);
        }
    }

    @AfterPermissionGranted(RC_STORAGE)
    public void setRecyclerViewAdapter() {
        if (EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            recylerView.setAdapter(chatAdapter);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.we_need_this_permission_to_display_chat_messages),
                    RC_STORAGE, WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

        if (meta != null) {
            Log.e(TAG, "title = " + meta.getTitle());
            Log.e(TAG, "author = " + meta.getAuthor());
            Log.e(TAG, "subject = " + meta.getSubject());
            Log.e(TAG, "keywords = " + meta.getKeywords());
            Log.e(TAG, "creator = " + meta.getCreator());
            Log.e(TAG, "producer = " + meta.getProducer());
            Log.e(TAG, "creationDate = " + meta.getCreationDate());
            Log.e(TAG, "modDate = " + meta.getModDate());
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load offset " + page);
    }

    private static ArrayList<Object> filter(ArrayList<Object> models, String search_txt) {

        if (search_txt.equals("")) {
            return models;
        }
        search_txt = search_txt.toLowerCase();
        final ArrayList<Object> filteredModelList = new ArrayList<>();
        for (Object model : models) {

            if (model instanceof RealmChat && ((RealmChat) model).getText() != null) {
                final String text1 = ((RealmChat) model).getText().toLowerCase();

                if (text1.contains(search_txt)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    public void refreshAdapter() {
        chatAdapter.selected_usersList = multiselect_list;
        chatAdapter.consolidatedList = objects;
        chatAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(objects.get(position)))
                multiselect_list.remove(objects.get(position));
            else
                multiselect_list.add(objects.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public static void initSearchView1(final ArrayList<Object> searchchats, final ChatAdapter chatAdapter) {
//        SearchView searchView = (SearchView) search_menu.findItem(R.id.action_search).getActionView();

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final ArrayList<Object> filteredModelList = filter(searchchats, query);
                chatAdapter.setFilter(filteredModelList);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                ArrayList<Object> filteredModelList = new ArrayList<Object>();
                filteredModelList = filter(searchchats, newText);
                chatAdapter.setFilter(filteredModelList);
                Log.d("Text", "Canging" + filteredModelList.size());
                return true;
            }

        });
        searchView.setOnSearchClickListener(v -> name_layout.setVisibility(View.GONE));
        searchView.setOnCloseListener(() -> {
            name_layout.setVisibility(View.VISIBLE);
            return false;
        });
    }

    public void SendChatMsg(JSONObject request) {
        final String[] URL = {null};
        try {
            URL[0] = API_URL + "chats";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    POST,
                    URL[0],
                    request,
                    response -> {
                        Log.d("Cyrilll", response.toString());
                        if (response != null) {
                            final RealmChat[] realmChat = new RealmChat[1];
                            Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                try {
                                    realmChat[0] = realm.createOrUpdateObjectFromJson(RealmChat.class, response.getJSONObject("chat"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            });
                            updateSentChatStatus(realmChat[0]);
                            broadcastWithSocket(response.toString());
                            new broadcastWithFirebase(response).execute();
                        }
                    },
                    error -> error.printStackTrace()
            ) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(chatContext).getString("com.ekumfi.juice" + APITOKEN, ""));
                    return headers;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            InitApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSentChatStatus(RealmChat realmChat) {
        for (int i = 0; i < objects.size(); i++) {
            Object obj = objects.get(i);
            if (obj instanceof RealmChat) {
                if (((RealmChat) obj).getChat_id().equals(realmChat.getChat_id())) {
                    objects.set(i, realmChat);
                    chatAdapter.notifyItemChanged(i);
                    mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
                }
            }
        }
    }

    @Override
    public void onCurrentLocationClick() {
        locationTypeMaterialDialog.dismiss();
        cardview.setVisibility(View.GONE);
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER")) {
            startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class)
                            .putExtra("LONGITUDE", realmConsumer.getLongitude())
                            .putExtra("LATITUDE", realmConsumer.getLatitude())
                            .putExtra("BUTTON_TEXT", "SEND LOCATION")
                    , MapsActivity.RC_CONFIRM_LOCATION);
        } else {
            startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class)
                            .putExtra("LONGITUDE", realmSeller.getLongitude())
                            .putExtra("LATITUDE", realmSeller.getLatitude())
                            .putExtra("BUTTON_TEXT", "SEND LOCATION")
                    , MapsActivity.RC_CONFIRM_LOCATION);
        }
    }

    @Override
    public void onLiveLocationClick() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    sendLiveLocation(location.getLongitude(), location.getLatitude());
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                            }
                            Boolean coarseLocationGranted = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            }
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // Got last known location. In some rare situations this can be null.
                                                if (location != null) {
                                                    // Logic to handle location object

                                                    sendLiveLocation(location.getLongitude(), location.getLatitude());
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                                    /*new GoogleMap.OnMyLocationChangeListener() {
                                                        @Override
                                                        public void onMyLocationChange(Location location) {
                                                            Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                                                        }
                                                    };*/
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // Got last known location. In some rare situations this can be null.
                                                if (location != null) {
                                                    // Logic to handle location object
                                                    sendLiveLocation(location.getLongitude(), location.getLatitude());
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            } else {
                                // No location access granted.
                            }
                        }
                );

                // ...

                // Before you perform the actual permission request, check whether your app
                // already has the permissions, and whether your app needs to show a permission
                // rationale dialog. For more details, see Request permissions.
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        } else {
            ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                        }
                        Boolean coarseLocationGranted = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        }
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                // Logic to handle location object
                                                sendLiveLocation(location.getLongitude(), location.getLatitude());
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                                    /*new GoogleMap.OnMyLocationChangeListener() {
                                                        @Override
                                                        public void onMyLocationChange(Location location) {
                                                            Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                                                        }
                                                    };*/
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                            fusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                // Logic to handle location object
                                                sendLiveLocation(location.getLongitude(), location.getLatitude());
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // No location access granted.
                        }
                    }
            );

            // ...

            // Before you perform the actual permission request, check whether your app
            // already has the permissions, and whether your app needs to show a permission
            // rationale dialog. For more details, see Request permissions.
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    public void sendLiveLocation(double lng, double lat) {
        locationTypeMaterialDialog.dismiss();
        cardview.setVisibility(View.GONE);
        Double longitude = lng;
        Double latitude = lat;

        RealmChat realmChat = new RealmChat(
                getId(),
                UUID.randomUUID().toString(),
                null,
                null,
                null,
                null,
                latitude + "," + longitude,
                null,
                "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=" + getResources().getString(R.string.google_maps_key),
                "live_location",
                "",
                0,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? 1 : 0,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", ""),
                "live_location",
                CONSUMER_ID,
                SELLER_ID,
                AGENT_ID,
                "",
                "",
                null,
                null
        );

        saveTempChatToRealm(realmChat);

        String json_string = new Gson().toJson(realmChat);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json_string);
            SendChatMsg(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SendChatAttachmentAsyncTask extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        RealmChat realmChat;
        private Context context;
        private Exception exception;
        // private ProgressDialog progressDialog;

        private SendChatAttachmentAsyncTask(Context context, RealmChat realmChat) {
            this.context = context;
            this.realmChat = realmChat;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = keyConst.API_URL + "chats";
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                Uri uri = Uri.parse(realmChat.getAttachment_url().substring(3));
                File file = new File(uri.getPath());
                multipartEntityBuilder.addPart("file", new FileBody(file));

                multipartEntityBuilder.addTextBody("attachment_type", realmChat.getAttachment_type());
                multipartEntityBuilder.addTextBody("attachment_title", realmChat.getAttachment_title());
                multipartEntityBuilder.addTextBody("chat_id", realmChat.getChat_id());
                multipartEntityBuilder.addTextBody("consumer_id", CONSUMER_ID);
                multipartEntityBuilder.addTextBody("seller_id", SELLER_ID);
                multipartEntityBuilder.addTextBody("sent_by_consumer", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? "1" : "0");

                // Progress listener - updates task's progress
                MyHttpEntity.ProgressListener progressListener =
                        progress -> publishProgress((int) progress);

                // POST
                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                        progressListener));
                httpPost.setHeader("accept", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));


                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();
//                Log.d("56876", EntityUtils.toString(httpEntity));
                if (statusCode == 200 || statusCode == 201) {
                    // Server response
                    responseString = EntityUtils.toString(httpEntity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                e.printStackTrace();
                Log.e("UPLOAD", e.getMessage());
                this.exception = e;
            } catch (IOException e) {
                if (e.toString().contains("HttpHostConnectException")) {
                    responseString = "Connection error";
                } else {
                    responseString = "Error occurred! Http Status Code: ";
                }
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if (result.contains("Error occurred! Http Status Code: ")) {
                    Toast.makeText(context, context.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                } else if (result.equals("Connection error")) {
                    Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject responseJson = null;
                    try {
                        responseJson = new JSONObject(result);
                        final RealmChat[] realmChat = new RealmChat[1];
                        JSONObject finalResponseJson1 = responseJson;
                        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                            try {
                                realmChat[0] = realm.createOrUpdateObjectFromJson(RealmChat.class, finalResponseJson1.getJSONObject("chat"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        updateSentChatStatus(realmChat[0]);
                        broadcastWithSocket(responseJson.toString());
                        new broadcastWithFirebase(responseJson).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
    }

    public void init() {
        statusMsg.setText(NO_CLASS_CHATS);
        controls.setVisibility(View.VISIBLE);
        recylerView.setVisibility(View.VISIBLE);
        objects.clear();
        populateObjects(chatContext);
        objects.addAll(newObjects);
        chatAdapter.notifyDataSetChanged();
        mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
    }

    public void initChatSocket() {
        chatSocket = Socket
                .Builder.with(GUID_WS_URL)
                .build();
        chatSocket.connect();
        chatSocket.clearListeners();

        chatSocket.onEvent(EVENT_OPEN, new Socket.OnEventListener() {
            @Override
            public void onMessage(String event) {
                Log.d("mywebsocket1", "Connected");

                chatSocket.join("chat:" + CONSUMER_ID + SELLER_ID + AGENT_ID);

                chatSocket.onEventResponse("chat:" + CONSUMER_ID + SELLER_ID + AGENT_ID, new Socket.OnEventResponseListener() {
                    @Override
                    public void onMessage(String event, String data) {

                    }
                });

                chatSocket.setMessageListener(new Socket.OnMessageListener() {
                    @Override
                    public void onMessage(String data) {
                        JSONObject jsonObject = null;
                        JSONObject jsonResponse = null;
                        String message = "";
                        try {
                            jsonObject = new JSONObject(data);
                            switch (jsonObject.getInt("t")) {
                                case 0:
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 7:
                                    jsonResponse = jsonObject.getJSONObject("d");
                                    Log.d("mywebsocket1", jsonResponse.toString());
                                    Realm.init(chatContext);
                                    JSONObject finalJsonResponse = jsonResponse;
                                    if (finalJsonResponse.getJSONObject("data").has("connected_chat")) {
                                        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                            /*try {
                                                updateParticipantConnectionStatus(finalJsonResponse, realm, 1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/
                                        });

                                    } else if (finalJsonResponse.getJSONObject("data").has("disconnected_chat")) {
                                        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                            /*try {
                                                updateParticipantConnectionStatus(finalJsonResponse, realm, 0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }*/
                                        });
                                    } else if (finalJsonResponse.getJSONObject("data").has("chat")) {
                                        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                            try {
                                                RealmChat realmChat = realm.createOrUpdateObjectFromJson(RealmChat.class, finalJsonResponse.getJSONObject("data").getJSONObject("chat"));
                                                RealmChat realmReferencedChat = null;


                                                if (finalJsonResponse.getJSONObject("data").has("referenced_chat")) {
                                                    realmReferencedChat = realm.createOrUpdateObjectFromJson(RealmChat.class, finalJsonResponse.getJSONObject("data").getJSONObject("referenced_chat"));
                                                    if (realmReferencedChat != null) {
                                                        realmChat.setReply_body(realmReferencedChat.getText() != null ? realmReferencedChat.getText() : realmReferencedChat.getAttachment_title());
                                                        if (realmChat.getSent_by_consumer() == 1) {
                                                            realmChat.setReply_name(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? "Me" : CONSUMER_NAME);
                                                        } else {
                                                            realmChat.setReply_name(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("SELLER") ? "Me" : SELLER_NAME);
                                                        }
                                                    }
                                                }


                                                addMsgToChat(finalJsonResponse, realmChat);
                                            } catch (JSONException e) {

                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                    }
                                    else if (finalJsonResponse.getJSONObject("data").has("availability")) {
                                        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                            String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
                                            if (role.equals("CONSUMER")) {
                                                try {
                                                    String availability = finalJsonResponse.getJSONObject("data").getString("availability");
                                                    realm.where(RealmSeller.class).equalTo("seller_id", SELLER_ID).findFirst().setAvailability(availability);
                                                    MessageActivity.availability.setText(availability);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else if (role.equals("SELLER")) {
                                                if (CONSUMER_ID.equals("")) {
                                                    try {
                                                        String availability = finalJsonResponse.getJSONObject("data").getString("availability");
                                                        realm.where(RealmAgent.class).findFirst().setAvailability(availability);
                                                        MessageActivity.availability.setText(availability);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    try {
                                                        String availability = finalJsonResponse.getJSONObject("data").getString("availability");
                                                        realm.where(RealmSeller.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst().setAvailability(availability);
                                                        MessageActivity.availability.setText(availability);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    break;
                                case 8:
                                    break;
                                case 9:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                final String[] id = {"0"};
                Realm.init(chatContext);
                Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                    RealmResults<RealmChat> results = Realm.getInstance(RealmUtility.getDefaultConfig(chatContext))
                            .where(RealmChat.class)
                            .equalTo("consumer_id", CONSUMER_ID)
                            .equalTo("seller_id", SELLER_ID)
                            .sort("created_at", Sort.DESCENDING)
                            .findAll();
                    ArrayList<RealmChat> myArrayList = new ArrayList<>();
                    for (RealmChat realmChat : results) {
                        if (!(realmChat.getChat_id().startsWith("z"))) {
                            myArrayList.add(realmChat);
                        }
                    }
                    if (results.size() < 3) {
                        id[0] = "0";
                    }
                    else{
                        id[0] = String.valueOf(myArrayList.get(0).getId());
                    }
                });

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "scoped-chats",
                        response -> {
                            if (response != null) {
                                try {
                                    JSONObject jsonObjectResponse = new JSONObject(response);

                                    Realm.init(chatContext);
                                    Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                                        try {
                                            realm.createOrUpdateAllFromJson(RealmChat.class, jsonObjectResponse.getJSONArray("chats"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    try {
                                        JSONObject jsonData = new JSONObject()
                                                .put(
                                                        "connected_chat", PreferenceManager.getDefaultSharedPreferences(chatContext).getString("com.ekumfi.juice" + MYUSERID,  "")
                                                );
//                                        broadcastWithSocket(jsonData.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    init();
                                    sendUnsentChats();

                                    participants.clear();
//                                    populateParticipants(chatContext);
//                                    participants.addAll(newParticipants);
//                                    participantsAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            Log.d("Cyrilll", error.toString());
                            myVolleyError(chatContext, error);
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("consumer_id", CONSUMER_ID);
                        params.put("seller_id", SELLER_ID);
                        params.put("id", id[0]);
                        return params;
                    }

                    /* Passing some request headers*/
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("accept", "application/json");
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(chatContext).getString("com.ekumfi.juice" + APITOKEN, ""));
                        return headers;
                    }
                };
                ;
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                InitApplication.getInstance().addToRequestQueue(stringRequest);
            }
        });

        chatSocket.onEvent(EVENT_RECONNECT_ATTEMPT, new Socket.OnEventListener() {
            @Override
            public void onMessage(String event) {
                Log.d("mywebsocket1", "reconnecting");
            }
        });
        chatSocket.onEvent(EVENT_CLOSED, new Socket.OnEventListener() {
            @Override
            public void onMessage(String event) {
                Log.d("mywebsocket1", "connection closed");
            }
        });
    }

    public static void addMsgToChat(JSONObject finalJsonResponse, RealmChat realmChat) throws JSONException, ParseException {
        for (int i = 0; i < objects.size(); i++) {
            Object obj = objects.get(i);
            if (obj instanceof RealmChat) {
                RealmChat chat = (RealmChat) obj;

                if (((RealmChat) obj).getChat_id().equals(finalJsonResponse.getJSONObject("data").getJSONObject("chat").getString("chat_id"))) {
                    objects.set(i, realmChat);
                    chatAdapter.notifyItemChanged(i);
                    mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
                    return;
                }
            }
        }


        long time = 0;
        long prevTime = 0;
        String dateString = realmChat.getCreated_at();
        String prevDateString = null;
        if (dateString != null) {
            time = dateFormat.parse(dateString).getTime();
        }

        String formattedDate = getFormattedDate(chatContext, time);
        String prevFormattedDate = null;

        if (objects.size() == 0) {
            if (!formattedDate.equals("January 1, 1970")) {
                objects.add(new DateItem(formattedDate));
            }
        } else {
            prevDateString = ((RealmChat) objects.get(objects.size() - 1)).getCreated_at();
            if (prevDateString != null) {
                prevTime = dateFormat.parse(prevDateString).getTime();
                prevFormattedDate = getFormattedDate(chatContext, prevTime);
            }
            if (prevFormattedDate != null && !prevFormattedDate.equals(formattedDate)) {
                if (!formattedDate.equals("January 1, 1970")) {
                    objects.add(new DateItem(formattedDate));
                }
            }
        }


        objects.add(realmChat);

        chatAdapter.notifyItemChanged(objects.size() - 1);
        mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
    }

    public void populateObjects(Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
            String consumer_id = CONSUMER_ID;
            RealmResults<RealmChat> results = realm.where(RealmChat.class)
                    .equalTo("consumer_id", consumer_id)
                    .equalTo("seller_id", SELLER_ID)
                    .sort("created_at", Sort.ASCENDING)
                    .findAll();
            if (results.size() > 0) {
                statusMsg.setVisibility(View.GONE);
            }
            newObjects.clear();
            for (int i = 0; i < results.size(); i++) {
                RealmChat realmChat = results.get(i);
                Log.d("asdffdssdss", String.valueOf(realmChat.getId()));
                if (realmChat.getSent_by_consumer() == 1) {
                    realmChat.setName(CONSUMER_NAME.equals("") ? AGENT_NAME : CONSUMER_NAME);
                } else {
                    realmChat.setName(SELLER_NAME);
                }

                setChatRefParams(realmChat, context);

                if (results.size() > 0) {
                    try {
                        long time = 0;
                        long prevTime = 0;
                        String dateString = realmChat.getCreated_at();
                        String prevDateString = null;
                        if (dateString != null) {
                            time = dateFormat.parse(dateString).getTime();
                        }

                        String formattedDate = getFormattedDate(context, time);
                        String prevFormattedDate = null;

                        if (i == 0) {
                            if (formattedDate != null && !formattedDate.equals("January 1, 1970")) {
                                newObjects.add(new DateItem(formattedDate));
                            }
                        } else {
                            prevDateString = results.get(i - 1).getCreated_at();
                            if (prevDateString != null) {
                                prevTime = dateFormat.parse(prevDateString).getTime();
                                prevFormattedDate = getFormattedDate(context, prevTime);
                            }
                            if (prevFormattedDate != null && !prevFormattedDate.equals(formattedDate)) {
                                if (formattedDate != null && !formattedDate.equals("January 1, 1970")) {
                                    newObjects.add(new DateItem(formattedDate));
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                newObjects.add(realmChat);
            }
        });
    }

    public void sendUnsentChats() {
        if (messageActivity != null) {
            Realm.init(chatContext);
            Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
                RealmResults<RealmChat> results = realm.where(RealmChat.class)
                        .equalTo("consumer_id", CONSUMER_ID)
                        .equalTo("seller_id", SELLER_ID)
                        .beginsWith("created_at", "z")
                        .findAll();
                for (RealmChat realmChat : results) {
                    JSONObject jsonObject = null;
                    RealmChat recreatedRealChat = new RealmChat(
                            getId(),
                            realmChat.getChat_id(),
                            realmChat.getChat_ref_id(),
                            realmChat.getText(),
                            realmChat.getLink(),
                            realmChat.getLink_title(),
                            realmChat.getLink_description(),
                            realmChat.getLink_image(),
                            realmChat.getAttachment_url(),
                            realmChat.getAttachment_type(),
                            realmChat.getAttachment_title(),
                            realmChat.getSent_by_consumer(),
                            realmChat.getRead_by_recipient(),
                            realmChat.getSender_role(),
                            realmChat.getTag(),
                            realmChat.getConsumer_id(),
                            realmChat.getSeller_id(),
                            realmChat.getAgent_id(),
                            realmChat.getWholesaler_id(),
                            realmChat.getEkumfi_info_id(),
                            realmChat.getCreated_at(),
                            realmChat.getUpdated_at()
                    );
                    if (realmChat.getAttachment_url() != null && realmChat.getAttachment_url().startsWith("URI")) {
                        new SendChatAttachmentAsyncTask(chatContext, recreatedRealChat).execute();
                    } else {
                        try {
                            String json_string = new Gson().toJson(recreatedRealChat);
                            jsonObject = new JSONObject(json_string);
                            SendChatMsg(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    public void saveTempChatToRealm(RealmChat realmChat) {
        Realm.init(chatContext);
        Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).executeTransaction(realm -> {
            setChatRefParams(realmChat, chatContext);
            RealmChat chat = realm.where(RealmChat.class)
                    .equalTo("consumer_id", CONSUMER_ID)
                    .equalTo("seller_id", SELLER_ID)
                    .sort("created_at", Sort.DESCENDING)
                    .findFirst();
            if (chat == null) {
                realmChat.setCreated_at("z1975-05-05 14:24:16");
            } else {
                String created_at = chat
                        .getCreated_at();
                if (String.valueOf(created_at.charAt(0)).toLowerCase().equals("z")) {
                    created_at = created_at.substring(1);
                }
                try {
                    realmChat.setCreated_at("z" + Const.dateTimeFormat.format(Const.dateTimeFormat.parse(created_at).getTime() + 1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            realm.copyToRealmOrUpdate(realmChat);
            statusMsg.setVisibility(View.GONE);
            objects.add(realmChat);
            chatAdapter.notifyItemInserted(objects.size() - 1);
            mLinearLayoutManager.scrollToPositionWithOffset(objects.size() - 1, 0);
            if (realmChat.getAttachment_url() != null && !realmChat.getAttachment_type().equals("map") && !realmChat.getAttachment_type().equals("live_location")) {
                Toast.makeText(chatContext, chatContext.getString(R.string.uploading_file), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setChatRefParams(RealmChat realmChat, Context context) {
        if (realmChat.getChat_ref_id() != null) {
            RealmChat realmReferencedChat = Realm.getInstance(RealmUtility.getDefaultConfig(chatContext)).where(RealmChat.class).equalTo("chat_id", realmChat.getChat_ref_id()).findFirst();
            realmChat.setReply_body(realmReferencedChat.getText() != null ? realmReferencedChat.getText() : realmReferencedChat.getAttachment_title());

            if (realmReferencedChat.getSent_by_consumer() == 1) {
                realmChat.setReply_name(PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? "Me" : CONSUMER_NAME);
            } else {
                realmChat.setReply_name(PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "ROLE", "").equals("SELLER") ? "Me" : SELLER_NAME);
            }
        }
    }

    private boolean isDeleteIconShowable() {
        for (int i = 0; i < multiselect_list.size(); i++) {
            String created_at = ((RealmChat) multiselect_list.get(i)).getCreated_at();
            if (!created_at.startsWith("z")) {
                return false;
            }
        }
        return true;
    }

    public void broadcastWithSocket(String result) {
        if (isNetworkAvailable(chatContext)) {

            if (chatSocket.getState() == Socket.State.OPEN) {
                if (chatSocket != null) {
                    chatSocket.send("chat:" + CONSUMER_ID + SELLER_ID + AGENT_ID, result);
                }
            }
        }
    }

    public class broadcastWithFirebase extends AsyncTask<Void, Integer, String> {


        // private ProgressDialog progressDialog;
        private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
        private final String[] SCOPES = {MESSAGING_SCOPE};
        JSONObject chatJson = null;

        public broadcastWithFirebase(JSONObject chatJson) {

            this.chatJson = chatJson;
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONObject jsonObject = null;
            try {
                String availability;
                String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("CONSUMER")) {
                    availability = getIntent().getStringExtra("AVAILABILITY");
                } else {
                    if (CONSUMER_ID.equals("")) {
                        Realm.init(getApplicationContext());
                        availability = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmAgent.class).findFirst().getAvailability();
                    } else {
                        Realm.init(getApplicationContext());
                        availability = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmSeller.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst().getAvailability();
                    }
                }
                JSONObject chat = chatJson.getJSONObject("chat");
                jsonObject = new JSONObject().put(
                        "message", new JSONObject()
                                .put("topic", CONSUMER_ID + SELLER_ID)
                                /*.put("notification", new JSONObject()
                                        .put("body", jsonObject.getJSONObject("chat").has("attachment_url") ? "Attachment" : jsonObject.getJSONObject("chat").getString("text"))
                                        .put("title", PreferenceManager.getDefaultSharedPreferences(chatActivity).getString(COURSEPATH, "") + " Chat")
                                )*/
                                .put("data", new JSONObject()
                                        .put("type", "chat")
                                        .put("chatresponse", chatJson.toString())
                                        .put("body", chat.has("attachment_url") && !chat.isNull( "attachment_url" ) ? "Attachment" : chat.getString("text"))
                                        .put("title", "Chat message from " + NAME)
                                        .put("NAME", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER") ? CONSUMER_NAME : CONSUMER_ID.equals("") ? AGENT_NAME : SELLER_NAME)
                                        .put("SELLER_ID", SELLER_ID)
                                        .put("CONSUMER_ID", CONSUMER_ID)
                                        .put("PROFILE_IMAGE_URL", PROFILE_IMAGE_URL)
                                        .put("AVAILABILITY", availability)
                                )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }


            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String content = jsonObject.toString();
            RequestBody body = RequestBody.create(mediaType, content);
            Request request = new Request.Builder()

                    .url("https://fcm.googleapis.com/v1/projects/ekumfi-juice/messages:send")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + ACCESSTOKEN, ""))
                    .build();
            try {
                okhttp3.Response response = client.newCall(request).execute();
                String s = response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            // Init and show dialog

        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void displayFromUri(Uri uri) {
        pdfFileName = getFileName(uri);

        pdfView.fromUri(uri)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .nightMode(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(ISNIGHTMODE, false))
                .load();
    }

    private void openFile() {
        String[] mimeTypes =
                {
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.template",


                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.template",


                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "application/vnd.openxmlformats-officedocument.presentationml.template",
                        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",


                        "application/pdf",


                        "text/plain"
                };

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, RC_DOC);
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if
        (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Files.FileColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {

                    int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private static String getDriveFilePath(Context context, Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1232;

    private void createFile(String fileName, String type) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    private void alterDocument(Context context, Uri uri) {
        try {
            ParcelFileDescriptor pfd = context.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(("Overwritten at " + System.currentTimeMillis() +
                    "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            });
}
