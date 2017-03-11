package com.stasl.softtecotesttask.post;

public class PostModel
{
    private int id;

    private String body;

    private String title;

    private int userId;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getBody ()
    {
        return body;
    }

    public void setBody (String body)
    {
        this.body = body;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public int getUserId ()
    {
        return userId;
    }

    public void setUserId (int userId)
    {
        this.userId = userId;
    }

    @Override
    public String toString()
    {
        return "[id = "+id+", body = "+body+", title = "+title+", userId = "+userId+"]";
    }
}
