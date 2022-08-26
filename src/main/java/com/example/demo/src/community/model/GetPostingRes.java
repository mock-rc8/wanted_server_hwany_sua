package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostingRes {
    private PostingMore postingMore;
    private List<Comment> commentList;
    private DetailUser user;
}
