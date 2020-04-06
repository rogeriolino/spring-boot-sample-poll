package app.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="id")
@Entity
@Table(name="poll_votes")
public class Vote {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(nullable=false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(nullable=false)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Option option;

    @ManyToOne
    @JoinColumn(nullable=false)
    @JsonIgnore
    @OnDelete(action=OnDeleteAction.CASCADE)
    private Poll poll;

    @ManyToOne
    @JoinColumn(nullable=true)
    @OnDelete(action=OnDeleteAction.CASCADE)
    private User user;

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}