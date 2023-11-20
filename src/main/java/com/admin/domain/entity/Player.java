package com.admin.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

/**
 * 玩家数据
 * by kyle
 */
@Data
@Document(collection = "tb_roleinfo")
@EqualsAndHashCode(callSuper = false)
public class Player {

    @Id
    @javax.persistence.Id
//    @Indexed(unique = true, name = "rid_index")
    private long rid;

    @Column(name = "user_name")
    private String userName;


    @Column(name = "rolename")
    private String rolename;


    @Column(name = "logo")
    private String logo;


    @Column(name = "logoframe")
    private String logoframe;


    @Column(name = "mail_box")
    private String mailBox;


    @Column(name = "phone")
    private String phone;


    @Column(name = "sex")
    private int sex;


    @Column(name = "des")
    private String des;

    //1是机器人，非1不是机器人

    @Column(name = "is_robot")
    private byte isRobot;


    @Column(name = "reg_ip")
    private String regIp;


    @Column(name = "reg_time")
    private long regTime;


    @Column(name = "lastlogin_ip")
    private String lastlogin_ip;


    @Column(name = "lastlogin_time")
    private long lastlogin_time;

    //1表示在线，非1表示不在线

    @Column(name = "is_online")
    private byte isOnline;

    //表示当前账号是否是激活状态，1表示激活，非1表示未激活，一般用于冻结账号

    @Column(name = "is_activate")
    private byte isActivate;

    //当前玩家身上的金币
    @Column(name = "gold")
    private long gold;

    //当前冻结时间
    private long freeze_time;

}