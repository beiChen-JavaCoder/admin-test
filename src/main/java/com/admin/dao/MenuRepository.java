package com.admin.dao;

import com.admin.domain.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Xqf
 * @version 1.0
 */
public interface MenuRepository extends MongoRepository<Menu,String> {
}
