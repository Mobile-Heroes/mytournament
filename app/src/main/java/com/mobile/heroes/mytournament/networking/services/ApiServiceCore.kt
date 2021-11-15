package com.mobile.heroes.mytournament.networking.services

import com.mobile.heroes.mytournament.networking.Constants
import com.mobile.heroes.mytournament.networking.services.AccountResource.AccountResponce
import com.mobile.heroes.mytournament.networking.services.FavoriteResource.FavoriteRequest
import com.mobile.heroes.mytournament.networking.services.FavoriteResource.FavoriteResponse
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldRequest
import com.mobile.heroes.mytournament.networking.services.FieldResource.FieldResponse
import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupRequest
import com.mobile.heroes.mytournament.networking.services.GroupResource.GroupResponse
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginRequest
import com.mobile.heroes.mytournament.networking.services.LoginResource.LoginResponse
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchRequest
import com.mobile.heroes.mytournament.networking.services.MatchResource.MatchResponce
import com.mobile.heroes.mytournament.networking.services.PaymentResource.PaymentRequest
import com.mobile.heroes.mytournament.networking.services.PaymentResource.PaymentResponse
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentRequest
import com.mobile.heroes.mytournament.networking.services.TeamTournamentResource.TeamTournamentResponse
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsRequest
import com.mobile.heroes.mytournament.networking.services.UserStatsResource.UserStatsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceCore {

    //Login
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    //Get Account
    @GET(Constants.USER_ACCOUNT)
    fun getAccount(@Header("Authorization") token: String): Call<AccountResponce>

    //Favorites
    @POST(Constants.FAVORITE_URL)
    fun postFavorite(@Header("Authorization") token: String, @Body favorite: FavoriteRequest): Call<FavoriteResponse>

    @GET(Constants.FAVORITE_URL)
    fun getFavorite(@Header("Authorization") token: String): Response<FavoriteResponse>

    @GET("${Constants.FAVORITE_URL}/{id}")
    fun getOneFavorite(@Header("Authorization") token: String,
                       @Path("id") id:String,): Response<FavoriteResponse>

    @PUT("${Constants.FAVORITE_URL}/{id}")
    fun updateFavorite(@Header("Authorization") token: String,
                       @Path("id") id:String,
                       @Body favorite: FavoriteRequest
    ): Call<FavoriteResponse>

    @DELETE("${Constants.FAVORITE_URL}/{id}")
    fun deleteFavorite(@Header("Authorization") token: String,
                       @Path("id") id:String,
                       @Body favorite: FavoriteRequest
    ): Call<FavoriteResponse>

    //Fields
    @POST(Constants.FIELD_URL)
    fun postField(@Header("Authorization") token: String, @Body field: FieldRequest): Call<FieldResponse>

    @GET(Constants.FIELD_URL)
    fun getField(@Header("Authorization") token: String): Response<FieldResponse>

    @GET("${Constants.FIELD_URL}/{id}")
    fun getOneField(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<FieldResponse>

    @PUT("${Constants.FIELD_URL}/{id}")
    fun updateField(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body field: FieldRequest
    ): Call<FieldResponse>

    @DELETE("${Constants.FIELD_URL}/{id}")
    fun deleteField(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body field: FieldRequest
    ): Call<FieldResponse>

    //Groups
    @POST(Constants.GROUP_URL)
    fun postGroup(@Header("Authorization") token: String, @Body group: GroupRequest): Call<GroupResponse>

    @GET(Constants.GROUP_URL)
    fun getGroup(@Header("Authorization") token: String): Response<GroupResponse>

    @GET("${Constants.GROUP_URL}/{id}")
    fun getOneGroup(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<GroupResponse>

    @PUT("${Constants.GROUP_URL}/{id}")
    fun updateGroup(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body group: GroupRequest
    ): Call<GroupResponse>

    @DELETE("${Constants.GROUP_URL}/{id}")
    fun deleteGroup(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body group: GroupRequest
    ): Call<GroupResponse>

    //Matches
    @POST(Constants.MATCH_URL)
    fun postMatch(@Header("Authorization") token: String, @Body match: MatchRequest): Call<MatchResponce>

    @GET(Constants.MATCH_URL)
    fun getMatch(@Header("Authorization") token: String): Response<MatchResponce>

    @GET("${Constants.MATCH_URL}/{id}")
    fun getOneMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,): Response<MatchResponce>

    @PUT("${Constants.MATCH_URL}/{id}")
    fun updateMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body match: MatchRequest): Call<MatchResponce>

    @DELETE("${Constants.MATCH_URL}/{id}")
    fun deleteMatch(@Header("Authorization") token: String,
                    @Path("id") id:String,
                    @Body match: MatchRequest): Call<MatchResponce>

    //Payments
    @POST(Constants.PAYMENT_URL)
    fun postPayment(@Header("Authorization") token: String, @Body Payment: PaymentRequest): Call<PaymentResponse>

    @GET(Constants.PAYMENT_URL)
    fun getPayment(@Header("Authorization") token: String): Response<PaymentResponse>

    @GET("${Constants.PAYMENT_URL}/{id}")
    fun getOnePayment(@Header("Authorization") token: String,
                      @Path("id") id:String,): Response<PaymentResponse>

    @PUT("${Constants.PAYMENT_URL}/{id}")
    fun updatePayment(@Header("Authorization") token: String,
                      @Path("id") id:String,
                      @Body payment: PaymentRequest
    ): Call<PaymentResponse>

    @DELETE("${Constants.PAYMENT_URL}/{id}")
    fun deletePayment(@Header("Authorization") token: String,
                      @Path("id") id:String,
                      @Body payment: PaymentRequest
    ): Call<PaymentResponse>

    //Team Tournaments
    @POST(Constants.TEAM_TOURNAMENT_URL)
    fun postTeamTournament(@Header("Authorization") token: String, @Body teamTournament: TeamTournamentRequest): Call<TeamTournamentResponse>

    @GET(Constants.TEAM_TOURNAMENT_URL)
    fun getTeamTournament(@Header("Authorization") token: String): Response<TeamTournamentResponse>

    @GET("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun getOneTeamTournament(@Header("Authorization") token: String,
                             @Path("id") id:String,): Response<TeamTournamentResponse>

    @PUT("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun updateTeamTournament(@Header("Authorization") token: String,
                             @Path("id") id:String,
                             @Body teamTournament: TeamTournamentRequest
    ): Call<TeamTournamentResponse>

    @DELETE("${Constants.TEAM_TOURNAMENT_URL}/{id}")
    fun deleteTeamTournament(@Header("Authorization") token: String,
                             @Path("id") id:String,
                             @Body teamTournament: TeamTournamentRequest
    ): Call<TeamTournamentResponse>

    //User Stats
    @POST(Constants.USER_STATS_URL)
    fun postUserStats(@Header("Authorization") token: String, @Body userStats: UserStatsRequest): Call<UserStatsResponse>

    @GET(Constants.USER_STATS_URL)
    fun getUserStats(@Header("Authorization") token: String): Response<UserStatsResponse>

    @GET("${Constants.USER_STATS_URL}/{id}")
    fun getOneUserStats(@Header("Authorization") token: String,
                        @Path("id") id:String,): Response<UserStatsResponse>

    @PUT("${Constants.USER_STATS_URL}/{id}")
    fun updateUserStats(@Header("Authorization") token: String,
                        @Path("id") id:String,
                        @Body userStats: UserStatsRequest
    ): Call<UserStatsResponse>

    @DELETE("${Constants.USER_STATS_URL}/{id}")
    fun deleteUserStats(@Header("Authorization") token: String,
                        @Path("id") id:String,
                        @Body userStats: UserStatsRequest
    ): Call<UserStatsResponse>

}