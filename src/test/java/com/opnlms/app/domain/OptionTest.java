package com.opnlms.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.opnlms.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = new Option();
        option1.setId("id1");
        Option option2 = new Option();
        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);
        option2.setId("id2");
        assertThat(option1).isNotEqualTo(option2);
        option1.setId(null);
        assertThat(option1).isNotEqualTo(option2);
    }
}
