//package com.nahuo.quicksale.adapter;
//
//import android.content.Context;
//import android.util.SparseIntArray;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.SectionIndexer;
//import android.widget.TextView;
//
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.Constant;
//import com.nahuo.quicksale.im.Sidebar;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ContactAdapter extends ArrayAdapter<ChatUserModel> implements SectionIndexer {
//
//  private LayoutInflater layoutInflater;
//
//  private SparseIntArray positionOfSection;
//  private SparseIntArray sectionOfPosition;
//  private Sidebar sidebar;
//  private int res;
//  private Context mContext;
//
//  public ContactAdapter(Context context, int resource, List<ChatUserModel> objects, Sidebar sidebar) {
//    super(context, resource, objects);
//    this.res = resource;
//    this.sidebar = sidebar;
//    layoutInflater = LayoutInflater.from(context);
//    mContext = context;
//  }
//
//  @Override
//  public View getView(int position, View convertView, ViewGroup parent) {
//
//
//    if (convertView == null) {
//      convertView = layoutInflater.inflate(res, null);
//    }
//
//    ViewHolder holder = (ViewHolder) convertView.getTag();
//    if (holder == null) {
//      holder = new ViewHolder();
//
//      holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
//      holder.unreadMsgView = (TextView) convertView.findViewById(R.id.unread_msg_number);
//      holder.nameTextview = (TextView) convertView.findViewById(R.id.name);
//      holder.tvHeader = (TextView) convertView.findViewById(R.id.header);
//      convertView.setTag(holder);
//    }
//    ChatUserModel user = getItem(position);
//    if (user == null) return convertView;
//    // 设置nick，demo里不涉及到完整user，用username代替nick显示
//    final String username = user.getUsername();
//    String header = user.getHeader();
//    if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
//      if ("".equals(header)) {
//        holder.tvHeader.setVisibility(View.GONE);
//      } else {
//        holder.tvHeader.setVisibility(View.VISIBLE);
//        holder.tvHeader.setText(header);
//      }
//    } else {
//      holder.tvHeader.setVisibility(View.GONE);
//    }
//
//    final ImageView avatarimg = holder.avatar;
//    // 显示申请与通知item
//    if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
//      holder.nameTextview.setText(user.getNick());
//      // holder.avatar.setImageResource(R.drawable.new_friends_icon);
//
//      holder.avatar.post(new Runnable() {
//
//        @Override
//        public void run() {
//          // TODO Auto-generated method stub
//          avatarimg.setImageResource(R.drawable.new_friends_icon);
//        }
//      });
//      if (user.getUnreadMsgCount() > 0) {
//        holder.unreadMsgView.setVisibility(View.VISIBLE);
//        holder.unreadMsgView.setText(user.getUnreadMsgCount() + "");
//      } else {
//        holder.unreadMsgView.setVisibility(View.INVISIBLE);
//      }
//      holder.avatar.setOnClickListener(null);
//    } else if (username.equals(Constant.GROUP_USERNAME)) {
//      // 群聊item
//      holder.nameTextview.setText(user.getNick());
//      holder.avatar.setImageResource(R.drawable.groups_icon);
//
//    } else if (username.equals(Constant.ADD_NAHUO_USER)) {
//      // 添加user
//      holder.nameTextview.setText(user.getNick());
//      // holder.avatar.setImageResource(R.drawable.groups_icon);
//
//      holder.avatar.post(new Runnable() {
//
//        @Override
//        public void run() {
//          // TODO Auto-generated method stub
//          avatarimg.setImageResource(R.drawable.groups_icon);
//        }
//      });
//      holder.avatar.setOnClickListener(null);
//    } else {
//      holder.nameTextview.setText(user.getNick() == null ? username : user.getNick());
//      if (holder.unreadMsgView != null) holder.unreadMsgView.setVisibility(View.INVISIBLE);
//      // avatar.setImageResource(R.drawable.default_avatar);
//      if (user.getNick() != null) {
//
//        String d_imgcover = Const.getShopLogo(user.getUsername());
//        final String imageurl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
//
//
//        holder.avatar.post(new Runnable() {
//
//          @Override
//          public void run() {
//            // TODO Auto-generated method stub
//            Picasso.with(mContext).load(imageurl).into(avatarimg);
//          }
//        });
//
//
//
//      } else {
//        holder.avatar.setImageResource(R.drawable.default_avatar);
//      }
//
//      holder.avatar.setOnClickListener(new OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
////          Intent userInfoIntent = new Intent(mContext, UserInfoActivity.class);
////          userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, Integer.valueOf(username));
////          mContext.startActivity(userInfoIntent);
//        }
//      });
//    }
//
//
//    return convertView;
//  }
//
//  @Override
//  public ChatUserModel getItem(int position) {
//    return super.getItem(position);
//  }
//
//  @Override
//  public int getCount() {
//
//    return super.getCount();
//  }
//
//  public int getPositionForSection(int section) {
//    return positionOfSection.get(section);
//  }
//
//  public int getSectionForPosition(int position) {
//    return sectionOfPosition.get(position);
//  }
//
//  @Override
//  public Object[] getSections() {
//    positionOfSection = new SparseIntArray();
//    sectionOfPosition = new SparseIntArray();
//    int count = getCount();
//    List<String> list = new ArrayList<String>();
//    list.add(getContext().getString(R.string.search_header));
//    positionOfSection.put(0, 0);
//    sectionOfPosition.put(0, 0);
//    for (int i = 1; i < count; i++) {
//
//      String letter = getItem(i).getHeader();
//      System.err.println("contactadapter getsection getHeader:" + letter + " name:"
//          + getItem(i).getUsername());
//      int section = list.size() - 1;
//      if (list.get(section) != null && !list.get(section).equals(letter)) {
//        list.add(letter);
//        section++;
//        positionOfSection.put(section, i);
//      }
//      sectionOfPosition.put(i, section);
//    }
//    return list.toArray(new String[list.size()]);
//  }
//
//  private static class ViewHolder {
//    ImageView avatar;
//    TextView unreadMsgView;
//    TextView nameTextview;
//    TextView tvHeader;
//  }
//
//
//}
