package com.ll.exam.qsl.user.repository;

import com.ll.exam.qsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;


import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.function.LongSupplier;

import static com.ll.exam.qsl.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SiteUser getQslUser(Long id) {
        /*
        SELECT *
        FROM site_user
        WHERE id = 1
        */

        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(id))
                .fetchOne();
    }

    @Override
    public int getQslCount() {
        long count = jpaQueryFactory
                .select(siteUser.count())
                .from(siteUser)
                .fetchOne();

        return (int)count;
    }

    @Override
    public SiteUser getQslUserOrderByIdAscOne() {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(1L))
                .orderBy(siteUser.id.asc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public List<SiteUser> getQslUsersOrderByIdAsc() {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .orderBy(siteUser.id.asc())
                .fetch();
    }

    @Override
    public List<SiteUser> searchQsl(String kw) {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.username.contains(kw)
                        .or(siteUser.email.contains(kw)))
                .orderBy(siteUser.id.desc())
                .fetch();
    }


    @Override
    public Page<SiteUser> searchQsl(String kw, Pageable pageable) {
        List<SiteUser> users =  jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.username.contains(kw)
                        .or(siteUser.email.contains(kw)) )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(siteUser.id.desc())
                .fetch();

        LongSupplier totalSupplier = () -> 2;

        return PageableExecutionUtils.getPage(users, pageable, null);
    }
}
