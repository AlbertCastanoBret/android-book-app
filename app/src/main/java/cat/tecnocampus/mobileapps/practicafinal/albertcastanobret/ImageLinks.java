package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import java.io.Serializable;

public class ImageLinks implements Serializable {
    private String smallThumbnail;
    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

