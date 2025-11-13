package com.backend.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QVerificationCode is a Querydsl query type for VerificationCode
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVerificationCode extends EntityPathBase<VerificationCode> {

    private static final long serialVersionUID = -298991117L;

    public static final QVerificationCode verificationCode = new QVerificationCode("verificationCode");

    public final StringPath code = createString("code");

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath verified = createBoolean("verified");

    public QVerificationCode(String variable) {
        super(VerificationCode.class, forVariable(variable));
    }

    public QVerificationCode(Path<? extends VerificationCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVerificationCode(PathMetadata metadata) {
        super(VerificationCode.class, metadata);
    }

}

