package com.novax.common.core.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 当前页码（从1开始）
   */
  private Integer page;

  /**
   * 每页大小
   */
  private Integer pageSize;

  /**
   * 总记录数
   */
  private Long total;

  /**
   * 总页数
   */
  private Integer totalPages;

  /**
   * 数据列表
   */
  private List<T> records;

  /**
   * 是否有上一页
   */
  private Boolean hasPrevious;

  /**
   * 是否有下一页
   */
  private Boolean hasNext;

  public PageResult(Integer page, Integer pageSize, Long total, List<T> records) {
    this.page = page;
    this.pageSize = pageSize;
    this.total = total;
    this.records = records;
    this.totalPages = (int) Math.ceil((double) total / pageSize);
    this.hasPrevious = page > 1;
    this.hasNext = page < totalPages;
  }

  /**
   * 构建分页结果
   */
  public static <T> PageResult<T> of(Integer page, Integer pageSize, Long total, List<T> records) {
    return new PageResult<>(page, pageSize, total, records);
  }

  /**
   * 构建空分页结果
   */
  public static <T> PageResult<T> empty(Integer page, Integer pageSize) {
    return new PageResult<>(page, pageSize, 0L, List.of());
  }
}
