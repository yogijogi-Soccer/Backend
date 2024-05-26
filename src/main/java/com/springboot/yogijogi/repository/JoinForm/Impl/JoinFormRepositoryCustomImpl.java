package com.springboot.yogijogi.repository.JoinForm.Impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogi.entity.JoinForm;
import com.springboot.yogijogi.entity.QJoinForm;
import com.springboot.yogijogi.entity.QMember;
import com.springboot.yogijogi.entity.QTeam;
import com.springboot.yogijogi.repository.JoinForm.JoinFormCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class JoinFormRepositoryCustomImpl implements JoinFormCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<JoinForm> findPendingJoinFormsByTeam_TeamId(Long teamId) {
        QJoinForm qJoinForm = QJoinForm.joinForm;
        QMember qMember = QMember.member;
        QTeam qTeam = QTeam.team;

        return jpaQueryFactory
                .select(
                        Projections.fields(JoinForm.class,
                                qMember.name.as("memberName"),
                                qMember.position.as("memberPosition"),
                                qJoinForm.joinStatus,
                                qJoinForm.additional_info
                        ))
                .from(qJoinForm)
                .leftJoin(qJoinForm.member, qMember)
                .leftJoin(qJoinForm.team, qTeam)
                .where(qTeam.teamId.eq(teamId)
                        .and(qJoinForm.joinStatus.eq("대기중")))
                .fetch();
    }
}
