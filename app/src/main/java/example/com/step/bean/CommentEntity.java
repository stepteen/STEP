package example.com.step.bean;

import java.util.Date;

/**
 * Created by qinghua on 2016/12/3.
 * 存放评论信息的实体
 * 这里的synamic_id对应Synamic里的ringId
 * observer_id用户id
 * receiver_id对应Synamic实体中的creatorNo发表者id
 */

public class CommentEntity {
    private Integer synamicId;//发表的朋友圈的编号
    private String  observerId;//评论者id
    private  String observerName;//评论者昵称
    private String  receiverId;//接收者id
    private  String receiverName;//接收者昵称
    private  String  comment_content;//评论内容
    private String  commentHeadPicture;
    private String observerHeadPicture;//评论者头像
    private Date commentTime;//评论时间
    public CommentEntity()
    {

    }
    public CommentEntity(String observer_name, String comment_content) {
        this.observerName = observer_name;
        this.comment_content = comment_content;
    }

    public Integer getSynamicId() {
        return synamicId;
    }

    public void setSynamicId(Integer synamicId) {
        this.synamicId = synamicId;
    }

    public String getObserverId() {
        return observerId;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getCommentHeadPicture() {
        return commentHeadPicture;
    }

    public void setCommentHeadPicture(String commentHeadPicture) {
        this.commentHeadPicture = commentHeadPicture;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getObserverHeadPicture() {
        return observerHeadPicture;
    }

    public void setObserverHeadPicture(String observerHeadPicture) {
        this.observerHeadPicture = observerHeadPicture;
    }
}
