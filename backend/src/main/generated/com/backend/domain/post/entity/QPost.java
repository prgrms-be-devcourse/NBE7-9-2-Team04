package com.backend.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 668423616L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.backend.global.entity.QBaseEntity _super = new com.backend.global.entity.QBaseEntity(this);

    public final ListPath<com.backend.domain.comment.entity.Comment, com.backend.domain.comment.entity.QComment> comments = this.<com.backend.domain.comment.entity.Comment, com.backend.domain.comment.entity.QComment>createList("comments", com.backend.domain.comment.entity.Comment.class, com.backend.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final DateTimePath<java.time.LocalDateTime> deadline = createDateTime("deadline", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath introduction = createString("introduction");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final EnumPath<PinStatus> pinStatus = createEnum("pinStatus", PinStatus.class);

    public final EnumPath<PostCategoryType> postCategoryType = createEnum("postCategoryType", PostCategoryType.class);

    public final NumberPath<Integer> recruitCount = createNumber("recruitCount", Integer.class);

    public final EnumPath<PostStatus> status = createEnum("status", PostStatus.class);

    public final StringPath title = createString("title");

    public final com.backend.domain.user.entity.QUser users;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.users = inits.isInitialized("users") ? new com.backend.domain.user.entity.QUser(forProperty("users"), inits.get("users")) : null;
    }

}

