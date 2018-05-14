package adr.ycx.com.drawdemo.data;

import java.io.Serializable;

public class SimpleData implements Serializable {

    String imageUrl;
    String imageName;
    String imageCreateTime;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageCreateTime() {
        return imageCreateTime;
    }

    public void setImageCreateTime(String imageCreateTime) {
        this.imageCreateTime = imageCreateTime;
    }
}
