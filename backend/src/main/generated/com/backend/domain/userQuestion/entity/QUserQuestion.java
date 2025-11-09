package com.backend.domain.userQuestion.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserQuestion is a Querydsl query type for UserQuestion
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserQuestion extends EntityPathBase<UserQuestion> {

    private static final long serialVersionUID = 1667453090L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserQuestion userQuestion = new QUserQuestion("userQuestion");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final NumberPath<Integer> aiScore = createNumber("aiScore", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final com.backend.domain.question.entity.QQuestion question;

    public final com.backend.domain.user.entity.QUser user;

    public QUserQuestion(String variable) {
        this(UserQuestion.class, forVariable(variable), INITS);
    }

    public QUserQuestion(Path<? extends UserQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserQuestion(PathMetadata metadata, PathInits inits) {
        this(UserQuestion.class, metadata, inits);
    }

    public QUserQuestion(Class<? extends UserQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new com.backend.domain.question.entity.QQuestion(forProperty("question"), inits.get("question")) : null;
        this.user = inits.isInitialized("user") ? new com.backend.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

