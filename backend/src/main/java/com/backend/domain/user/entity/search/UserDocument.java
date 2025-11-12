package com.backend.domain.user.entity.search;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDocument {
    @Id private String id;
    private String name;
    private String nickname;
    private String email;
}
