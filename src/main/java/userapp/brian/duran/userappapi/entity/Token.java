package userapp.brian.duran.userappapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import userapp.brian.duran.userappapi.enums.RevocationReasonEnum;
import userapp.brian.duran.userappapi.enums.TokenTypeEnum;

@Entity
@Table(name = "tokens",
    indexes = {
      @Index(name = "idx_tokens_user_created", columnList = "user_id, created_at DESC"),
      @Index(name = "idx_tokens_family_id", columnList = "family_id"),
      @Index(name = "idx_tokens_expires_at", columnList = "expires_at"),
      @Index(name = "idx_tokens_token_hash", columnList = "token_hash")
    },
    uniqueConstraints = {
      @UniqueConstraint(name = "ux_tokens_jti", columnNames = {"jti"})
    }
)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Token extends BaseEntityUuid {

    @Column(name = "token_hash", length = 128)
    private String tokenHash;

    @Column(name = "jti", nullable = false)
    private UUID jti;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "parent_jti")
    private UUID parentJti;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenTypeEnum tokenType = TokenTypeEnum.BEARER;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "revocation_reason")
    @Builder.Default
    private RevocationReasonEnum revocationReason = RevocationReasonEnum.NOT_REVOKED;

    @Column(nullable = false)
    @Builder.Default
    private boolean expired = false;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    // Contexto del cliente (IP / User-Agent)
    @Embedded
    private ClientInfo clientInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    public void revoke(RevocationReasonEnum reason) {
        if (this.revoked) {
            throw new IllegalStateException("El token ya estaba revocado.");
        }
        this.revoked = true;
        this.revocationReason = reason;
    }
}
