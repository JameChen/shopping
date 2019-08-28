//package com.nahuo.quicksale.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.drawable.Drawable;
//import android.text.Html;
//import android.text.Spannable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.TextView.BufferType;
//
//import com.hyphenate.chat.EMMessage;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.ItemDetailsActivity;
//import com.nahuo.quicksale.PicGalleryActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.SmileUtils;
//import com.nahuo.quicksale.im.ChatActivity;
//import com.nahuo.quicksale.im.MyContextMenu;
//import com.nahuo.quicksale.oldermodel.MoreRecordModel;
//import com.nahuo.quicksale.oldermodel.ShopItemModel;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
///**
// * @author 诚 更多聊天记录适配器
// */
//public class MoreRecordAdapter extends MyBaseAdapter<MoreRecordModel> {
//    private Html.ImageGetter imageGetter;
//    private Resources        mResource;
//
//    private LayoutInflater   inflater;
//    private int              mId;
//    private Activity         activity;
//
//    public MoreRecordAdapter(Context context, int meid) {
//        super(context);
//        // TODO Auto-generated constructor stub
//        mResource = context.getResources();
//        inflater = LayoutInflater.from(context);
//        mId = meid;
//        activity = (Activity)context;
//        imageGetter = new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                try {
//                    Drawable drawable = null;
//                    int rId = Integer.parseInt(source);
//                    drawable = mResource.getDrawable(rId);
//                    drawable.setBounds(0, 0, Const.getQQFaceWidth(mContext), Const.getQQFaceWidth(mContext));
//                    return drawable;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//
//            }
//
//        };
//    }
//
//    // 判断emmessage是不是商品消息
//    private boolean isItemMessage(String message) {
//
//        if (message.startsWith("[商品:") && message.endsWith("]")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private View createViewByMessage(MoreRecordModel message, int position) {
//        switch (message.getMsgtype()) {
//
//            case 2: // txt
//                return mId == message.getFrom() ? inflater.inflate(R.layout.row_received_picture, null) : inflater
//                        .inflate(R.layout.row_sent_picture, null);
//
//            default:
//                if (isItemMessage(message.getContents())) {
//                    return mId == message.getFrom() ? inflater.inflate(R.layout.row_received_item_message, null)
//                            : inflater.inflate(R.layout.row_sent_item_message, null);
//                } else {
//                    return mId == message.getFrom() ? inflater.inflate(R.layout.row_received_message, null) : inflater
//                            .inflate(R.layout.row_sent_message, null);
//                }
//
//        }
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//
//        final MoreRecordModel message = mdata.get(position);
//
//        int count = getCount();
//        if (count == 0) {
//            return convertView;
//        }
//        ViewHolder holder = null;
//
//       // if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = createViewByMessage(message, position);
//
//            if (message.getMsgtype() == 2) {
//                holder.iv = ((ImageView)convertView.findViewById(R.id.iv_sendPicture));
//                holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                holder.tv = (TextView)convertView.findViewById(R.id.percentage);
//                holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//            } else if (message.getMsgtype() == 1) {
//
//                if (isItemMessage(message.getContents())) {
//
//                    holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                    holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                    // 这里是款式内容
//                    holder.item_cover = (ImageView)convertView.findViewById(R.id.tv_chat_item_img);
//                    holder.item_view = (RelativeLayout)convertView.findViewById(R.id.chat_contents);
//                    holder.tv = (TextView)convertView.findViewById(R.id.tv_chat_item_intro);
//                    holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                } else {
//                    try {
//
//                        holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                        holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                        // 这里是文字内容
//                        holder.tv = (TextView)convertView.findViewById(R.id.tv_chatcontent);
//                        holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            } else {
//                try {
//
//                    holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                    holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                    // 这里是文字内容
//                    holder.tv = (TextView)convertView.findViewById(R.id.tv_chatcontent);
//                    holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            holder.head_iv.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    int userid = 0;
//                    if (mId == message.getFrom()) {
//                        userid = message.getFrom();
//                    } else {
//                        userid = message.getFrom();
//                    }
////                    Intent userInfoIntent = new Intent(activity, UserInfoActivity.class);
////                    userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, userid);
////                    activity.startActivity(userInfoIntent);
//                }
//            });
//           // convertView.setTag(holder);
//    //    } else {
//           /* holder = (ViewHolder)convertView.getTag();
//
//            // 因发送商品、文本都是txt模式。会有view复用问题，这里判断有问题的话，重置一下viewholder
//            if (message.getMsgtype() == 1) {
//
//                boolean isitemMessage = isItemMessage(message.getContents());
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
//
//                    if (message.getMsgtype() == 2) {
//                        holder.iv = ((ImageView)convertView.findViewById(R.id.iv_sendPicture));
//                        holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                        holder.tv = (TextView)convertView.findViewById(R.id.percentage);
//                        holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                        holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//                    } else if (message.getMsgtype() == 1) {
//
//                        if (isItemMessage(message.getContents())) {
//
//                            holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                            holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                            // 这里是款式内容
//                            holder.item_cover = (ImageView)convertView.findViewById(R.id.tv_chat_item_img);
//                            holder.item_view = (LinearLayout)convertView.findViewById(R.id.tv_chat_item_view);
//                            holder.tv = (TextView)convertView.findViewById(R.id.tv_chat_item_intro);
//                            holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                        } else {
//                            try {
//
//                                holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                                holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                                // 这里是文字内容
//                                holder.tv = (TextView)convertView.findViewById(R.id.tv_chatcontent);
//                                holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    } else {
//                        try {
//
//                            holder.staus_iv = (ImageView)convertView.findViewById(R.id.msg_status);
//                            holder.head_iv = (ImageView)convertView.findViewById(R.id.iv_userhead);
//                            // 这里是文字内容
//                            holder.tv = (TextView)convertView.findViewById(R.id.tv_chatcontent);
//                            holder.tv_userId = (TextView)convertView.findViewById(R.id.tv_userid);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    holder.head_iv.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            int userid = 0;
//                            if (mId == message.getFrom()) {
//                                userid = message.getTo();
//                            } else {
//                                userid = message.getFrom();
//                            }
//                            Intent userInfoIntent = new Intent(activity, UserInfoActivity.class);
//                            userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, userid);
//                            activity.startActivity(userInfoIntent);
//                        }
//                    });
//                    convertView.setTag(holder);
//                }
//            }
//
//            if (message.getMsgtype() == 2) {
//
//                holder.head_iv.setVisibility(View.VISIBLE);
//                holder.tv.setVisibility(View.GONE);
//
//            } else if (message.getMsgtype() == 1) {
//                if (isItemMessage(message.getContents())) {
//
//                    // holder.staus_iv.setVisibility(View.GONE);
//                    holder.head_iv.setVisibility(View.VISIBLE);
//                    // 这里是款式内容
//                    holder.item_cover.setVisibility(View.VISIBLE);
//                    holder.item_view.setVisibility(View.VISIBLE);
//                    holder.tv.setVisibility(View.VISIBLE);
//
//                } else {
//                    try {
//                        holder.tv.setVisibility(View.VISIBLE);
//                        holder.head_iv.setVisibility(View.VISIBLE);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//            }
//
//        }*/
//
//        switch (message.getMsgtype()) {
//        // 根据消息type显示item
//
//            case 1: // 文本
//
//                if (isItemMessage(message.getContents())) {
//                    handleItemMessage(message, holder, position);
//                } else {
//                    handleTextMessage(message, holder, position);
//                }
//
//                break;
//
//            case 2: // 图片
//                handleImageMessage(message, holder, position, convertView);
//                break;
//
//            default:
//                handleTextMessage(message, holder, position);
//                break;
//
//        }
//
//        TextView timestamp = (TextView)convertView.findViewById(R.id.timestamp);
//        String date = FunctionHelper.getFriendlyTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(
//                message.getDatetime())));
//
//        timestamp.setText(date);
//        timestamp.setVisibility(View.VISIBLE);
//
//        // 头像
//        if (message.getFrom() == mId) {
//            String d_imgcover = Const.getShopLogo(message.getFrom());
//            String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//
//            Picasso.with(mContext).load(imageurl).into(holder.head_iv);
//        } else {
//            String d_imgcover1 = Const.getShopLogo(message.getFrom());
//            String imageurl1 = ImageUrlExtends.getImageUrl(d_imgcover1, Const.LIST_COVER_SIZE);
//
//            Picasso.with(mContext).load(imageurl1).into(holder.head_iv);
//        }
//
//        return convertView;
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
//    private void handleImageMessage(final MoreRecordModel message, final ViewHolder holder, final int position,
//            View convertView) {
//        // holder.pb.setTag(position);
//        if (holder.tv != null)
//            holder.tv.setText("");
//        /*
//         * holder.iv.setOnLongClickListener(new OnLongClickListener() {
//         *
//         * @Override public boolean onLongClick(View v) {
//         *
//         * activity.startActivityForResult( (new Intent(activity, MyContextMenu.class)).putExtra("position",
//         * position).putExtra("type", EMMessage.Type.IMAGE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//         *
//         * return true; } });
//         */
//        if (holder.iv != null) {
//            Picasso.with(mContext).load(message.getContents()).resize(220, 220).into(holder.iv);
//            final ArrayList<String> mBasePicUrls = new ArrayList<String>();
//            mBasePicUrls.add(message.getContents());
//            holder.iv.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    Intent intent = new Intent(mContext, PicGalleryActivity.class);
//                    // intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, mPager.getCurrentItem());
//                    intent.putStringArrayListExtra(PicGalleryActivity.EXTRA_URLS, mBasePicUrls);
//                    intent.putExtra(PicGalleryActivity.EXTRA_TRANSFER_IMG_URL, false);
//                    mContext.startActivity(intent);
//                }
//            });
//            /*
//             * public void onClick(View v) { Intent intent = new Intent(mContext, PicGalleryActivity.class);
//             * intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, mPager.getCurrentItem());
//             * intent.putStringArrayListExtra(PicGalleryActivity.EXTRA_URLS, mBasePicUrls); startActivity(intent); }
//             */
//        }
//
//    }
//
//    /**
//     * 文本消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     */
//    private void handleTextMessage(MoreRecordModel message, ViewHolder holder, final int position) {
//
//        SmileUtils.setinitparm(mContext);
//        Spannable span = SmileUtils.getSmiledText(mContext, message.getContents());
//        // 设置内容
//
//        holder.tv.setText(span, BufferType.SPANNABLE);
//        holder.tv.setTag(span);
//        holder.tv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//
//                activity.startActivityForResult(
//                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
//                                EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//
//                return true;
//            }
//        });
//
//    }
//
//    /**
//     * 商品消息
//     *
//     * @param message
//     * @param holder
//     * @param position
//     */
//    private void handleItemMessage(MoreRecordModel message, ViewHolder holder, final int position) {
//        // TextMessageBody txtBody = (TextMessageBody) message.getBody();
//        ShopItemModel item = getItemMessage(message.getContents());
//        SmileUtils.setinitparm(mContext);
//        Spannable span = SmileUtils.getSmiledText(mContext, item.getIntro());
//        // 设置内容
//        holder.tv.setText(span, BufferType.SPANNABLE);
//
//        // TextMessageBody txtBody = (TextMessageBody)message.getBody();
//
//        holder.tv.setTag(message.getContents());
//        holder.tv.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//
//                activity.startActivityForResult(
//                        (new Intent(activity, MyContextMenu.class)).putExtra("position", position).putExtra("type",
//                                EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
//
//                return true;
//            }
//        });
//
//        if (isItemMessage(message.getContents())) {
//            if (holder.item_cover != null) {
//                // 商品图片
//                Picasso.with(mContext).load(ImageUrlExtends.getImageUrl(item.getCover(), Const.LIST_ITEM_SIZE)).into(holder.item_cover);
//            }
//            if (holder.item_view != null) {
//                holder.item_view.setTag(item.ID);
//                holder.item_view.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // 进入商品详情
//                        Intent gotoItemIntent = new Intent(mContext, ItemDetailsActivity.class);
//                        gotoItemIntent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.valueOf(v.getTag().toString()));
//                        mContext.startActivity(gotoItemIntent);
//                    }
//                });
//            }
//        }
//
//    }
//
//    // 根据emmessage对象返回这个对象对应的商品对象，如果不是商品对象，则返回null
//    private ShopItemModel getItemMessage(String message) {
//
//        if (message.startsWith("[商品:") && message.endsWith("]")) {
//            String itemJson = message.substring(4, message.length() - 1);
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
//    // 转到wp
//    private static class ViewHolder {
//        ImageView    iv;
//        TextView     tv;
//        // ProgressBar pb;
//        ImageView    staus_iv;
//        ImageView    head_iv;
//        TextView     tv_userId;
//        ImageView    playBtn;
//        TextView     timeLength;
//        TextView     size;
//        LinearLayout container_status_btn;
//        LinearLayout ll_container;
//        ImageView    iv_read_status;
//        // 显示已读回执状态
//        TextView     tv_ack;
//        // 显示送达回执状态
//        TextView     tv_delivered;
//
//        TextView     tv_file_name;
//        TextView     tv_file_size;
//        TextView     tv_file_download_state;
//
//        ImageView    item_cover;
//        RelativeLayout item_view;
//
//    }
//
//}
