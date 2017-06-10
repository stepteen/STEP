package example.com.step.bean;

import java.io.Serializable;
import java.util.Date;

public class Synamic implements Serializable{
    private Integer ringId;//发表的朋友圈的编号
    private String creatorNo;//发表人ID
    private String creatorName;//发表人名字
    private String description;//动态内容
    private String userHeadPicture;//头像
    private String picture;//发表的动态中的图片
    private Integer reviewId;//评论的条数
    private Integer thumb;//点赞数
    private Date createTime;
    private  Integer ringStatus;
    private  String TimeLab;
    // 状态
    public static class ringStatus {
        public static final int VALID = 0; // 可用
        public static final int INVALID = 1; // 禁用
    }

    public Synamic()
    {

    }
    public Synamic(Integer ringId, String creatorNo, String createName, String description, String userHeadPicture, String picture, Integer reviewId, Integer thumb) {
        this.ringId = ringId;
        this.creatorNo = creatorNo;
        this.creatorName = createName;
        this.description = description;
        this.userHeadPicture = userHeadPicture;
        this.picture = picture;
        this.reviewId = reviewId;
        this.thumb = thumb;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getTimeLab() {return TimeLab;}

    public void setTimeLab(String timeLab) {TimeLab = timeLab;}

    public Integer getRingStatus() {return ringStatus;}

    public void setRingStatus(Integer ringStatus) {this.ringStatus = ringStatus;}

    public Integer getRingId() {return ringId;}

    public void setRingId(Integer ringId) {this.ringId = ringId;}

    public String getCreatorNo() {return creatorNo;}

    public void setCreatorNo(String creatorNo) {this.creatorNo = creatorNo;}


    public String getUserHeadPicture() {return userHeadPicture;}

    public void setUserHeadPicture(String userHeadPicture) {this.userHeadPicture = userHeadPicture;}

    public String getPicture() {return picture;}

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setPicture(String picture) {this.picture = picture;}

    public Integer getReviewId() {return reviewId;}

    public void setReviewId(Integer reviewId) {this.reviewId = reviewId;}

    public Integer getThumb() {return thumb;}

    public void setThumb(Integer thumb) {this.thumb = thumb;}

    public Date getCreateTime() {return createTime;}

    public void setCreateTime(Date createTime) {this.createTime = createTime;}
}
