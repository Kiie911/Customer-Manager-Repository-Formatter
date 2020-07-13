package com.hiep.repository;

import com.hiep.model.Province;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface ProvinceRepository extends PagingAndSortingRepository<Province,Long> {
}
