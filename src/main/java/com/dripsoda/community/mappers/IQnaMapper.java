package com.dripsoda.community.mappers;

import com.dripsoda.community.entities.qna.CategoryEntity;
import com.dripsoda.community.entities.qna.QnaArticleEntity;
import com.dripsoda.community.vos.qna.QnaArticleReadVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IQnaMapper {

    int deleteQnaArticle(@Param(value = "index")int index);
    int insertArticle(QnaArticleEntity article);

    int updateQnaArticle(QnaArticleEntity qnaArticle);

    void updateView(@Param(value = "index") int index);

    QnaArticleEntity selectArticleByIndex(@Param(value = "index") int index, int categoryId);


    CategoryEntity[] selectCategories();

    List<QnaArticleEntity> selectQnaArticlesByHelp();

    List<QnaArticleEntity> selectQnaArticlesByEvent();

    List<QnaArticleEntity> selectQnaArticlesByQna();
    int selectCategoryIdByIndex(@Param(value = "index") int index);

   QnaArticleReadVo selectHelpPageByIndex(@Param(value = "index") int index, int categoryId);

}
