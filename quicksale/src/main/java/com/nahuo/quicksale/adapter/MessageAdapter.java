//package com.nahuo.quicksale.adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.content.FileProvider;
//import android.text.Spannable;
//import android.util.Log;
//import android.view.ContextMenu;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.easemob.EMCallBack;
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMConversation;
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.EMMessage.ChatType;
//import com.easemob.chat.EMMessage.Type;
//import com.easemob.chat.FileMessageBody;
//import com.easemob.chat.ImageMessageBody;
//import com.easemob.chat.LocationMessageBody;
//import com.easemob.chat.NormalFileMessageBody;
//import com.easemob.chat.TextMessageBody;
//import com.easemob.chat.VideoMessageBody;
//import com.easemob.chat.VoiceMessageBody;
//import com.easemob.exceptions.EaseMobException;
//import com.easemob.util.DateUtils;
//import com.easemob.util.EMLog;
//import com.easemob.util.FileUtils;
//import com.easemob.util.LatLng;
//import com.easemob.util.TextFormater;
//import com.nahuo.library.helper.DisplayUtil;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.ItemDetailsActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.common.SmileUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.im.AlertDialogActivity;
//import com.nahuo.quicksale.im.ChatActivity;
//import com.nahuo.quicksale.im.ImageCache;
//import com.nahuo.quicksale.im.ImageUtils;
//import com.nahuo.quicksale.im.LoadVideoImageTask;
//import com.nahuo.quicksale.im.MyContextMenu;
//import com.nahuo.quicksale.im.ShowBigImage;
//import com.nahuo.quicksale.im.ShowNormalFileActivity;
//import com.nahuo.quicksale.im.ShowVideoActivity;
//import com.nahuo.quicksale.im.VoicePlayClickListener;
//import com.nahuo.quicksale.oldermodel.ShopItemModel;
//import com.nahuo.quicksale.task.LoadImageTask;
//import com.nahuo.quicksale.util.GlideUtls;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Hashtable;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import static com.easemob.util.FileUtils.getMIMEType;
//import static com.hyphenate.chat.EMMessage.Status.INPROGRESS;
//import static com.tencent.open.utils.Global.getPackageName;
//
//public class MessageAdapter extends BaseAdapter {
//    private final static String TAG = "msg";
//    private static final int MESSAGE_TYPE_RECV_TXT = 0;
//    private static final int MESSAGE_TYPE_SENT_TXT = 1;
//    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
//    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
//    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
//    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
//    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
//    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
//    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
//    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
//    private static final int MESSAGE_TYPE_SENT_FILE = 10;
//    private static final int MESSAGE_TYPE_RECV_FILE = 11;
//    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
//    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
//    public static final String IMAGE_DIR = "chat/image/";
//    public static final String VOICE_DIR = "chat/audio/";
//    public static final String VIDEO_DIR = "chat/video";
//    public OnClickResendListener mListener;
//    private String username;
//    private LayoutInflater inflater;
//    private Activity activity;
//    boolean f = false;
//    // reference to conversation object in chatsdk
//    private EMConversation conversation;
//    private Context context;
//
//    public MessageAdapter(Context context, String username) {
//        this.username = username;
//        this.context = context;
//        inflater = LayoutInflater.from(context);
//        activity = (Activity) context;
//        this.conversation = EMChatManager.getInstance().getConversation(username);
//    }
//
//
//    /**
//     * 获取item数
//     */
//    public int getCount() {
//        return conversation.getMsgCount();
//    }
//
//    /**
//     * 刷新页面
//     */
//    public void refresh() {
//        notifyDataSetChanged();
//    }
//
//    public EMMessage getItem(int position) {
//        return conversation.getMessage(position);
//    }
//
//    public long getItemId(int position) {
//        return position;
//    }
//
//    /**
//     * 获取item类型
//     */
//    public int getItemViewType(int position) {
//        EMMessage message = conversation.getMessage(position);
//        if (message.getType() == EMMessage.Type.TXT) {
//            if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
//            }
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL
//                    : MESSAGE_TYPE_SENT_VOICE_CALL;
//        }
//        if (message.getType() == EMMessage.Type.IMAGE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
//
//        }
//        if (message.getType() == EMMessage.Type.LOCATION) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
//        }
//        if (message.getType() == EMMessage.Type.VOICE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
//        }
//        if (message.getType() == EMMessage.Type.VIDEO) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
//        }
//        if (message.getType() == EMMessage.Type.FILE) {
//            return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
//        }
//
//        return -1;// invalid
//    }
//
//    public int getViewTypeCount() {
//        return 14;
//    }
//
//    // 判断emmessage是不是商品消息
//    private boolean isItemMessage(EMMessage message) {
//        TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    // 根据emmessage对象返回这个对象对应的商品对象，如果不是商品对象，则返回null
//    private ShopItemModel getItemMessage(EMMessage message) {
//        TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
//            String itemJson = txtBody.getMessage().substring(4, txtBody.getMessage().length() - 1);
//            JSONObject itemJsonObject;
//            try {
//                itemJsonObject = new JSONObject(itemJson);
//
//                ShopItemModel item = new ShopItemModel();
//                item.ID = itemJsonObject.getInt("id");
//                item.setCover(itemJsonObject.getString("cover"));
//                item.setIntro(itemJsonObject.getString("intro"));
//
//                return item;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    private View createViewByMessage(EMMessage message, int position) {
//        switch (message.getType()) {
//            case LOCATION:
//                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location,
//                        null) : inflater.inflate(R.layout.row_sent_location, null);
//            case IMAGE:
//                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture,
//                        null) : inflater.inflate(R.layout.row_sent_picture, null);
//
//            case VOICE:
//                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null)
//                        : inflater.inflate(R.layout.row_sent_voice, null);
//            case VIDEO:
//                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_video, null)
//                        : inflater.inflate(R.layout.row_sent_video, null);
//            case FILE:
//                return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_file, null)
//                        : inflater.inflate(R.layout.row_sent_file, null);
//            default:
//                // 语音电话
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                    return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(
//                            R.layout.row_received_voice_call, null) : inflater.inflate(R.layout.row_sent_voice_call,
//                            null);
//                } else {
//                    if (isItemMessage(message)) {
//                        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(
//                                R.layout.row_received_item_message, null) : inflater.inflate(
//                                R.layout.row_sent_item_message, null);
//                    } else {
//                        return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(
//                                R.layout.row_received_message, null) : inflater
//                                .inflate(R.layout.row_sent_message, null);
//                    }
//                }
//        }
//    }
//
//    @SuppressLint("NewApi")
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final EMMessage message = getItem(position);
//
//
//        ChatType chatType = message.getChatType();
//
//        ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = createViewByMessage(message, position);
//            if (message.getType() == EMMessage.Type.IMAGE) {
//                try {
//                    holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
//                    holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                    holder.tv = (TextView) convertView.findViewById(R.id.percentage);
//                    holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
//                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                    holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                    holder.rl_view = (RelativeLayout) convertView.findViewById(R.id.rl_picture);
//                } catch (Exception e) {
//                }
//
//            } else if (message.getType() == EMMessage.Type.TXT) {
//
//                if (isItemMessage(message)) {
//                    try {
//                        holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                        holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                        holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                        // 这里是款式内容
//                        holder.item_cover = (ImageView) convertView.findViewById(R.id.tv_chat_item_img);
//                        //   holder.item_view = (LinearLayout) convertView.findViewById(R.id//.tv_chat_item_view);
//                        holder.tv = (TextView) convertView.findViewById(R.id.tv_chat_item_intro);
//
//                        holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                        holder.rl_view = (RelativeLayout) convertView.findViewById(R.id.chat_contents);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    try {
//                        holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                        holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                        holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                        // 这里是文字内容
//                        holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
//                        holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    // 语音通话
//                    if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                        holder.iv = (ImageView) convertView.findViewById(R.id.iv_call_icon);
//                        holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
//                    }
//                }
//
//            } else if (message.getType() == EMMessage.Type.VOICE) {
//                try {
//                    holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
//                    holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                    holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
//                    holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                    holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                    holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
//                    holder.rl_view = (RelativeLayout) convertView.findViewById(R.id.chat_contents);
//                } catch (Exception e) {
//                }
//            } else if (message.getType() == EMMessage.Type.LOCATION) {
//                try {
//                    holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                    holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
//                    holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                    holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                } catch (Exception e) {
//                }
//            } else if (message.getType() == EMMessage.Type.VIDEO) {
//                try {
//                    holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
//                    holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                    holder.tv = (TextView) convertView.findViewById(R.id.percentage);
//                    holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
//                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                    holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
//                    holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
//                    holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
//                    holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
//                    holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//
//                } catch (Exception e) {
//                }
//            } else if (message.getType() == EMMessage.Type.FILE) {
//                try {
//                    holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                    holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
//                    holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
//                    holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                    holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                    holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
//                    holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
//                    // 这里是进度值
//
//                } catch (Exception e) {
//                }
//                try {
//                    holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                } catch (Exception e) {
//                }
//
//            }
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//
//            // 因发送商品、文本都是txt模式。会有view复用问题，这里判断有问题的话，重置一下viewholder
//            if (message.getType() == EMMessage.Type.TXT) {
//
//                boolean isitemMessage = isItemMessage(message);
//                boolean resetHolder = false;
//                if (isitemMessage) {
//                    if (holder.item_cover == null) {
//                        resetHolder = true;
//                    }
//                } else {// 不是item模式，但是view是item模式
//                    if (holder.item_cover != null) {
//                        resetHolder = true;
//                    }
//                }
//
//                if (resetHolder) {
//                    holder = new ViewHolder();
//                    convertView = createViewByMessage(message, position);
//                    if (isitemMessage) {
//                        try {
//                            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                            // 这里是款式内容
//                            holder.item_cover = (ImageView) convertView.findViewById(R.id.tv_chat_item_img);
//                            //   holder.item_view = (LinearLayout) convertView.findViewById(R.id.tv_chat_item_view);
//                            holder.tv = (TextView) convertView.findViewById(R.id.tv_chat_item_intro);
//                            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//                            holder.rl_view = (RelativeLayout) convertView.findViewById(R.id.chat_contents);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        try {
//                            holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
//                            holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
//                            holder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
//                            // 这里是文字内容
//                            holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
//                            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        // 语音通话
//                        if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                            holder.iv = (ImageView) convertView.findViewById(R.id.iv_call_icon);
//                            holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
//                        }
//                    }
//                    convertView.setTag(holder);
//                }
//            }
//        }
//        // 群聊时，显示接收的消息的发送人的名称
//        if (chatType == ChatType.GroupChat && message.direct == EMMessage.Direct.RECEIVE)
//            // demo用username代替nick
//            holder.tv_userId.setText(message.getFrom());
//
//        // 如果是发送的消息并且不是群聊消息，显示已读textview
//        if (message.direct == EMMessage.Direct.SEND && chatType != ChatType.GroupChat) {
//            //    holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
//            holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
//
//            holder.head_iv.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
////                    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
////
////                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.parseInt(message.getFrom()));
////                    context.startActivity(userInfoIntent);
//                }
//            });
//
//        } else {
//
//            holder.head_iv.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
////                    Intent userInfoIntent = new Intent(context, UserInfoActivity.class);
////                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.parseInt(message.getFrom()));
////                    context.startActivity(userInfoIntent);
//                }
//            });
//
//            // 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
//            if ((message.getType() == Type.TXT || message.getType() == Type.LOCATION) && !message.isAcked
//                    && chatType != ChatType.GroupChat) {
//                // 不是语音通话记录
//                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                    try {
//                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
//                        // 发送已读回执
//                        message.isAcked = true;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        switch (message.getType()) {
//            // 根据消息type显示item
//            case IMAGE: // 图片
//                handleImageMessage(message, holder, position, convertView);
//                break;
//            case TXT: // 文本
//                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                    if (isItemMessage(message)) {
//                        handleItemMessage(message, holder, position);
//                    } else {
//                        handleTextMessage(message, holder, position);
//                    }
//
//                } else {
//                    // 语音电话
//                    handleVoiceCallMessage(message, holder, position);
//                }
//                break;
//            case LOCATION: // 位置
//                handleLocationMessage(message, holder, position, convertView);
//                break;
//            case VOICE: // 语音
//                handleVoiceMessage(message, holder, position, convertView);
//                break;
//            case VIDEO: // 视频
//                handleVideoMessage(message, holder, position, convertView);
//                break;
//            case FILE: // 一般文件
//                handleFileMessage(message, holder, position, convertView);
//                break;
//            default:
//                // not supported
//        }
//
//        if (message.direct == EMMessage.Direct.SEND) {
//
//            if (message.getFrom().equals(Constant.ADMIN_NAHUO_USER)) {
//                holder.head_iv.setImageResource(R.mipmap.app_logo);
//
//            } else {
//                String d_imgcover = Const.getShopLogo(message.getFrom());
//                String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//
//                Picasso.with(this.context).load(imageurl).placeholder(R.drawable.default_avatar).into(holder.head_iv);
//
//                String fromCurrent = conversation.getMessage(position).getFrom();
//
//                SetMarginTopLogo(0, holder);
//
//                speacialshow(message, holder, R.drawable.talk_left);
//
//
//            }
//
//            View statusView = convertView.findViewById(R.id.msg_status);
//            // 重发按钮点击事件
//            statusView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    // 显示重发消息的自定义alertdialog
//                    Intent intent = new Intent(activity, AlertDialogActivity.class);
//                    intent.putExtra("msg", activity.getString(R.string.confirm_resend));
//                    intent.putExtra("title", activity.getString(R.string.resend));
//                    intent.putExtra("cancel", true);
//                    intent.putExtra("position", position);
//                    if (message.getType() == EMMessage.Type.TXT)
//                        /*
//                         * activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_TEXT);
//                         */
//                        mListener.ClickResendMsg(ChatActivity.REQUEST_CODE_TEXT, position);
//                    else if (message.getType() == EMMessage.Type.VOICE)
//                        /*
//                         * activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VOICE);
//                         */
//                        mListener.ClickResendMsg(ChatActivity.REQUEST_CODE_VOICE, position);
//                    else if (message.getType() == EMMessage.Type.IMAGE)
//                        /*
//                         * activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_PICTURE);
//                         */
//                        mListener.ClickResendMsg(ChatActivity.REQUEST_CODE_PICTURE, position);
//                    else if (message.getType() == EMMessage.Type.LOCATION)
//                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_LOCATION);
//                    else if (message.getType() == EMMessage.Type.FILE)
//                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_FILE);
//                    else if (message.getType() == EMMessage.Type.VIDEO)
//                        activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VIDEO);
//
//                }
//            });
//
//        } else {
//
//            if (message.getFrom().equals(Constant.ADMIN_NAHUO_USER)) {
//                holder.head_iv.setImageResource(R.mipmap.app_logo);
//
//            } else {
//
//
//                String d_imgcover = Const.getShopLogo(message.getFrom());
//                String imageurl = "";
//                if (message.getFrom().equals(com.nahuo.constant.Constant.EccId.ECC_SYSTEM + "")) {
//                    imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//                } else {
//                    imageurl = SpManager.getECC_HEADIMAGE(context);
//                }
////                if (!TextUtils.isEmpty(imageurl)) {
////                    Picasso.with(this.context).load(imageurl).placeholder(R.drawable.default_avatar).into(holder.head_iv);
////                }else {
////                    holder.head_iv.setImageResource(R.drawable.default_avatar);
////                }
//                GlideUtls.glidePic(context, imageurl, holder.head_iv);
//                String fromCurrent = conversation.getMessage(position).getFrom();
//                SetMarginTopLogo(0, holder);
//                speacialshow(message, holder, R.drawable.talk_right);
//
//
//            }
//
//        }
//
//
//        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
//        String date = FunctionHelper.getFriendlyTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
//                message.getMsgTime())));
//
//
//        if (position == 0) {
//            timestamp.setText(date);
//            timestamp.setVisibility(View.VISIBLE);
//        } else {
//            // 两条消息时间离得如果稍长，显示时间
//            if (DateUtils.isCloseEnough(message.getMsgTime(), conversation.getMessage(position - 1).getMsgTime())) {
//                timestamp.setVisibility(View.GONE);
//            } else {
//                timestamp.setText(date);
//                timestamp.setVisibility(View.VISIBLE);
//            }
//
//
//        }
//        if (holder.tv != null) {
//            holder.tv.setGravity(Gravity.LEFT | Gravity.CENTER);
//        }
//        return convertView;
//    }
//
//    private void SetMarginTopLogo(int top, ViewHolder holder) {
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.head_iv.getLayoutParams();
//        params.topMargin = DisplayUtil.dip2px(activity, top);
//        holder.head_iv.setLayoutParams(params);
//    }
//
//
//    private void speacialshow(EMMessage message, ViewHolder holder, int id) {
//        if (message.getType() == Type.TXT) {
//
//            if (isItemMessage(message)) {
//                if (holder.rl_view != null)
//                    holder.rl_view.setBackgroundResource(id);
//            } else {
//                if (holder.tv != null)
//                    holder.tv.setBackgroundResource(id);
//            }
//        } else {
//            if (holder.rl_view != null)
//                holder.rl_view.setBackgroundResource(id);
//        }
//    }
//
//    /**
//     * 文本消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     */
//    private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
//        TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        SmileUtils.setinitparm(context);
//        Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
//        // 设置内容
//        if (message.direct == EMMessage.Direct.SEND) {
//
//            String Mysapn = span.toString();
//
//            //    Mysapn  =Mysapn.substring( Mysapn.indexOf('①',1)+1);
//
//            holder.tv.setText(Mysapn, TextView.BufferType.SPANNABLE);
//            holder.tv.setTag(Mysapn);
//        } else {
//
//            holder.tv.setText(span, TextView.BufferType.SPANNABLE);
//            try {
//                if (message != null) {
//                    String weichatString = message.getStringAttribute("weichat", null);
//                    if (weichatString != null) {
//                        JSONObject weichatJson = new JSONObject(weichatString);
//                        String inviteEnquiry = weichatJson.optString("ctrlType");
//                        if (inviteEnquiry.equals("inviteEnquiry")) {
//                            holder.tv.setText(SpManager.getECC_LAST_MSG(context));
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            holder.tv.setTag(span);
//        }
//        // 对内容做处理
//     /*  表情 SpannableStringBuilder sb = AKUtil.handler(context,holder.tv,
//        holder.tv.setText(sb);
//        holder.tv.setTag(sb);*/
//        holder.tv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                activity.startActivityForResult(
//                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
//                                EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//                return true;
//            }
//        });
//        if (message.direct == EMMessage.Direct.SEND) {
//            switch (message.status) {
//                case SUCCESS: // 发送成功
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.GONE);
//                    break;
//                case FAIL: // 发送失败
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.VISIBLE);
//                    break;
//                case INPROGRESS: // 发送中
//                    holder.pb.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    // 发送消息
//                    sendMsgInBackground(message, holder);
//            }
//        }
//    }
//
//    /**
//     * 商品消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     */
//    private void handleItemMessage(EMMessage message, ViewHolder holder, final int position) {
//        // TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        ShopItemModel item = getItemMessage(message);
//        SmileUtils.setinitparm(context);
//        Spannable span = SmileUtils.getSmiledText(context, item.getIntro());
//        // 设置内容
//        holder.tv.setText(span, TextView.BufferType.SPANNABLE);
//        //表情
//        /*SpannableStringBuilder sb = AKUtil.handler(context,holder.tv,
//                item.getIntro());
//        holder.tv.setText(sb);*/
//
//        TextMessageBody txtBody = (TextMessageBody) message.getBody();
//
//        holder.tv.setTag(txtBody.getMessage());
//        holder.tv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                activity.startActivityForResult(
//                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
//                                EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//                return true;
//            }
//        });
//
//        if (isItemMessage(message)) {
//            if (holder.item_cover != null) {
//                // 商品图片
//                Picasso.with(context).load(ImageUrlExtends.getImageUrl(item.getCover(), Const.LIST_ITEM_SIZE)).into(holder.item_cover);
//            }
//            if (holder.rl_view != null) {
//                holder.rl_view.setTag(item.ID);
//                holder.rl_view.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // 进入商品详情
//                        Intent gotoItemIntent = new Intent(context, ItemDetailsActivity.class);
//                        gotoItemIntent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.valueOf(v.getTag().toString()));
//                        context.startActivity(gotoItemIntent);
//                    }
//                });
//            }
//        }
//
//        if (message.direct == EMMessage.Direct.SEND) {
//            switch (message.status) {
//                case SUCCESS: // 发送成功
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.GONE);
//                    break;
//                case FAIL: // 发送失败
//                    holder.pb.setVisibility(View.GONE);
//                    holder.staus_iv.setVisibility(View.VISIBLE);
//                    break;
//                case INPROGRESS: // 发送中
//                    holder.pb.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    // 发送消息
//                    sendMsgInBackground(message, holder);
//            }
//        }
//    }
//
//    /**
//     * 语音通话记录
//     *
//     * @param message
//     * @param holder
//     * @param position
//     */
//    private void handleVoiceCallMessage(EMMessage message, ViewHolder holder, final int position) {
//        TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        holder.tv.setText(txtBody.getMessage());
//
//    }
//
//    /**
//     * 图片消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     * @param convertView
//     */
//    private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position,
//                                    View convertView) {
//        holder.pb.setTag(position);
//        holder.iv.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                activity.startActivityForResult(
//                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
//                                EMMessage.Type.IMAGE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//                return true;
//            }
//        });
//
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            // "it is receive msg";
//            if (message.status == EMMessage.Status.INPROGRESS) {
//                // "!!!! back receive";
//                holder.iv.setImageResource(R.drawable.default_image);
//                showDownloadImageProgress(message, holder);
//                // downloadImage(message, holder);
//            } else {
//                // "!!!! not back receive, show image directly");
//                holder.pb.setVisibility(View.GONE);
//                holder.tv.setVisibility(View.GONE);
//                holder.iv.setImageResource(R.drawable.default_image);
//                ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
//                if (imgBody.getLocalUrl() != null) {
//                    // String filePath = imgBody.getLocalUrl();
//                    String remotePath = imgBody.getRemoteUrl();
//                    String filePath = ImageUtils.getImagePath(remotePath);
//                    String thumbRemoteUrl = imgBody.getThumbnailUrl();
//                    String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
//                    showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
//                }
//            }
//            return;
//        }
//
//        // process send message
//        // send pic, show the pic directly
//        ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
//        String filePath = imgBody.getLocalUrl();
//        if (filePath != null && new File(filePath).exists()) {
//            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message);
//        } else {
//            showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message);
//        }
//
//        switch (message.status) {
//            case SUCCESS:
//                holder.pb.setVisibility(View.GONE);
//                holder.tv.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.GONE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.GONE);
//                holder.tv.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//                // set a timer
//                final Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//                            public void run() {
//                                holder.pb.setVisibility(View.VISIBLE);
//                                holder.tv.setVisibility(View.VISIBLE);
//                                holder.tv.setText(message.progress + "%");
//                                if (message.status == EMMessage.Status.SUCCESS) {
//                                    holder.pb.setVisibility(View.GONE);
//                                    holder.tv.setVisibility(View.GONE);
//                                    // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
//                                    timer.cancel();
//                                } else if (message.status == EMMessage.Status.FAIL) {
//                                    holder.pb.setVisibility(View.GONE);
//                                    holder.tv.setVisibility(View.GONE);
//                                    // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
//                                    // message.setProgress(0);
//                                    holder.staus_iv.setVisibility(View.VISIBLE);
//
//                                    Toast.makeText(
//                                            activity,
//                                            activity.getString(R.string.send_fail)
//                                                    + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
//
//                                    timer.cancel();
//                                }
//
//                            }
//                        });
//
//                    }
//                }, 0, 500);
//                break;
//            default:
//                sendPictureMessage(message, holder);
//        }
//    }
//
//    /**
//     * 视频消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     * @param convertView
//     */
//    private Map<String, Timer> timers = new Hashtable<String, Timer>();
//    private void handleVideoMessage(final EMMessage message, final ViewHolder holder, final int position,
//                                    View convertView) {
//        VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
//        // final File image=new File(PathUtil.getInstance().getVideoPath(),
//        // videoBody.getFileName());
//        String localThumb = videoBody.getLocalThumb();
//
//        holder.iv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                activity.startActivityForResult(new Intent(activity,
//                                ContextMenu.class).putExtra("position", position)
//                                .putExtra("type", EMMessage.Type.VIDEO.ordinal()),
//                        ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//                return true;
//            }
//        });
//
//        if (localThumb != null) {
//
//            showVideoThumbView(localThumb, holder.iv,
//                    videoBody.getThumbnailUrl(), message);
//        }
//        if (videoBody.getLength() > 0) {
//            String time = DateUtils.toTimeBySecond(videoBody.getLength());
//            holder.timeLength.setText(time);
//        }
//        holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);
//
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            if (videoBody.getVideoFileLength() > 0) {
//                String size = TextFormater.getDataSize(videoBody
//                        .getVideoFileLength());
//                holder.size.setText(size);
//            }
//        } else {
//            if (videoBody.getLocalUrl() != null
//                    && new File(videoBody.getLocalUrl()).exists()) {
//                String size = TextFormater.getDataSize(new File(videoBody
//                        .getLocalUrl()).length());
//                holder.size.setText(size);
//            }
//        }
//
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//
//            // System.err.println("it is receive msg");
//            if (message.status == EMMessage.Status.INPROGRESS) {
//                // System.err.println("!!!! back receive");
//                holder.iv.setImageResource(R.drawable.default_image);
//                showDownloadImageProgress(message, holder);
//
//            } else {
//                // System.err.println("!!!! not back receive, show image directly");
//                holder.iv.setImageResource(R.drawable.default_image);
//                if (localThumb != null) {
//                    showVideoThumbView(localThumb, holder.iv,
//                            videoBody.getThumbnailUrl(), message);
//                }
//
//            }
//
//            return;
//        }
//        holder.pb.setTag(position);
//
//        // until here ,deal with send video msg
//        switch (message.status) {
//            case SUCCESS:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.GONE);
//                holder.tv.setVisibility(View.GONE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.GONE);
//                holder.tv.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//                if (timers.containsKey(message.getMsgId()))
//                    return;
//                // set a timer
//                final Timer timer = new Timer();
//                timers.put(message.getMsgId(), timer);
//                timer.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                holder.pb.setVisibility(View.VISIBLE);
//                                holder.tv.setVisibility(View.VISIBLE);
//                                holder.tv.setText(message.progress + "%");
//                                if (message.status == EMMessage.Status.SUCCESS) {
//                                    holder.pb.setVisibility(View.GONE);
//                                    holder.tv.setVisibility(View.GONE);
//                                    // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
//                                    timer.cancel();
//                                } else if (message.status == EMMessage.Status.FAIL) {
//                                    holder.pb.setVisibility(View.GONE);
//                                    holder.tv.setVisibility(View.GONE);
//                                    // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
//                                    // message.setProgress(0);
//                                    holder.staus_iv.setVisibility(View.VISIBLE);
//                                    Toast.makeText(
//                                            activity,
//                                            activity.getString(R.string.send_fail)
//                                                    + activity
//                                                    .getString(R.string.connect_failuer_toast),
//                                            0).show();
//                                    timer.cancel();
//                                }
//
//                            }
//                        });
//
//                    }
//                }, 0, 500);
//                break;
//            default:
//                // sendMsgInBackground(message, holder);
//                sendPictureMessage(message, holder);
//
//        }
////        VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
//////         final File image=new File(PathUtil.getInstance().getVideoPath(),
//////         videoBody.getFileName());
////        String localThumb = videoBody.getLocalThumb();
////        holder.iv.setOnLongClickListener(new OnLongClickListener() {
////
////            @Override
////            public boolean onLongClick(View v) {
////                activity.startActivityForResult(
////                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
////                                Type.VOICE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
////                return true;
////            }
////        });
////        if (localThumb != null) {
////              showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(), message);
////        }
////        if (videoBody.getLength() > 0) {
////            String time = DateUtils.toTimeBySecond(videoBody.getLength());
////            holder.timeLength.setText(time);
////        }
////        holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);
////
////        if (message.direct == EMMessage.Direct.RECEIVE) {
////            if (videoBody.getVideoFileLength() > 0) {
////                String size = TextFormater.getDataSize(videoBody.getVideoFileLength());
////                holder.size.setText(size);
////            }
////        } else {
////            if (videoBody.getLocalUrl() != null && new File(videoBody.getLocalUrl()).exists()) {
////                String size = TextFormater.getDataSize(new File(videoBody.getLocalUrl()).length());
////                holder.size.setText(size);
////            }
////        }
////
////        if (message.direct == EMMessage.Direct.RECEIVE) {
////
////            // System.err.println("it is receive msg");
////            if (message.status == EMMessage.Status.INPROGRESS) {
////                // System.err.println("!!!! back receive");
////                holder.iv.setImageResource(R.drawable.default_image);
////                showDownloadImageProgress(message, holder);
////
////            } else {
////                // System.err.println("!!!! not back receive, show image directly");
////                holder.iv.setImageResource(R.drawable.default_image);
////                if (localThumb != null) {
////                      showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(), message);
////                }
////
////            }
////
////            return;
////        }
////        holder.pb.setTag(position);
////
////        // until here ,deal with send video msg
////        switch (message.status) {
////            case SUCCESS:
////                holder.pb.setVisibility(View.GONE);
////                holder.staus_iv.setVisibility(View.GONE);
////                holder.tv.setVisibility(View.GONE);
////                break;
////            case FAIL:
////                holder.pb.setVisibility(View.GONE);
////                holder.tv.setVisibility(View.GONE);
////                holder.staus_iv.setVisibility(View.VISIBLE);
////                break;
////            case INPROGRESS:
////                // set a timer
////                final Timer timer = new Timer();
////                timer.schedule(new TimerTask() {
////
////                    @Override
////                    public void run() {
////                        activity.runOnUiThread(new Runnable() {
////
////                            @Override
////                            public void run() {
////                                holder.pb.setVisibility(View.VISIBLE);
////                                holder.tv.setVisibility(View.VISIBLE);
////                                holder.tv.setText(message.progress + "%");
////                                if (message.status == EMMessage.Status.SUCCESS) {
////                                    holder.pb.setVisibility(View.GONE);
////                                    holder.tv.setVisibility(View.GONE);
////                                    // message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
////                                    timer.cancel();
////                                } else if (message.status == EMMessage.Status.FAIL) {
////                                    holder.pb.setVisibility(View.GONE);
////                                    holder.tv.setVisibility(View.GONE);
////                                    // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
////                                    // message.setProgress(0);
////                                    holder.staus_iv.setVisibility(View.VISIBLE);
////                                    Toast.makeText(
////                                            activity,
////                                            activity.getString(R.string.send_fail)
////                                                    + activity.getString(R.string.connect_failuer_toast),
////                                            Toast.LENGTH_SHORT).show();
////                                    timer.cancel();
////                                }
////
////                            }
////                        });
////
////                    }
////                }, 0, 500);
////                break;
////            default:
////                // sendMsgInBackground(message, holder);
////                sendPictureMessage(message, holder);
////
////        }
//
//    }
//    /**
//     * 展示视频缩略图
//     *
//     * @param localThumb
//     *            本地缩略图路径
//     * @param iv
//     * @param thumbnailUrl
//     *            远程缩略图路径
//     * @param message
//     */
//    private void showVideoThumbView(String localThumb, ImageView iv,
//                                    String thumbnailUrl, final EMMessage message) {
//        // first check if the thumbnail image already loaded into cache
//        Bitmap bitmap = ImageCache.getInstance().get(localThumb);
//        if (bitmap != null) {
//            // thumbnail image is already loaded, reuse the drawable
//            iv.setImageBitmap(bitmap);
//            iv.setClickable(true);
//            iv.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    VideoMessageBody videoBody = (VideoMessageBody) message
//                            .getBody();
//                    // TODO 打开视频页面
//                    System.err.println("video view is on click");
//                     Intent intent = new Intent(activity,
//                     ShowVideoActivity.class);
//                     intent.putExtra("localpath", videoBody.getLocalUrl());
//                     intent.putExtra("secret", videoBody.getSecret());
//                     intent.putExtra("remotepath", videoBody.getRemoteUrl());
//                     if (message != null
//                     && message.direct == EMMessage.Direct.RECEIVE
//                     && !message.isAcked
//                     && message.getChatType() != ChatType.GroupChat) {
//                     message.isAcked = true;
//                     try {
//                     EMChatManager.getInstance().ackMessageRead(
//                     message.getFrom(), message.getMsgId());
//                     } catch (Exception e) {
//                     e.printStackTrace();
//                     }
//                     }
//                     activity.startActivity(intent);
//
//                }
//            });
//
//        } else {
//            new LoadVideoImageTask().execute(localThumb, thumbnailUrl, iv,
//                    activity, message, this);
//        }
//
//    }
//    /**
//     * 语音消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     * @param convertView
//     */
//    private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position,
//                                    View convertView) {
//        VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
//        holder.tv.setText(voiceBody.getLength() + "\"");
//        holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this,
//                activity, username));
//
//        holder.iv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                activity.startActivityForResult((new Intent(activity,
//                                MyContextMenu.class)).putExtra("position", position).putExtra("type", EMMessage.Type.VOICE.ordinal()),
//                        ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//                return true;
//            }
//        });
//
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            if (message.isAcked) {
//                // 隐藏语音未读标志
//                holder.iv_read_status.setVisibility(View.INVISIBLE);
//            } else {
//                holder.iv_read_status.setVisibility(View.VISIBLE);
//            }
//            System.err.println("it is receive msg");
//            if (message.status == EMMessage.Status.INPROGRESS) {
//                holder.pb.setVisibility(View.VISIBLE);
//                System.err.println("!!!! back receive");
//                ((FileMessageBody) message.getBody()).setDownloadCallback(new EMCallBack() {
//
//                    @Override
//                    public void onSuccess() {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                holder.pb.setVisibility(View.INVISIBLE);
//                                notifyDataSetChanged();
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onProgress(int progress, String status) {
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                holder.pb.setVisibility(View.INVISIBLE);
//                            }
//                        });
//
//                    }
//                });
//
//            } else {
//                holder.pb.setVisibility(View.INVISIBLE);
//
//            }
//            return;
//        }
//
//        // until here, deal with send voice msg
//        switch (message.status) {
//            case SUCCESS:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.GONE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//
//                break;
//            default:
//                sendMsgInBackground(message, holder);
//        }
//    }
//    public  void openFile(File var0, Activity var1) {
//        Intent var2 = new Intent();
//        var2.addFlags(268435456);
//        var2.setAction("android.intent.action.VIEW");
//        String var3 = getMIMEType(var0);
//        final Uri destinationUri;
//        String authority = BWApplication.getInstance().getPackageName() + ".provider";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //通过FileProvider创建一个content类型的Uri
//            destinationUri = FileProvider.getUriForFile( BWApplication.getInstance(), authority, var0);
//        } else {
//            destinationUri = Uri.fromFile(var0);
//        }
//        var2.setDataAndType(destinationUri, var3);
//        try {
//            var1.startActivity(var2);
//        } catch (Exception var5) {
//            var5.printStackTrace();
//            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    /**
//     * 文件消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     * @param convertView
//     */
//    private void handleFileMessage(final EMMessage message, final ViewHolder holder, int position, View convertView) {
//        final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message.getBody();
//        final String filePath = fileMessageBody.getLocalUrl();
//        holder.tv_file_name.setText(fileMessageBody.getFileName());
//        holder.tv_file_size.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
//        holder.ll_container.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                File file = new File(filePath);
//                if (file != null && file.exists()) {
//                    // 文件存在，直接打开
//                    openFile(file, (Activity) context);
//                } else {
//                    // 下载
//                    context.startActivity(new Intent(context, ShowNormalFileActivity.class).putExtra("msgbody",
//                            fileMessageBody));
//                }
//                if (message.direct == EMMessage.Direct.RECEIVE && !message.isAcked) {
//                    try {
//                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
//                        message.isAcked = true;
//                    } catch (EaseMobException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        if (message.direct == EMMessage.Direct.RECEIVE) { // 接收的消息
//            System.err.println("it is receive msg");
//            File file = new File(filePath);
//            if (file != null && file.exists()) {
//                holder.tv_file_download_state.setText("已下载");
//            } else {
//                holder.tv_file_download_state.setText("未下载");
//            }
//            return;
//        }
//
//        // until here, deal with send voice msg
//        switch (message.status) {
//            case SUCCESS:
//                holder.pb.setVisibility(View.INVISIBLE);
//                holder.staus_iv.setVisibility(View.INVISIBLE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.INVISIBLE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//                // set a timer
//                final Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                holder.pb.setVisibility(View.VISIBLE);
//                                if (message.status == EMMessage.Status.SUCCESS) {
//                                    holder.pb.setVisibility(View.INVISIBLE);
//                                    timer.cancel();
//                                } else if (message.status == EMMessage.Status.FAIL) {
//                                    holder.pb.setVisibility(View.INVISIBLE);
//                                    holder.staus_iv.setVisibility(View.VISIBLE);
//                                    Toast.makeText(
//                                            activity,
//                                            activity.getString(R.string.send_fail)
//                                                    + activity.getString(R.string.connect_failuer_toast),
//                                            Toast.LENGTH_SHORT).show();
//                                    timer.cancel();
//                                }
//
//                            }
//                        });
//
//                    }
//                }, 0, 500);
//                break;
//            default:
//                // 发送消息
//                sendMsgInBackground(message, holder);
//        }
//
//    }
//
//    /**
//     * 处理位置消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     * @param convertView
//     */
//    private void handleLocationMessage(final EMMessage message, final ViewHolder holder, final int position,
//                                       View convertView) {
//        TextView locationView = ((TextView) convertView.findViewById(R.id.tv_location));
//        LocationMessageBody locBody = (LocationMessageBody) message.getBody();
//        locationView.setText(locBody.getAddress());
//        LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
//        // locationView.setOnClickListener(new MapClickListener(loc, locBody.getAddress()));
//
//        if (message.direct == EMMessage.Direct.RECEIVE) {
//            return;
//        }
//        // deal with send message
//        switch (message.status) {
//            case SUCCESS:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.GONE);
//                break;
//            case FAIL:
//                holder.pb.setVisibility(View.GONE);
//                holder.staus_iv.setVisibility(View.VISIBLE);
//                break;
//            case INPROGRESS:
//                holder.pb.setVisibility(View.VISIBLE);
//                break;
//            default:
//                sendMsgInBackground(message, holder);
//        }
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param message
//     * @param holder
//     */
//    public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {
//        holder.staus_iv.setVisibility(View.GONE);
//        holder.pb.setVisibility(View.VISIBLE);
//        // 调用sdk发送异步发送方法
//        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                updateSendedView(message, holder);
//            }
//
//            @Override
//            public void onError(int code, String error) {
//                updateSendedView(message, holder);
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//            }
//
//        });
//
//    }
//
//    /*
//     * chat sdk will automatic download thumbnail image for the image message we need to register callback show the
//     * download progress
//     */
//    private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
//        System.err.println("!!! show download image progress");
//        // final ImageMessageBody msgbody = (ImageMessageBody)
//        // message.getBody();
//        final FileMessageBody msgbody = (FileMessageBody) message.getBody();
//
//        msgbody.setDownloadCallback(new EMCallBack() {
//
//            @Override
//            public void onSuccess() {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // message.setBackReceive(false);
//                        if (message.getType() == EMMessage.Type.IMAGE) {
//                            holder.pb.setVisibility(View.GONE);
//                            holder.tv.setVisibility(View.GONE);
//                        }
//                        notifyDataSetChanged();
//                    }
//                });
//            }
//
//            @Override
//            public void onError(int code, String message) {
//
//            }
//
//            @Override
//            public void onProgress(final int progress, String status) {
//                if (message.getType() == EMMessage.Type.IMAGE) {
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            holder.tv.setText(progress + "%");
//
//                        }
//                    });
//                }
//
//            }
//
//        });
//    }
//
//    /*
//     * send message with new sdk
//     */
//    private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {
//        try {
//
//            // before send, update ui
//            holder.staus_iv.setVisibility(View.GONE);
//            holder.pb.setVisibility(View.VISIBLE);
//            holder.tv.setVisibility(View.VISIBLE);
//            holder.tv.setText("0%");
//            // if (chatType == ChatActivity.CHATTYPE_SINGLE) {
//            EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
//
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "send image message successfully");
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            // send success
//                            holder.pb.setVisibility(View.GONE);
//                            holder.tv.setVisibility(View.GONE);
//                        }
//                    });
//                }
//
//                @Override
//                public void onError(int code, String error) {
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            holder.pb.setVisibility(View.GONE);
//                            holder.tv.setVisibility(View.GONE);
//                            // message.setSendingStatus(Message.SENDING_STATUS_FAIL);
//                            holder.staus_iv.setVisibility(View.VISIBLE);
//                            Toast.makeText(
//                                    activity,
//                                    activity.getString(R.string.send_fail)
//                                            + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    });
//                }
//
//                @Override
//                public void onProgress(final int progress, String status) {
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            holder.tv.setText(progress + "%");
//                        }
//                    });
//                }
//
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 更新ui上消息发送状态
//     *
//     * @param message
//     * @param holder
//     */
//    private void updateSendedView(final EMMessage message, final ViewHolder holder) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                // send success
//                if (message.getType() == EMMessage.Type.VIDEO) {
//                    holder.tv.setVisibility(View.GONE);
//                }
//                if (message.status == EMMessage.Status.SUCCESS) {
//                    if (message.getType() == EMMessage.Type.FILE) {
//                        holder.pb.setVisibility(View.INVISIBLE);
//                        holder.staus_iv.setVisibility(View.INVISIBLE);
//                    } else {
//                        holder.pb.setVisibility(View.GONE);
//                        holder.staus_iv.setVisibility(View.GONE);
//                    }
//
//                } else if (message.status == EMMessage.Status.FAIL) {
//                    if (message.getType() == EMMessage.Type.FILE) {
//                        holder.pb.setVisibility(View.INVISIBLE);
//                    } else {
//                        holder.pb.setVisibility(View.GONE);
//                    }
//                    holder.staus_iv.setVisibility(View.VISIBLE);
//                    Toast.makeText(
//                            activity,
//                            activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    /**
//     * load image into image view
//     *
//     * @param thumbernailPath
//     * @param iv
//     * @return the image exists or not
//     */
//    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath,
//                                  String remoteDir, final EMMessage message) {
//        // String imagename =
//        // localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1,
//        // localFullSizePath.length());
//        // final String remote = remoteDir != null ? remoteDir+imagename :
//        // imagename;
//        final String remote = remoteDir;
//        EMLog.d("###", "local = " + localFullSizePath + " remote: " + remote);
//        // first check if the thumbnail image already loaded into cache
//        Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
//        if (bitmap != null) {
//            // thumbnail image is already loaded, reuse the drawable
//            iv.setImageBitmap(bitmap);
//            iv.setClickable(true);
//            iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.err.println("image view on click");
//                    Intent intent = new Intent(activity, ShowBigImage.class);
//                    File file = new File(localFullSizePath);
//                    if (file.exists()) {
//                        Uri uri = Uri.fromFile(file);
//                        intent.putExtra("uri", uri);
//                        System.err.println("here need to check why download everytime");
//                    } else {
//                        // The local full size pic does not exist yet.
//                        // ShowBigImage needs to download it from the server
//                        // first
//                        // intent.putExtra("", message.get);
//                        ImageMessageBody body = (ImageMessageBody) message.getBody();
//                        intent.putExtra("secret", body.getSecret());
//                        intent.putExtra("remotepath", remote);
//                    }
//                    if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
//                            && message.getChatType() != ChatType.GroupChat) {
//                        try {
//                            EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
//                            message.isAcked = true;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    activity.startActivity(intent);
//                }
//            });
//            return true;
//        } else {
//
//            new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv,
//                    activity, message);
//            return true;
//        }
//
//    }
//
//    public static interface OnClickResendListener {
//        void ClickResendMsg(int code, int position);
//    }
//
//    public void setClickResendListener(OnClickResendListener listener) {
//        mListener = listener;
//    }
//
//    public static class ViewHolder {
//        ImageView iv;
//        TextView tv;
//        ProgressBar pb;
//        ImageView staus_iv;
//        ImageView head_iv;
//        TextView tv_userId;
//        ImageView playBtn;
//        TextView timeLength;
//        TextView size;
//        LinearLayout container_status_btn;
//        LinearLayout ll_container;
//        ImageView iv_read_status;
//        // 显示已读回执状态
//        // TextView tv_ack;
//        // 显示送达回执状态
//        TextView tv_delivered;
//
//        TextView tv_file_name;
//        TextView tv_file_size;
//        TextView tv_file_download_state;
//
//        ImageView item_cover;
//        // LinearLayout item_view;
//
//
//        RelativeLayout rl_view;
//    }
//
//}
