package com.ksinfo.tomodomo.controller.board;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ksinfo.tomodomo.R;
import com.ksinfo.tomodomo.model.itf.BoardInterface;
import com.ksinfo.tomodomo.model.vo.board.PostAndReplyVO;
import com.ksinfo.tomodomo.model.vo.board.PostVO;
import com.ksinfo.tomodomo.model.vo.board.ReplyVO;
import com.ksinfo.tomodomo.model.vo.board.SearchPostVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class SearchPostAdapter extends RecyclerView.Adapter<SearchPostHolder> {
    private final BoardInterface boardInterface;
    private List<SearchPostVO> postList;
    private long boardId;
    private long postId;
    private String sort;
    private String searchKeyword;

    public SearchPostAdapter(BoardInterface boardInterface) {
        this.boardInterface = boardInterface;
        postList = new ArrayList<>(0);
    }

    public void getPostList() {
        boardInterface.getPostList(searchKeyword, sort, boardId, postId)
                .enqueue(new Callback<ArrayList<SearchPostVO>>() {
            @Override
            public void onResponse(
                @NonNull Call<ArrayList<SearchPostVO>> call,
                @NonNull Response<ArrayList<SearchPostVO>> response
            ) {
                if (response.isSuccessful()) {
                    postList = response.body();
                    postId = postList.get(postList.size() - 1).getPostId();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(
                @NonNull Call<ArrayList<SearchPostVO>> call,
                @NonNull Throwable t
            ) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public SearchPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                                      .inflate(R.layout.bd_search_post_list, parent, false);
        SearchPostHolder searchPostHolder = new SearchPostHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = searchPostHolder.getAdapterPosition();
                if (position > RecyclerView.NO_POSITION) {
                    long postId = postList.get(position).getPostId();
                    boardInterface.getPost(postId).enqueue(new Callback<PostAndReplyVO>() {
                        @Override
                        public void onResponse(
                            Call<PostAndReplyVO> call,
                            Response<PostAndReplyVO> response
                        ) {
                            PostAndReplyVO postAndReply = response.body();
                            PostVO post = postAndReply.getPost();
                            ArrayList<ReplyVO> replyList = postAndReply.getReplyList();

                            Context context = itemView.getContext();
                            Intent intent = new Intent(context, PostActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("post", post);
                            intent.putParcelableArrayListExtra("replyList", replyList);
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<PostAndReplyVO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        return searchPostHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPostHolder holder, int position) {
        SearchPostVO post = postList.get(position);

        holder.setBoardName(post.getBoardTopicName());
        holder.setPostTitle(post.getPostTitle());
        holder.setPostImage(post.getPostFileUrl());
        holder.setPostContents(post.getPostContents());

        if (position == getItemCount() - 5) {
            boardInterface.getPostList(searchKeyword, sort, boardId, post.getPostId())
                    .enqueue(new Callback<ArrayList<SearchPostVO>>() {
                @Override
                public void onResponse(
                    @NonNull Call<ArrayList<SearchPostVO>> call,
                    @NonNull Response<ArrayList<SearchPostVO>> response
                ) {
                    Log.d("isSuccessful", Boolean.toString(response.isSuccessful()));
                }

                @Override
                public void onFailure(
                    @NonNull Call<ArrayList<SearchPostVO>> call,
                    @NonNull Throwable t
                ) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public void addPostList() {
        boardInterface.getPostList(searchKeyword, sort, boardId, postId)
                .enqueue(new Callback<ArrayList<SearchPostVO>>() {
            @Override
            public void onResponse(
                @NonNull Call<ArrayList<SearchPostVO>> call,
                @NonNull Response<ArrayList<SearchPostVO>> response
            ) {
                if (response.isSuccessful()) {
                    int position = postList.size();
                    postList.addAll(response.body());
                    postId = postList.get(postList.size() - 1).getPostId();
                    notifyItemInserted(position);
                }
            }

            @Override
            public void onFailure(
                @NonNull Call<ArrayList<SearchPostVO>> call,
                @NonNull Throwable t
            ) {
                t.printStackTrace();
            }
        });
    }
}