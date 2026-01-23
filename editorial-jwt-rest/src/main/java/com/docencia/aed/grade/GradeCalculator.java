package com.docencia.aed.grade;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Calcula la nota del ejercicio en base a los reportes XML de Maven Surefire.
 *
 * Reglas:
 * - Hay 4 bloques (servicios) evaluables mediante tests de integración:
 *   1) Auth
 *   2) Authors
 *   3) Books
 *   4) Publishers
 * - Cada bloque suma hasta 2.5 puntos. Total = 10.
 * - La puntuación de cada bloque es proporcional a los tests que pasen dentro de ese bloque.
 *
 * Salida:
 * - Genera target/nota.txt con el desglose y la nota total.
 */
public class GradeCalculator {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private static final Map<String, String> GROUPS = Map.of(
            "Auth", "com.docencia.aed.integration.AuthIntegrationTest",
            "Authors", "com.docencia.aed.integration.AuthorApiIntegrationTest",
            "Books", "com.docencia.aed.integration.BookApiIntegrationTest",
            "Publishers", "com.docencia.aed.integration.PublisherApiIntegrationTest"
    );

    private static final double POINTS_PER_GROUP = 2.5;

    public static void main(String[] args) throws Exception {
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path reportsDir = projectRoot.resolve("target").resolve("surefire-reports");
        Path out = projectRoot.resolve("target").resolve("nota.txt");

        StringBuilder sb = new StringBuilder();
        sb.append("=== Calculo de nota (progreso) ===\n");
        sb.append("Carpeta reportes: ").append(reportsDir).append("\n\n");

        double total = 0.0;

        for (var entry : GROUPS.entrySet()) {
            String groupName = entry.getKey();
            String testClass = entry.getValue();

            TestStats stats = readStats(reportsDir, testClass);
            double score = stats.totalTests == 0 ? 0.0 : (POINTS_PER_GROUP * ((double) stats.passedTests / stats.totalTests));
            total += score;

            sb.append(groupName).append(" (max ").append(POINTS_PER_GROUP).append(")\n");
            sb.append("  Test class: ").append(testClass).append("\n");
            sb.append("  Total: ").append(stats.totalTests)
              .append(" | Passed: ").append(stats.passedTests)
              .append(" | Failed: ").append(stats.failedTests)
              .append(" | Errors: ").append(stats.errors)
              .append(" | Skipped: ").append(stats.skipped)
              .append("\n");
            sb.append("  Nota bloque: ").append(DF.format(score)).append("\n\n");
        }

        // clamp
        if (total < 0) total = 0;
        if (total > 10) total = 10;

        sb.append("NOTA FINAL: ").append(DF.format(total)).append(" / 10.00\n");

        write(out, sb.toString());
        System.out.println(sb);
    }

    private static TestStats readStats(Path reportsDir, String testClass) {
        // Surefire report file naming: TEST-<fqcn>.xml
        Path report = reportsDir.resolve("TEST-" + testClass + ".xml");
        if (!Files.exists(report)) {
            return new TestStats(0, 0, 0, 0, 0);
        }

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(report.toFile());
            Element suite = (Element) doc.getElementsByTagName("testsuite").item(0);

            int tests = parseIntAttr(suite, "tests");
            int failures = parseIntAttr(suite, "failures");
            int errors = parseIntAttr(suite, "errors");
            int skipped = parseIntAttr(suite, "skipped");

            int passed = Math.max(0, tests - failures - errors - skipped);
            return new TestStats(tests, passed, failures, errors, skipped);
        } catch (Exception e) {
            // Si no se puede leer, no puntúa.
            return new TestStats(0, 0, 0, 0, 0);
        }
    }

    private static int parseIntAttr(Element el, String attr) {
        String v = el.getAttribute(attr);
        if (v == null || v.isBlank()) return 0;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void write(Path out, String content) throws IOException {
        Files.createDirectories(out.getParent());
        Files.writeString(out, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private record TestStats(int totalTests, int passedTests, int failedTests, int errors, int skipped) {}
}
