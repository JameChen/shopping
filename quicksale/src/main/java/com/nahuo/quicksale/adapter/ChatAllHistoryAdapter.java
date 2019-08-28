//package com.nahuo.quicksale.adapter;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.ImageMessageBody;
//import com.easemob.chat.TextMessageBody;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.im.ChatAllHistoryFragment.myEMConversation;
//import com.nahuo.quicksale.oldermodel.ChatUserModel;
//import com.nahuo.quicksale.util.GlideUtls;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//public class ChatAllHistoryAdapter extends BaseAdapter {
//
//    private LayoutInflater inflater;
//    private Map<String, ChatUserModel> mistfriend;
//    private Context mContext;
//    private List<myEMConversation> mobj;
//
//    public ChatAllHistoryAdapter(Context context, int textViewResourceId, List<myEMConversation> objects,
//                                 Map<String, ChatUserModel> listfriend) {
//        inflater = LayoutInflater.from(context);
//        mistfriend = listfriend;
//        mContext = context;
//        mobj = objects;
//
//    }
//
//    @Override
//    public int getCount() {
//        return ListUtils.isEmpty(mobj) ? 0 : mobj.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return ListUtils.isEmpty(mobj) ? null : mobj.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return -1;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
//            holder = new ViewHolder();
//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
//            holder.message = (TextView) convertView.findViewById(R.id.message);
//            holder.time = (TextView) convertView.findViewById(R.id.time);
//            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
//            holder.msgState = convertView.findViewById(R.id.msg_state);
//            holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        // if (position % 2 == 0) {
//        holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
//        /*
//         * } else { holder.list_item_layout .setBackgroundResource(R.drawable.mm_listitem_grey); }
//         */
//
//        // 获取与此用户/群组的会话
//        myEMConversation conversation = mobj.get(position);
//        myEMConversation my = conversation;
//        // 获取用户username或者群组groupid
//        String username = conversation.getUserName();
//        // 这里处理
//        if (username.equals("test_test")) {
//            final int userId = my.getUserid();
//            if (!TextUtils.isEmpty(my.getLastcontents()))
//                holder.message.setText(my.getLastcontents());
//            holder.unreadLabel.setVisibility(View.GONE);
//            holder.name.setText(my.getNick());
//            holder.name.setTag(my.getUserid());
//            String d_imgcover = Const.getShopLogo(my.getUserid());
//            String imageurl = "";
//            if (my.getUserid() == com.nahuo.constant.Constant.EccId.ECC_SYSTEM) {
//                imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//            } else {
//                imageurl = SpManager.getECC_HEADIMAGE(mContext);
//            }
//            // mImageLoader.displayImage(imageurl, holder.avatar, mOptions);
////            if (!TextUtils.isEmpty(imageurl)) {
////                Picasso.with(mContext).load(imageurl).placeholder(R.drawable.default_avatar).into(holder.avatar);
////            } else {
////                holder.avatar.setImageResource(R.drawable.default_avatar);
////            }
//            GlideUtls.glidePic(mContext, imageurl, holder.avatar);
////            String date = "";
////            if (!StringUtils.isEmptyWithTrim(my.getCreatetime())) {
////                date = FunctionHelper.getFriendlyTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
////                        my.getCreatetime())));
////
////
////            }
//            if (conversation.getMyunreadcount() > 0) {
//                // 显示与此用户的消息未读数
//                holder.unreadLabel.setText(String.valueOf(conversation.getMyunreadcount()));
//                holder.unreadLabel.setVisibility(View.VISIBLE);
//            } else {
//                holder.unreadLabel.setVisibility(View.INVISIBLE);
//            }
//
//            if (conversation.getMymsgcount() != 0) {
//                // 把最后一条消息的内容作为item的message内容
//                EMMessage lastMessage = conversation.getMyMessage();
//                if (isItemMessage(lastMessage)) {
//                    holder.message.setText("[商品]");
//                } else {
//                /*holder.message.setText(
//                        SmileUtils.getSmiledText(getContext(), getMessageDigest(lastMessage, (this.getContext()))),
//                        BufferType.SPANNABLE);*/
////                SpannableStringBuilder sb = AKUtil.handler(mContext,holder.message,
////                        getMessageDigest(lastMessage, (this.getContext())));
//                    holder.message.setText(getMessageDigest(lastMessage, (mContext)));
//                }
//            /*
//             * holder.time.setText(DateUtils.getTimestampString(new Date( lastMessage.getMsgTime())));
//             */
//                String date1 = FunctionHelper.getFriendlyTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
//                        lastMessage.getMsgTime())));
//                holder.time.setText(date1);
//                if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
//                    holder.msgState.setVisibility(View.VISIBLE);
//                } else {
//                    holder.msgState.setVisibility(View.GONE);
//                }
//            } else {
//                holder.message.setText("");
//                holder.time.setText("");
//            }
//            return convertView;
//        }
////
////        holder.avatar.setOnClickListener(new OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
//////                Intent userInfoIntent = new Intent(mContext, UserInfoActivity.class);
//////                userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.valueOf(username));
//////                mContext.startActivity(userInfoIntent);
////            }
////        });
////        List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
////        EMContact contact = null;
////        boolean isGroup = false;
////        for (EMGroup group : groups) {
////            if (group.getGroupId().equals(username)) {
////                isGroup = true;
////                contact = group;
////                break;
////            }
////        }
////        if (isGroup) {
////            // 群聊消息，显示群聊头像
////            holder.avatar.setImageResource(R.drawable.groups_icon);
////            holder.name.setText(contact.getNick() != null ? contact.getNick() : username);
////        } else {
////            // 本地或者服务器获取用户详情，以用来显示头像和nick
////            ChatUserModel model = mistfriend.get(username);
////
////            // 这里可能是陌生人的会话
////
////            if (model != null) {
////                String d_imgcover = Const.getShopLogo(model.getUsername());
////                String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
////                // mImageLoader.displayImage(imageurl, holder.avatar, mOptions);
////                Picasso.with(mContext).load(imageurl).into(holder.avatar);
////                holder.name.setText(model.getNick() == null ? username : model.getNick());
////            } else {
////                holder.avatar.setImageResource(R.drawable.default_avatar);
////                holder.name.setText(username);
////            }
////
////
////
////            if(username.equals("861619")||username.equals("861624")){
////                holder.name.setText("专属客服-"+"小格调");
////            }
////
////            if(username.equals("862418")){
////                holder.name.setText("系统通知消息");
////            }
////
////            if(username.equals(SpManager.getECC_USER_ID(mContext))){
////                holder.name.setText("专属客服-"+SpManager.getECC_USER_NAME(mContext));
////            }
////
////
////
////        }
////
//
//
//        return convertView;
//    }
//
//    // 判断emmessage是不是商品消息
//
//    private boolean isItemMessage(EMMessage message) {
//        if (message.getBody() instanceof TextMessageBody) {
//            TextMessageBody txtBody = (TextMessageBody) message.getBody();
//            if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 根据消息内容和消息类型获取消息内容提示
//     *
//     * @param message
//     * @param context
//     * @return
//     */
//    private String getMessageDigest(EMMessage message, Context context) {
//        String digest = "";
//        switch (message.getType()) {
//            case LOCATION: // 位置消息
//                if (message.direct == EMMessage.Direct.RECEIVE) {
//                    // 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
//                    // digest = EasyUtils.getAppResourceString(context,
//                    // "location_recv");
//                    digest = getStrng(context, R.string.location_recv);
//                    digest = String.format(digest, message.getFrom());
//                    return digest;
//                } else {
//                    // digest = EasyUtils.getAppResourceString(context,
//                    // "location_prefix");
//                    digest = getStrng(context, R.string.location_prefix);
//                }
//                break;
//            case IMAGE: // 图片消息
//                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
//                digest = getStrng(context, R.string.picture) + imageBody.getFileName();
//                break;
//            case VOICE:// 语音消息
//                digest = getStrng(context, R.string.voice);
//                break;
//            case VIDEO: // 视频消息
//                digest = getStrng(context, R.string.video);
//                break;
//            case TXT: // 文本消息
//                if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
//                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
//                    digest = txtBody.getMessage();
//                } else {
//                    TextMessageBody txtBody = (TextMessageBody) message.getBody();
//                    digest = getStrng(context, R.string.voice_call) + txtBody.getMessage();
//                }
//                break;
//            case FILE: // 普通文件消息
//                digest = getStrng(context, R.string.file);
//                break;
//            default:
//                System.err.println("error, unknow type");
//                return "";
//        }
//
//        return digest;
//    }
//
//    private static class ViewHolder {
//        /**
//         * 和谁的聊天记录
//         */
//        TextView name;
//        /**
//         * 消息未读数
//         */
//        TextView unreadLabel;
//        /**
//         * 最后一条消息的内容
//         */
//        TextView message;
//        /**
//         * 最后一条消息的时间
//         */
//        TextView time;
//        /**
//         * 用户头像
//         */
//        ImageView avatar;
//        /**
//         * 最后一条消息的发送状态
//         */
//        View msgState;
//        /**
//         * 整个list中每一行总布局
//         */
//        RelativeLayout list_item_layout;
//
//    }
//
//    String getStrng(Context context, int resId) {
//        return context.getResources().getString(resId);
//    }
//}
