package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Section.class);
        Section section1 = new Section();
        section1.setId("id1");
        Section section2 = new Section();
        section2.setId(section1.getId());
        assertThat(section1).isEqualTo(section2);
        section2.setId("id2");
        assertThat(section1).isNotEqualTo(section2);
        section1.setId(null);
        assertThat(section1).isNotEqualTo(section2);
    }
}
