package tuanbm.hust.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignupResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private Errors errors;

    /**
     * No args constructor for use in serialization
     *
     */
    public SignupResponse() {
    }

    /**
     *
     * @param message
     * @param errors
     */
    public SignupResponse(String message, Errors errors) {
        super();
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

}