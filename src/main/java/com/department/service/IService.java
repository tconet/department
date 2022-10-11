package com.department.service;

import com.department.entity.CostCenter;
import com.department.entity.query.SearchRequest;
import org.springframework.data.domain.Page;

/**
 * <p>
 * This interface represents the service contract for provide a generic search
 * mechanism out of the box.
 * @param <B> Represents the final Entity class, any one present inside
 *            this path com.department.entity.*
 * @param <R> The final repository implementation.
 */
public interface IService <B> {

    public Page<B> search(SearchRequest request);
}
