package cn.springcloud.gray.retriever;

import cn.springcloud.gray.utils.GenericUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.ListUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author saleson
 * @date 2020-01-31 16:14
 */
public class GenericRetriever<FUNC> {

    private final Map<RetrieverCacheKey, FuncRetriever> retrieverCache = new ConcurrentHashMap<>(64);
//    private com.github.benmanes.caffeine.cache.Cache<RetrieverCacheKey, FuncRetriever> retrieverCache = Caffeine.newBuilder()
//            .initialCapacity(20)
//            .softValues()
//            .recordStats()
//            .build();

    private List<FUNC> funcList;
    private Class<?> genericDefineSuperCls;
    private int genericIndex = 0;


    public GenericRetriever(Class<?> genericDefineSuperCls) {
        this(new ArrayList<>(), genericDefineSuperCls);
    }

    public GenericRetriever(List<FUNC> funcList, Class<?> genericDefineSuperCls) {
        this(funcList, genericDefineSuperCls, 0);
    }

    public GenericRetriever(List<FUNC> funcList, Class<?> genericDefineSuperCls, int genericIndex) {
        this.funcList = new ArrayList<>(funcList);
        this.genericDefineSuperCls = genericDefineSuperCls;
        this.genericIndex = genericIndex;
    }


    /**
     * 检索泛型符合该对象的功能
     *
     * @param genericTarget
     * @return
     */
    public FUNC retrieve(Object genericTarget) {
        List<FUNC> funcs = retrieveFunctions(genericTarget);
        if (funcs.isEmpty()) {
            return null;
        }
        return funcs.get(0);
    }


    /**
     * 检索泛型符合该类型的功能
     *
     * @param genericTargetCls
     * @return
     */
    public FUNC retrieve(Class genericTargetCls) {
        List<FUNC> funcs = retrieveFunctions(genericTargetCls);
        if (funcs.isEmpty()) {
            return null;
        }
        return funcs.get(0);
    }


    /**
     * 检索泛型符合该对象的功能列表
     *
     * @param genericTarget
     * @return
     */
    public List<FUNC> retrieveFunctions(Object genericTarget) {
        RetrieverCacheKey cacheKey = createRetrieverCacheKey(genericTarget);
        return retrieveFunctions(cacheKey, genericTarget.getClass());
    }


    /**
     * 检索泛型符合该类型的功能列表
     *
     * @param genericTargetCls
     * @return
     */
    public List<FUNC> retrieveFunctions(Class genericTargetCls) {
        RetrieverCacheKey cacheKey = createRetrieverCacheKey(genericTargetCls);
        return retrieveFunctions(cacheKey, genericTargetCls);
    }


    public void addFunction(FUNC func) {
        funcList.add(func);
        invalidateCache();
    }

    public void addFunctions(FUNC... funcs) {
        addFunctions(Arrays.asList(funcs));
    }

    public void addFunctions(Collection<FUNC> funcs) {
        funcList.addAll(funcs);
        invalidateCache();
    }

    public void invalidateCache() {
        retrieverCache.clear();
    }


    /**
     * 判断功能对象是否符合对象类型
     *
     * @param function
     * @param genericTargetCls
     * @return
     */
    protected boolean supportsEvent(FUNC function, Class<?> genericTargetCls) {
        return GenericUtils.match(function, genericDefineSuperCls, genericTargetCls, genericIndex);
    }


    /**
     * 创建缓存key
     *
     * @param genericTarget
     * @return
     */
    protected RetrieverCacheKey createRetrieverCacheKey(Object genericTarget) {
        return createRetrieverCacheKey(genericTarget.getClass());
    }


    /**
     * 创建缓存key
     *
     * @param genericTargetCls
     * @return
     */
    protected RetrieverCacheKey createRetrieverCacheKey(Class genericTargetCls) {
        return new RetrieverCacheKey(genericTargetCls);
    }

    private List<FUNC> retrieveFunctions(RetrieverCacheKey cacheKey, Class genericTargetCls) {
        FuncRetriever funcRetriever = retrieverCache.get(cacheKey);
        if (Objects.isNull(funcRetriever)) {
//            synchronized (this) {
//            funcRetriever = retrieverCache.get(cacheKey);
//            if (!Objects.isNull(funcRetriever)) {
//                return funcRetriever.getFunctions();
//            }
            funcRetriever = new FuncRetriever();
            List<FUNC> listeners =
                    retrieveGrayEventListeners(genericTargetCls, funcRetriever);
            retrieverCache.putIfAbsent(cacheKey, funcRetriever);
            return listeners;
        }
//        }
        return funcRetriever.getFunctions();
    }


    private List<FUNC> retrieveGrayEventListeners(Class<?> genericTargetCls, FuncRetriever funcRetriever) {
        List<FUNC> funcs = new ArrayList<>();
        for (FUNC func : funcList) {
            if (supportsEvent(func, genericTargetCls)) {
                funcs.add(func);
            }
        }
        AnnotationAwareOrderComparator.sort(funcs);
        funcRetriever.addAllFuncs(funcs);
        return funcs;
    }


    @AllArgsConstructor
    @Data
    protected class RetrieverCacheKey {
        private Class<?> genericTargetCls;
    }

    protected class FuncRetriever {
        private List<FUNC> funcs = new ArrayList<>();


        public void addAllFuncs(Collection<FUNC> funcs) {
            this.funcs.addAll(funcs);
            AnnotationAwareOrderComparator.sort(this.funcs);
        }


        public List<FUNC> getFunctions() {
            return ListUtils.unmodifiableList(funcs);
        }
    }

}
