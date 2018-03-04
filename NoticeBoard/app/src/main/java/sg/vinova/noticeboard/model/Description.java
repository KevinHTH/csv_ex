package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vinova on 4/5/17.
 */
@Getter
@Setter
public class Description {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("user")
    private LoginResponse user;

    @SerializedName("category")
    private Category category;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("description")
    private String description;

    @SerializedName("type_mode")
    private String typeMode;

    @SerializedName("type_property")
    private String type_property;

    @SerializedName("photo_url")
    private String photoUrl;

    @SerializedName("images")
    List<ImageObject> images;
    @SerializedName("no_of_bedrooms")
    private int noOfBedRooms;

    @SerializedName("rental_per_month")
    private float rentalPerMonth;

    @SerializedName("sales_price")
    private float salePrice;

    @SerializedName("psf")
    private float psf;

    @SerializedName("size")
    private float size;



    @SerializedName("price")
    private float price;

    @SerializedName("block")
    private String block;


    @SerializedName("date")
    private String date;

    @SerializedName("owner_item")
    private boolean ownerItem;

    public String getPhotoUrl() {
        if (images != null && images.size() > 0) {
            return images.get(0).getPhotoUrl();
        }
        return photoUrl;
    }

    public interface TYPE{
        String RENT = "rent";
        String SALE = "sale";
    }
}
