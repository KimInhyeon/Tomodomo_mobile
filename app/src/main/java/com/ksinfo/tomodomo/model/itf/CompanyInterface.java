package com.ksinfo.tomodomo.model.itf;

import com.ksinfo.tomodomo.model.vo.company.CompanyMainVO;
import com.ksinfo.tomodomo.model.vo.company.CompanyReviewAverageVO;
import com.ksinfo.tomodomo.model.vo.company.CompanyReviewVO;
import com.ksinfo.tomodomo.model.vo.company.CompanySearchVO;
import com.ksinfo.tomodomo.model.vo.company.CompanyVoteResultDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CompanyInterface {
	@GET("company/search")
	Call<List<CompanySearchVO>> getCompanySearchResult(@Query("companyName") String companyName);

	@FormUrlEncoded
	@POST("member/loginApp")
	Call<HashMap<String, String>> loginApp(@FieldMap HashMap<String, String> params);

	@FormUrlEncoded
	@POST("company/review/write")
//계정정보 추가 필요
	Call<HashMap<String, String>> writeCompanyReviewApi(@FieldMap Map<String, Object> params);

	@GET("company?a=true")
//
	Call<List<CompanyMainVO>> getPopularCompanyList();

	@GET("company/review/{companyId}?a=true")
	Call<ArrayList<CompanyReviewVO>> getCompanyReviewList(@Path("companyId") long companyId, @Query("page") int page);


	@GET("company/review/{companyId}?averageStar")
	Call<CompanyReviewAverageVO> getCompanyAverage(@Path("companyId") long companyId);

	@GET("company/{companyId}/recommend")
	Call<CompanyVoteResultDto> getCompanyRecommendVoteResult(@Path("companyId") long companyId);

	@FormUrlEncoded
	@POST("company/recommend")
	Call<CompanyVoteResultDto> companyRecommendPush(
			@FieldMap Map<String, Object> params
	);
}