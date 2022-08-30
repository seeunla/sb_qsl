package com.ll.exam.qsl.user.entity;

import com.ll.exam.qsl.interesetKeyword.entity.InterestKeyword;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private Set<InterestKeyword> interestKeyword = new HashSet<>();

    public void addInterestKeywordContent(String keyword) {
        interestKeyword.add(new InterestKeyword(keyword));
    }
}
