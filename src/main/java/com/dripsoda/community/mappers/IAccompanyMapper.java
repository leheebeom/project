package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.accompany.ArticleBestTravleDto;
import com.dripsoda.community.dtos.accompany.ArticleCommentDto;
import com.dripsoda.community.dtos.accompany.ArticleRecentListDto;
import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IAccompanyMapper {
    int deleteArticle(@Param(value = "index") int index);

    int deleteComment(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int insertImage(ImageEntity image);

    int insertArticle(ArticleEntity article);

    int insertCommentLike(CommentLikeEntity commentLike);

    int insertRequest(RequestEntity request);

    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "region") RegionEntity region,
                                               @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    List<ArticleEntity> selectArticles();

    List<ArticleRecentListDto> selectArticlesForHome();

    List<ArticleBestTravleDto> selectArticlesForRecent();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    RequestEntity selectRequestByRequesterArticleIndex(@Param(value = "requesterUserEmail") String requesterUserEmail,
                                                       @Param(value = "articleIndex") int articleIndex);

    CommentEntity selectCommentByArticleIndexAndIndex(@Param(value = "id") Integer id, @Param("commentId") Integer commentId);


    void updateView(@Param(value = "index") int index);

    int insertComment(CommentEntity comment);

    int insertReplyComment(CommentEntity comment);

    List<CommentEntity> selectCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    List<ArticleCommentDto> selectArticleCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    long SelectCountCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    List<CommentEntity> findByCommentArticleIndex(Integer articleIndex);

    void createComment(Map<String, Object> paramMap);

    List<CommentEntity> findCommentByArticle(  @Param(value = "id") Integer id);

    CommentEntity selectCommentByIndex(@Param(value = "index") int index);

    int readCommentsCountByArticleIndex(int articleIndex);

    int readCommentsCountByParentCommentIndex(int parentCommentIndex);

    int updateComment(CommentEntity comment);
    int updateArticle(ArticleEntity article);
    int updateCommentLikes( @Param(value = "like") int like,
                            @Param(value = "id") Integer id,
                            @Param(value = "commentId") Integer commentId);


    //댓글이 자기자신인지 아닌지
    RequestEntity selectRequestByCommentArticleIndex(@Param(value = "userEmail") String requesterUserEmail,
                                                     @Param(value = "articleIndex") int articleIndex);
}
