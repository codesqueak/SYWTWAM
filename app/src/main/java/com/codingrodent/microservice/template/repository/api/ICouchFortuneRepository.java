package com.codingrodent.microservice.template.repository.api;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import org.springframework.context.annotation.Profile;

@Profile("prod")
public interface ICouchFortuneRepository extends ISyncFortuneRepository<FortuneEntity, String> {

}
