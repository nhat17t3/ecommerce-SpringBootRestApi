package com.nhat.demoSpringbooRestApi.services;

import com.nhat.demoSpringbooRestApi.dtos.CommentRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Comment;
import com.nhat.demoSpringbooRestApi.models.UserProductKey;

import java.util.List;


public interface CommentService {

    List<Comment> getAllComments ();

    Comment findCommentById(UserProductKey commentId);

    List<Comment> findAllCommentByProductId (int productId);

    Comment createComment (CommentRequestDTO comment);

    Comment updateComment ( CommentRequestDTO comment);

    String deleteComment (UserProductKey commentId);
}
