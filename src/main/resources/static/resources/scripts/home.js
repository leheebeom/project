loginButton.addEventListener('click', () => {
    window.location.href = '../../member/userLogin';
});

myButton.addEventListener('click', () => {
    window.location.href = '../../member/userMy';
});




document.addEventListener('DOMContentLoaded', function () {
    const swiper = new Swiper(".swiper-container", {
        slidesPerView: 7,
        spaceBetween: 10,
        centeredSlides: false,
        autoplay: {
            delay: 0,
            disableOnInteraction: false,
        },
        speed: 5000,
        loop: true,
        loopAdditionalSlides: 1,

    });
    const travelSwiper = new Swiper(".travel-swiper-container", {
        slidesPerGroup: 2,
        slidesPerView: 4,
        observer: true,
        observeParents: true,
        spaceBetween: 15,
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });
    if (travelSwiper.isBeginning) {
        document.querySelector('.swiper-button-prev').style.visibility = 'hidden';
    }
    if (travelSwiper.isEnd) {
        document.querySelector('.swiper-button-next').style.visibility = 'hidden';
    }
    travelSwiper.on('slideChange', function () {
        if (travelSwiper.isBeginning) {
            document.querySelector('.swiper-button-prev').style.visibility = 'hidden';
        } else {
            document.querySelector('.swiper-button-prev').style.visibility = 'visible';
        }
        if (travelSwiper.isEnd) {
            document.querySelector('.swiper-button-next').style.visibility = 'hidden';
        } else {
            document.querySelector('.swiper-button-next').style.visibility = 'visible';
        }
    });
});


(() => {
    const formId = 'searchForm';
    const formContainerId = 'searchFormContainer';

    const removeOrRecreateForm = () => {
        const screenWidth = window.innerWidth;
        const form = document.getElementById('searchForm');
        const formContainer = document.getElementById('searchFormContainer');

        // 창 너비가 1200px 미만인 경우 form 요소를 삭제
        if (screenWidth < 1200) {
            // form 요소가 존재하는지 확인 후 삭제
            if (form) {
                form.remove();
            }
        } else {
            // 창 너비가 1200px 이상인 경우 form 요소를 재생성하고, 원래 위치에 삽입
            if (!form) {
                const newForm = document.createElement('form');
                newForm.className = 'search-form';
                newForm.method = 'get';
                newForm.id = formId;

                const label = document.createElement('label');
                label.className = 'input-container';

                const span = document.createElement('span');
                span.hidden = true;
                span.textContent = '여행 qna을 검색해보세요!';

                const i = document.createElement('i');
                i.className = 'icon fa-solid fa-magnifying-glass';

                const input = document.createElement('input');
                input.className = 'input';
                input.maxLength = 50;
                input.name = 'keyword';
                input.placeholder = '여행 qna을 검색해보세요!';
                input.type = 'text';

                label.appendChild(span);
                label.appendChild(i);
                label.appendChild(input);

                newForm.appendChild(label);

                // 원래 위치에 form 요소를 삽입
                formContainer.appendChild(newForm);
            }
        }
    };

    // 함수를 창 크기 조절 이벤트에 연결
    window.addEventListener('resize', removeOrRecreateForm);
    // 초기에도 함수 호출
    removeOrRecreateForm();
})();

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