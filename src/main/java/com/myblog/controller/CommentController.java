package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @PostMapping("/posts/{postId}/comments")
    //Object can return any type of class
    public ResponseEntity<Object> createComment(@PathVariable(value = "postId") long postId,
                                                @Valid @RequestBody CommentDto commentDto,
                                                BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommentDto dto = commentService.createComment(postId, commentDto);
        return  new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts/comments
    @GetMapping("/posts/comments")
    public List<CommentDto> getAllComments(){
        return commentService.getAllComments();
    }

    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") long postId){
        return commentService.getCommentsByPostId(postId);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") long postId,
                                                     @PathVariable(value = "id") long commentId){
        CommentDto dto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable(value = "postId") long postId,
                                                    @PathVariable(value = "id") long commentId,
                                                    @Valid @RequestBody CommentDto commentDto,
                                                    BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CommentDto dto = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/1/comments/1
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public  ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long postId,
                                                 @PathVariable(value = "id") long commentId){
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok("Post:" + postId  + " of Comment:" + commentId + " is Deleted Successfully...");
    }
}
