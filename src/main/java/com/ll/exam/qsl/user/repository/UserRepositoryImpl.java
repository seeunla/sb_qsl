package com.ll.exam.qsl.user.repository;

import com.ll.exam.qsl.interesetKeyword.entity.InterestKeyword;
import com.ll.exam.qsl.interesetKeyword.entity.QInterestKeyword;
import com.ll.exam.qsl.user.entity.QSiteUser;
import com.ll.exam.qsl.user.entity.SiteUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;


import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.function.LongSupplier;

import static com.ll.exam.qsl.interesetKeyword.entity.QInterestKeyword.interestKeyword;
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
        JPAQuery<SiteUser> usersQuery =  jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.username.contains(kw)
                        .or(siteUser.email.contains(kw)) )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(siteUser.getType(), siteUser.getMetadata());
            usersQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<SiteUser> users = usersQuery.fetch();

        LongSupplier totalSupplier = () -> 2;

        return PageableExecutionUtils.getPage(users, pageable, totalSupplier);
    }


    @Override
    public List<SiteUser> getQslUsersByInterestKeyword(String kw) {
        /*SELECT SU.*
        FROM site_user AS SU
        INNER JOIN site_user_interest_keywords AS SUIK
        ON SU.id = SUIK.site_user_id
        INNER JOIN interest_keyword AS IK
        ON IK.content = SUIK.interest_keywords_content
        WHERE IK.content = "축구";
        */
        QInterestKeyword IK = new QInterestKeyword("IK");

        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .innerJoin(siteUser.interestKeywords, IK)
                .where(IK.content.eq(kw))
                .fetch();
    }

    @Override
    public List<InterestKeyword> getInterestKeywordByFollowings(SiteUser user) {
        return jpaQueryFactory
                .select(interestKeyword)
                .from(interestKeyword)
                .innerJoin(interestKeyword.user, siteUser)
                .where(interestKeyword.user.in(user.getFollowings()))
                .fetch();
    }

    @Override
    public List<String> getByInterestKeywordContentsByFollowingsOf(SiteUser user) {

        QSiteUser siteUser2 = new QSiteUser("siteUser2");

        return jpaQueryFactory
                .select(interestKeyword.content)
                .distinct()
                .from(interestKeyword)
                .innerJoin(interestKeyword.user, siteUser)
                .innerJoin(siteUser.followers, siteUser2)
                .where(siteUser2.id.eq(user.getId()))
                .fetch();
    }
}
