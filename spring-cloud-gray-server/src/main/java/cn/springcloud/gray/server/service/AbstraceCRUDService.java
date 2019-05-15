package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public abstract class AbstraceCRUDService<MODEL, REPOSITORY extends JpaRepository<T, ID>, T, ID extends Serializable> {


    protected abstract REPOSITORY getRepository();

    protected abstract ModelMapper<MODEL, T> getModelMapper();

    protected void save(T entity) {
        getRepository().save(entity);
    }

    public void saveModel(MODEL entity) {
        save(getModelMapper().model2do(entity));
    }

    protected List<T> save(Iterable<T> entities) {
        return getRepository().save(entities);
    }

    public List<MODEL> saveModels(Iterable<MODEL> entities) {
        return getModelMapper().dos2models(save(getModelMapper().models2dos(entities)));
    }

    protected T findOne(ID id) {
        return getRepository().findOne(id);
    }

    public MODEL findOneModel(ID id) {
        return getModelMapper().do2model(findOne(id));
    }


    public boolean exists(ID id) {
        return getRepository().exists(id);
    }

    protected List<T> findAll() {
        return getRepository().findAll();
    }

    protected List<T> findAll(Iterable<ID> ids) {
        return getRepository().findAll(ids);
    }

    public List<MODEL> findAllModel() {
        return getModelMapper().dos2models(findAll());
    }

    public List<MODEL> findAllModel(Iterable<ID> ids) {
        return getModelMapper().dos2models(findAll(ids));
    }


    public long count() {
        return getRepository().count();
    }

    public void delete(ID id) {
        getRepository().delete(id);
    }


    protected void delete(Iterable<? extends T> entities) {
        getRepository().delete(entities);
    }

    public void deleteModel(Iterable<MODEL> models) {
        delete(getModelMapper().models2dos(models));
    }

    public void deleteAll() {
        getRepository().deleteAll();
    }


}
