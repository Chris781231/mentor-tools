package training360.mentortools.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(name = "github_account")
    private String githubAccount;

    private String description;

    public Student(String name, String email, String githubAccount, String description) {
        this.name = name;
        this.email = email;
        this.githubAccount = githubAccount;
        this.description = description;
    }
}
