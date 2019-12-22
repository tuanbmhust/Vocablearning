package tuanbm.hust.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagWordPost {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("list")
    @Expose
    private String list;

    /**
     * No args constructor for use in serialization
     *
     */
    public TagWordPost() {
    }

    /**
     *
     * @param type
     * @param list
     */
    public TagWordPost(String type, String list) {
        super();
        this.type = type;
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

}