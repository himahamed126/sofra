package com.example.sofra.data.api;

import com.example.sofra.data.model.commission.Commission;
import com.example.sofra.data.model.restaurant.Category;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.data.model.contactUs.ContactUs;
import com.example.sofra.data.model.foodlist.Foodlist;
import com.example.sofra.data.model.notification.Notification;
import com.example.sofra.data.model.offers.Offers;

import com.example.sofra.data.model.order.Order;
import com.example.sofra.data.model.publicData.GeneralResponse;
import com.example.sofra.data.model.restaurant.RestaurantDetails;
import com.example.sofra.data.model.restaurant.Restaurants;
import com.example.sofra.data.model.reviews.Reviews;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServices {

    @POST("client/login")
    @FormUrlEncoded
    Call<Client> getLoginClient(@Field("email") String email,
                                @Field("password") String password);

    @POST("client/sign-up")
    @Multipart
    Call<Client> getClientRegister(@Part("name") RequestBody name,
                                   @Part("email") RequestBody email,
                                   @Part("password") RequestBody password,
                                   @Part("password_confirmation") RequestBody password_confirmation,
                                   @Part("phone") RequestBody phone,
                                   @Part("region_id") RequestBody region_id,
                                   @Part MultipartBody.Part file);

    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Client> getLoginRestaurant(@Field("email") String email,
                                    @Field("password") String password);

    @POST("restaurant/sign-up")
    @Multipart
    Call<Client> getRestaurantRegister(@Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part("password_confirmation") RequestBody password_confirmation,
                                       @Part("phone") RequestBody phone,
                                       @Part("whatsapp") RequestBody whatsapp,
                                       @Part("region_id") RequestBody region_id,
                                       @Part("delivery_cost") RequestBody delivery_cost,
                                       @Part("minimum_charger") RequestBody minimum_charger,
                                       @Part MultipartBody.Part file,
                                       @Part("delivery_time") RequestBody delivery_time);

    @GET("cities")
    Call<GeneralResponse> getCities();

    @GET("regions")
    Call<GeneralResponse> getRegions(@Query("city_id") int city_id);

    @POST("restaurant/new-category")
    @Multipart
    Call<GeneralResponse> addNewCategory(@Part("name") RequestBody name,
                                         @Part MultipartBody.Part file,
                                         @Part("api_token") RequestBody api_token);

    @POST("restaurant/update-category")
    @Multipart
    Call<GeneralResponse> updateCategory(@Part("name") RequestBody name,
                                         @Part MultipartBody.Part file,
                                         @Part("api_token") RequestBody api_token,
                                         @Part("category_id") RequestBody category_id);

    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<GeneralResponse> deleteCategory(@Field("api_token") String api_token,
                                         @Field("category_id") int category_id);

    @GET("restaurant/my-items")
    Call<Foodlist> getAllFoodItem(@Query("api_token") String api_token,
                                  @Query("category_id") int category_id);

    @GET("restaurants")
    Call<Restaurants> getAllRestaurants(@Query("page") int page);

    @GET("items")
    Call<Foodlist> getItems(@Query("restaurant_id") int restaurant_id,
                            @Query("category_id") int category_id);

    @GET("restaurant/reviews")
    Call<Reviews> getReviews(@Query("api_token") String api_token,
                             @Query("restaurant_id") int restaurant_id,
                             @Query("page") int page);

    @GET("restaurants")
    Call<Restaurants> getRestWithFilter(@Query("keyword") String keyword,
                                        @Query("region_id") int region_id);

    @GET("categories")
    Call<Category> getCategories(@Query("restaurant_id") int restaurant_id);

    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<Reviews> addReviews(@Field("rate") int rate,
                             @Field("comment") String comment,
                             @Field("restaurant_id") int restaurant_id,
                             @Field("api_token") String api_token);

    @GET("restaurant")
    Call<RestaurantDetails> getRestaurantDetails(@Query("restaurant_id") int restaurant_id);

    @POST("client/new-order")
    @FormUrlEncoded
    Call<Order> addNewOrder(@Field("restaurant_id") int restaurant_id,
                            @Field("note") String note,
                            @Field("address") String address,
                            @Field("payment_method_id") int payment_method_id,
                            @Field("phone") int phone,
                            @Field("name") String name,
                            @Field("api_token") String api_token,
                            @Field("items[]") List<Integer> items,
                            @Field("quantities[]") List<Integer> quantities,
                            @Field("notes[]") List<String> notes);

    @GET("client/my-orders")
    Call<Order> getMyOrder(@Query("api_token") String api_token,
                           @Query("state") String state, @Query("page") int page);

    @POST("client/decline-order")
    @FormUrlEncoded
    Call<Order> declineOrder(@Field("order_id") int order_id,
                             @Field("api_token") String api_token);

    @POST("client/confirm-order")
    @FormUrlEncoded
    Call<Order> confirmOrder(@Field("order_id") int order_id,
                             @Field("api_token") String api_token);

    @GET("offers")
    Call<Offers> getOffers(@Query("restaurant_id") int restaurant_id);

    @GET("offer")
    Call<Offers> getOffersDetails(@Query("offer_id") int offer_id);

    @POST("contact")
    @FormUrlEncoded
    Call<ContactUs> sendMessage(@Field("name") String name,
                                @Field("email") String email,
                                @Field("phone") String phone,
                                @Field("type") String type,
                                @Field("content") String content);

    @GET("client/notifications")
    Call<Notification> getNotification(@Query("api_token") String api_token);

    @POST("client/profile")
    @Multipart
    Call<GeneralResponse> editProfileClient(@Part("api_token") RequestBody api_token,
                                            @Part("name") RequestBody name,
                                            @Part("phone") RequestBody phone,
                                            @Part("email") RequestBody email,
                                            @Part("password") RequestBody password,
                                            @Part("password_confirmation") RequestBody password_confirmation,
                                            @Part("region_id") RequestBody region_id,
                                            @Part MultipartBody.Part profile_image);

    @POST("restaurant/new-item")
    @Multipart
    Call<GeneralResponse> addItem(@Part("description") RequestBody description,
                                  @Part("price") RequestBody price,
                                  @Part("preparing_time") RequestBody preparing_time,
                                  @Part("name") RequestBody name,
                                  @Part MultipartBody.Part photo,
                                  @Part("api_token") RequestBody api_token,
                                  @Part("offer_price") RequestBody offer_price,
                                  @Part("category_id") RequestBody category_id);

    @POST("restaurant/update-item")
    @Multipart
    Call<GeneralResponse> updateItem(@Part("description") RequestBody description,
                                     @Part("price") RequestBody price,
                                     @Part("category_id") RequestBody category_id,
                                     @Part("name") RequestBody name,
                                     @Part MultipartBody.Part photo,
                                     @Part("item_id") RequestBody item_id,
                                     @Part("api_token") RequestBody api_token,
                                     @Part("offer_price") RequestBody offer_price
    );

    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<GeneralResponse> deleteItem(@Field("item_id") int item_id,
                                     @Field("api_token") String api_token);

    @GET("restaurant/my-orders")
    Call<Order> getRestaurantOrders(@Query("api_token") String api_token,
                                    @Query("state") String state,
                                    @Query("page") int page);

    @POST("restaurant/accept-order")
    @FormUrlEncoded
    Call<GeneralResponse> acceptRestaurantOrder(@Field("api_token") String api_token,
                                                @Field("order_id") int order_id);

    @POST("restaurant/reject-order")
    @FormUrlEncoded
    Call<GeneralResponse> rejectRestaurantOrder(@Field("api_token") String api_token,
                                                @Field("order_id") int order_id);

    @POST("restaurant/confirm-order")
    @FormUrlEncoded
    Call<GeneralResponse> confirmRestaurantOrderDelivery(@Field("order_id") int order_id,
                                                         @Field("api_token") String api_token);

    @POST("restaurant/profile")
    @Multipart
    Call<GeneralResponse> editProfileRestaurant(@Part("email") RequestBody email,
                                                @Part("name") RequestBody name,
                                                @Part("phone") RequestBody phone,
                                                @Part("region_id") RequestBody region_id,
                                                @Part("delivery_cost") RequestBody delivery_cost,
                                                @Part("minimum_charger") RequestBody minimum_charger,
                                                @Part("availability") RequestBody availability,
                                                @Part MultipartBody.Part photo,
                                                @Part("api_token") RequestBody api_token,
                                                @Part("delivery_time") RequestBody delivery_time,
                                                @Part("whatapp") RequestBody whatapp);

    @GET("restaurant/my-offers")
    Call<Offers> getMyOffers(@Query("api_token") String api_token,
                             @Query("page") int page);

    @POST("restaurant/new-offer")
    @Multipart
    Call<Offers> addNewOffer(@Part("description") RequestBody description,
                             @Part("price") RequestBody price,
                             @Part("starting_at") RequestBody starting_at,
                             @Part("name") RequestBody name,
                             @Part MultipartBody.Part photo,
                             @Part("ending_at") RequestBody ending_at,
                             @Part("api_token") RequestBody api_token,
                             @Part("offer_price") RequestBody offer_price);

    @POST("restaurant/update-offer")
    @Multipart
    Call<Offers> updateOffer(@Part("description") RequestBody description,
                             @Part("price") RequestBody price,
                             @Part("starting_at") RequestBody starting_at,
                             @Part("name") RequestBody name,
                             @Part MultipartBody.Part photo,
                             @Part("ending_at") RequestBody ending_at,
                             @Part("offer_id") RequestBody offer_id,
                             @Part("api_token") RequestBody api_token);

    @POST("restaurant/delete-offer")
    @FormUrlEncoded
    Call<Offers> deleteOffer(@Field("offer_id") int offer_id,
                             @Field("api_token") String api_token);

    @POST("restaurant/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> changeRestaurantPassword(@Field("api_token") String api_token,
                                                   @Field("old_password") String old_password,
                                                   @Field("password") String password,
                                                   @Field("password_confirmation") String password_confirmation);

    @POST("restaurant/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> changeClientPassword(@Field("api_token") String api_token,
                                               @Field("old_password") String old_password,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation);

    @GET("restaurant/commissions")
    Call<Commission> getCommission(@Query("api_token") String api_token);

    @GET("restaurant/notifications")
    Call<Notification> getRestaurantNotification(@Query("api_token") String api_token);

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<GeneralResponse> restPasswordClient(@Field("email") String email);

    @POST("client/new-password")
    @FormUrlEncoded
    Call<GeneralResponse> newPasswordClient(@Field("code") String code,
                                            @Field("password") String password,
                                            @Field("password_confirmation") String password_confirmation);

    @POST("restaurant/reset-password")
    @FormUrlEncoded
    Call<GeneralResponse> restPasswordRestaurant(@Field("email") String email);

    @POST("restaurant/new-password")
    @FormUrlEncoded
    Call<GeneralResponse> newPasswordRestaurant(@Field("code") String code,
                                                @Field("password") String password,
                                                @Field("password_confirmation") String password_confirmation);
}
