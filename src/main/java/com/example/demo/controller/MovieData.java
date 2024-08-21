package com.example.demo.controller;

import java.util.List;

public class MovieData {

    private Data data;
    private boolean status;
    private String message;

    // Getters and setters
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data {
        private Title title;

        // Getters and setters
        public Title getTitle() {
            return title;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public static class Title {
            private String id;
            private TitleType titleType;
            private List<ItemCategory> keywordItemCategories;

            // Getters and setters
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public TitleType getTitleType() {
                return titleType;
            }

            public void setTitleType(TitleType titleType) {
                this.titleType = titleType;
            }

            public List<ItemCategory> getKeywordItemCategories() {
                return keywordItemCategories;
            }

            public void setKeywordItemCategories(List<ItemCategory> keywordItemCategories) {
                this.keywordItemCategories = keywordItemCategories;
            }

            public static class TitleType {
                private String id;

                // Getter and setter
                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }
            }

            public static class ItemCategory {
                private ItemCategoryDetail itemCategory;
                private Keywords keywords;

                // Getters and setters
                public ItemCategoryDetail getItemCategory() {
                    return itemCategory;
                }

                public void setItemCategory(ItemCategoryDetail itemCategory) {
                    this.itemCategory = itemCategory;
                }

                public Keywords getKeywords() {
                    return keywords;
                }

                public void setKeywords(Keywords keywords) {
                    this.keywords = keywords;
                }

                public static class ItemCategoryDetail {
                    private String id;
                    private String itemCategoryId;
                    private String text;

                    // Getters and setters
                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getItemCategoryId() {
                        return itemCategoryId;
                    }

                    public void setItemCategoryId(String itemCategoryId) {
                        this.itemCategoryId = itemCategoryId;
                    }

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }
                }

                public static class Keywords {
                    private String __typename;
                    private int total;
                    private List<Edge> edges;

                    // Getters and setters
                    public String getTypename() {
                        return __typename;
                    }

                    public void setTypename(String __typename) {
                        this.__typename = __typename;
                    }

                    public int getTotal() {
                        return total;
                    }

                    public void setTotal(int total) {
                        this.total = total;
                    }

                    public List<Edge> getEdges() {
                        return edges;
                    }

                    public void setEdges(List<Edge> edges) {
                        this.edges = edges;
                    }

                    public static class Edge {
                        private Node node;

                        // Getter and setter
                        public Node getNode() {
                            return node;
                        }

                        public void setNode(Node node) {
                            this.node = node;
                        }

                        public static class Node {
                            private Keyword keyword;

                            // Getter and setter
                            public Keyword getKeyword() {
                                return keyword;
                            }

                            public void setKeyword(Keyword keyword) {
                                this.keyword = keyword;
                            }

                            public static class Keyword {
                                private String id;
                                private Text text;
                                private Category category;

                                // Getters and setters
                                public String getId() {
                                    return id;
                                }

                                public void setId(String id) {
                                    this.id = id;
                                }

                                public Text getText() {
                                    return text;
                                }

                                public void setText(Text text) {
                                    this.text = text;
                                }

                                public Category getCategory() {
                                    return category;
                                }

                                public void setCategory(Category category) {
                                    this.category = category;
                                }

                                public static class Text {
                                    private String text;

                                    // Getter and setter
                                    public String getText() {
                                        return text;
                                    }

                                    public void setText(String text) {
                                        this.text = text;
                                    }
                                }

                                public static class Category {
                                    private String id;

                                    // Getter and setter
                                    public String getId() {
                                        return id;
                                    }

                                    public void setId(String id) {
                                        this.id = id;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

