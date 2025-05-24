package com.idld.coursservice.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Syllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long syllabus_id;

    @ElementCollection
    @CollectionTable(name = "weekly_topics", joinColumns = @JoinColumn(name = "syllabus_id"))
    @MapKeyColumn(name = "week")
    @Column(name = "topic")
    private Map<Integer, String> weeklyTopics;

    private String name;

    @ElementCollection
    @CollectionTable(name = "readings", joinColumns = @JoinColumn(name = "syllabus_id"))
    @MapKeyColumn(name = "week")
    @Column(name = "reading")

    private Map<Integer, String> readings;

    @ElementCollection
    @CollectionTable(name = "assignments", joinColumns = @JoinColumn(name = "syllabus_id"))
    @MapKeyColumn(name = "week")
    @Column(name = "assignment")

    private Map<Integer, String> assignments;

    @ElementCollection
    @CollectionTable(name = "exams", joinColumns = @JoinColumn(name = "syllabus_id"))
    @MapKeyColumn(name = "week")
    @Column(name = "exam")

    private Map<Integer, String> exams;

    private String gradingPolicy;

    private boolean assigned;
}
