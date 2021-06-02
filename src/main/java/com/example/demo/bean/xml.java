package com.example.demo.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "xml")
public class xml {

//    @JacksonXmlProperty(localName = "SuiteTicket")
//    private String SuiteTicket;
//
//    @JacksonXmlProperty(localName = "Encrypt")
//    private String Encrypt;
//
    @XmlElement(name = "ToUserName")
    private String ToUserName;

    @XmlElement(name = "FromUserName")
    private String FromUserName;

    @XmlElement(name = "CreateTime")
    private String CreateTime;

    @XmlElement(name = "MsgType")
    private String MsgType;

    @XmlElement(name = "Event")
    private String Event;

    @XmlElement(name = "ChatId")
    private String ChatId;

    @XmlElement(name = "ChangeType")
    private String ChangeType;

    @XmlElement(name = "UpdateDetail")
    private String UpdateDetail;

    @XmlElement(name = "SuiteTicket")
    private String SuiteTicket;
}
