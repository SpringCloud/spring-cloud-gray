package cn.springcloud.gray.server.service;

import cn.springcloud.gray.server.dao.mapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public abstract class AbstraceCRUDService<MODEL, REPOSITORY extends JpaRepository<T, ID>, T, ID extends Serializable> {


    protected abstract REPOSITORY getRepository();

    protected abstract ModelMapper<MODEL, T> getModelMapper();

    protected void save(T entity) {
        getRepository().save(entity);
    }

    @Transactional
    public void saveModel(MODEL entity) {
        T t = model2do(entity);
        save(t);

    }

    protected Iterable<T> save(Iterable<T> entities) {
        return getRepository().save(entities);
    }

    public List<MODEL> saveModels(Iterable<MODEL> entities) {
        return dos2models(save(models2dos(entities)));
    }

    protected T findOne(ID id) {
        return getRepository().findOne(id);
    }

    public MODEL findOneModel(ID id) {
        return do2model(findOne(id));
    }


    public boolean exists(ID id) {
        return getRepository().exists(id);
    }

    protected Iterable<T> findAll() {
        return getRepository().findAll();
    }

    protected Iterable<T> findAll(Iterable<ID> ids) {
        return getRepository().findAll(ids);
    }

    public List<MODEL> findAllModel() {
        return dos2models(findAll());
    }

    public List<MODEL> findAllModel(Iterable<ID> ids) {
        return dos2models(findAll(ids));
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
        delete(models2dos(models));
    }

    public void deleteAll() {
        getRepository().deleteAll();
    }


    protected List<MODEL> dos2models(Iterable<T> dos) {
        return getModelMapper().dos2models(dos);
    }

    protected List<T> models2dos(Iterable<MODEL> models) {
        return getModelMapper().models2dos(models);
    }


    protected MODEL do2model(T t) {
        return getModelMapper().do2model(t);
    }


    protected T model2do(MODEL model) {
        return getModelMapper().model2do(model);
    }

    protected void do2model(T t, MODEL model) {
        getModelMapper().do2model(t, model);
    }

    protected void model2do(MODEL model, T t) {
        getModelMapper().model2do(model, t);
    }

}
