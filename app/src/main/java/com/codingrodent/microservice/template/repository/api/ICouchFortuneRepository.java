package com.codingrodent.microservice.template.repository.api;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import org.springframework.context.annotation.Profile;

/**
 * Let spring build basic repository - we don't have to supply a body for this
 * <p>
 * Repository to be used when Couchbase is  present and selected
 */

@Profile("prod")
public interface ICouchFortuneRepository extends ISyncFortuneRepository<FortuneEntity, String> {

}
