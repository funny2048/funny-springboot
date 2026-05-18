package com.funny.framework.codegen.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * FileRenameUtil 测试类
 */
public class FileRenameUtilTest {

    public static void main(String[] args) {
        try {
            // 创建测试目录和文件
            String testDir = createTestDirectory();
            
            System.out.println("=== 测试开始 ===");
            System.out.println("测试目录: " + testDir);
            
            // 测试1: 检查需要重命名的文件
            System.out.println("\n=== 测试1: 检查需要重命名的文件 ===");
            List<String> filesNeedingRename = FileRenameUtil.getFilesNeedingFtlSuffix(testDir, true);
            System.out.println("需要重命名的文件数量: " + filesNeedingRename.size());
            for (String file : filesNeedingRename) {
                System.out.println("  - " + file);
            }
            
            // 测试2: 为非递归模式重命名文件
            System.out.println("\n=== 测试2: 非递归重命名文件 ===");
            List<String> renamedFiles1 = FileRenameUtil.addFtlSuffixToFiles(testDir, false);
            System.out.println("重命名成功的文件数量: " + renamedFiles1.size());
            for (String file : renamedFiles1) {
                System.out.println("  - " + file);
            }
            
            // 测试3: 为递归模式重命名文件
            System.out.println("\n=== 测试3: 递归重命名文件 ===");
            List<String> renamedFiles2 = FileRenameUtil.addFtlSuffixToFiles(testDir, true);
            System.out.println("重命名成功的文件数量: " + renamedFiles2.size());
            for (String file : renamedFiles2) {
                System.out.println("  - " + file);
            }
            
            // 测试4: 恢复文件名
            System.out.println("\n=== 测试4: 恢复文件名 ===");
            for (String filePath : renamedFiles2) {
                String restoredPath = FileRenameUtil.removeFtlSuffix(filePath);
                System.out.println("恢复文件: " + filePath + " -> " + restoredPath);
            }
            
            System.out.println("\n=== 测试完成 ===");
            
            // 清理测试文件
            cleanupTestDirectory(testDir);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建测试目录和文件
     */
    private static String createTestDirectory() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String testDir = Paths.get(tempDir, "file_rename_test").toString();
        
        // 创建测试目录
        Files.createDirectories(Paths.get(testDir));
        
        // 创建测试文件
        createTestFile(testDir, "test1.txt", "测试文件1内容");
        createTestFile(testDir, "test2.java", "测试文件2内容");
        createTestFile(testDir, "test3.xml", "测试文件3内容");
        
        // 创建子目录和文件
        String subDir = Paths.get(testDir, "subdir").toString();
        Files.createDirectories(Paths.get(subDir));
        createTestFile(subDir, "subfile1.properties", "子目录文件1内容");
        createTestFile(subDir, "subfile2.yaml", "子目录文件2内容");
        
        // 创建一个已经是.ftl后缀的文件
        createTestFile(testDir, "already.ftl", "已经是.ftl后缀的文件");
        
        // 创建.DS_Store文件（模拟macOS系统文件）
        createTestFile(testDir, ".DS_Store", "macOS系统文件");
        createTestFile(subDir, ".DS_Store", "子目录中的macOS系统文件");
        
        return testDir;
    }
    
    /**
     * 创建测试文件
     */
    private static void createTestFile(String directory, String fileName, String content) throws IOException {
        File file = new File(directory, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
    
    /**
     * 清理测试目录
     */
    private static void cleanupTestDirectory(String testDir) throws IOException {
        Path path = Paths.get(testDir);
        if (Files.exists(path)) {
            Files.walk(path)
                 .sorted((a, b) -> -a.compareTo(b)) // 从子文件到父目录排序
                 .forEach(p -> {
                     try {
                         Files.delete(p);
                     } catch (IOException e) {
                         System.err.println("删除文件失败: " + p);
                     }
                 });
        }
    }
}