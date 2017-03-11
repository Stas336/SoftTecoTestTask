package com.stasl.softtecotesttask.post;

import java.util.ArrayList;
import java.util.List;

public class PostData
{
    private List<List<PostModel>> posts;
    private final int POSTS_LIMIT_PER_PAGE = 6;

    public PostData(){
        posts = new ArrayList<>();
        posts.add(new ArrayList<PostModel>());
    }
    public PostData(List<List<PostModel>> posts) {
        this.posts = posts;
    }
    public PostModel getPost(int number) throws Exception {
        for (List<PostModel> poststemp:posts)
        {
            for (PostModel post:poststemp)
            {
                if (post.getId() == number)
                {
                    return post;
                }
            }
        }
        throw new Exception("Post not found");
    }
    public List<PostModel> getPage(int number)
    {
        return posts.get(number);
    }
    public void setPost(PostModel newPost, int number) {
        for (int i = 0;i < posts.size();i++)
        {
            for (int j = 0;j < POSTS_LIMIT_PER_PAGE;j++)
            {
                if (posts.get(i).get(j).getId() == number)
                {
                    posts.get(i).set(j, newPost);
                }
            }
        }
    }
    public void addPost(PostModel post)
    {
        if (posts.get(posts.size() - 1).size() < POSTS_LIMIT_PER_PAGE)
        {
            posts.get(posts.size() - 1).add(post);
        }
        else
        {
            List<PostModel> newPosts = new ArrayList<>();
            newPosts.add(post);
            posts.add(newPosts);
        }
    }
    public void deletePost(int number) throws Exception {
        for (int i = 0;i < posts.size();i++)
        {
            for (int j = 0;j < POSTS_LIMIT_PER_PAGE;j++)
            {
                if (posts.get(i).get(j).getId() == number)
                {
                    posts.get(i).remove(j);
                }
            }
        }
        throw new Exception("Post not found");
    }
    public int size()
    {
        return posts.size();
    }
    public int getPOSTS_LIMIT_PER_PAGE()
    {
        return this.POSTS_LIMIT_PER_PAGE;
    }
}
