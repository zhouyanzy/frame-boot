package top.zhouy.frameboot.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import top.zhouy.frameboot.model.Product;

import java.util.List;

/**
 * @author zhouYan
 * @date 2019/6/17 14:04
 */
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {
    /**
     * 只写方法声明，不用实现
     * @param productName
     * @return
     */
    public List<Product> findByProductNameLike(String productName);
}
