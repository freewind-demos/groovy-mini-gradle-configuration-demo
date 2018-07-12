package my

import org.junit.Test

import static org.fest.assertions.api.Assertions.assertThat

class HelloSpec {

    @Test
    void "test hello"() {
        String greeting = Hello.hello("gradle")
        assertThat(greeting).isEqualTo("Hello, gradle!")
    }

}
