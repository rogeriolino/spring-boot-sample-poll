package app.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","username"})
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique=true, nullable=false)
    @NotBlank(message="Pelase provide the username")
    @Size(min=3, message="Minimum 3 characteres")
    private String username;

    @JsonProperty(access=Access.WRITE_ONLY)
    @Column(nullable=false)
    @NotBlank(message="Pelase provide the password")
    private String password;

    @Column(nullable=false)
    private String[] roles;

    @Override
    public String toString() {
        return this.username;
    }
}