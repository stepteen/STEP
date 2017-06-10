package example.com.step.bean;

/**
 * Created by qinghua on 2016/7/31.
 */
public class Page {
    private static Integer startIndex;
    private static Integer pageSize;
    public Page()
    {
        startIndex=0;
        pageSize=10;
    }
    public Integer getStartIndex() {
        return startIndex;
    }



    public Integer getPageSize() {
        return pageSize;
    }



    public void addstartIndex()
    {
        startIndex=startIndex+pageSize;
    }
}
