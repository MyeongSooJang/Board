package ms.board.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ms.board.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor

public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column(unique = true)
    private Long memberId;

    private String memberName;

    private String memberPwd;

    private int memberAge;

    @Email
    private String memberEmail;

    private String memberPhone;

    private String memberRole;
}
