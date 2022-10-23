package com.cesar.sharing.service;

import com.cesar.sharing.entity.query.SearchRequest;
import org.springframework.data.domain.Page;

/**
 * <p>
 * This interface represents the service contract for provide a generic search
 * mechanism out of the box.
 * @param <B> Represents the final Entity class, any one present inside
 *            this path com.department.entity.*
 */
public interface IServiceSearch<B> {

    Page<B> search(SearchRequest request);
}
