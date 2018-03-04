package sg.vinova.noticeboard.network;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.ClusterResponse;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.model.MessageResponse;
import sg.vinova.noticeboard.model.SignUpResponse;

import static sg.vinova.noticeboard.network.MyApi.PREFIX;
import static sg.vinova.noticeboard.network.MyApi.VERSION;

/**
 * Created by cuong on 4/27/17.
 */

public interface MyApi {
    String PREFIX = "api/";
    String VERSION = "v1/";

    @FormUrlEncoded
    @POST(PREFIX + VERSION + "auth_cluster/sign_in")
    Flowable<BaseObjectResponse<ClusterResponse>> postcode(@Field("code") String code,
                                                           @Field("pass_code") String passCode);

    @FormUrlEncoded
    @POST(PREFIX + VERSION + "auth/sign_in")
    Flowable<BaseObjectResponse<LoginResponse>> login(@Field("email") String email,
                                                      @Field("password") String password,
                                                      @Field("device_token") String deviceToken,
                                                      @Field("platform") String platform);

    @FormUrlEncoded
    @POST(PREFIX + VERSION + "auth/sign_up")
    Flowable<BaseObjectResponse<LoginResponse>> signUp(@Field("email") String email,
                                                       @Field("password") String password,
                                                       @Field("password_confirmation") String passwordConfirmation,
                                                       @Field("username") String username,
                                                       @Field("phone_number") String phoneNumber,
                                                       @Field("device_token") String deviceToken,
                                                       @Field("platform") String platform);

    @FormUrlEncoded
    @POST(PREFIX + VERSION + "auth/forgot_password")
    Flowable<BaseObjectResponse<MessageResponse>> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @PUT(PREFIX + VERSION + "user/change_password")
    Flowable<BaseObjectResponse<MessagesApi>> changePassword(@Field("old_password") String oldPass,
                                                             @Field("new_password") String newPass,
                                                             @Field("new_password_confirmation") String newPassConfirmation);

    @FormUrlEncoded
    @PUT(PREFIX + VERSION + "user")
    Flowable<BaseObjectResponse<LoginResponse>> updateProfile(@Field("username") String username,
                                                              @Field("phone_number") String phoneNumber);

    @GET(PREFIX + VERSION + "category")
    Flowable<BaseListObjectResponse<Category>> getListCategory();


    @POST(PREFIX + VERSION + "category/notice_board_description")
    Flowable<BaseObjectResponse<Description>> postDescription(@Body RequestBody body);

    @GET(PREFIX + VERSION + "user")
    Flowable<BaseObjectResponse<LoginResponse>> getUser();


    @GET(PREFIX + VERSION + "category/{id}/items")
    Flowable<BaseListObjectResponse<Description>> getListItem(@Path("id") String id,
                                                              @Query("page") String page,
                                                              @Query("per_page") String per_page);

    @GET(PREFIX + VERSION + "category/{id}/items")
    Flowable<BaseListObjectResponse<Description>> getLostFound(@Path("id") String id,
                                                               @Query("page") String page,
                                                               @Query("per_page") String per_page,
                                                               @Query("type_mode") String type_mode);

    @POST(PREFIX + VERSION + "category/notice_board_description")
    Flowable<BaseObjectResponse<LoginResponse>> postItem(@Body RequestBody body);


    @POST(PREFIX + VERSION + "category/notice_board_gogreen_sale")
    Flowable<BaseObjectResponse<LoginResponse>> postItemSale(@Body RequestBody body);

    @DELETE(PREFIX + VERSION + "category/{id}/items/{id_item}")
    Flowable<BaseObjectResponse<MessagesApi>> deleteItem(@Path("id") String id, @Path("id_item") String idItem);

    @POST(PREFIX + VERSION + "feedback")
    Flowable<BaseObjectResponse<MessagesApi>> sendFeedback(@Body RequestBody body);

    // add

    @POST(PREFIX + VERSION + "notice_board_estate_property")
    Flowable<BaseObjectResponse> postEstateSaleRent(@Body RequestBody body);

    @PUT(PREFIX + VERSION + "notice_board_estate_property/{id}")
    Flowable<BaseObjectResponse<MessagesApi>> updateEstateProperty(@Path("id") String id, @Body RequestBody body);


    // transaction
    @POST(PREFIX + VERSION + "noticeboard_transaction_sales")
    Flowable<BaseObjectResponse> postTransactionSale(@Body RequestBody body);

    @POST(PREFIX + VERSION + "notice_board_transaction_rentals")
    Flowable<BaseObjectResponse> postTransactionRent(@Body RequestBody body);


    @PUT(PREFIX + VERSION + "noticeboard_transaction_sales/{id}")
    Flowable<BaseObjectResponse<MessagesApi>> updateTransactionSale(@Path("id") String id, @Body RequestBody body);

    @PUT(PREFIX + VERSION + "notice_board_transaction_rentals/{id}")
    Flowable<BaseObjectResponse<MessagesApi>> updateTransactionRent(@Path("id") String id, @Body RequestBody body);

    // SMS OPT

    @FormUrlEncoded
    @PUT(PREFIX + VERSION + "user/phone_confirmation")
    Flowable<BaseObjectResponse<LoginResponse>> phoneConfirmation(@Field("code") String code);


    @PUT(PREFIX + VERSION + "user/resend_code")
    Flowable<BaseObjectResponse<MessagesApi>> resendCode();


    @GET(PREFIX + VERSION + "block_user")
    Flowable<BaseListObjectResponse<BlockedUsers>> getBlockedUsers(@Query("page") String page,
                                                                   @Query("per_page") String per_page);

    @POST(PREFIX + VERSION + "block_user/{block_id}/active_block")
    Flowable<BaseObjectResponse<BlockedUsers>> blockUser(@Path("block_id") String blockId);

    @DELETE(PREFIX + VERSION + "block_user/{block_id}/unactive_block")
    Flowable<BaseObjectResponse<MessageResponse>> unlockUser(@Path("block_id") String blockId);

    @FormUrlEncoded
    @POST(PREFIX + VERSION + "report")
    Flowable<BaseObjectResponse<MessageResponse>> report(@Field("id_category") String categoryId,
                                                         @Field("id_item") String itemId,
                                                         @Field("description") String des);
}
