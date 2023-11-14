package xyz.silencelurker.project.shop.productionapi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import xyz.silencelurker.project.shop.productionapi.entity.Production;

/**
 * @author Silence_Lurker
 */
public interface IProductionService {

    /**
     * 通过ID查询产品
     * 
     * @author Silence_Lurker
     * 
     * @param id
     * @return
     */
    Production selectProductionById(Integer id);

    /**
     * 通过id数组查询并以list形式返回
     * 
     * @param ids id数组
     * @return
     */
    List<Production> selectProductionByIds(Integer[] ids);

    /**
     * 通过id列表查询并通过list形式返回
     * 
     * @param ids id列表
     * @return
     */
    List<Production> selectProductionByIds(List<Integer> ids);

    /**
     * 按照ids查找产品，并限定数量返回
     * 
     * @param ids       id列表
     * @param pageCount 页面数据个数
     * @param nowPage   当前页数
     * @return
     */
    Page<Production> getProductionByIdsWithCount(List<Integer> ids, int pageCount, int nowPage);

    /**
     * 保存一个新的产品
     * 
     * @param newProduction
     * @return
     */
    Void saveProduction(Production newProduction);

    /**
     * 按照传入的模板删除产品信息
     * 
     * @param targetProduction
     * @return
     */
    Production deleteProduction(Production targetProduction);

    /**
     * 和删除功能类似，但仅需要知道id即可删除
     * 
     * @param id
     * @return
     */
    Production deleteProductionById(Integer id);

}
