package com.ksinfo.tomodomo.model.itf;

import com.ksinfo.tomodomo.model.vo.notice.NoticeDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NoticeInterface {
    @GET("common/notice/noticeAndroid")
    Call<List<NoticeDto>> getNoticeList();
}