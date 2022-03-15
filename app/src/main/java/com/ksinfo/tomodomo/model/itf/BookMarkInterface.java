package com.ksinfo.tomodomo.model.itf;

import com.ksinfo.tomodomo.model.vo.member.BookmarkPostDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookMarkInterface {
    /*
    @GET("bookmark/getMyPostListAndroid")
    Call<List<BookmarkPostVO>> getMyPostList(@Query("userId") Long userId,
                                             @Query("offset") Integer offset);
     */
    @GET("bookmark/getMyPostListAndroid")
    Call<List<BookmarkPostDto>> getMyPostList(@Query("userId") Long userId);

}