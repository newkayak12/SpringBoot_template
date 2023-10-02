package com.server.base.repository.domains;

import com.server.base.components.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created on 2023-05-10
 * Project user-service
 */
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "account")
@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Account implements Serializable {
    @Id
    @Column(name = "userNo", columnDefinition = "BIGINT(20)")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;
    @Column(name = "userId", columnDefinition = "VARCHAR (100)")
    private String userId;
    @Column(name = "userPwd", columnDefinition = "VARCHAR (500)")
    private String userPwd;
    @Column(name = "regDate", columnDefinition = "DATETIME default CURRENT_TIMESTAMP()")
    private LocalDateTime regDate;
    @Column(name = "lastSignDate", columnDefinition = "DATETIME")
    private LocalDateTime lastSignDate;

    @Column(name = "role", columnDefinition = "VARCHAR( 32 )")
    @Enumerated(value = EnumType.STRING)
    private Role role;


    @PostLoad
    public void renewSignDate(){
        this.lastSignDate = LocalDateTime.now();
    }

    @PrePersist
    public void signUpDate() {
        this.regDate = LocalDateTime.now();
    }


}
