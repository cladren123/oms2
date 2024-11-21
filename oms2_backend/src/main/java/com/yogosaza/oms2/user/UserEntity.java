package com.yogosaza.oms2.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oms2_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String loginId;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "oms2_user_roles", joinColumns = @JoinColumn(name="user_id"))
    private List<String> roles;

    private String name;
}
