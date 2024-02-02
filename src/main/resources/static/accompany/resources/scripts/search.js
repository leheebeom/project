const accompany = window.document.querySelectorAll('.accompany');
const accompanyArray = Array.from(accompany);

accompanyArray.forEach(article => {
    const currentDate = new Date();
    const dateFromElement = article.querySelector('.date-From');
    const dateString = dateFromElement.innerText;

    const dateObject = new Date(currentDate.getFullYear(), parseInt(dateString.split('-')[0]) - 1, parseInt(dateString.split('-')[1]));

    const status = currentDate > dateObject ? 'expired' : 'status';

    const statusElement = window.document.createElement('span');
    statusElement.classList.add('status');

    if (status === 'expired') {
        statusElement.classList.add('expired');
        statusElement.innerText = '모집 마감';
    } else {
        statusElement.innerText = '모집중';
    }

    const titleContainerElement = article.querySelector('.title-container');
    titleContainerElement.prepend(statusElement);
});

const searchFrom = window.document.getElementById('searchForm');
const searchButton = window.document.getElementById('searchButton');
searchButton?.addEventListener('click', () => {
    searchFrom['keyword'].focus();
});