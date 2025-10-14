package com.khan.job_quest.jobs.entity;

import com.khan.job_quest.common.entity.BaseEntity;
import com.khan.job_quest.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    private String location;

    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    private User employer;

    @Column(nullable = false)
    private String companyName;
}
