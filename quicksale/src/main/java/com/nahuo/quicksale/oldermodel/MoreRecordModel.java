package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;

public class MoreRecordModel implements Serializable {
    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLastcontent() {
        return lastcontent;
    }

    public void setLastcontent(String lastcontent) {
        this.lastcontent = lastcontent;
    }

    public String getNickto() {
        return nickto;
    }

    public void setNickto(String nickto) {
        this.nickto = nickto;
    }

    @Expose
    private String                    nickto;
    @Expose
    private String                    lastcontent;

    @Expose
    private String                    msgid;
    @Expose
    private int                       from;
    @Expose
    private int                       to;
    @Expose
    private String                    datetime;
    @Expose
    private String                    contents;
    @Expose
    private int                       msgtype;
    @Expose
    private String                    nick;
    private static final List<String> FACE_LIST = Arrays.asList(new String[] {"[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]",
            "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[呲牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]",
            "[抓狂]", "[吐]", "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[悠闲]", "[奋斗]",
            "[咒骂]", "[疑问]", "[嘘]", "[晕]", "[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]", "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]",
            "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]",
            "[啤酒]", "[篮球]", "[乒乓]", "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[嘴唇]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]",
            "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]", "[弱]", "[握手]", "[胜利]",
            "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]",
            "[磕头]", "[回头]", "[跳绳]", "[挥手]", "[激动]", "[街舞]", "[献吻]", "[左太极]", "[右太极]"});

    public static Spanned getTextHtml(String normalText, Context context, Html.ImageGetter imageGetter) {
        // TODO Auto-generated method stub
        String formattedText = normalText;
        if (normalText.contains("[") && normalText.contains("]")) {
            for (String face : FACE_LIST) {
                if (formattedText.contains("[") && formattedText.contains("]")) {
                    if (formattedText.contains(face)) {
                        formattedText = formattedText.replace(
                                face,
                                "<img src=\""
                                        + context.getResources().getIdentifier(
                                                context.getPackageName() + ":drawable/qq_" + FACE_LIST.indexOf(face),
                                                null, null) + "\" />");
                    }
                } else {
                    break;
                }
            }
        }

        return Html.fromHtml(formattedText, imageGetter, null);
    }
}
