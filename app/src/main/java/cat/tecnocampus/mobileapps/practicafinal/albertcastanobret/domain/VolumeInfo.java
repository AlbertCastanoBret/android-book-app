package cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.domain;

import java.io.Serializable;

import cat.tecnocampus.mobileapps.practicafinal.albertcastanobret.domain.ImageLinks;

public class VolumeInfo implements Serializable {
    private String title;
    private String subtitle;
    private String[] authors;
    private String description;
    private ImageLinks imageLinks;

    private String previewLink;
    private String infoLink;

    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public String[] getAuthors() {
        return authors;
    }
    public String getDescription() {return description;}
    public ImageLinks getImageLinks(){
        return imageLinks;
    }
    public String getPreviewLink() {return previewLink;}
    public String getInfoLink() {return infoLink;}
}

