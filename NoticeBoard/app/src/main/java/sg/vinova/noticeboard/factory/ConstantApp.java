package sg.vinova.noticeboard.factory;

/**
 * Created by Ray on 3/6/17.
 */

public interface ConstantApp {

    String EMAIL_ADDRESS = "william@vinova.sg";
    String TERM_URL = "http://www.residentagent.sg/index.php/2-uncategorised/112-tack-it-terms-of-use";

    int MAX_PHOTO_POST = 10;

    interface CATEGORY {
        interface ICON {
            String NOTICE = "sticky_note";
            String BUTLER = "resident_butler";
            String AGENT = "resident_agent";
        }

        interface TYPE {
            String DESCRIPTION = "noticeboard_description";
            String GO_GREEN_SALE = "noticeboard_gogreen_sale";
            String ESTATE_SALE = "noticeboard_estate_sale";
            String ESTATE_RENT = "noticeboard_estate_rent";
            String ESTATE_PROPERTY = "noticeboard_estate_property";
            String TRANSACTION = "noticeboard_transaction";
            String TRANSACTION_SALE = "noticeboard_transaction_sale";
            String TRANSACTION_RENT = "noticeboard_transaction_rental";
            String CONTACT = "noticeboard_contact";
            String WEBVIEW = "webview";
        }

        interface FIXNAME {
            //type description
            String FEEDBACK = "feedback";
            String ANNOUNCEMENTS_BY_ESTAEMGT = "announcements_by_estatemgt";
            String LOST_FOUND = "gogreen_lost_found";
            String PHOTO_SHARE = "residents_photos_sharing";
            String RECIPES = "residents_recipes_sharing"; // get together - hobby the same

            // type contact
            String DIRECTTORY = "important_phone_directory";
            //
            String AGENT_DIRECTORY = "resident_agent_directory";
            String GAME = "resident_agent_compass_game";
            String COOK_BOOK = "resident_agent_ecook_book";
            String BUTLER_BOOK = "resident_butler_ebutler_book";
            String GROUPTOUR_VIEWING = "grouptour_viewing_schedule";
        }

        interface URLWEB {
            String AGENT_DIRECTORY = "http://www.residentagent.sg/index.php/find-singapore-property/find-singapore-property-through-registered-residentagent";
            String COOKBOOK = "http://www.residentagent.sg/index.php/cook-book";
            String GAME = "http://www.residentagent.sg/index.php/find-singapore-property/residentagent-house-hunting-game";
            String BUTLER_BOOK = "http://www.residentagent.sg/index.php/butler-book";
        }
    }

    interface FEEDBACK {
        String FEEDBACK_APP = "feedback_app";
        String FEEDBACK_MCST = "feedback_mcst";
    }

    interface TYPESUBMIT {
        String RENT = "rent";
        String SALE = "sale";
    }


}
