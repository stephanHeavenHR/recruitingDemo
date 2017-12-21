package com.havenhr.entity;

import com.havenhr.common.ApplicationStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "application", uniqueConstraints = @UniqueConstraint(columnNames = {"offer", "candidate"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Application {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @JoinColumn(name = "offer", updatable = false)
    @ManyToOne
    private Offer related;

    @Email
    @Column(name = "candidate")
    private String candidate;

    @Column(name = "resume")
    private String resume;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private ApplicationStatus applicationStatus;

    @Override
    public String toString() {
        return "Application{" +
               "id=" +
               id +
               ", related=" +
               related +
               ", candidate='" +
               candidate +
               '\'' +
               ", resume='" +
               resume +
               '\'' +
               ", applicationStatus=" +
               applicationStatus +
               '}';
    }
}
