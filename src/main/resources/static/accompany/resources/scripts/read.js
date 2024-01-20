const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const region = window.document.getElementById('region');
const capacity = window.document.getElementById('capacity');
const dateFrom = window.document.getElementById('dateFrom');
const dateTo = window.document.getElementById('dateTo');
const content = window.document.getElementById('content');
const createdAt = window.document.getElementById('createdAt');
const nickname = window.document.getElementById('nickname');
const requestButton = window.document.getElementById('requestButton');
const retractButton = window.document.getElementById('retractButton');
const deleteButton = window.document.getElementById('deleteButton');
const modifyButton = window.document.getElementById('modifyButton');
const viewCount = window.document.getElementById('viewCount');
const contentForm = window.document.getElementById('contentForm');
const replyForm = window.document.getElementById('replyForm');
const replyButtonAll  = window.document.querySelectorAll('.reply-button');
const parentIndex = window.document.getElementById('number');

const showForm = form => form.classList.add('visible');
const hideForm = form => form.classList.remove('visible');
// 초기 상태 설정
showForm(contentForm);
hideForm(replyForm);

const checkRequest = () => {
    cover.show('서버와 통신 중입니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                if (responseJson['result'] === true) {
                    //요청한적 있음.
                    retractButton.classList.add('visible');
                } else {
                    //요청한적 없음
                    requestButton.classList.add('visible');
                }
            } else {
                alert('일부 정보를 불러오지 못하였습니다.');
            }
        }
    };
    xhr.send();
};

const xhr = new XMLHttpRequest();
cover.show('요청한 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.')
xhr.open('POST', window.location.href);
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            const createdAtObj = new Date(responseJson['createdAt']);
            const dateFromObj = new Date(responseJson['dateFrom']);
            const dateToObj = new Date(responseJson['dateTo']);
            coverImage.setAttribute('src', `../cover-image/${id}`);
            title.innerText = responseJson['title'];
            window.document.title = `${responseJson['title']} :: 드립소다`;
            region.innerText = responseJson['regionValue'];
            capacity.innerText = `${responseJson['capacity']}명`;
            dateFrom.innerText = `${dateFromObj.getFullYear()} - ${dateFromObj.getMonth() + 1} - ${dateFromObj.getDate()}`;
            dateTo.innerText = `${dateToObj.getFullYear()} - ${dateToObj.getMonth() + 1} - ${dateToObj.getDate()}`;
            content.innerHTML = responseJson['content'];
            nickname.innerText = responseJson['userNickname'];
            if ((responseJson['mine'] ?? false) === true) {
                modifyButton.classList.add('visible');
                deleteButton.classList.add('visible');
            }
            viewCount.innerText = `조회수 ${responseJson['viewCount']}`;
            createdAt.innerText = `${createdAtObj.getFullYear()}-${createdAtObj.getMonth() + 1}-${createdAtObj.getDate()} -${createdAtObj.getHours()}:${createdAtObj.getMinutes()}`;
            checkRequest();

        } else if (xhr.status === 404) {
            alert('존재하지 않는 동행 게시글입니다.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        }

    }
};
xhr.send();

deleteButton.addEventListener('click', () => {
    if (!confirm('정말로 동행을 삭제할까요?')) {
        return;
    }
    cover.show('동행을 삭제하고 있습니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('동행을 성공적으로 삭제하였습니다.');
                        window.location.href = `../`;
                        break;
                    case 'k' :
                        alert('k');
                        break;
                    default :
                        alert('알 수 없는 이유로 동행을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});

modifyButton.addEventListener('click', () => {
    window.location.href = `../modify/${id}`;
});


requestButton.addEventListener('click', () => {
    if (requestButton.dataset.sigend === 'false') {
        window.location.href = '../../member/userLogin';
        return;
    }
    cover.show('동행 신청 처리 중입니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../';
                        break;
                    case 'not_signed' :
                        alert('로그인 정보가 유효하지 않습니다.');
                        break
                    case 'yourself' :
                        alert('나야나');
                        break
                    case 'success' :
                        alert('동행 신청에 성공하였습니다.');
                        window.location.reload();
                        break;
                    default :
                        alert('알 수 없는 이유로 동행 신청에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});

const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};



contentForm.onsubmit = e => {
    e.preventDefault();
    if (contentForm['content'].value === '') {
        contentForm['content'].focus();
        alert('댓글을 작성해주세요.');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', contentForm['content'].value);
    xhr.open('POST', `../read/${id}/comments`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        const id = responseJson['articleId'];
                        window.location.reload();
                        break
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../';
                        break;
                    case 'not_signed' :
                        alert('로그인정보가 유효하지 않습니다.');
                        break
                    default:
                        warning.show('알 수 없는 이유로 댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};

let commentId
replyButtonAll.forEach((reply) => {
    // Adding a click event listener to each reply button
    reply.addEventListener('click', () => {
        showForm(replyForm);
        hideForm(contentForm);
        replyForm['content'].focus();
        commentId = reply.dataset.index;
        console.log(commentId);
    });
});


replyForm.onsubmit = e => {
    e.preventDefault();
    if (replyForm['content'].value === '') {
        alert('댓글을 작성해주세요.');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', replyForm['content'].value);
    xhr.open('POST', `./${id}/reply/${commentId}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        const id = responseJson['articleId'];
                        window.location.reload();
                        break
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../';
                        break;
                    case 'not_signed' :
                        alert('로그인정보가 유효하지 않습니다.');
                        break
                    default:
                        warning.show('알 수 없는 이유로 댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};