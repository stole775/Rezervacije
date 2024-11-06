package com.reservationbroker.reservation.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Data
@Embeddable
public class UserUslugaId implements Serializable {
    private static final long serialVersionUID = 2256200613066227963L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @Column(name = "usluga_id", nullable = false)
    private Long uslugaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserUslugaId entity = (UserUslugaId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.uslugaId, entity.uslugaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uslugaId);
    }

}