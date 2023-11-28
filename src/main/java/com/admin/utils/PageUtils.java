package com.admin.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页
 * @author Xqf
 * @version 1.0
 */
public class PageUtils {


    /**
     * 分页函数
     * @author pochettino
     * @param currentPage   当前页数
     * @param pageSize  每一页的数据条数
     * @param list  要进行分页的数据列表
     * @return  当前页要展示的数据
     */
//    public Page getPages(Integer currentPage, Integer pageSize, List list) {
//        Page page = new PageImpl(list);
//        if(list==null){
//            return  null;
//        }
//        int size = list.size();
//
//        if(pageSize > size) {
//            pageSize = size;
//        }
//        if (pageSize!=0){
//            // 求出最大页数，防止currentPage越界
//            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;
//
//            if(currentPage > maxPage) {
//                currentPage = maxPage;
//            }
//        }
//        // 当前页第一条数据的下标
//        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
//
//        List pageList = new ArrayList();
//
//        // 将当前页的数据放进pageList
//        for(int i = 0; i < pageSize && curIdx + i < size; i++) {
//            pageList.add(list.get(curIdx + i));
//        }
//
////        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
//        return page;
//    }
    public static <T> List<T> getPages(List<T> list, int currentPage, int pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        int totalSize = list.size();
        int totalPages = (int) Math.ceil((double) totalSize / pageSize);

        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        return list.stream()
                .skip((currentPage - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}
