package com.lsnju.base.util;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.lsnju.base.xml.sax.TpSaxEventRecorder;
import com.lsnju.base.xml.sax.event.SaxEvent;
import com.lsnju.base.xml.sax.event.StartEvent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lisong
 * @since 2023/3/24 15:32
 * @version V1.0
 */
@Slf4j
public class TpSaxEventRecorderTest {
    @Test
    public void test_001() {
        final ClassLoader classLoader = TpSaxEventRecorderTest.class.getClassLoader();
        TpSaxEventRecorder recorder = new TpSaxEventRecorder();
        try {
            recorder.recordEvents(classLoader.getResourceAsStream("logback-test.xml"));
            final List<SaxEvent> saxEventList = recorder.getSaxEventList();
            log.info("size = {}", saxEventList.size());
            for (SaxEvent event : saxEventList) {
                log.info("{}", event);
                if (event instanceof StartEvent) {
                    final String tagPath = ((StartEvent) event).getTagPath();
                    log.info("== path={}, localName={}, qName={}", tagPath, event.getLocalName(), event.getQName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
