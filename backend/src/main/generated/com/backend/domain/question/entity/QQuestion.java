package com.backend.domain.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -456476980L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final ListPath<com.backend.domain.answer.entity.Answer, com.backend.domain.answer.entity.QAnswer> answers = this.<com.backend.domain.answer.entity.Answer, com.backend.domain.answer.entity.QAnswer>createList("answers", com.backend.domain.answer.entity.Answer.class, com.backend.domain.answer.entity.QAnswer.class, PathInits.DIRECT2);

    public final com.backend.domain.user.entity.QUser author;

    public final EnumPath<QuestionCategoryType> categoryType = createEnum("categoryType", QuestionCategoryType.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final ComparablePath<java.util.UUID> groupId = createComparable("groupId", java.util.UUID.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath isApproved = createBoolean("isApproved");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final StringPath title = createString("title");

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new com.backend.domain.user.entity.QUser(forProperty("author"), inits.get("author")) : null;
    }

}

