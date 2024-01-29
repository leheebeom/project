package com.dripsoda.community.models;

public class PagingModel {
    public static final int DEFAULT_PAGINATION_COUNT = 10; // paginationCount 기본값
    public static final int DEFAULT_ROW_COUNT_PER_PAGE = 20; // rowCountPerPage 기본값

    public static final int PAGE_RANGE_FACTOR = 5;
    public final int paginationCount; // 페이지 하단에 표시할 페이지 버튼의 개수 (기본값 DEFAULT_PAGINATION_COUNT)
    public final int rowCountPerPage; // 한 페이지에 표시할 게시글의 개수 (기본값 DEFAULT_ROW_COUNT_PER_PAGE)
    public int totalRowCount; // 진짜 전체 게시글의 개수
    public final int requestPage; // 현재 클라이언트가 보겠다고 요청한 페이지 번호

    public final int maxPage; // 이동할 수 있는 최대 페이지 번호 (총 게시글 개수에 따라 달라짐)
    public final int minPage = 1; // 이동할 수 있는 최소 페이지 번호 (항상 1)
    public final int boundStartPage; // 페이지 하단에 표시할 페이지 버튼 중 가장 작은 값
    public final int boundEndPage; // 페이지 하단에 표시할 페이지 버튼 중 가장 큰 값

    public PagingModel(int totalRowCount, int requestPage) {
        this(PagingModel.DEFAULT_PAGINATION_COUNT, PagingModel.DEFAULT_ROW_COUNT_PER_PAGE, totalRowCount, requestPage);
        // int 타입 매개변수 총 4개를 받는 생성자는 바로 밑 PagingModel 이겠다.
        // 이 방식으로 객체화가 되면 이 호출에 의하여 아래 메서드가 호출이 되는데, 별도로 값을 명시하지 않으면 기본값인 정적 메서드 DEFAULT 내용을
        // 쓰겠다는 의미이다.
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
//        requestPage = requestPage < 1 ? 1 : requestPage; 과 같은 내용
        if(requestPage < this.minPage) {
            requestPage = this.minPage;
        }
        this.requestPage = requestPage;
//        this.boundStartPage = (this.requestPage / this.paginationCount) * this.paginationCount + 1;
        this.boundStartPage = Math.max(minPage, requestPage - PAGE_RANGE_FACTOR);
//        this.boundEndPage = Math.min(this.maxPage, (this.requestPage / this.paginationCount) * this.paginationCount + this.paginationCount);
        // 계산해서 나온 값과 내가 가진 최대 페이지 중 작은 것
        this.boundEndPage = Math.min(maxPage, requestPage + PAGE_RANGE_FACTOR);
    }
}
