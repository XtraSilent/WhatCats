package com.xtrasilent.whatcats;

/**
 * Created by Juned on 2/8/2017.
 */

public class DataAdapter
{
    public String ImageURL;
    public String ImageTitle;
    public String username;

        public String getImageUrl() {

            return ImageURL;
        }

        public void setImageUrl(String imageServerUrl) {

            this.ImageURL = imageServerUrl;
        }

        public String getImageTitle() {

            return ImageTitle;
        }

        public void setImageTitle(String Imagetitlename) {

            this.ImageTitle = Imagetitlename;
        }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}