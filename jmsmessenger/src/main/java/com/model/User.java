package com.model;

public class User {

  private String name;
  private String chatText;
  private String colorCode;

  public User(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getChatText() {
    return chatText;
  }

  public void setChatText(String chatText) {
    this.chatText = chatText;
  }

  public String getColorCode() {
    return colorCode;
  }

  public void setColorCode(String colorCode) {
    this.colorCode = colorCode;
  }
}
