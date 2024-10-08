package com.myblog.payload;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@ApiModel(description = "Post model information")
@Data
public class CommentDto {

    private Long id;
    @NotEmpty
    @Size(min = 2, message = "Comment name should have at least 2 characters")
    private String name;
    @NotEmpty
    @Size(min = 5, message = "Comment message should have at least 5 characters")
    private String body;
    @NotEmpty
    @Email
    private String email;
}
