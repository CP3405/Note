package com.example.demo_newsapp.gson;


import com.google.gson.annotations.SerializedName;

import java.util.List;

// TODO: 2021/5/5  api response
public class ApiResponse {


    /**
     * articles : [{"author":"Zach Braziller","content":"The Knicks were supposed to be the local team in danger of a disastrous road trip. Instead, so far the Nets have flopped, equaling a season-high three-game losing streak. Now, the remaining three gam\u2026 [+1941 chars]","description":"After losing two straight in Milwaukee, the Nets trail the 76ers by two games in the loss column for the top spot in the East with six games to go.","publishedAt":"2021-05-06T01:58:41Z","source":{"name":"New York Post"},"title":"Nets looking to reverse road trip free-fall","url":"https://nypost.com/2021/05/05/nets-looking-to-reverse-road-trip-free-fall/","urlToImage":"https://nypost.com/wp-content/uploads/sites/2/2021/05/Nash.Durant.jpg?quality=90&strip=all&w=1200"}]
     * status : ok
     * totalResults : 13908
     */

    @SerializedName("articles")
    private List<News> newsList;
    private String status;
    private String message;
    @SerializedName("totalResults")
    private Integer totalResults;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
