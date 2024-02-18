package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.accompany.*;
import com.dripsoda.community.entities.accompany.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IAccompanyMapper {
    int deleteArticle(@Param(value = "index") int index);

    int deleteComment(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int deleteCommentLike(@Param(value = "userEmail") String userEmail, @Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int deleteLike(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int deleteRequest(@Param(value = "userEmail") String userEmail, @Param(value = "id") int id);

    int insertArticle(ArticleEntity article);

    int insertImage(ImageEntity image);

    int insertRequest(RequestEntity request);

    int insertComment(CommentEntity comment);

    //    int insertReplyComment(CommentEntity comment);
    int insertCommentLike(CommentLikeEntity commentLike);

    void updateView(@Param(value = "index") int index);

    int updateArticle(ArticleEntity article);

    int updateComment(CommentEntity comment);

    int updateCommentLikes(@Param(value = "like") int like,
                           @Param(value = "id") Integer id,
                           @Param(value = "commentId") Integer commentId);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "region") RegionEntity region,
                                               @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    List<ArticleEntity> selectArticles();

    List<ArticleKeywordDto> selectArticlesForKeyword(@Param(value = "keyword") String keyword);

    List<ArticleUserMyDto> selectArticlesForUserMy(@Param(value = "userEmail") String userEmail);

    List<ArticleCommentDto> selectArticleCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    List<ArticleRecentListDto> selectArticlesForHome();

    List<ArticleManagerDto> selectArticlesForManager();

    List<ArticleBestTravleDto> selectArticlesForRecent();

    List<CommentEntity> findCommentByArticle(@Param(value = "id") Integer id);

    List<CommentEntity> selectCommentsByUserEmail(@Param(value = "userEmail") String userEmail);

    List<CommentEntity> selectComments();

    List<CommentLikeEntity> selectCommentLikeForRead(@Param(value = "email") String email);

    int SelectCountArticle();

    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    CommentEntity selectCommentByIndex(@Param(value = "index") int index);

    CommentEntity selectCommentByArticleIndexAndIndex(@Param(value = "id") Integer id, @Param("commentId") Integer commentId);

    CommentLikeEntity selectCommentLikeByIndexAndCommentIndex(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    RequestEntity selectRequestByRequesterArticleIndex(@Param(value = "requesterUserEmail") String requesterUserEmail,
                                                       @Param(value = "articleIndex") int articleIndex);

    long SelectCountCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

}
