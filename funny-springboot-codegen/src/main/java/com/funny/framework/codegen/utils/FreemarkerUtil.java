package com.funny.framework.codegen.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FreemarkerUtil {

    public static String generateFilesFromTemplates(String templateDir, String outputDir, Map<String, String> dataModel) throws Exception {
        // 初始化 FreeMarker 配置
        Configuration cfg = new Configuration(new Version("2.3.31"));
        cfg.setDirectoryForTemplateLoading(new File(templateDir));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
        String codeOutputDir = outputDir + "/code";
        String zipOutputDir = outputDir + "/zip";
        // 获取模板目录及其子目录下的所有 .ftl 文件
        Files.walk(Paths.get(templateDir))
                .filter(path -> path.toString().endsWith(".ftl"))
                .forEach(path -> processTemplate(cfg, path, templateDir, codeOutputDir, dataModel));

        // 打包输出目录为 ZIP 文件
        String artifactId = dataModel.get("artifactId");
        String version = dataModel.get("version");
        String zipFileName = artifactId + "-" + version + ".zip";
        // 将 ZIP 文件放在与输出目录同级的位置
        Path outputZipFile = Paths.get(zipOutputDir).resolveSibling(zipFileName);
        packDirectoryToZip(Paths.get(codeOutputDir), outputZipFile);
        return outputZipFile.toString();
    }

    private static void processTemplate(Configuration cfg, Path curPath, String templateDir, String outputDir, Map<String, String> dataModel) {
        try {
            System.out.println(curPath);
            boolean addPackagePath = false;
            if (curPath.toString().endsWith(".java.ftl")) {
                addPackagePath = true;
            }
            String artifactId = dataModel.get("artifactId");
            // 计算相对于模板根目录的路径，用于保留目录结构
            final Path templateRelativePath = Paths.get(templateDir).relativize(curPath);
            File outputFile = null;
            String targetPath = "";
            if (addPackagePath) {
                targetPath = rewritePathWithPackage(templateRelativePath.toString(), dataModel.get("package"));
            } else {
                targetPath = templateRelativePath.toString();
            }
            // 替换 sample-web 和 sample-api
            String path = targetPath.replace("sample-web", artifactId + "-web")
                    .replace("sample-api", artifactId + "-api");

            Path outPutRrelativePath = Paths.get(path);
            outputFile = new File(outputDir, outPutRrelativePath.toString().replace(".ftl", ""));
            // 确保输出目录结构存在
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            // 处理模板并写入输出文件
            try (Writer out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {

                Template template = cfg.getTemplate(templateRelativePath.toString());
                template.process(dataModel, out);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing template: " + curPath, e);
        }
    }

    /**
     * 根据包名重写文件路径
     *
     * @param originalPath 原始文件路径
     * @param packageName  目标包名
     * @return 重写后的文件路径
     */
    public static String rewritePathWithPackage(String originalPath, String packageName) {
        // 将包名按点分割成数组
        String[] packageParts = packageName.split("\\.");
        String after = Joiner.on(File.separator).join(packageParts);
        // 替换原始路径中的目录部分
        int javaIndex = originalPath.indexOf("java"+File.separator);
        if (javaIndex == -1) {
            throw new IllegalArgumentException("Invalid path format: does not contain 'java\\'");
        }
        // 拼接最终的路径
        String newPath = originalPath.substring(0, javaIndex)
                + File.separator + "java" + File.separator + after
                + originalPath.substring(javaIndex + "java".length());
        return newPath;
    }
    /**
     * 将指定目录打包为 ZIP 文件
     *
     * @param sourceDirPath 要打包的源目录
     * @param zipFilePath   生成的 ZIP 文件路径
     * @throws IOException 如果发生 I/O 错误
     */
    public static void packDirectoryToZip(Path sourceDirPath, Path zipFilePath) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
            Files.walk(sourceDirPath)
                    .filter(path -> !Files.isDirectory(path)) // 排除目录本身
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zipOut.putNextEntry(zipEntry);
                            Files.copy(path, zipOut);
                        } catch (IOException e) {
                            throw new RuntimeException("Error packing file: " + path, e);
                        }
                    });
        }
    }

}
