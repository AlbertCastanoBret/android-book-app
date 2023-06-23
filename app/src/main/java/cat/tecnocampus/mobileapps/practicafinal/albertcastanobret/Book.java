package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import com.google.gson.annotations.SerializedName;

public class Book {
    private String id;
    private VolumeInfo volumeInfo;

    public String getId() {
        return id;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

}
