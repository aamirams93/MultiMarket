//package com.srbru.repo;
//
//
//
//import java.util.List;
//
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.srbru.entity.PackageValue;
//
//
//@Repository
//public interface PackRepo extends CrudRepository<PackageValue, Integer> {
//
//    List<PackageValue> findPackagesByCustomerEmail(@Param("email") String email);
//    List<PackageValue>  findByPackStatus(@Param("packStatus") char packStatus);
//    
//    
//
//}
//
