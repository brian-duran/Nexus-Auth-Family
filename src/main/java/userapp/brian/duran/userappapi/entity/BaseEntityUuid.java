package userapp.brian.duran.userappapi.entity;

import jakarta.persistence.*;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseEntityUuid extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "binary(16)")
  private UUID id;

  @PrePersist
  void ensureId() {
    if (id == null) id = UUID.randomUUID();
  }
}
