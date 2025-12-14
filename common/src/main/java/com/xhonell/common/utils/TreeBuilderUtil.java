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

        // 初始化所有节点的 children 容器（空列表）
        items.forEach(item -> childrenSetter.accept(item, new ArrayList<>()));

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
                    // 将 item 添加到 parent 的 children 中
                    List<T> children = getChildren(parent, childrenSetter);
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

        return roots;
    }

    /**
     * 通过 childrenSetter 获取节点的 children 引用（假定已通过 childrenSetter 初始化为空 list）。
     * 该方法依赖于 childrenSetter 的实现是以 List 为 children 字段类型。
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> getChildren(T node, BiConsumer<T, List<T>> childrenSetter) {
        // 通过临时 list 设置后再读取（反射会更通用，但为简单起见要求 childrenSetter 先前已设置空 List）
        // 这里的实现假定 childrenSetter.accept(node, list) 在 buildTree 开始时已被调用一次初始化。
        // 所以直接尝试反射读取名为 "children" 的字段会更稳，但为了泛用性我们采用如下技巧：
        try {
            // 反射尝试读取 children 字段（常见命名为 children）
            java.lang.reflect.Field f = node.getClass().getDeclaredField("children");
            f.setAccessible(true);
            Object val = f.get(node);
            if (val instanceof List) {
                return (List<T>) val;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }

        // 如果未能通过 reflection 读取 children 字段（可能字段名不同），则尝试以下：
        // 创建一个临时 list，调用 childrenSetter 将其关联到 node，然后再返回这个 list。
        List<T> tmp = new ArrayList<>();
        childrenSetter.accept(node, tmp);
        return tmp;
    }
}
