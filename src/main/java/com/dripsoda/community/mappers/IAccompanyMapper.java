package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IAccompanyMapper {
    int deleteArticle(@Param(value = "index") int index);

    int insertImage(ImageEntity image);

    int insertArticle(ArticleEntity article);

    int insertRequest(RequestEntity request);

    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "region") RegionEntity region,
                                               @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    RequestEntity selectRequestByRequesterArticleIndex(@Param(value = "requesterUserEmail") String requesterUserEmail,
                                                       @Param(value = "articleIndex") int articleIndex);

    int updateArticle(ArticleEntity article);

    void updateView(@Param(value = "index") int index);

    int insertComment(CommentEntity comment);
    int insertReplyComment(CommentEntity comment);
    List<CommentEntity> selectCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    long SelectCountCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);
    List<CommentEntity>findByCommentArticleIndex(Integer articleIndex);
    void createComment(Map<String, Object> paramMap);

    List<CommentEntity> findCommentByArticle(Integer article);

    CommentEntity selectCommentByIndex(@Param(value = "index") int index);
    int readCommentsCountByArticleIndex(int articleIndex);
    int readCommentsCountByParentCommentIndex(int parentCommentIndex);
    int updateComment(CommentEntity comment);
    int updateCommentIsDeletedByCommentIndex(@Param("commentIndex") int commentIndex);
    int deletedCommentByCommentIndex(int commentIndex);

    //댓글이 자기자신인지 아닌지
    RequestEntity selectRequestByCommentArticleIndex(@Param(value = "userEmail") String requesterUserEmail,
                                                       @Param(value = "articleIndex") int articleIndex);
}
