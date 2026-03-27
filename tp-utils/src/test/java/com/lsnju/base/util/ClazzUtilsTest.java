package com.lsnju.base.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.lsnju.base.model.JarInfo;

/**
 * Unit tests for {@link ClazzUtils}.
 */
class ClazzUtilsTest {

    @Test
    void getParentDirectoryFromJar_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> ClazzUtils.getParentDirectoryFromJar(null));
    }

    @Test
    void getParentDirectoryFromJar_withClass_returnsNonNull() {
        String dir = ClazzUtils.getParentDirectoryFromJar(ClazzUtilsTest.class);
        Assertions.assertNotNull(dir);
    }

    @Test
    void getParentDirectoryFromJar_noArg_usesClazzUtils() {
        String dir = ClazzUtils.getParentDirectoryFromJar();
        Assertions.assertNotNull(dir);
    }

    @Test
    void getWorkingDir_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> ClazzUtils.getWorkingDir(null));
    }

    @Test
    void getWorkingDir_withClass_returnsNonNull() {
        String dir = ClazzUtils.getWorkingDir(ClazzUtilsTest.class);
        Assertions.assertNotNull(dir);
    }

    @Test
    void getWorkingDir_noArg_returnsNonNull() {
        Assertions.assertNotNull(ClazzUtils.getWorkingDir());
    }

    @Test
    void exist_knownClass_true() {
        Assertions.assertTrue(ClazzUtils.exist("java.lang.String"));
    }

    @Test
    void exist_unknownClass_false() {
        Assertions.assertFalse(ClazzUtils.exist("com.lsnju.base.util.NonexistentType98765"));
    }

    @Test
    void getImplementationVersion_null_returnsNa() {
        Assertions.assertEquals("na", ClazzUtils.getImplementationVersion(null));
    }

    @Test
    void getImplementationVersion_stringClass_returnsNonBlankOrNa() {
        String v = ClazzUtils.getImplementationVersion(String.class);
        Assertions.assertNotNull(v);
        Assertions.assertFalse(v.isEmpty());
    }

    @Test
    void getCodeLocation_null_returnsNa() {
        Assertions.assertEquals("na", ClazzUtils.getCodeLocation(null));
    }

    @Test
    void getCodeLocation_clazz_returnsNonNullString() {
        String loc = ClazzUtils.getCodeLocation(ClazzUtilsTest.class);
        Assertions.assertNotNull(loc);
    }

    @Test
    void splitJarName_noDash() {
        String[] ss = ClazzUtils.splitJarName("guava");
        Assertions.assertEquals("guava", ss[0]);
        Assertions.assertEquals("", ss[1]);
    }

    @Test
    void splitJarName_nameAndVersion() {
        String[] ss = ClazzUtils.splitJarName("spring-boot-3.0.0");
        Assertions.assertEquals("spring-boot", ss[0]);
        Assertions.assertEquals("3.0.0", ss[1]);
    }

    @Test
    void splitJarName_twoSegmentsNoLeadingDigitVersion() {
        String[] ss = ClazzUtils.splitJarName("foo-bar");
        Assertions.assertEquals("foo-bar", ss[0]);
        Assertions.assertEquals("", ss[1]);
    }

    @Test
    void splitJarName_prereleaseWithNumericMiddle() {
        String[] ss = ClazzUtils.splitJarName("foo-1.0-RC1");
        Assertions.assertEquals("foo", ss[0]);
        Assertions.assertEquals("1.0-RC1", ss[1]);
    }

    @Test
    void getURLs_nullClassLoader_returnsEmpty() {
        Assertions.assertTrue(ClazzUtils.getURLs(null).isEmpty());
    }

    @Test
    void getURLs_urlClassLoader_collectsUrls(@TempDir File tempDir) throws Exception {
        File jar = new File(tempDir, "dummy.jar");
        Assertions.assertTrue(jar.createNewFile() || jar.exists());
        URL jarUrl = jar.toURI().toURL();
        try (URLClassLoader ucl = new URLClassLoader(new URL[]{jarUrl}, null)) {
            List<URL> urls = ClazzUtils.getURLs(ucl);
            Assertions.assertEquals(1, urls.size());
            Assertions.assertEquals(jarUrl.toString(), urls.get(0).toString());
        }
    }

    @Test
    void getJarURLs_filtersNonJarEntries(@TempDir File tempDir) throws Exception {
        File jar = new File(tempDir, "lib.jar");
        Assertions.assertTrue(jar.createNewFile() || jar.exists());
        File txt = new File(tempDir, "readme.txt");
        Assertions.assertTrue(txt.createNewFile() || txt.exists());
        URL jarUrl = jar.toURI().toURL();
        URL txtUrl = txt.toURI().toURL();
        try (URLClassLoader ucl = new URLClassLoader(new URL[]{jarUrl, txtUrl}, null)) {
            List<URL> jars = ClazzUtils.getJarURLs(ucl);
            Assertions.assertEquals(1, jars.size());
            Assertions.assertTrue(jars.get(0).getPath().endsWith(".jar"));
        }
    }

    @Test
    void getURL_null_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> ClazzUtils.getURL(null));
    }

    @Test
    void getURL_returnsLocationOrNull() {
        URL url = ClazzUtils.getURL(ClazzUtilsTest.class);
        // May be null in some JDK/module setups; if present must be valid URL
        if (url != null) {
            Assertions.assertNotNull(url.getProtocol());
        }
    }

    @Test
    void fromMF_readsTestManifest() throws IOException {
        URL mf = ClazzUtilsTest.class.getResource("/custom-test-manifest.mf");
        Assertions.assertNotNull(mf);
        JarInfo info = ClazzUtils.fromMF(mf);
        Assertions.assertNotNull(info);
        Assertions.assertEquals("tp-utils-test-artifact", info.getMfName());
        Assertions.assertEquals("9.9.9-test", info.getMfVersion());
    }

    @Test
    void allJarInfo_returnsNonNullList() throws IOException {
        List<JarInfo> list = ClazzUtils.allJarInfo();
        Assertions.assertNotNull(list);
        Assertions.assertFalse(list.isEmpty(), "classpath should expose at least one MANIFEST.MF");
    }
}
