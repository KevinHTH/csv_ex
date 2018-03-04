package sg.vinova.noticeboard.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vinova on 28/4/17.
 */


@Getter
@Setter
public class Category {
    /**"id": 1,
         "name": "Residents Season-Greetings™",
         "fixed_name": "residents_season_greetings",
         "type_mode": "noticeboard_description",
         "icon_mode": "sticky_note",
         "total": 0,
         "photo_url": "http://noticeboard-staging.s3.amazonaws.com/staging/categories/photos/1
     _%20partition/original/season_greeting.png?1493780216",
         "position": 1*/

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("fixed_name")
    private String fixedName;

    @SerializedName("type_mode")
    private String typeMode;

    @SerializedName("icon_mode")
    private String iconMode;

    @SerializedName("total")
    private int total;

    @SerializedName("photo_url")
    private String photoUrl;

    @SerializedName("position")
    private int position;

    @SerializedName("number_unreads")
    private int numberUnread;

    @SerializedName("permision_action")
    private boolean permissionAction;

    public Category(int id, String name, String fixedName, String typeMode, String iconMode, int total,
                    String photoUrl, int position, int numberUnread, boolean permissionAction) {
        this.id = id;
        this.name = name;
        this.fixedName = fixedName;
        this.typeMode = typeMode;
        this.iconMode = iconMode;
        this.total = total;
        this.photoUrl = photoUrl;
        this.position = position;
        this.numberUnread = numberUnread;
        this.permissionAction = permissionAction;
    }

    public Category() {
    }


    public interface ICON{
        String NOTICE = "sticky_note";
        String BUTLER = "resident_butler";
        String AGENT = "resident_agent";
    }

    public interface TYPE{
        String DESCRIPTION = "noticeboard_description";
        String GO_GREEN_SALE = "noticeboard_gogreen_sale";
        String ESTATE_SALE = "noticeboard_estate_sale";
        String ESTATE_RENT = "noticeboard_estate_rent";
        String TRANSACTION_SALE = "noticeboard_transaction_sale";
        String TRANSACTION_RENT = "noticeboard_transaction_rental";
        String ESTATE_PROPERTY = "noticeboard_estate_property";
        String CONTACT = "noticeboard_contact";
        String WEBVIEW = "webview";
    }

    public interface FIXNAME{
        //type description
        String FEEDBACK = "feedback";
        String LOST_FOUND = "gogreen_lost_found";
        String PHOTO_SHARE = "residents_photos_sharing";
        String RECIPES = "residents_recipes_sharing"; // get together - hobby the same
        String GOGREEN_ITEM_WANTED = "gogreen_items_wanted"; // get together - hobby the same
        String GOGREEN_ITEM_GIVEAWAY = "gogreen_give_away"; // get together - hobby the same

        // type contact
        String DIRECTTORY = "important_phone_directory";
        //
        String AGENT_DIRECTORY = "resident_agent_directory";
        String GAME =  "resident_agent_compass_game";
        String COOK_BOOK = "resident_agent_ecook_book";
        String BUTLER_BOOK =  "resident_butler_ebutler_book";
        String PROPERTY_RENT =  "estate_property_rent";
        String PROPERTY_SALE =  "estate_property_sale";
        String GOGREEN_GARAGE_SALE =  "gogreen_garage_sale";

        String MINIMALL_PRODUCTS =  "estate_minimall_products";
        String MINIMALL_SERVICES =  "estate_minimall_services";

        String RENTAL_TRANSACTIONS =  "estate_rental_transactions";
        String SALE_TRANSACTIONS =  "estate_sale_transactions";
        String PROPERTY_SALE_RENT= "property_for_sale_rent";

        String GROUPTOUR_VIEWING = "grouptour_viewing_schedule";
    }
    public interface URLWEB{
        String AGENT_DIRECTORY= "http://www.residentagent.sg/index.php/find-singapore-property/find-singapore-property-through-registered-residentagent";
        String COOKBOOK= "http://www.residentagent.sg/index.php/cook-book";
        String GAME= "http://www.residentagent.sg/index.php/find-singapore-property/residentagent-house-hunting-game";
        String BUTLER_BOOK = "http://www.residentagent.sg/index.php/butler-book";
    }
}
