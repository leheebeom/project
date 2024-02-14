package com.dripsoda.community.models;

public class PagingModel {
    public static final int DEFAULT_PAGINATION_COUNT = 10; // paginationCount 기본값
    public static final int DEFAULT_ROW_COUNT_PER_PAGE = 10; // rowCountPerPage 기본값

    public static final int PAGE_RANGE_FACTOR = 5;
    public final int paginationCount; // 버튼 갯수 (페이지 번호랑 상등 하여 사용하지는 않음)
    public final int rowCountPerPage; // 한 페이지에 표시할 게시글의 개수 (기본값 DEFAULT_ROW_COUNT_PER_PAGE)
    public int totalRowCount; // 진짜 전체 게시글의 개수
    public final int requestPage; // 클라이언트 요청 페이지

    public final int maxPage; // 이동할 수 있는 최대 페이지 번호 (총 게시글 개수에 따라 달라짐)
    public final int minPage = 1; // 최소 페이지 번호 (항상 1)
    public final int boundStartPage; // 페이지 버튼 중 가장 작은 값
    public final int boundEndPage; // 페이지 버튼 중 가장 큰 값 

    public PagingModel(int totalRowCount, int requestPage) {
        this(PagingModel.DEFAULT_PAGINATION_COUNT, PagingModel.DEFAULT_ROW_COUNT_PER_PAGE, totalRowCount, requestPage);
       //별도 명시x 기본값 사용
    }

    public PagingModel(int paginationCount, int rowCountPerPage, int totalRowCount, int requestPage) {
//        super();
        this.paginationCount = paginationCount;
        this.rowCountPerPage = rowCountPerPage;
        this.totalRowCount = totalRowCount;
        this.maxPage = Math.max(1, this.totalRowCount / this.rowCountPerPage + (this.totalRowCount % this.rowCountPerPage == 0 ? 0 : 1));
        if (requestPage > this.maxPage) {
            requestPage = this.maxPage;
        }

        if(requestPage < this.minPage) {
            requestPage = this.minPage;
        }
        this.requestPage = requestPage;

        this.boundStartPage = Math.max(minPage, requestPage - PAGE_RANGE_FACTOR);

        this.boundEndPage = Math.min(maxPage, requestPage + PAGE_RANGE_FACTOR);
    }
}
