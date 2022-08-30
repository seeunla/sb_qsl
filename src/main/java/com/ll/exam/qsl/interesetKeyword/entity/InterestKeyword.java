package com.ll.exam.qsl.interesetKeyword.entity;

import com.ll.exam.qsl.user.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
public class InterestKeyword {
    @Id
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterestKeyword that = (InterestKeyword) o;

        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    public InterestKeyword(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return content;
    }
}
