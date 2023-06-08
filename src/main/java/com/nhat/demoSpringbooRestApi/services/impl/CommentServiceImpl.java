package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.dtos.CommentRequestDTO;
import com.nhat.demoSpringbooRestApi.exceptions.ResourceNotFoundException;
import com.nhat.demoSpringbooRestApi.models.Comment;
import com.nhat.demoSpringbooRestApi.models.Product;
import com.nhat.demoSpringbooRestApi.models.User;
import com.nhat.demoSpringbooRestApi.models.UserProductKey;
import com.nhat.demoSpringbooRestApi.repositories.CommentRepo;
import com.nhat.demoSpringbooRestApi.services.CommentService;
import com.nhat.demoSpringbooRestApi.services.ProductService;
import com.nhat.demoSpringbooRestApi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepo commentRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Override
    public List<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    @Override
    public Comment findCommentById(UserProductKey commentId) {
        return commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
    }

    @Override
    public List<Comment> findAllCommentByProductId(int productId) {
        return commentRepo.findAllByProductId(productId);
    }

    @Override
    public Comment createComment(CommentRequestDTO comment) {
        Product product = productService.getProductById(comment.getProductId());
        User user = userService.getUserById((comment.getUserId()));

        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setCreatedAt(LocalDateTime.now());
        newComment.setProduct(product);
        newComment.setUser(user);

        return commentRepo.save(newComment);
    }

    @Override
    public Comment updateComment( CommentRequestDTO comment) {
        UserProductKey userProductKey = new UserProductKey(comment.getUserId(), comment.getProductId());
        Comment updateComment = findCommentById(userProductKey);

//        Product product = productService.getProductById(comment.getProductId());
//        User user = userService.getUserById((comment.getUserId()));
        updateComment.setContent(comment.getContent());
        updateComment.setUpdatedAt(LocalDateTime.now());
//        updateComment.setProduct(product);
//        updateComment.setUser(user);

        return commentRepo.save(updateComment);
    }

    @Override
    public String deleteComment(UserProductKey commentId) {
        Comment existingComment = findCommentById(commentId);
        commentRepo.delete(existingComment);
        return "Comment with CommentID: " + commentId + " deleted successfully !!!";
    }
}
