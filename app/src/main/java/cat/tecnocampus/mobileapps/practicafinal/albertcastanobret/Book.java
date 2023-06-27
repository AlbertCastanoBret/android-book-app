package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Book implements Serializable{
    private String id;
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
}
