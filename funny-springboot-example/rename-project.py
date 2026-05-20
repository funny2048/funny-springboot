#!/usr/bin/env python3
"""
项目重命名脚本 — 将标准 example 项目转换为业务项目

用法: python3 rename-project.py <新的根artifactId>
示例: python3 rename-project.py funny-lure

效果:
  1. 根 artifactId:  funny-springboot-example → funny-lure
  2. 子模块目录/名称: example-* → lure-*
  3. pom.xml 依赖引用同步更新
  4. Java 包名: com.funny.example → com.funny.lure
  5. 自动清理 target/ 目录
"""

import os
import re
import shutil
import sys
from pathlib import Path

EXCLUDED_DIRS = {'.git', 'target', '.idea', 'node_modules', '__pycache__'}
TEXT_EXTENSIONS = {'.xml', '.yml', '.yaml', '.properties', '.java', '.md', '.txt', '.factory'}


def find_text_files(project_root: Path) -> list[Path]:
    """扫描所有需要文本替换的文件"""
    files = []
    for root, dirs, filenames in os.walk(project_root):
        dirs[:] = [d for d in dirs if d not in EXCLUDED_DIRS]
        for f in filenames:
            p = Path(root) / f
            if p.suffix in TEXT_EXTENSIONS:
                files.append(p)
    return files


def replace_in_file(filepath: Path, replacements: list[tuple[str, str]]) -> bool:
    """在文件中执行文本替换，返回是否发生了修改"""
    try:
        content = filepath.read_text(encoding='utf-8')
    except (UnicodeDecodeError, PermissionError):
        return False
    original = content
    for old, new in replacements:
        content = content.replace(old, new)
    if content != original:
        filepath.write_text(content, encoding='utf-8')
        return True
    return False


def rename_package_dirs(project_root: Path, old_short: str, new_short: str):
    """重命名各模块下的 Java 包目录: com/funny/{old} → com/funny/{new}"""
    for src_tree in ('main', 'test'):
        for module_dir in project_root.iterdir():
            if not module_dir.is_dir() or module_dir.name in EXCLUDED_DIRS:
                continue
            java_base = module_dir / 'src' / src_tree / 'java' / 'com' / 'funny'
            if not java_base.exists():
                continue
            old_pkg = java_base / old_short
            new_pkg = java_base / new_short
            if not old_pkg.exists():
                continue
            if new_pkg.exists():
                # 目标已存在，合并内容
                for item in old_pkg.rglob('*'):
                    if item.is_file():
                        rel = item.relative_to(old_pkg)
                        dest = new_pkg / rel
                        dest.parent.mkdir(parents=True, exist_ok=True)
                        shutil.move(str(item), str(dest))
                shutil.rmtree(old_pkg)
                print(f'  [合并包] {old_pkg.relative_to(project_root)} → {new_pkg.relative_to(project_root)}')
            else:
                old_pkg.rename(new_pkg)
                print(f'  [重命名] {old_pkg.relative_to(project_root)} → {new_pkg.relative_to(project_root)}')


def rename_module_dirs(project_root: Path, old_short: str, new_short: str):
    """重命名模块目录: example-client → lure-client"""
    for module_dir in sorted(project_root.iterdir()):
        if not module_dir.is_dir() or module_dir.name in EXCLUDED_DIRS:
            continue
        if module_dir.name.startswith(f'{old_short}-'):
            new_name = new_short + module_dir.name[len(old_short):]
            new_path = project_root / new_name
            module_dir.rename(new_path)
            print(f'  [模块] {module_dir.name} → {new_name}')


def clean_target_dirs(project_root: Path):
    """删除所有 target/ 目录"""
    for root, dirs, _ in os.walk(project_root):
        if 'target' in dirs:
            target = Path(root) / 'target'
            shutil.rmtree(target, ignore_errors=True)
            print(f'  [清理] {target.relative_to(project_root)}')


def main():
    if len(sys.argv) != 2:
        print('用法: python3 rename-project.py <新的根artifactId>')
        print('示例: python3 rename-project.py funny-lure')
        sys.exit(1)

    new_root_artifact_id = sys.argv[1]
    project_root = Path(__file__).resolve().parent

    # ── 读取当前根 artifactId ──
    root_pom = project_root / 'pom.xml'
    if not root_pom.exists():
        print(f'错误: {project_root} 下未找到 pom.xml')
        sys.exit(1)

    content = root_pom.read_text(encoding='utf-8')
    # 跳过 <parent> 块内的 artifactId，取项目自身的
    match = re.search(r'</parent>.*?<artifactId>([^<]+)</artifactId>', content, re.DOTALL)
    if not match:
        # 没有 parent 的情况，直接取第一个 artifactId
        match = re.search(r'<artifactId>([^<]+)</artifactId>', content)
    if not match:
        print('错误: 无法从 pom.xml 解析 artifactId')
        sys.exit(1)
    old_root_artifact_id = match.group(1)

    # ── 提取短名称 ──
    old_short = old_root_artifact_id.split('-')[-1]  # example
    new_short = new_root_artifact_id.split('-')[-1]   # lure

    if old_short == new_short and old_root_artifact_id == new_root_artifact_id:
        print(f'错误: 新名称与旧名称完全相同')
        sys.exit(1)

    # ── 构建替换规则（长串优先，避免部分匹配）──
    replacements = [
        (old_root_artifact_id, new_root_artifact_id),
        (f'{old_short}-client', f'{new_short}-client'),
        (f'{old_short}-service', f'{new_short}-service'),
        (f'{old_short}-dao', f'{new_short}-dao'),
        (f'{old_short}-web', f'{new_short}-web'),
        (f'com.funny.{old_short}', f'com.funny.{new_short}'),
    ]

    # ── 显示计划 ──
    print('=' * 50)
    print('项目重命名计划')
    print('=' * 50)
    print(f'  项目根目录:     {project_root}')
    print(f'  根 artifactId:  {old_root_artifact_id} → {new_root_artifact_id}')
    print(f'  子模块前缀:     {old_short}-* → {new_short}-*')
    print(f'  Java 包名:      com.funny.{old_short} → com.funny.{new_short}')
    print()
    print('替换规则:')
    for old, new in replacements:
        print(f'  {old} → {new}')
    print()

    # ── 确认 ──
    confirm = input('确认执行？(y/N): ').strip().lower()
    if confirm != 'y':
        print('已取消')
        sys.exit(0)

    # ── Phase 1: 文本替换 ──
    print('\n[1/4] 执行文本替换...')
    files = find_text_files(project_root)
    modified = 0
    for f in files:
        if replace_in_file(f, replacements):
            modified += 1
            print(f'  ✓ {f.relative_to(project_root)}')
    print(f'  共修改 {modified} 个文件')

    # ── Phase 2: 重命名 Java 包目录 ──
    print('\n[2/4] 重命名 Java 包目录...')
    rename_package_dirs(project_root, old_short, new_short)

    # ── Phase 3: 重命名模块目录 ──
    print('\n[3/4] 重命名模块目录...')
    rename_module_dirs(project_root, old_short, new_short)

    # ── Phase 4: 清理 target ──
    print('\n[4/4] 清理 target/ 目录...')
    clean_target_dirs(project_root)

    print()
    print('=' * 50)
    print('重命名完成')
    print('=' * 50)
    print(f'  根 artifactId:  {old_root_artifact_id} → {new_root_artifact_id}')
    print(f'  子模块前缀:     {old_short} → {new_short}')
    print(f'  Java 包名:      com.funny.{old_short} → com.funny.{new_short}')
    print()
    print('后续步骤:')
    print('  1. mvn clean compile   # 验证编译')
    print('  2. git add . && git commit -m "refactor: rename project"')


if __name__ == '__main__':
    main()
