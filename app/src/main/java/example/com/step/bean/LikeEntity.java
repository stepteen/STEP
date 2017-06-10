package example.com.step.bean;

/**
 * Created by qinghua on 2016/12/3.
 * 点赞实体
 * 这里的synamic_id对应Synamic里的ringId
 * like_id就是用户id
 */

public class LikeEntity {

    private Integer synamic_id;//动态编号
    private String  like_id;//点赞者id
    private String  likerHeadPicture;//点赞者头像
    public Integer getSynamic_id() {
        return synamic_id;
    }

    public void setSynamic_id(Integer synamic_id) {
        this.synamic_id = synamic_id;
    }

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }


    public String getLikerHeadPicture() {
        return likerHeadPicture;
    }

    public void setLikerHeadPicture(String likerHeadPicture) {
        this.likerHeadPicture = likerHeadPicture;
    }
}
