package com.socialmedia.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import com.socialmedia.socialmedia.domain.Resource;
public interface ResourceRepository  extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource>{
    
}
