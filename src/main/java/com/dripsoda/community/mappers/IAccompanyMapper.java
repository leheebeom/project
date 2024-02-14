package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.accompany.*;
import com.dripsoda.community.entities.accompany.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IAccompanyMapper {
    int deleteLike(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int deleteCommentLike(@Param(value = "userEmail") String userEmail, @Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int deleteRequest(@Param(value = "userEmail") String userEmail, @Param(value = "id") int id);

    int deleteArticle(@Param(value = "index") int index);

    int deleteComment(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    int insertImage(ImageEntity image);

    int insertArticle(ArticleEntity article);

    int insertCommentLike(CommentLikeEntity commentLike);

    int insertRequest(RequestEntity request);


    List<CommentLikeEntity> selectCommentLikeForRead(@Param(value = "email") String email);
    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "region") RegionEntity region,
                                               @Param(value = "lastArticleIndex") int lastArticleIndex);

    CommentLikeEntity selectCommentLikeByIndexAndCommentIndex(@Param(value = "id") Integer id, @Param("commentIndex") Integer commentIndex);

    List<ArticleKeywordDto> selectArticlesForKeyword(@Param(value = "keyword") String keyword);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    List<ArticleEntity> selectArticles();

    List<CommentEntity> selectCommentsByUserEmail(@Param(value = "userEmail") String userEmail);
    List<CommentEntity> selectComments();
    List<ArticleRecentListDto> selectArticlesForHome();
    List<ArticleManagerDto> selectArticlesForManager();

    List<ArticleBestTravleDto> selectArticlesForRecent();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);

    RequestEntity selectRequestByRequesterArticleIndex(@Param(value = "requesterUserEmail") String requesterUserEmail,
                                                       @Param(value = "articleIndex") int articleIndex);

    CommentEntity selectCommentByArticleIndexAndIndex(@Param(value = "id") Integer id, @Param("commentId") Integer commentId);


    void updateView(@Param(value = "index") int index);

    int insertComment(CommentEntity comment);

    int insertReplyComment(CommentEntity comment);

    List<ArticleUserMyDto> selectArticlesForUserMy(@Param(value = "userEmail") String userEmail);

    List<CommentEntity> selectCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    List<ArticleCommentDto> selectArticleCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    int SelectCountArticle();

    long SelectCountCommentsByArticleIndex(@Param("articleIndex") Integer articleIndex);

    List<CommentEntity> findByCommentArticleIndex(Integer articleIndex);

    void createComment(Map<String, Object> paramMap);

    List<CommentEntity> findCommentByArticle(@Param(value = "id") Integer id);

    CommentEntity selectCommentByIndex(@Param(value = "index") int index);

    int readCommentsCountByArticleIndex(int articleIndex);

    int readCommentsCountByParentCommentIndex(int parentCommentIndex);

    int updateComment(CommentEntity comment);

    int updateArticle(ArticleEntity article);

    int updateCommentLikes(@Param(value = "like") int like,
                           @Param(value = "id") Integer id,
                           @Param(value = "commentId") Integer commentId);


    //댓글이 자기자신인지 아닌지
    RequestEntity selectRequestByCommentArticleIndex(@Param(value = "userEmail") String requesterUserEmail,
                                                     @Param(value = "articleIndex") int articleIndex);
}
