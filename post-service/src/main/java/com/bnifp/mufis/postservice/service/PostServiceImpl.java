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
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    public void setMapper(ModelMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public PostOutput getOne(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        Post temp = post.get();
        return this.mapper.map(temp, PostOutput.class);
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
    public PostOutput addOne(PostInput input) {
        Post post = mapper.map(input, Post.class);
        postRepository.save(post);
        return this.mapper.map(post, PostOutput.class);
    }

    @Override
    public PostOutput updateOne(Long id, PostInput input){
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return null;
        }
        Post temp = post.get();
        this.mapper.map(input, temp);
        postRepository.save(temp);
        return this.mapper.map(temp, PostOutput.class);
    }

    @Override
    public void deleteOne(Long id) {
        postRepository.deleteById(id);
    }

}
