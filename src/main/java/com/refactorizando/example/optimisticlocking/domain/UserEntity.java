package com.refactorizando.example.optimisticlocking.domain;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.Type;

@Data
@Entity
public class UserEntity {

  @Id
  @GeneratedValue
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID id;

  private Integer savings = 0;

  @Version
  private Long hello;
}
