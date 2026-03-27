package com.lsnju.base.util;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProfilerTest {

    @AfterEach
    void tearDown() {
        Profiler.reset();
    }

    @Test
    void defaultState_afterReset() {
        Profiler.reset();
        Assertions.assertNull(Profiler.getEntry());
        Assertions.assertEquals(-1L, Profiler.getDuration());
        Assertions.assertEquals("", Profiler.dump());
    }

    @Test
    void startEnterRelease_buildsEntryTree() {
        Profiler.start("root");
        Profiler.enter("child");
        Profiler.release("child-done");
        Profiler.release("root-done");

        Profiler.Entry root = Profiler.getEntry();
        Assertions.assertNotNull(root);
        Assertions.assertTrue(root.getMessage().contains("root"));
        Assertions.assertTrue(root.getMessage().contains("root-done"));

        List<Profiler.Entry> subs = root.getSubEntries();
        Assertions.assertEquals(1, subs.size());
        Assertions.assertTrue(subs.get(0).getMessage().contains("child"));
        Assertions.assertTrue(subs.get(0).getMessage().contains("child-done"));
    }

    @Test
    void dump_containsTreeMarkersAndPrefixes() {
        Profiler.start("A");
        Profiler.enter("B");
        Profiler.release();
        Profiler.release();

        String dump = Profiler.dump("P1-", "P2-");
        Assertions.assertNotNull(dump);
        Assertions.assertTrue(dump.contains("P1-"));
        Assertions.assertTrue(dump.contains("P2-`---") || dump.contains("P2-+---"));
        Assertions.assertTrue(dump.contains(" - A"));
        Assertions.assertTrue(dump.contains(" - B"));
    }

    @Test
    void dump_unreleasedShowsMarker() {
        Profiler.start("root");
        Profiler.enter("child");
        String dump = Profiler.dump();
        Assertions.assertTrue(dump.contains("[UNRELEASED]"));
    }

    @Test
    void subEntries_isUnmodifiable() {
        Profiler.start("root");
        Profiler.enter("child");
        Profiler.release();
        Profiler.release();

        Profiler.Entry root = Profiler.getEntry();
        Assertions.assertNotNull(root);
        Assertions.assertThrows(UnsupportedOperationException.class,
            () -> root.getSubEntries().add(root));
    }
}
