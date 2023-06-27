package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret;

import java.io.Serializable;

public class VolumeInfo implements Serializable {
    private String title;
    private String subtitle;

    private String[] authors;

    private ImageLinks imageLinks;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String[] getAuthors() {
        return authors;
    }
    public ImageLinks getImageLinks(){
        return imageLinks;
    }
}

