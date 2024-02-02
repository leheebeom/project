package com.dripsoda.community.services;

import com.dripsoda.community.entities.qna.CategoryEntity;
import com.dripsoda.community.entities.qna.QnaArticleEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.mappers.IAccompanyMapper;
import com.dripsoda.community.mappers.IMemberMapper;
import com.dripsoda.community.mappers.IQnaMapper;
import com.dripsoda.community.models.PagingModel;
import com.dripsoda.community.vos.qna.QnaArticleReadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "com.dripsoda.community.services.QnaService")
public class QnaService {
    private final IAccompanyMapper accompanyMapper;
    private final IMemberMapper memberMapper;
    private final IQnaMapper qnaMapper;

    @Autowired
    public QnaService(IAccompanyMapper accompanyMapper, IMemberMapper memberMapper, IQnaMapper qnaMapper) {
        this.accompanyMapper = accompanyMapper;
        this.memberMapper = memberMapper;
        this.qnaMapper = qnaMapper;
    }

    public IResult putArticle(QnaArticleEntity article) {
        return this.qnaMapper.insertArticle(article) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public void updateViews(int index) {
        qnaMapper.updateView(index);
    }

    public QnaArticleEntity getArticle(int index, int categoryId) {
        return this.qnaMapper.selectArticleByIndex(index, categoryId);
    }

    public IResult modifyQnaArticle(QnaArticleEntity qnaArticle) {
        QnaArticleEntity oldQnaArticle = this.qnaMapper.selectArticleByIndex(qnaArticle.getIndex(), qnaArticle.getCategoryId());
        if(oldQnaArticle == null) {
            return CommonResult.FAILURE;
        }
        if(!qnaArticle.getUserEmail().equals(oldQnaArticle.getUserEmail())) {
            return CommonResult.FAILURE;
        }
        qnaArticle.setIndex(oldQnaArticle.getIndex())
                .setUserEmail(oldQnaArticle.getUserEmail())
                .setCategoryId(oldQnaArticle.getCategoryId())
                .setCreatedAt(oldQnaArticle.getCreatedAt());
        return this.qnaMapper.updateQnaArticle(qnaArticle) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;

    }

    public IResult deleteQnaArticle(int index) {
        return this.qnaMapper.deleteQnaArticle(index) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

     public QnaArticleReadVo getHelpMove(int index, int categoryId) {
        return  this.qnaMapper.selectHelpPageByIndex(index, categoryId);
     }

     public int ArticleTotalCount(int categoryId) {
        return this.qnaMapper.selectArticleTotalCount(categoryId);
     }

     public int getCategoryId(int index){
        return this.qnaMapper.selectCategoryIdByIndex(index);
     }


    public CategoryEntity[] getCategories() {
        return this.qnaMapper.selectCategories();
    }

//    public List<QnaArticleEntity> getHelpQnaArticles(PagingModel pagingModel) {
//        int startIndex = (pagingModel.requestPage - 1) * pagingModel.rowCountPerPage;
//        int rowCountPerPage = pagingModel.rowCountPerPage;
//        return this.qnaMapper.selectQnaArticlesByHelp(startIndex, rowCountPerPage);
//    }

    public List<QnaArticleEntity> getArticlesByTypeAndPaging(int type, PagingModel pagingModel) {
        int startIndex = (pagingModel.requestPage - 1) * pagingModel.rowCountPerPage;
        int rowCountPerPage = pagingModel.rowCountPerPage;
        return this.qnaMapper.selectArticleByCategoryIdAndPaging(type,startIndex,rowCountPerPage);
    }


//    public List<QnaArticleEntity> getEventQnaArticles() {
//        return this.qnaMapper.selectQnaArticlesByEvent();
//    }
//    public List<QnaArticleEntity> getQnaArticles() {
//        return this.qnaMapper.selectQnaArticlesByQna();
//    }



 }
