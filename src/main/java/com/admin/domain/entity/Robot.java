package com.admin.domain.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("game_robot_name")
@ApiModel("机器人名称")
public class Robot {

    @Id
    private Long id;
    @Indexed(unique = true)
    @Field("robot_name")
    private String robotName;

}
