package ru.doublebyte.availabilitymonitor.entities;

import javax.persistence.*;

@Entity
@Table(name = "test_result_difference")
public class TestResultDifference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    @JoinColumn(
        name = "result_id",
        nullable = false
    )
    private TestResult result;

    @ManyToOne(
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE
    )
    @JoinColumn(
        name = "prev_result_id",
        nullable = false
    )
    private TestResult prevResult;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultDifference() {

    }

    public TestResultDifference(TestResult result, TestResult prevResult) {
        this.result = result;
        this.prevResult = prevResult;
    }

    ///////////////////////////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }

    public TestResult getResult() {
        return result;
    }

    public TestResult getPrevResult() {
        return prevResult;
    }
}
