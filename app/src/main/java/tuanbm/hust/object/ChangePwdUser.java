package tuanbm.hust.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePwdUser {

    @SerializedName("old_pass")
    @Expose
    private String oldPass;
    @SerializedName("new_pass")
    @Expose
    private String newPass;
    @SerializedName("email")
    @Expose
    private String email;

    public ChangePwdUser(String oldPass, String newPass, String email){
        super();
        this.oldPass = oldPass;
        this.newPass = newPass;
        this.email = email;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
