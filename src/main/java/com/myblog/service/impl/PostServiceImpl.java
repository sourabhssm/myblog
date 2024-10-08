package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) { //PostDto is POJO class
        //convert DTO to Entity by creating method
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);
        //convert entity to DTO
        PostDto newPostDto = mapToDto(newPost);
        return newPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

//        Sort sort = null;
//        if (sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())){
//            sort = Sort.by(sortBy).ascending();
//        } else {
//           sort = Sort.by(sortBy).descending();
//        }

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort); //method overloading
        Page<Post> pagePost = postRepository.findAll(pageable);

        //get content for page object
        List<Post> post = pagePost.getContent();

        List<PostDto> postDtos = post.stream().map(posts -> mapToDto(posts)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setPostDtos(postDtos);
        postResponse.setPageNo(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLast(pagePost.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        return mapToDto(post);
    }

    @Override
    public PostDto updatePostById(PostDto postDto, long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)

        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        //get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );
        postRepository.delete(post);
    }

    //convert DTO to entity
    Post mapToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }

    //convert entity to DTO
    PostDto mapToDto(Post newPost) {
        PostDto postDto = modelMapper.map(newPost, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(newPost.getId());
//        postDto.setTitle(newPost.getTitle());
//        postDto.setDescription(newPost.getDescription());
//        postDto.setContent(newPost.getContent());
        return postDto;
    }
}
