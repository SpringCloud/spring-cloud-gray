package cn.springcloud.gray.server.dao.repository;

import cn.springcloud.gray.server.dao.model.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserDO, String>, JpaSpecificationExecutor<UserDO> {


    UserDO findByAccount(String account);
}
