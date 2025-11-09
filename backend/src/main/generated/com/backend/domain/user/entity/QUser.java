package com.backend.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1074162794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final EnumPath<AccountStatus> accountStatus = createEnum("accountStatus", AccountStatus.class);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final NumberPath<Integer> aiQuestionUsedCount = createNumber("aiQuestionUsedCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final StringPath github = createString("github");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath image = createString("image");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final com.backend.domain.ranking.entity.QRanking ranking;

    public final com.backend.domain.resume.entity.QResume resume;

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final com.backend.domain.subscription.entity.QSubscription subscription;

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ranking = inits.isInitialized("ranking") ? new com.backend.domain.ranking.entity.QRanking(forProperty("ranking"), inits.get("ranking")) : null;
        this.resume = inits.isInitialized("resume") ? new com.backend.domain.resume.entity.QResume(forProperty("resume"), inits.get("resume")) : null;
        this.subscription = inits.isInitialized("subscription") ? new com.backend.domain.subscription.entity.QSubscription(forProperty("subscription"), inits.get("subscription")) : null;
    }

}

