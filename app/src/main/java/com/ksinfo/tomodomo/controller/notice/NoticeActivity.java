package com.ksinfo.tomodomo.controller.notice;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ksinfo.tomodomo.R;
import com.ksinfo.tomodomo.TomodomoApplication;
import com.ksinfo.tomodomo.model.itf.NoticeInterface;
import com.ksinfo.tomodomo.model.vo.notice.NoticeDto;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends AppCompatActivity {
    @Inject NoticeInterface noticeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nt_recycleview);

        TomodomoApplication tomodomoApplication = (TomodomoApplication) getApplication();
        tomodomoApplication.getApplicationComponent().inject(this);

        noticeInterface.getNoticeList().enqueue(new Callback<List<NoticeDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<NoticeDto>> call, Response<List<NoticeDto>> response) {
                System.out.println("start NoticeActivity-getNoticeList-onResponse.");

                //메모 RecyclerView단계1 어답터 변수 생성.
                //메모 adapter의 역할 : RecyclerView에 출력할 데이터를 받는다. 그리고 이를 1개씩 출력할 수 있도록 넘겨주는 역할같다.
                NoticeRecyclerViewAdapter adapter = new NoticeRecyclerViewAdapter();

                if (response.isSuccessful()) {
                    //메모 서버로부터 공지사항목록을 수신(단, 공지하겠다고 결정한 것들만 수신)
                    List<NoticeDto> noticeList = response.body();
                    Log.d("NoticeAct-noticeLists", noticeList.toString());

                    for (NoticeDto vo : noticeList) {
                        adapter.addItem(vo);
                    }

                    //메모 1단계 : RecyclerView 변수 생성 및 xml R객체와 연결.
                    //메모 recyclerViewOfNoticeList : 레이아웃상에서 리스트(목록)형태로 출력해주는 역할.(#그릇 역할)
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewOfNoticeList);

                    //메모 2단계 LinearLayoutManager : 수평,수직으로 배치시켜주는 레이아웃 매니저. 일반적으로 1개씩 보여주는 스크롤방식을 지원한다.
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NoticeActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //메모 3단계
                    recyclerView.setAdapter(adapter);

                } else {
                    Log.d("error", "error");
                }

            }

            @Override
            public void onFailure(Call<List<NoticeDto>> call, Throwable t) {

            }
        });
    }
}