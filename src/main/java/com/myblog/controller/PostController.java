package com.myblog.controller;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts") //"/api/v1/posts" - versioning of project by giving api notation like this
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //http://localhost:8080/api/posts
    //create blog post restAPI
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @PostMapping
    //<Object> can return any type of class
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){
    // If there are validation errors, those errors are stored in the BindingResult object
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/posts
//    @GetMapping
//    public List<PostDto> getAllPosts(){
//        return postService.getAllPosts();
//    }

    //http://localhost:8080/api/posts?pageNo=0&pageSize=50&sortBy=title&sortDir=asc   //query parameter
    //get all post restAPI
    @GetMapping  //pagination & sorting in rest api
    public PostResponse getAllPosts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int  pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                    @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    //http://localhost:8080/api/posts/1    //path parameter
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
        PostDto dto = postService.getPostById(id);
        return ResponseEntity.ok(dto); //new ResponseEntity<>(dto, HttpStatus.ok)
    }

    //http://localhost:8080/api/posts/5  //path parameter
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePostById(@Valid @RequestBody PostDto postDto,
                                                  @PathVariable("id") long id, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PostDto dto = postService.updatePostById(postDto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/5  //path parameter
    @PreAuthorize("hasRole('ADMIN')")  //Giving access to this method only
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable("id") long id){
        postService.deletePostById(id);
        return ResponseEntity.ok("id: " + id + " - Deleted Successfully..");
    }

}
