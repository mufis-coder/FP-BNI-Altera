package com.bnifp.mufis.postservice.service;

import com.bnifp.mufis.postservice.dto.input.PostInput;
import com.bnifp.mufis.postservice.dto.output.PostOutput;
import com.bnifp.mufis.postservice.model.Post;
import com.bnifp.mufis.postservice.repository.PostRepository;
import org.apache.commons.collections4.IterableUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public PostOutput getOne(Long id) {
        return null;
    }

    @Override
    public List<PostOutput> getAll() {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> postList = IterableUtils.toList(posts);

        List<PostOutput> outputs = new ArrayList<>();
        for(Post post: postList){
            outputs.add(mapper.map(post, PostOutput.class));
        }

        return outputs;
    }

    @Override
    public void addOne(PostInput input) {
        Post post = mapper.map(input, Post.class);
        postRepository.save(post);
    }

    @Override
    public void deleteOne(Long id) {

    }

}
