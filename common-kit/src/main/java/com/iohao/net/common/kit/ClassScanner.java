/*
 * ionet
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.net.common.kit;

import com.iohao.net.common.kit.exception.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.jar.*;
import lombok.extern.slf4j.*;

/**
 * Classpath scanner that discovers classes matching a predicate filter.
 * <p>
 * Supports scanning from both file system directories and JAR archives.
 *
 * @author 渔民小镇
 * @date 2021-12-12
 */
@Slf4j(topic = IonetLogName.CommonStdout)
public class ClassScanner {
    /** Package path to scan (in URL path format, e.g. "com/iohao/net/") */
    final String packagePath;
    /** Predicate filter; returns {@code true} to keep the class */
    final Predicate<Class<?>> predicateFilter;

    ClassLoader classLoader;

    /**
     * Create a scanner for the given package with a class filter.
     *
     * @param packagePath     the base package to scan (dot-separated, e.g. "com.iohao.net")
     * @param predicateFilter predicate that returns {@code true} for classes to include
     */
    public ClassScanner(String packagePath, Predicate<Class<?>> predicateFilter) {
        this.predicateFilter = predicateFilter != null ? predicateFilter : clazz -> true;

        var path = packagePath.replace('.', '/');
        path = path.endsWith("/") ? path : path + '/';

        this.packagePath = path;
    }

    /**
     * Scan the classpath and return all classes that match the predicate filter.
     *
     * @return list of matching classes
     */
    public List<Class<?>> listScan() {
        this.initClassLoad();

        Set<Class<?>> classSet = new HashSet<>();

        try {
            Enumeration<URL> urlEnumeration = classLoader.getResources(packagePath);

            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();

                if ("jar".equals(protocol)) {
                    scanJar(url, classSet);
                } else if ("file".equals(protocol)) {
                    scanFile(url, classSet);
                }
            }

        } catch (IOException e) {
            ThrowKit.ofRuntimeException(e);
        }

        return new ArrayList<>(classSet);
    }

    private void initClassLoad() {
        if (this.classLoader != null) {
            return;
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        this.classLoader = classLoader != null ? classLoader : ClassScanner.class.getClassLoader();
    }

    /**
     * Return the resource URLs for the configured package path, deduplicated by URI.
     *
     * @return list of unique resource URLs
     * @throws IOException if an I/O error occurs while reading resources
     */
    public List<URL> listResource() throws IOException {
        this.initClassLoad();

        List<URL> list = new ArrayList<>();
        Set<URI> uriSet = new HashSet<>();

        Enumeration<URL> urlEnumeration = classLoader.getResources(packagePath);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();

            try {
                URI uri = url.toURI();
                if (uriSet.contains(uri)) {
                    continue;
                }

                uriSet.add(uri);
                list.add(url);
            } catch (URISyntaxException e) {
                log.error(e.getMessage(), e);
            }
        }

        return list;
    }

    private void scanJar(URL url, Set<Class<?>> classSet) throws IOException {
        URLConnection urlConn = url.openConnection();

        if (urlConn instanceof JarURLConnection jarUrlConn) {
            jarUrlConn.setUseCaches(false);
            try (JarFile jarFile = jarUrlConn.getJarFile()) {

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    // jarEntryName
                    String jarEntryName = entry.getName();

                    if (jarEntryName.isEmpty()) {
                        continue;
                    }

                    if (jarEntryName.charAt(0) == '/') {
                        jarEntryName = jarEntryName.substring(1);
                    }

                    if (entry.isDirectory() || !jarEntryName.startsWith(packagePath)) {
                        continue;
                    }

                    // Scan classes under packagePath
                    if (jarEntryName.endsWith(".class")) {
                        jarEntryName = jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
                        loadClass(jarEntryName, classSet);
                    }
                }
            }
        }
    }

    private void scanFile(URL url, Set<Class<?>> classSet) {
        File file;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
            return;
        }

        String classPath = getClassPath(file);
        if (classPath == null) {
            return;
        }

        scanFile(file, classPath, classSet);
    }

    private void scanFile(File file, String classPath, Set<Class<?>> classSet) {
        if (file.isDirectory()) {

            File[] files = file.listFiles();

            if (files == null) {
                return;
            }

            for (File value : files) {
                scanFile(value, classPath, classSet);
            }

        } else if (file.isFile()) {

            String absolutePath = file.getAbsolutePath();

            if (absolutePath.endsWith(".class")) {

                String className = absolutePath
                        .substring(classPath.length(), absolutePath.length() - 6)
                        .replace(File.separatorChar, '.');

                loadClass(className, classSet);
            }
        }
    }

    private String getClassPath(File file) {
        String absolutePath = file.getAbsolutePath();

        if (!absolutePath.endsWith(File.separator)) {
            absolutePath = absolutePath + File.separator;
        }

        String ret = packagePath.replace('/', File.separatorChar);

        int index = absolutePath.lastIndexOf(ret);

        if (index == -1) {
            log.warn("Package path [{}] not found in absolute path [{}]", ret, absolutePath);
            return null;
        }

        return absolutePath.substring(0, index);
    }

    private void loadClass(String className, Set<Class<?>> classSet) {
        Class<?> clazz = null;

        try {
            clazz = classLoader.loadClass(className);
        } catch (ClassNotFoundException | LinkageError e) {
            log.debug(e.getMessage(), e);
        }

        if (clazz != null && !classSet.contains(clazz)) {
            if (predicateFilter.test(clazz)) {
                classSet.add(clazz);
            }
        }
    }
}
