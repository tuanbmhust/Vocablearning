package tuanbm.hust.object;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseWords {
    @SerializedName("mes")
    @Expose
    private String mes = null;
    @SerializedName("succes")
    @Expose
    private Boolean success;
    @SerializedName("en_vi")
    @Expose
    private List<Word> words;

    public ResponseWords(String mes, List<Word> words) {
        this.mes = mes;
        this.words = words;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public boolean isMesNullOrEmpty(){
        if(null == this.mes) return true;
        else return ("".equals(this.mes) ? true : false);
    }
}
