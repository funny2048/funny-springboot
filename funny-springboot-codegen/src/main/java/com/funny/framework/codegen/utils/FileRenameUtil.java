package com.funny.framework.codegen.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件重命名工具类
 * 用于读取文件夹路径并遍历文件，为所有文件名增加.ftl后缀
 */
public class FileRenameUtil {

    /**
     * 读取指定文件夹路径并遍历文件，为所有文件名增加.ftl后缀
     *
     * @param directoryPath 文件夹路径
     * @return 重命名成功的文件列表
     * @throws IOException 如果文件操作失败
     */
    public static List<String> addFtlSuffixToFiles(String directoryPath) throws IOException {
        return addFtlSuffixToFiles(directoryPath, false);
    }

    /**
     * 读取指定文件夹路径并遍历文件，为所有文件名增加.ftl后缀
     *
     * @param directoryPath 文件夹路径
     * @param recursive     是否递归处理子目录
     * @return 重命名成功的文件列表
     * @throws IOException 如果文件操作失败
     */
    public static List<String> addFtlSuffixToFiles(String directoryPath, boolean recursive) throws IOException {
        List<String> renamedFiles = new ArrayList<>();
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("指定的路径不存在或不是目录: " + directoryPath);
        }
        
        processDirectory(directory, recursive, renamedFiles);
        return renamedFiles;
    }

    /**
     * 处理目录中的文件
     *
     * @param directory     目录
     * @param recursive     是否递归处理子目录
     * @param renamedFiles  重命名成功的文件列表
     * @throws IOException 如果文件操作失败
     */
    private static void processDirectory(File directory, boolean recursive, List<String> renamedFiles) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (recursive) {
                    processDirectory(file, true, renamedFiles);
                }
            } else {
                // 检查是否为.DS_Store文件，如果是则删除
                if (file.getName().endsWith(".DS_Store") || file.getName().endsWith(".DS_Store.ftl")) {
                    Files.delete(file.toPath());
                    System.out.println("已删除文件: " + file.getAbsolutePath());
                    continue;
                }
                
                // 处理文件
                if (!file.getName().endsWith(".ftl")) {
                    String newFileName = file.getName() + ".ftl";
                    Path sourcePath = file.toPath();
                    Path targetPath = Paths.get(file.getParent(), newFileName);
                    
                    // 重命名文件
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    renamedFiles.add(targetPath.toString());
                }
            }
        }
    }

    /**
     * 批量重命名文件，为指定文件列表增加.ftl后缀
     *
     * @param filePaths 文件路径列表
     * @return 重命名成功的文件列表
     * @throws IOException 如果文件操作失败
     */
    public static List<String> addFtlSuffixToFileList(List<String> filePaths) throws IOException {
        List<String> renamedFiles = new ArrayList<>();
        
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                // 检查是否为.DS_Store文件，如果是则删除
                if (file.getName().endsWith(".DS_Store")) {
                    Files.delete(file.toPath());
                    System.out.println("已删除文件: " + file.getAbsolutePath());
                    continue;
                }
                
                if (!file.getName().endsWith(".ftl")) {
                    String newFileName = file.getName() + ".ftl";
                    Path sourcePath = file.toPath();
                    Path targetPath = Paths.get(file.getParent(), newFileName);
                    
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    renamedFiles.add(targetPath.toString());
                }
            }
        }
        
        return renamedFiles;
    }

    /**
     * 检查文件是否需要添加.ftl后缀
     *
     * @param filePath 文件路径
     * @return 是否需要添加.ftl后缀
     */
    public static boolean needsFtlSuffix(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && !file.getName().endsWith(".ftl");
    }

    /**
     * 获取目录中需要添加.ftl后缀的文件列表
     *
     * @param directoryPath 目录路径
     * @param recursive     是否递归检查子目录
     * @return 需要重命名的文件列表
     */
    public static List<String> getFilesNeedingFtlSuffix(String directoryPath, boolean recursive) {
        List<String> filesNeedingRename = new ArrayList<>();
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return filesNeedingRename;
        }
        
        collectFilesNeedingRename(directory, recursive, filesNeedingRename);
        return filesNeedingRename;
    }

    /**
     * 收集需要重命名的文件
     *
     * @param directory             目录
     * @param recursive             是否递归检查子目录
     * @param filesNeedingRename    需要重命名的文件列表
     */
    private static void collectFilesNeedingRename(File directory, boolean recursive, List<String> filesNeedingRename) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (recursive) {
                    collectFilesNeedingRename(file, true, filesNeedingRename);
                }
            } else {
                // 跳过.DS_Store文件
                if (file.getName().endsWith(".DS_Store")) {
                    continue;
                }
                
                if (!file.getName().endsWith(".ftl")) {
                    filesNeedingRename.add(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 恢复文件名为原始名称（移除.ftl后缀）
     *
     * @param filePath 文件路径
     * @return 恢复后的文件路径
     * @throws IOException 如果文件操作失败
     */
    public static String removeFtlSuffix(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("文件不存在或不是文件: " + filePath);
        }
        
        String fileName = file.getName();
        if (!fileName.endsWith(".ftl")) {
            throw new IllegalArgumentException("文件不是.ftl后缀: " + filePath);
        }
        
        String originalName = fileName.substring(0, fileName.length() - 4);
        Path sourcePath = file.toPath();
        Path targetPath = Paths.get(file.getParent(), originalName);
        
        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toString();
    }


    public static void main(String[] args) {
        try {
            // 创建测试目录和文件
            String testDir = "/Users/fangli/Documents/work_code/framework/framework-codegen/src/main/resources/templates/springboot3/monolith";
            System.out.println("=== 测试开始 ===");
            System.out.println("测试目录: " + testDir);
            // 测试1: 检查需要重命名的文件
            System.out.println("\n=== 测试1: 检查需要重命名的文件 ===");
            List<String> filesNeedingRename = FileRenameUtil.addFtlSuffixToFiles(testDir, true);
            System.out.println("需要重命名的文件数量: " + filesNeedingRename.size());
            for (String file : filesNeedingRename) {
                System.out.println("  - " + file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}