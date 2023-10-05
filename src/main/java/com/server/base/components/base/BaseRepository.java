package com.server.base.components.base;

import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseRepository {
    @Autowired protected JPQLQueryFactory query;
}
