package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignment.class);
        Assignment assignment1 = new Assignment();
        assignment1.setId("id1");
        Assignment assignment2 = new Assignment();
        assignment2.setId(assignment1.getId());
        assertThat(assignment1).isEqualTo(assignment2);
        assignment2.setId("id2");
        assertThat(assignment1).isNotEqualTo(assignment2);
        assignment1.setId(null);
        assertThat(assignment1).isNotEqualTo(assignment2);
    }
}
