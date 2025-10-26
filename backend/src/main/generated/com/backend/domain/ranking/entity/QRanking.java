package com.backend.domain.ranking.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRanking is a Querydsl query type for Ranking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanking extends EntityPathBase<Ranking> {

    private static final long serialVersionUID = -1589078618L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRanking ranking = new QRanking("ranking");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<Integer> rankValue = createNumber("rankValue", Integer.class);

    public final EnumPath<Tier> tier = createEnum("tier", Tier.class);

    public final NumberPath<Integer> totalScore = createNumber("totalScore", Integer.class);

    public final com.backend.domain.user.entity.QUser user;

    public QRanking(String variable) {
        this(Ranking.class, forVariable(variable), INITS);
    }

    public QRanking(Path<? extends Ranking> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRanking(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRanking(PathMetadata metadata, PathInits inits) {
        this(Ranking.class, metadata, inits);
    }

    public QRanking(Class<? extends Ranking> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.backend.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

