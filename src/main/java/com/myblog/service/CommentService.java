package com.myblog.service;

import com.myblog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getAllComments();

    List<CommentDto> getCommentsByPostId(long postId);

    CommentDto getCommentById(long postId, long commentId);

    CommentDto updateComment(long postId, long commentId, CommentDto commentDto);

    void deleteComment(long postId, long commentId);
}
