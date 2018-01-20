package com.example.credsrepo;

import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    @Column(name = "user_group", nullable = false)
    String group;

    @NotEmpty
    @Column(name = "account", nullable = false)
    String account;

    @NotEmpty
    @Column(name = "password", nullable = false)
    String password;

    @NotEmpty
    @Column(name = "salt", nullable = false)
    String salt;

    @NotEmpty
    @Column(name = "create_user", nullable = false)
    String createUser;

    @Column(name = "create_timestamp", nullable = false)
    Integer createTimeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Integer createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }


}
