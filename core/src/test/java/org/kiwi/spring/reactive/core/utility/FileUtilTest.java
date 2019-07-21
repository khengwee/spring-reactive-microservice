package org.kiwi.spring.reactive.core.utility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class FileUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testLoadFile() throws Exception {
        String output = FileUtil.loadFile("json/load_file_success.json");
        assertEquals("{\n  \"fileStatus\": \"success\"\n}", output);
    }
}
