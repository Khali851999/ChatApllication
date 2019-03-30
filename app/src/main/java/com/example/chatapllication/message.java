package com.example.chatapllication;
//step1 to redrieve data
//create a model class ,delcare all the things you want to retrieve;
// make default,parametrize constructor
//call getters and setters
//step 2 on mainactivity
public class message {
    private String content;
    private String username;

    public message() { }

    public message(String content, String username) {
        this.content = content;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

    }
}
