package cn.biggar.biggar.bean;

import android.net.Uri;

/**
 * Created by SUE on 2016/6/24 0024.
 */
public class MusicContainer {

    private String title;
    private String subhead;
    private String iconUrl;
    private Uri albumArtURI;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Uri getAlbumArtURI() {
        return albumArtURI;
    }

    public void setAlbumArtURI(Uri albumArtURI) {
        this.albumArtURI = albumArtURI;
    }
}
