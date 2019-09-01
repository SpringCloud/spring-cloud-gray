package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import cn.springcloud.gray.server.dao.mapper.UserMapper;
import cn.springcloud.gray.server.dao.model.UserDO;
import cn.springcloud.gray.server.dao.repository.UserRepository;
import cn.springcloud.gray.server.module.user.domain.UserInfo;
import cn.springcloud.gray.server.module.user.domain.UserQuery;
import cn.springcloud.gray.server.utils.PaginationUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserService extends AbstraceCRUDService<UserInfo, UserRepository, UserDO, String> {

    private static final String PASSWORD_SLAT = "!@#DFS3df";

    @Autowired
    private UserRepository repository;
    @Autowired
    private UserMapper userMapper;


    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    @Override
    protected ModelMapper<UserInfo, UserDO> getModelMapper() {
        return userMapper;
    }

    public Page<UserInfo> query(UserQuery userQuery, Pageable pageable) {

        Specification<UserDO> specification = new Specification<UserDO>() {

            @Override
            public Predicate toPredicate(Root<UserDO> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList();
                if(userQuery.getStatus()!=UserQuery.STATUS_ALL){
                    predicates.add(cb.equal(root.get("status").as(Integer.class), userQuery.getStatus()));
                }


                List<Predicate> orPredicates = new ArrayList();
                if(StringUtils.isNotEmpty(userQuery.getKey())){
                    predicates.add(cb.or(cb.like(root.get("account").as(String.class),
                            "%" + userQuery.getKey() + "%"),
                            cb.like(root.get("name").as(String.class),
                                    "%" + userQuery.getKey() + "%")));
                }

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return query.getRestriction();
//                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Page<UserDO> doPage = repository.findAll(specification, pageable);
        return PaginationUtils.convert(pageable, doPage, getModelMapper());
    }

    public void resetPassword(String userId, String password) {
        UserDO userDO = findOne(userId);
        if(userDO==null){
            return;
        }
        userDO.setPassword(markPassword(password));
        save(userDO);
    }

    private String markPassword(String password){
        return DigestUtils.md5Hex(password + PASSWORD_SLAT);
    }

    public UserDO findDOByAccount(String account){
        return repository.findByAccount(account);
    }

    public UserInfo login(String account, String password) {
        UserDO userDO = findDOByAccount(account);
        if(userDO==null || Objects.equals(userDO.getStatus(), UserInfo.STATUS_DISABLED)){
            return null;
        }
        if(!StringUtils.equals(markPassword(password), userDO.getPassword())){
            return null;
        }
        return do2model(userDO);
    }

    public UserInfo register(UserInfo userInfo, String password) {
        if(findDOByAccount(userInfo.getAccount())!=null){
            return null;
        }
        UserDO userDO = model2do(userInfo);
        userDO.setUserId(userDO.getAccount());
        userDO.setPassword(markPassword(password));
        userDO.setCreateTime(new Date());
        userDO.setOperateTime(userDO.getCreateTime());
        return do2model(save(userDO));
    }

    public void updateUserStatus(String userId, int statusDisabled) {
        UserDO userDO = findOne(userId);
        if(userDO==null){
            return;
        }
        userDO.setStatus(statusDisabled);
        save(userDO);
    }

    public boolean resetPassword(String userId, String oldPassword, String newPassword) {
        UserDO userDO = findOne(userId);
        if(userDO==null){
            return false;
        }
        if(!StringUtils.equals(markPassword(oldPassword), userDO.getPassword())){
            return false;
        }

        userDO.setPassword(markPassword(newPassword));
        save(userDO);
        return true;
    }

    public UserInfo updateUserInfo(UserInfo userInfo) {
        UserDO userDO = findOne(userInfo.getUserId());
        if(userDO==null){
            return null;
        }
        model2do(userInfo, userDO);
        userDO.setOperateTime(new Date());
        return do2model(save(userDO));
    }
}
