package com.xhonell.common.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * program: BaseServer
 * ClassName TreeBuilderUtil
 * description:
 * author: xhonell
 * create: 2025年11月02日22时37分
 * Version 1.0
 **/
public class TreeBuilderUtil {

    /**
     * 将扁平列表构建为树。
     *
     * @param items           原始扁平列表
     * @param idGetter        从 T 中获取 id (返回 ID)
     * @param parentIdGetter  从 T 中获取 parentId (返回 ID 或 null)
     * @param childrenSetter  将子节点列表设置回父节点的方法 (BiConsumer<T, List<T>>)
     * @param <T>             节点类型
     * @param <ID>            id 类型
     * @return 根节点列表（可能有多个根）
     */
    public static <T, ID> List<T> buildTree(
            List<T> items,
            Function<T, ID> idGetter,
            Function<T, ID> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter
    ) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        // id -> node 映射
        Map<ID, T> idNodeMap = items.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(idGetter, Function.identity(), (a, b) -> a));

        // 使用 Map 存储 children 列表，避免反射和字段名依赖
        Map<ID, List<T>> childrenMap = new HashMap<>();

        List<T> roots = new ArrayList<>(items.size());

        // 防止简单自环导致无限循环（记录处理过的 id）
        Set<ID> attached = new HashSet<>();

        for (T item : items) {
            ID parentId = parentIdGetter.apply(item);
            ID id = idGetter.apply(item);

            // 如果 parentId 不存在或 parentId == id -> 视为根节点
            if (parentId == null || id == null || parentId.equals(id) || !idNodeMap.containsKey(parentId)) {
                roots.add(item);
                attached.add(id);
            } else {
                T parent = idNodeMap.get(parentId);
                if (parent == null) {
                    roots.add(item);
                    attached.add(id);
                } else {
                    // 将 item 添加到 parent 的 children 列表
                    List<T> children = childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>());
                    children.add(item);
                    attached.add(id);
                }
            }
        }

        // 处理孤立节点（未被挂载也不是根的） - 以防出现环或未被处理的情况
        for (T item : items) {
            ID id = idGetter.apply(item);
            if (!attached.contains(id)) {
                roots.add(item);
            }
        }

        // 将 childrenMap 中的 children 设置回对应的节点
        childrenMap.forEach((parentId, children) -> {
            T parent = idNodeMap.get(parentId);
            if (parent != null) {
                childrenSetter.accept(parent, children);
            }
        });

        // 为没有子节点的根节点设置空列表
        roots.forEach(root -> {
            ID rootId = idGetter.apply(root);
            if (!childrenMap.containsKey(rootId)) {
                childrenSetter.accept(root, new ArrayList<>());
            }
        });

        return roots;
    }

    /**
     * 通过 childrenSetter 获取节点的 children 引用（假定已通过 childrenSetter 初始化为空 list）。
     * 该方法依赖于 childrenSetter 的实现是以 List 为 children 字段类型。
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> getChildren(T node, BiConsumer<T, List<T>> childrenSetter) {
        // 尝试反射读取可能的 children 字段名
        String[] possibleFieldNames = {"children", "childrenPermission", "childList", "childrenList"};

        for (String fieldName : possibleFieldNames) {
            try {
                java.lang.reflect.Field f = node.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                Object val = f.get(node);
                if (val instanceof List) {
                    return (List<T>) val;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }

        // 如果反射都失败了，返回一个新列表（但这可能会导致数据丢失，应该尽量避免）
        // 注意：这种情况不应该发生，因为 buildTree 开始时已经初始化了所有节点的 children
        return new ArrayList<>();
    }
}
