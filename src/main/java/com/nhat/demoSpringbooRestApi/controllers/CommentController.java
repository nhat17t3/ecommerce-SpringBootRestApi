package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.CommentRequestDTO;
import com.nhat.demoSpringbooRestApi.models.Comment;
import com.nhat.demoSpringbooRestApi.models.UserProductKey;
import com.nhat.demoSpringbooRestApi.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Comment>> getAllComments() {

        List<Comment> commentResponse = commentService.getAllComments();

        return new ResponseEntity<List<Comment>>(commentResponse, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable(name = "productId") int id) {
        List<Comment> commentResponse = commentService.findAllCommentByProductId(id);
        return new ResponseEntity<List<Comment>>(commentResponse, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Comment> getCommentById(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "productId") Integer productId) {
        UserProductKey userProductKey = new UserProductKey(userId, productId);
        Comment comment = commentService.findCommentById(userProductKey);
        return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Comment> addComment(@Valid @RequestBody CommentRequestDTO comment) {
        Comment savedComment = commentService.createComment(comment);
        return new ResponseEntity<Comment>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Comment> updateComment(@Valid @RequestBody CommentRequestDTO comment) {

        Comment savedComment = commentService.updateComment(comment);
        return new ResponseEntity<Comment>(savedComment, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteComment(@RequestParam(name = "userId") Integer userId,
                                                @RequestParam(name = "productId") Integer productId) {
        UserProductKey userProductKey = new UserProductKey(userId, productId);
        String message = commentService.deleteComment(userProductKey);
        return new ResponseEntity<String>(message, HttpStatus.OK);

    }


}
