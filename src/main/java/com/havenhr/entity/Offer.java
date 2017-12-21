package com.havenhr.entity;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "offer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Offer {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "job_title", unique = true)
    private String jobTitle;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;

    @Column(name = "number_of_applications")
    private Integer numberOfApplications;

    @OneToMany(mappedBy = "related", cascade = {ALL}, fetch = EAGER)
    private final Set<Application> applications = new HashSet<>();

    @Override
    public String toString() {
        return "Offer{" +
               "id=" +
               id +
               ", jobTitle='" +
               jobTitle +
               '\'' +
               ", startDate=" +
               startDate +
               ", numberOfApplications=" +
               numberOfApplications +
               ", applications=" +
               applications +
               '}';
    }
}
