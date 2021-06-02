package com.example.demo.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "xml")
public class Groupbean {

    @JacksonXmlProperty(localName = "MsgType")
    private String MsgType;

    @JacksonXmlProperty(localName = "Event")
    private String Event;

    @JacksonXmlProperty(localName = "ChatId")
    private String ChatId;

}
