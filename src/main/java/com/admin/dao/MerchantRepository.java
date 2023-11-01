package com.admin.dao;

import com.admin.domain.entity.MerchantEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Xqf
 * @version 1.0
 */
@Repository
public interface MerchantRepository extends MongoRepository<MerchantEntity,String> {
}
