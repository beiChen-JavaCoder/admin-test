package com.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("tb_gameinfo")
public class GameInfo {

    @Id
    private Long id;

    private String gameName;
    @Field("luaPath")
    private String luaPath;
    @Field("isActive")
    private boolean isActive;
    @Field("version")
    private Integer version;

}
