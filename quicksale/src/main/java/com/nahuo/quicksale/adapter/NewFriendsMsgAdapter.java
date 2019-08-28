//package com.nahuo.quicksale.adapter;
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.app.ProgressDialog;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnLongClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hyphenate.chat.EMChatManager;
//import com.hyphenate.chat.EMGroupManager;
//import com.nahuo.library.controls.LightAlertDialog;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.db.InviteMessgeDao;
//import com.nahuo.quicksale.oldermodel.InviteMessage;
//import com.nahuo.quicksale.oldermodel.InviteMessage.InviteMesageStatus;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {
//
//    private Context context;
//    private InviteMessgeDao messgeDao;
//
//    public NewFriendsMsgAdapter(Context context, int textViewResourceId,
//            List<InviteMessage> objects) {
//        super(context, textViewResourceId, objects);
//        this.context = context;
//        messgeDao = new InviteMessgeDao(context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = View.inflate(context, R.layout.row_invite_msg, null);
//            holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
//            holder.reason = (TextView) convertView.findViewById(R.id.message);
//            holder.name = (TextView) convertView.findViewById(R.id.name);
//            holder.status = (Button) convertView.findViewById(R.id.user_state);
//            holder.status_re = (Button) convertView
//                    .findViewById(R.id.user_state_re);
//            holder.groupContainer = (LinearLayout) convertView
//                    .findViewById(R.id.ll_group);
//            holder.groupname = (TextView) convertView
//                    .findViewById(R.id.tv_groupName);
//            // holder.time = (TextView) convertView.findViewById(R.id.time);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        final InviteMessage msg = getItem(position);
//
//        convertView.setOnLongClickListener(new OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                // TODO Auto-generated method stub
//
//                Builder builder = LightAlertDialog.Builder.create(context);
//                builder.setTitle("提示")
//                        .setMessage("是否删除本条消息")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                            int which) {
//                                        messgeDao.deleteMessage(msg.getFrom());
//                                        remove(msg);
//                                        notifyDataSetChanged();
//                                    }
//                                });
//                builder.show();
//
//                return false;
//            }
//        });
//
//        if (msg != null) {
//            /*
//             * if (msg.getGroupId() != null) { // 显示群聊提示
//             * holder.groupContainer.setVisibility(View.VISIBLE);
//             * holder.groupname.setText(msg.getGroupName()); } else {
//             * holder.groupContainer.setVisibility(View.GONE); }
//             */
//
//            holder.avator.setTag(msg.getFrom());
//            holder.avator.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    gotoUserInfo(v.getTag().toString());
//                }
//            });
//            holder.name.setTag(msg.getFrom());
//            holder.name.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    gotoUserInfo(v.getTag().toString());
//                }
//            });
//
//            holder.reason.setText(msg.getReason());
//            holder.name.setText(msg.getNick() == null ? msg.getFrom() : msg
//                    .getNick());
//
//            String d_imgcover = Const.getShopLogo(msg.getFrom());
//            String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//
//            Picasso.with(context).load(imageurl).into(holder.avator);
//            // holder.time.setText(DateUtils.getTimestampString(new
//            // Date(msg.getTime())));
//            if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
//                holder.status.setVisibility(View.INVISIBLE);
//                holder.status_re.setVisibility(View.INVISIBLE);
//                holder.reason.setText("已同意");
//            } else if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
//                holder.status.setVisibility(View.VISIBLE);
//                // holder.status_re.setVisibility(View.VISIBLE);
//                if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
//                    if (msg.getReason() == null) {
//                        // 如果没写理由
//                        holder.reason.setText("请求加你为好友");
//                    }
//                } else { // 入群申请
//                    if (TextUtils.isEmpty(msg.getReason())) {
//                        holder.reason.setText("申请加入群：" + msg.getGroupName());
//                    }
//                }
//                // 设置点击事件
//                holder.status.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // 同意别人发的好友请求
//                        acceptInvitation(holder.status, holder.status_re, msg);
//                    }
//                });
//
//                // 设置点击事件
//                holder.status_re.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // 同意别人发的好友请求
//                        refuseInvitation(holder.status_re, holder.status, msg);
//                    }
//                });
//            } else if (msg.getStatus() == InviteMesageStatus.AGREED) {
//                holder.status.setText("已接受");
//
//                holder.status.setEnabled(false);
//
//                holder.status_re.setVisibility(View.GONE);
//            } else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
//                holder.status_re.setText("已拒绝");
//
//                holder.status_re.setEnabled(false);
//                holder.status.setVisibility(View.GONE);
//            }
//
//            // 设置用户头像
//        }
//
//        return convertView;
//    }
//
//    private void gotoUserInfo(String username)
//    {
////        Intent userInfoIntent = new Intent(context,
////                UserInfoActivity.class);
////        userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID,
////                Integer.valueOf(username));
////        context.startActivity(userInfoIntent);
//        }
//
//    /**
//     * 同意好友请求或者群申请
//     *
//     * @param button
//     * @param username
//     */
//    private void acceptInvitation(final Button button, final Button button1,
//            final InviteMessage msg) {
//        final ProgressDialog pd = new ProgressDialog(context);
//        pd.setMessage("正在同意...");
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//
//        new Thread(new Runnable() {
//            public void run() {
//                // 调用sdk的同意方法
//                try {
//                    if (msg.getGroupId() == null) // 同意好友请求
//                        EMChatManager.getInstance().acceptInvitation(
//                                msg.getFrom());
//
//                    else
//                        // 同意加群申请
//                        EMGroupManager.getInstance().acceptApplication(
//                                msg.getFrom(), msg.getGroupId());
//                    ((Activity) context).runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            pd.dismiss();
//                            button.setText("已同意");
//                            msg.setStatus(InviteMesageStatus.AGREED);
//                            // 更新db
//                            ContentValues values = new ContentValues();
//                            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
//                                    .getStatus().ordinal());
//                            messgeDao.updateMessage(msg.getId(), values);
//
//                            button.setEnabled(false);
//                            button1.setVisibility(View.GONE);
//
//                        }
//                    });
//                } catch (final Exception e) {
//                    ((Activity) context).runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(context, "同意失败: " + e.getMessage(),
//                                    1).show();
//                        }
//                    });
//
//                }
//            }
//        }).start();
//    }
//
//    // 拒绝
//    @SuppressWarnings("unused")
//    private void refuseInvitation(final Button button, final Button button1,
//            final InviteMessage msg) {
//        final ProgressDialog pd = new ProgressDialog(context);
//        pd.setMessage("正在拒绝...");
//        pd.setCanceledOnTouchOutside(false);
//        pd.show();
//
//        new Thread(new Runnable() {
//            public void run() {
//                // 调用sdk的拒绝方法
//                try {
//
//                    EMChatManager.getInstance().refuseInvitation(msg.getFrom());
//
//                    ((Activity) context).runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            pd.dismiss();
//                            button.setText("已拒绝");
//                            msg.setStatus(InviteMesageStatus.REFUSED);
//                            // 更新db
//                            ContentValues values = new ContentValues();
//                            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
//                                    .getStatus().ordinal());
//                            messgeDao.updateMessage(msg.getId(), values);
//
//                            button.setEnabled(false);
//                            button1.setVisibility(View.GONE);
//
//                        }
//                    });
//
//                } catch (final Exception e) {
//                    ((Activity) context).runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            pd.dismiss();
//                            Toast.makeText(context, "拒绝失败: " + e.getMessage(),
//                                    1).show();
//                        }
//                    });
//
//                }
//            }
//        }).start();
//    }
//
//    private static class ViewHolder {
//        ImageView avator;
//        TextView name;
//        TextView reason;
//        Button status;
//        Button status_re;
//        LinearLayout groupContainer;
//        TextView groupname;
//        // TextView time;
//    }
//
//}
