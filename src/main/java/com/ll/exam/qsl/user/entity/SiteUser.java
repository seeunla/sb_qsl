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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @Builder.Default
    private Set<InterestKeyword> interestKeywords = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<SiteUser> followers = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<SiteUser> followings = new HashSet<>();

    public void addInterestKeywordContent(String keyword) {
        interestKeywords.add(new InterestKeyword(this, keyword));
    }

    public void follow(SiteUser following) {
        if (this == following) return;
        if (following == null) return;
        if (this.getId() == following.getId()) return;

        following.getFollowers().add(this);
        getFollowings().add(following);
    }

    public void removeInterestKeywordContent(String keyword) {
        interestKeywords.remove(new InterestKeyword(this, keyword));
    }

    public void findInterestKeywordContentByFollowings(SiteUser user) {
        user.getInterestKeywords();

    }
}
