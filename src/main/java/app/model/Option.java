package app.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id", "name"})
@Entity
@Table(name="poll_options")
public class Option {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable=false)
    @NotEmpty(message="Please fill all options")
    private String name;

    @Column(nullable=false)
    @JsonIgnore
    private Integer position;

    @Column(nullable=false)
    @JsonProperty(access=Access.READ_ONLY)
    private Integer votes = 0;

    @ManyToOne
    @JoinColumn(nullable=false)
    @JsonIgnore
    private Poll poll;

    @Override
    public String toString() {
        return this.name;
    }
}