package app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="id")
@Entity
@Table(name="polls")
public class Poll {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(unique=true, nullable=false)
    private String slug;

    @Column(nullable=false)
    @JsonProperty(access=Access.READ_ONLY)
    private Date createdAt = new Date();

    @Column(nullable=false)
    @NotEmpty(message="Please provide a title")
    @Size(min=3, message="Minimum 3 characteres")
    private String title;

    @Column(nullable=false)
    @NotEmpty(message="Please provide a title")
    @Size(min=3, message="Minimum 3 characteres")
    private String description;

    @Column(nullable=false)
    @JsonProperty(access=Access.READ_ONLY)
    private Integer votes = 0;

    @OneToMany(mappedBy="poll", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("position ASC")
    @Size(min=2, message="Please provide at least 2 options")
    @Valid
    private Set<Option> options;

    @Column(nullable=false)
    private boolean restricted = false;

    @ManyToOne
    @JoinColumn(nullable=true)
    @JsonProperty(access=Access.READ_ONLY)
    private User owner;

    public Poll() {
        this.options = new HashSet<>();
    }

    @Override
    public String toString() {
        return this.title;
    }
}