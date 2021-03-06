package com.ventas.ventadepasajes.domain.model.entity.dto;

public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private long role;

    public UserDto(long id, String name, String lastName, String email, String phone, long role){
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public long getRole() {
        return role;
    }
}
