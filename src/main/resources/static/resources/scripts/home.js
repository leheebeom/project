loginButton.addEventListener('click', () => {
    window.location.href = '../../member/userLogin';
});

myButton.addEventListener('click', () => {
    window.location.href = '../../member/userMy';
});




document.addEventListener('DOMContentLoaded', function () {
    const swiper = new Swiper(".swiper-container", {
        direction: 'horizontal',
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

const chatButton = window.document.getElementById('chatButton');
const chatMessage = window.document.getElementById('chatMessage');
const xButton = window.document.getElementById('xButton');
const chatMessageList = window.document.getElementById('chatMessageList');
const backButton = window.document.getElementById('backButton');
const chatMessageRequestButton = window.document.getElementById('chatMessageRequestButton');
const chatMessageForm = window.document.getElementById('chatMessageForm');
const rid = parseInt(chatMessageForm['rid'].value);
chatButton?.addEventListener('click', () => {
    // Chat 메시지와 X 버튼을 토글
    chatMessage.classList.toggle('visible');
    xButton.classList.add('visible');

});

xButton?.addEventListener('click', () => {
    // X 버튼을 클릭하면 Chat 메시지가 사라짐
    chatMessage.classList.remove('visible');
    chatMessageList.classList.remove('visible');
    xButton.classList.remove('visible');
});

chatMessageRequestButton?.addEventListener('click', () => {

    chatMessage.classList.remove('visible');
    chatMessageList.classList.add('visible');

});

backButton?.addEventListener('click', ()=> {
    chatMessageList.classList.remove('visible');
    chatMessage.classList.add('visible');

})

chatMessageForm.onsubmit = e => {
    e.preventDefault();
    if(chatMessageForm['content'].value === '') {
        alert('문의하실 사항이 없으신가요?');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', chatMessageForm['content'].value);
    formData.append('rid', chatMessageForm['rid'].value);
    xhr.open('POST', `./member/chatMessage/${rid}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        const roomIndex = responseJson['rid'];
                        alert('1대1 문의를 등록하셨습니다. 해당 연락처로 답장이 완료되면 답을 해드리겠습니다.');
                        window.location.reload();
                        break
                    case 'not_found' :
                        alert('존재하지 않는 채팅방입니다.');
                        break
                    case 'not_signed' :
                        alert('로그인 정보가 유효하지 않습니다.')
                        break
                    default:
                        warning.show('알 수 없는 이유로 채팅을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
}

function showCurrentTime() {
    // Date 객체를 생성하여 현재 시간을 가져옴
    const currentTime = new Date();
    const months = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
    const month = months[currentTime.getMonth()];
    const day = currentTime.getDate();
    let hours = currentTime.getHours();
    const meridiem = hours >= 12 ? '오후' : '오전';
    // 12시간제로 변환
    hours = hours % 12 || 12;
    // 분이 한 자리 숫자일 경우 앞에 0을 붙임
    const minutes = currentTime.getMinutes() < 10 ? '0' + currentTime.getMinutes() : currentTime.getMinutes();
    const formattedTime = `${month} ${day}일 ${meridiem} ${hours}:${minutes}`;
    document.getElementById('itemCurrentTime').innerText = formattedTime;
}

// 페이지 로드 시 처음 한 번 시간을 표시
showCurrentTime();


