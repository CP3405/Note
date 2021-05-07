package com.example.demo_newsapp.gson;

public class News {

    // TODO: 2021/5/5 重新生成news bean对象对象

    /**
     * author : Zach Braziller
     * content : The Knicks were supposed to be the local team in danger of a disastrous road trip. Instead, so far the Nets have flopped, equaling a season-high three-game losing streak. Now, the remaining three gam… [+1941 chars]
     * description : After losing two straight in Milwaukee, the Nets trail the 76ers by two games in the loss column for the top spot in the East with six games to go.
     * publishedAt : 2021-05-06T01:58:41Z
     * source : {"name":"New York Post"}
     * title : Nets looking to reverse road trip free-fall
     * url : https://nypost.com/2021/05/05/nets-looking-to-reverse-road-trip-free-fall/
     * urlToImage : https://nypost.com/wp-content/uploads/sites/2/2021/05/Nash.Durant.jpg?quality=90&strip=all&w=1200
     */

    private String author;
    private String content;
    private String description;
    private String publishedAt;
    private SourceBean source;
    private String title;
    private String url;
    private String urlToImage;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public SourceBean getSource() {
        return source;
    }

    public void setSource(SourceBean source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public static class SourceBean {
        /**
         * name : New York Post
         * id : engadget
         */

        private String name;

        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
