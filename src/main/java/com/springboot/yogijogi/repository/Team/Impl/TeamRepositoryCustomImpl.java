package com.springboot.yogijogi.repository.Team.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springboot.yogijogi.entity.QTeam;
import com.springboot.yogijogi.entity.Team;
import com.springboot.yogijogi.repository.Team.TeamRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Team> searchTeam(String team_name) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.team_name.like("%"+team_name+"%"))
                .fetch();
    }

    @Override
    public List<Team> searchRegion(String region) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.region.like("%"+region+"%"))
                .fetch();
    }

    @Override
    public List<Team> searchGender(String gender) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.gender.like("%"+gender+"%"))
                .fetch();
    }
    @Override
    public List<Team> searchDay(String activity_days) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.activity_days.contains(activity_days))
                .fetch();
    }
    @Override
    public List<Team> searchTime(String time) {
        QTeam qTeam = QTeam.team;
        return jpaQueryFactory.selectFrom(qTeam)
                .where(qTeam.activity_time.contains(time))
                .fetch();
    }
}
