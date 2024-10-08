package com.myblog.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "Post model information")
@Data
public class PostDto {

    private long id;
    //title should not be null or empty
    //title should have at least 2 characters
    @NotEmpty  //(message = "Is mandatory")
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private  String description;
    @NotEmpty
    @Size(min = 10, message = "Post content should have at least 10 characters")
    private  String content;
}
