package com.novax.common.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQuery implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 当前页码（从1开始）
   */
  @Min(value = 1, message = "Page must be greater than 0")
  private Integer page = 1;

  /**
   * 每页大小
   */
  @Min(value = 1, message = "Page size must be greater than 0")
  @Max(value = 100, message = "Page size must not exceed 100")
  private Integer pageSize = 10;

  /**
   * 排序字段
   */
  private String sortBy;

  /**
   * 排序方向（asc/desc）
   */
  private String sortOrder = "desc";

  /**
   * 获取偏移量（用于数据库查询）
   */
  public Integer getOffset() {
    return (page - 1) * pageSize;
  }

  /**
   * 获取限制数量（用于数据库查询）
   */
  public Integer getLimit() {
    return pageSize;
  }

  /**
   * 是否降序
   */
  public boolean isDescending() {
    return "desc".equalsIgnoreCase(sortOrder);
  }
}
