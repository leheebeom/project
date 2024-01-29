package com.dripsoda.community.services;

import com.dripsoda.community.dtos.accompany.ArticleBestTravleDto;
import com.dripsoda.community.dtos.accompany.ArticleCommentDto;
import com.dripsoda.community.dtos.accompany.ArticleRecentListDto;
import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.enums.accompany.CommentResult;
import com.dripsoda.community.enums.accompany.RequestResult;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.mappers.IAccompanyMapper;
import com.dripsoda.community.mappers.IMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

@Service(value = "com.dripsoda.community.services.AccompanyService")
public class AccompanyService {


    private static final Logger logger = LoggerFactory.getLogger(AccompanyService.class);
    private final IAccompanyMapper accompanyMapper;
    private final IMemberMapper memberMapper;

    @Autowired
    public AccompanyService(IAccompanyMapper accompanyMapper, IMemberMapper memberMapper) {
        this.accompanyMapper = accompanyMapper;
        this.memberMapper = memberMapper;
    }

    public ContinentEntity[] getContinents() {
        return this.accompanyMapper.selectContinents();
    }

    public CountryEntity[] getCountries() {
        return this.accompanyMapper.selectCountries();
    }

    public RegionEntity[] getRegions() {
        return this.accompanyMapper.selectRegions();
    }

    public ImageEntity getImage(int index) {
        return this.accompanyMapper.selectImageByIndex(index);
    }

    public List<CommentEntity> getAllCommentsForArticle(Integer articleId) {
        // 해당 게시글(articleId)에 대한 댓글 목록을 조회
        List<CommentEntity> comments = accompanyMapper.findCommentByArticle(articleId);
        // 조회된 댓글 목록 반환
        return comments;
    }

    public IResult modifyComment(CommentEntity comment) {
        CommentEntity oldComment = this.accompanyMapper.selectCommentByIndex(comment.getIndex());
        if (oldComment == null) {
            return CommentResult.NOT_FOUND;
        }
        if (!comment.getUserEmail().equals(oldComment.getUserEmail())) {
            return CommentResult.NOT_SIGNED;
        }
        comment.setIndex(oldComment.getIndex())
                .setArticleIndex(oldComment.getArticleIndex())
                .setCommentParentIndex(oldComment.getCommentParentIndex())
                .setUserEmail(oldComment.getUserEmail())
                .setCreatedAt(oldComment.getCreatedAt())
                .setDeleted(oldComment.getDeleted())
                .setLikes(oldComment.getLikes())
                .setModifiedAt(new Date());
        return this.accompanyMapper.updateComment(comment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public void updateViews(int index) {
        accompanyMapper.updateView(index);
    }

    public IResult uploadImage(ImageEntity image) {
        return this.accompanyMapper.insertImage(image) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public ArticleEntity getArticle(int index) {
        return this.accompanyMapper.selectArticleByIndex(index);
    }

    public ArticleSearchDto[] searchArticles(RegionEntity region, int lastArticleIndex) {
        return this.accompanyMapper.selectArticlesForSearch(region, lastArticleIndex);
    }


    public IResult putArticle(ArticleEntity article) {
        return this.accompanyMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult deleteArticle(int index) {
        return this.accompanyMapper.deleteArticle(index) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult deleteComment(Integer id, Integer commentId) {
        return this.accompanyMapper.deleteComment(id, commentId) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult modifyArticle(ArticleEntity article) {
        ArticleEntity oldArticle = this.accompanyMapper.selectArticleByIndex(article.getIndex());
        if (oldArticle == null) {
            return CommonResult.FAILURE;
        }
        if (!article.getUserEmail().equals(oldArticle.getUserEmail())) {
            return CommonResult.FAILURE;
        }
        if (article.getCoverImage() == null) {
            article.setCoverImage(oldArticle.getCoverImage())
                    .setCoverImageMime(oldArticle.getCoverImageMime());
        }
        article.setIndex(oldArticle.getIndex())
                .setUserEmail(oldArticle.getUserEmail())
                .setCreatedAt(oldArticle.getCreatedAt());
        return this.accompanyMapper.updateArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public IResult putRequest(UserEntity requester, int articleIndex) {
        if (requester == null) {
            return RequestResult.NOT_SIGNED;
        }

        ArticleEntity article = this.accompanyMapper.selectArticleByIndex(articleIndex);
        if (article == null) {
            return RequestResult.NOT_FOUND;
        }
        UserEntity requestee = this.memberMapper.selectUserByEmail(UserEntity.build().setEmail(article.getUserEmail()));
        if (requester.equals(requestee)) {
            return RequestResult.YOURSELF;
        }
        RequestEntity request = RequestEntity.build()
                .setRequesterUserEmail(requester.getEmail())
                .setRequesteeUserEmail(requestee.getEmail())
                .setArticleIndex(articleIndex)
                .setCreatedAt(new Date())
                .setGranted(false)
                .setDeclined(false);
        return this.accompanyMapper.insertRequest(request) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    @Transactional
    public IResult addCommentLike(UserEntity user,Integer id, Integer commentId) {
        ArticleEntity article = this.accompanyMapper.selectArticleByIndex(id);
        CommentEntity comment = this.accompanyMapper.selectCommentByArticleIndexAndIndex(id,commentId);
        if(user == null || !user.getEmail().equals(comment.getUserEmail())) {
            return CommentResult.NOT_SIGNED;
        }
        if (article == null || article.getIndex() == 0) {
            return CommentResult.NOT_FOUND;
        }

        if(comment == null || comment.getIndex() == 0) {
            return CommentResult.NOT_FOUND;
        }

        CommentLikeEntity commentLikeEntity = CommentLikeEntity.build()
                .setUserEmail(user.getEmail())
                .setArticleIndex(id)
                .setCommentIndex(commentId)
                .setChecked(true);
        comment.setLikes(comment.getLikes() + 1);
        int likeInsertResult = this.accompanyMapper.insertCommentLike(commentLikeEntity);
        if (likeInsertResult > 0) {
            int updateResult = this.accompanyMapper.updateCommentLikes(comment.getLikes(), comment.getArticleIndex(), comment.getIndex());

            if (updateResult > 0) {
                logger.info("Comment like added successfully");
                return CommonResult.SUCCESS;
            } else {
                logger.error(" updateResult failure");
                // updateResult 실패 시 CommonResult.FAILURE 반환
                return CommonResult.FAILURE;
            }
        } else {
            logger.error("likeInsertResult failure");
            // likeInsertResult 실패 시 CommonResult.FAILURE 반환
            return CommonResult.FAILURE;
        }
    }

    public boolean checkRequest(UserEntity requester, int articleIndex) {
        if (requester == null) {
            return false;
        }
        return this.accompanyMapper.selectRequestByRequesterArticleIndex(requester.getEmail(), articleIndex) != null;
    }

    public CommentEntity getCommentIndex(int index) {
        return this.accompanyMapper.selectCommentByIndex(index);
    }

    public List<CommentEntity> getCommentsByArticleIndex(Integer articleIndex) {
        return accompanyMapper.selectCommentsByArticleIndex(articleIndex);
    }

    @Transactional
    public IResult createComment(UserEntity user, Integer articleIndex, CommentEntity comment) {
        // 여기서 articleId는 새로운 댓글이 속한 게시물의 ID입니다.
        ArticleEntity article = this.accompanyMapper.selectArticleByIndex(articleIndex);
        if (article == null) {
            return CommentResult.NOT_FOUND;
        }
        if (user == null || !user.isAdmin() && !user.getEmail().equals(article.getUserEmail())) {
            return CommentResult.NOT_SIGNED;
        }
        long totalComment = accompanyMapper.SelectCountCommentsByArticleIndex(articleIndex) + 1;
        comment = CommentEntity.build()
                .setArticleIndex(article.getIndex())
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setContent(comment.getContent())
                .setCommentParentIndex(null)
                .setModifiedAt(comment.getModifiedAt())
                .setDeleted(false)
                .setLikes(0)
                .setComment(false)
                .setDepth(0)
                .setOrderNumber(totalComment);
        return this.accompanyMapper.insertComment(comment) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    //
    @Transactional
    public IResult replyComment(UserEntity user, Integer articleIndex, Integer commentId, CommentEntity reply) {
        ArticleEntity article = this.accompanyMapper.selectArticleByIndex(articleIndex);
        CommentEntity comment = this.accompanyMapper.selectCommentByIndex(commentId);
        if (article == null) {
            return CommentResult.NOT_FOUND;
        }
        if (user == null || !user.isAdmin() && !user.getEmail().equals(article.getUserEmail())) {
            return CommentResult.NOT_SIGNED;
        }
        if (comment == null) {
            return CommonResult.FAILURE;
        }
        reply = CommentEntity.build()
                .setArticleIndex(article.getIndex())
                .setUserEmail(user.getEmail())
                .setCreatedAt(new Date())
                .setContent(reply.getContent())
                .setCommentParentIndex(comment.getIndex())
                .setModifiedAt(reply.getModifiedAt())
                .setDeleted(false)
                .setLikes(0)
                .setComment(true)
                .setDepth(comment.getDepth() + 1)
                .setOrderNumber(comment.getOrderNumber());

        return this.accompanyMapper.insertComment(reply) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;


    }

//    모든 게시판 게시글 불러오기
//    public List<ArticleSearchDto> getArticlesForAll() {
//
//    }

    public List<ArticleCommentDto> getArticleCommentsByArticleIndex(Integer articleIndex) {
        return this.accompanyMapper.selectArticleCommentsByArticleIndex(articleIndex);
    }

    public List<ArticleEntity> getArticlesForAll() {
        return this.accompanyMapper.selectArticles();
    }


    public List<ArticleBestTravleDto> getArticlesForRecent() {
        return this.accompanyMapper.selectArticlesForRecent();
    }

    public List<ArticleRecentListDto> getArticles() {
        return this.accompanyMapper.selectArticlesForHome();
    }
    //모든 게시판 최신 게시글 8개 불러오
//    public  List<ArticleSearchDto> getNewArticlesForAll() {
//
//    }
}



