package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Description:代理分组 2014-7-7下午5:48:53
 */
public class AgentGroup implements Serializable {

    private static final long serialVersionUID = -5810179691238933548L;
    @Expose
    @SerializedName("ID")
    private int groupId;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    private int GroupID; // 灏�缁�
    @Expose
    private CanType CanPostActivity;
    @Expose
    private CanType CanJoin;
    @Expose
    private CanType CanPostTopic;
    @Expose
    private boolean IsMember;
    @Expose
    private CanType.JoinSettingType JoinSetting;
    @Expose
    private int MemberCount;

    public int getTopicCount() {
        return TopicCount;
    }

    public void setTopicCount(int topicCount) {
        TopicCount = topicCount;
    }

    @Expose
    private int TopicCount;

    public String getLogoUrl() {
        return LogoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        LogoUrl = logoUrl;
    }

    @Expose
    private String LogoUrl;

    /**
     * 该分组下的用户数
     */
    @Expose
    @SerializedName("UserCount")
    private int agentNum;

    public enum PlateType {
        首页, 话题, 碎碎念, 分享, 活动, 供货,
    }

    public CanType getCanPostActivity() {
        return CanPostActivity;
    }
    public CanType.JoinSettingType getJoinSetting() {
        return JoinSetting;
    }

    public void setJoinSetting(CanType.JoinSettingType joinSetting) {
        JoinSetting = joinSetting;
    }
    public void setCanPostActivity(CanType canPostActivity) {
        CanPostActivity = canPostActivity;
    }

    public CanType getCanPostTopic() {
        return CanPostTopic;
    }

    public void setCanPostTopic(CanType canPostTopic) {
        CanPostTopic = canPostTopic;
    }
    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public void setGroupID(int groupID) {
        GroupID = groupID;
    }

    public int getGroupID() {
        return GroupID;
    }

    public CanType getCanJoin() {
        return CanJoin;
    }

    public void setCanJoin(CanType canJoin) {
        CanJoin = canJoin;
    }
    public boolean isIsMember() {
        return IsMember;
    }

    public void setIsMember(boolean isMember) {
        IsMember = isMember;
    }

    @Expose
    private int[] Plate;

    /**
     * 是否可编辑，默认可编辑的
     */
    private boolean editable = true;

    private boolean selected;

    private List<AgentGroup> subGroups;

    private List<Agent> groupAgents;

    public AgentGroup() {

    }

    public AgentGroup(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(int agentNum) {
        this.agentNum = agentNum;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<AgentGroup> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<AgentGroup> subGroups) {
        this.subGroups = subGroups;
    }

    public List<Agent> getGroupAgents() {
        return groupAgents;
    }

    public void setGroupAgents(List<Agent> groupAgents) {
        this.groupAgents = groupAgents;
    }

    public boolean getHasPlateWithEnum(PlateType plate) {
        int plateNumber = 0;
        if (plate == PlateType.首页) {
            plateNumber = 0;
        } else if (plate == PlateType.话题) {
            plateNumber = 1;
        } else if (plate == PlateType.碎碎念) {
            plateNumber = 2;
        } else if (plate == PlateType.分享) {
            plateNumber = 3;
        } else if (plate == PlateType.活动) {
            plateNumber = 4;
        } else if (plate == PlateType.供货) {
            plateNumber = 5;
        }
        for (int plates : Plate) {
            if (plateNumber == plates) {
                return true;
            }
        }
        return false;
    }

    public class CanType implements Serializable {

        private static final long serialVersionUID = -3729031810077531167L;

        @Expose
        private boolean IsSuccess;
        @Expose
        private String Message;
        @Expose
        private String Code;
        @Expose
        private CanTypeDataType Data;

        public boolean isIsSuccess() {
            return IsSuccess;
        }

        public void setIsSuccess(boolean isSuccess) {
            IsSuccess = isSuccess;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }

        public CanTypeDataType getData() {
            return Data;
        }

        public void setData(CanTypeDataType data) {
            Data = data;
        }


        public class CanTypeDataType implements Serializable {

            private static final long serialVersionUID = -3729031810077531170L;

            @Expose
            private int ID;

            public int getID() {
                return ID;
            }

            public void setID(int iD) {
                ID = iD;
            }

        }
        public class JoinSettingType implements Serializable {

            private static final long serialVersionUID = -3729031810077531166L;

            @Expose
            private int Type;
            @Expose
            private String Question;
            @Expose
            private String Answer;
            @Expose
            private String Tips;

            public int getType() {
                return Type;
            }

            public void setType(int type) {
                Type = type;
            }

            public String getQuestion() {
                return Question;
            }

            public void setQuestion(String question) {
                Question = question;
            }

            public String getAnswer() {
                return Answer;
            }

            public void setAnswer(String answer) {
                Answer = answer;
            }

            public String getTips() {
                return Tips;
            }

            public void setTips(String tips) {
                Tips = tips;
            }

        }

    }
}
