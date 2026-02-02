package com.example.studentmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String username;   // ✅ Step 6.1

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    @JsonIgnoreProperties({"students", "teachers"}) // prevents loops if Dept has back refs
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnoreProperties({"students"}) // prevents loops if Course has back ref
    private Set<Course> courses = new HashSet<>();
}
